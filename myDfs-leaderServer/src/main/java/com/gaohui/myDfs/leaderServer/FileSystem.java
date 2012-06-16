package com.gaohui.myDfs.leaderServer;

import com.gaohui.myDfs.core.Chunk;
import com.gaohui.myDfs.core.ChunkImage;
import com.gaohui.myDfs.core.FileImage;
import com.gaohui.myDfs.core.FileStatus;
import com.gaohui.myDfs.core.Path;
import com.gaohui.myDfs.core.Paths;
import com.gaohui.myDfs.core.StorageNode;
import com.gaohui.myDfs.leaderServer.namespace.Namespaces;
import com.gaohui.myDfs.leaderServer.namespace.PathNode;
import com.gaohui.myDfs.leaderServer.namespace.RichPath;
import com.gaohui.myDfs.leaderServer.namespace.action.ActionExecutorFactory;
import com.gaohui.myDfs.leaderServer.namespace.action.ActionType;
import com.gaohui.myDfs.leaderServer.namespace.action.EditLogWriter;
import com.gaohui.myDfs.leaderServer.namespace.action.NamespaceAction;
import com.gaohui.myDfs.storageServer.service.StorageServerService;
import com.gaohui.rpc.client.DefaultClientServiceFactory;
import com.gaohui.rpc.client.DefaultServiceShop;
import com.gaohui.rpc.client.ServiceShop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * User: Administrator
 * Date: 11-2-27 Time: 下午8:48
 *
 * @author Basten Gao
 */
public class FileSystem {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystem.class);
    private Set<PathNode> rootPathNodes = null;
    /**
     * path:Path => file:fileImage
     */
    private Map<Path, FileImage> fileImageMap = new HashMap<Path, FileImage>();
    private Set<StorageNode> storageNodes = new HashSet<StorageNode>();
    private EditLogWriter logWriter = null;

    public FileSystem() {
        LOGGER.debug("read fileImage");
        rootPathNodes = Namespaces.processNamespace();
        try {
            logWriter = new EditLogWriter();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 获取FileStatus,如果不存在返回null
     *
     * @param path
     * @return
     */
    public FileStatus getFileStatus(Path path) {
        PathNode pathNode = getPathNode(path);
        if (pathNode != null) {
            return new FileStatus(pathNode.getPath(), pathNode.isAllowsChildren());
        } else {
            return null;
        }
    }

    /**
     * 根据Path查找相应的PathNode
     *
     * @param path
     * @return 如果不存在，返回null
     */
    private PathNode getPathNode(Path path) {
        List<String> names = PathNode.getPathNodeNames(path);
        return getPathNode(names, 0, rootPathNodes);
    }

    private static PathNode getPathNode(List<String> names, int index, Set<PathNode> pathNodes) {
        for (PathNode node : pathNodes) {
            if (node.getName().equals(names.get(index))) {
                if (index == names.size() - 1) {
                    return node;
                } else {
                    return getPathNode(names, index + 1, node.getChildren());
                }
            }
        }
        return null;
    }

    /**
     * 如果不存在返回null
     *
     * @param path
     * @return
     */
    public FileImage getFileImage(Path path) {
        return fileImageMap.get(path);
    }

    /**
     * 添加块
     *
     * @param node
     * @param chunks
     */
    public void putChunkFileOfNode(StorageNode node, Set<Chunk> chunks) {
        LOGGER.debug("{} putChunks ", node);
        putNode(node);
        for (Chunk chunk : chunks) {
            FileImage fileImage = fileImageMap.get(chunk.getOwner());
            if (fileImage == null) {
                fileImage = new FileImage();
                fileImageMap.put(chunk.getOwner(), fileImage);
            }
            ChunkImage chunkImage = fileImage.getChunkImage(chunk.getOrdinal());
            if (chunkImage == null) {
                chunkImage = new ChunkImage();
                chunkImage.setChunk(chunk);
                chunkImage.setStorageNodes(new HashSet<StorageNode>());
                fileImage.putChunkImage(chunk.getOrdinal(), chunkImage);
            }
            chunkImage.getStorageNodes().add(node);
        }
    }

    private void putNode(StorageNode storageNode) {
        storageNodes.add(storageNode);
    }

    /**
     * 返回所有的存储节点
     *
     * @return
     */
    public Set<StorageNode> getStorageNodes() {
        return storageNodes;
    }

    public boolean newFile(Path path) {
        RichPath richPath = new RichPath(path, false);
        NamespaceAction action = new NamespaceAction(ActionType.ADD, new RichPath[]{richPath});

        return doAction(action);
    }

    public boolean mkDir(Path path) {
        RichPath richPath = new RichPath(path, true);
        NamespaceAction action = new NamespaceAction(ActionType.ADD, new RichPath[]{richPath});

        return doAction(action);
    }

    public boolean rename(Path srcPath, Path dstPath) {
        RichPath srcRichPath = new RichPath(srcPath, true); //  true or false all is righ
        RichPath dstRichPath = new RichPath(dstPath, true); //  true or false all is righ
        NamespaceAction action = new NamespaceAction(ActionType.RENAME, new RichPath[]{srcRichPath, dstRichPath});

        return doAction(action);
    }

    public boolean delete(Path path) {
        //TODO test path is file or directory
        PathNode pathNode = PathNode.findPathNodeByPath(rootPathNodes, path.getPath());
        if (pathNode == null) {
            return false;
        }

        RichPath richPath = new RichPath(path, true); //  true or false all is right
        NamespaceAction action = new NamespaceAction(ActionType.DELETE, new RichPath[]{richPath});
        if (!pathNode.isAllowsChildren()) {
            //TODO find all storageServers own this chunks of path
            FileImage fileImage = fileImageMap.get(path);
            ChunkImage chunkImage = fileImage.getChunkImage(0);
            Set<StorageNode> storageNodes = chunkImage.getStorageNodes();
            for (StorageNode node : storageNodes) {
                ServiceShop serviceShop = new DefaultServiceShop(new DefaultClientServiceFactory(new InetSocketAddress(node.getLocation(), 7777)));
                StorageServerService storageServerService = serviceShop.getService(StorageServerService.class); //TODO to be provided
                storageServerService.deleteFile(path);
                serviceShop.returnService(storageServerService);
            }
        }
        return doAction(action);
    }

    private boolean doAction(NamespaceAction action) {
        boolean result = ActionExecutorFactory.getActionExecution(action).execute(rootPathNodes, action);
        if (result) {
            try {
                logWriter.recordAction(action);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return result;
    }

    public FileStatus[] listFileStatus(Path path) {
        if (Paths.ROOT.equals(path)) {
            return convertToFileStatus(rootPathNodes);
        } else {
            PathNode pathNode = PathNode.findPathNodeByPath(rootPathNodes, path.getPath());
            if (pathNode.isAllowsChildren()) {
                return convertToFileStatus(pathNode.getChildren());
            }
        }
        return new FileStatus[0];
    }

    private FileStatus[] convertToFileStatus(Set<PathNode> nodes) {
        FileStatus[] fileStatuses = new FileStatus[nodes.size()];

        Iterator<PathNode> it = nodes.iterator();
        for (int i = 0; i < fileStatuses.length && it.hasNext(); i++) {
            fileStatuses[i] = convertToFileStatus(it.next());
        }

        return fileStatuses;
    }

    private FileStatus convertToFileStatus(PathNode node) {
        return new FileStatus(node.getPath(), node.isAllowsChildren());
    }
}
