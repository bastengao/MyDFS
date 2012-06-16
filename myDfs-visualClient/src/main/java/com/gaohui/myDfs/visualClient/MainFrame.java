package com.gaohui.myDfs.visualClient;

import com.gaohui.myDfs.client.FileSystem;
import com.gaohui.myDfs.client.FileSystemImpl;
import com.gaohui.myDfs.core.FileStatus;
import com.gaohui.myDfs.core.Path;
import com.gaohui.myDfs.core.Paths;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * User: Administrator
 * Date: 11-4-28 Time: 下午9:26
 *
 * @author Basten Gao
 */
public class MainFrame extends JFrame {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainFrame.class);

    public MainFrame() throws HeadlessException {
        super("分布式文件系统客户端");
        super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            super.setIconImage(ImageIO.read(ClassLoader.getSystemResourceAsStream("Shamrock.bmp")));
        } catch (IOException e) {
            e.printStackTrace(); //TODO
        }

        final JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        super.getContentPane().add(statusPanel, BorderLayout.SOUTH);

        final JTree tree = new JTree();
        final PopupMenu menu = new PopupMenu();
        final MenuItem downloadItem = new MenuItem("download");
        final MenuItem addDirItem = new MenuItem("mkDir");
        final MenuItem addFileItem = new MenuItem("uploadFile");
        final MenuItem deleteItem = new MenuItem("delete");
        menu.add(downloadItem);
        menu.add(addDirItem);
        menu.add(addFileItem);
        menu.add(deleteItem);
        tree.add(menu);
        tree.addMouseListener(new MouseAdapter() {
            private Point point = null;
            private boolean selected = false;
            private int row = -1;

            @Override
            public void mousePressed(MouseEvent e) {
                selected = false;
                if (e.getButton() == MouseEvent.BUTTON3) {
                    row = tree.getRowForLocation(e.getX(), e.getY());
                    if (row != -1) {
                        tree.setSelectionRow(row);
                        point = e.getPoint();
                        selected = true;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (selected) {
                        TreePath treePath = tree.getSelectionPath();
                        FileNode fileNode = (FileNode) treePath.getLastPathComponent();
                        FileStatus fileStatus = fileNode.getFileStatus();
                        downloadItem.setEnabled(false);
                        addDirItem.setEnabled(false);
                        addFileItem.setEnabled(false);
                        if (fileStatus.isDirectory()) {
                            addDirItem.setEnabled(true);
                            addFileItem.setEnabled(true);
                        } else {
                            downloadItem.setEnabled(true);
                        }
                        menu.show(tree, point.x, point.y);
                    }
                }
            }
        });

        final TreeCellRenderer treeCellRenderer = tree.getCellRenderer();
        TreeCellRenderer newTreeCellRenderer = (TreeCellRenderer) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{TreeCellRenderer.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("getTreeCellRendererComponent")) {
                    Object value = args[1];
                    if (value instanceof FileNode) {
                        FileNode node = (FileNode) value;
                        Path path = node.getFileStatus().getPath();
                        String[] names = Paths.breaks(path.getPath());
                        if (names.length >= 1) {
                            args[1] = names[names.length - 1];
                        }
                    }
                }
                return method.invoke(treeCellRenderer, args);
            }
        });

        TreeCellRenderer new2TreeCellRenderer = new TreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                if (value instanceof FileNode) {
                    FileNode node = (FileNode) value;
                    Path path = node.getFileStatus().getPath();
                    String[] names = Paths.breaks(path.getPath());
                    if (names.length >= 1) {
                        String renderValue = names[names.length - 1];
                        return treeCellRenderer.getTreeCellRendererComponent(tree, renderValue, selected, expanded, leaf, row, hasFocus);
                    }
                }
                return treeCellRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
            }
        };
        tree.setCellRenderer(newTreeCellRenderer);

        final FileSystem fileSystem = new FileSystemImpl(new InetSocketAddress(8888));

        tree.addTreeExpansionListener(new TreeExpansionListener() {
            @Override
            public void treeExpanded(TreeExpansionEvent event) {
                LOGGER.debug("treeExpanded:{}", event);
                TreePath treePath = event.getPath();
                FileNode fileNode = (FileNode) treePath.getLastPathComponent();
                expandFileNode(fileSystem, fileNode);
                reloadTreeNode(fileNode, tree);
            }

            @Override
            public void treeCollapsed(TreeExpansionEvent event) {
                LOGGER.debug("treeCollapsed:{}", event);
            }
        });

        addDirItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath treePath = tree.getSelectionPath();
                FileNode fileNode = (FileNode) treePath.getLastPathComponent();
                Path path = fileNode.getFileStatus().getPath();
                String dirName = JOptionPane.showInputDialog(MainFrame.this, "目录名");
                Path newDirPath = null;
                if (!dirName.trim().equals("")) {
                    if (Paths.ROOT.equals(path)) {
                        newDirPath = new Path(path.getPath() + dirName);
                    } else {
                        newDirPath = new Path(path.getPath() + "/" + dirName);
                    }
                    if (fileSystem.mkDir(newDirPath)) {
                        FileNode newDirNode = new FileNode(newDirPath, fileSystem, new FileStatus(newDirPath, true));
                        fileNode.add(newDirNode);
                        reloadTreeNode(fileNode, tree);
                    }
                }
            }
        });

        addFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath treePath = tree.getSelectionPath();
                final FileNode fileNode = (FileNode) treePath.getLastPathComponent();
                Path path = fileNode.getFileStatus().getPath();
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(MainFrame.this);
                File file = null;
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                }
                final String fileName = JOptionPane.showInputDialog(MainFrame.this, "文件名");
                final Path newFilePath = new Path(path.getPath() + "/" + fileName);
                FileStatus fileStatus = fileSystem.getFileStatus(newFilePath);
                if (fileStatus != null) {
                    return;
                }
                JLabel statusLabel = new JLabel("正在传输...");
                JProgressBar progressBar = new JProgressBar();
                progressBar.setIndeterminate(true);
                statusPanel.add(progressBar);
                statusPanel.add(statusLabel);
                final File finalFile = file;
                SwingWorker worker = new SwingWorker() {

                    @Override
                    protected Object doInBackground() throws Exception {
                        OutputStream outputStream = fileSystem.createFile(newFilePath);
                        InputStream inputStream = new FileInputStream(finalFile);
                        ByteStreams.copy(new FileInputStream(finalFile), outputStream);
                        outputStream.close();
                        inputStream.close();
                        return null;
                    }

                    @Override
                    protected void done() {
                        FileNode newFileNode = new FileNode(newFilePath, fileSystem, new FileStatus(newFilePath, false));
                        fileNode.add(newFileNode);
                        reloadTreeNode(fileNode, tree);
                        statusPanel.removeAll();
                        MainFrame.this.validateTree();
                    }
                };
                worker.execute();
                MainFrame.this.validateTree();
            }
        });

        downloadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath treePath = tree.getSelectionPath();
                FileNode fileNode = (FileNode) treePath.getLastPathComponent();
                Path path = fileNode.getFileStatus().getPath();
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogType(JFileChooser.SAVE_DIALOG);
                int returnVal = chooser.showOpenDialog(MainFrame.this);
                File file = null;
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    file = chooser.getSelectedFile();
                }

                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    inputStream = fileSystem.openFile(path);
                    outputStream = new FileOutputStream(file);
                    ByteStreams.copy(inputStream, outputStream);

                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                        outputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath treePath = tree.getSelectionPath();
                FileNode fileNode = (FileNode) treePath.getLastPathComponent();
                Path path = fileNode.getFileStatus().getPath();
                JOptionPane.showMessageDialog(MainFrame.this, path);
                if (fileSystem.delete(path)) {
                    reloadTreeNode((FileNode) treePath.getParentPath().getLastPathComponent(), tree);
                    ((DefaultTreeModel) tree.getModel()).reload();
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this, "删除失败");
                }
            }
        });

        Path rootPath = new Path("/");
        FileNode rootNode = new FileNode(rootPath, null, new FileStatus(rootPath, true));
        expandFileNode(fileSystem, rootNode);
        FileTreeModel treeModel = new FileTreeModel(rootNode);

        tree.setModel(treeModel);

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fileSystem.close();
            }
        });
        super.getContentPane().add(new JScrollPane(tree), BorderLayout.CENTER);
        super.pack();
        super.setLocationRelativeTo(null);
    }

    private void reloadTreeNode(FileNode fileNode, JTree tree) {
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        treeModel.reload(fileNode);
    }

    private void expandFileNode(FileSystem fileSystem, FileNode parentFileNode) {
        Path path = parentFileNode.getFileStatus().getPath();
        FileStatus[] fileStatuses = fileSystem.listFileStatus(path);
        parentFileNode.removeAllChildren();
        for (FileStatus fileStatus : fileStatuses) {
            FileNode fileNode = new FileNode(fileStatus.getPath(), fileSystem, fileStatus);
            parentFileNode.add(fileNode);
        }
    }

    public static class FileTreeModel extends DefaultTreeModel {
        public FileTreeModel(TreeNode root) {
            super(root);
        }

        public FileTreeModel(TreeNode root, boolean asksAllowsChildren) {
            super(root, asksAllowsChildren);
        }
    }

    /**
     * 树的节点,代表文件或者目录
     */
    public static class FileNode extends DefaultMutableTreeNode {
        private FileSystem clientService = null;

        public FileStatus getFileStatus() {
            return fileStatus;
        }

        private FileStatus fileStatus;

        public FileNode(FileSystem clientService, FileStatus fileStatus) {
            this.clientService = clientService;
            this.fileStatus = fileStatus;
        }

        public FileNode(Object userObject, FileSystem clientService, FileStatus fileStatus) {
            super(userObject);
            this.clientService = clientService;
            this.fileStatus = fileStatus;
        }

        public FileNode(Object userObject, boolean allowsChildren, FileSystem clientService, FileStatus fileStatus) {
            super(userObject, allowsChildren);
            this.clientService = clientService;
            this.fileStatus = fileStatus;
        }

        @Override
        public boolean isLeaf() {
            return fileStatus.isFile();
        }

        @Override
        public boolean getAllowsChildren() {
            return fileStatus.isDirectory();
        }
    }
}
