package com.gaohui.myDfs.storageServer;

import com.gaohui.myDfs.core.Chunk;
import com.gaohui.myDfs.core.Path;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

/**
 * User: Administrator
 * Date: 11-2-3 Time: 下午11:39
 *
 * @author Basten Gao
 */
public class ChunkUtilTest {
    @Test
    public void testGetChunk() {
        String dir = "/a/b";
        String fileName = "Abc.java.123.par";
        Chunk chunk = ChunkUtil.getChuck(dir, fileName);
        Assert.assertNotNull(chunk);
        Assert.assertEquals(123, chunk.getOrdinal());
        Assert.assertEquals("/a/b/Abc.java", chunk.getOwner().getPath());
    }

    @Test
    public void testGetChunk2() {
        String dir = "/a/b";
        String fileName = "Abc.java.123.xxx";
        Chunk chunk = ChunkUtil.getChuck(dir, fileName);
        Assert.assertNull(chunk);
    }

    @Test
    public void testParseChunk() {
        URL rootPath = ClassLoader.getSystemResource("chunks");
        Chunk chunk = new Chunk(new Path("/a/b/c.java"), 0);
        try {
            File file = ChunkUtil.parseChunk(rootPath.getFile(), chunk);
            System.out.println(file);
            Assert.assertNotNull(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testParserChunkNotExists() throws FileNotFoundException {
        URL rootPath = ClassLoader.getSystemResource("chunks");
        Chunk chunk = new Chunk(new Path("/a/b/noExists.java"), 0);
        File file = ChunkUtil.parseChunk(rootPath.getFile(), chunk);
    }

    @Test(expected = FileNotFoundException.class)
    public void testParserChunkNotFile() throws FileNotFoundException {
        URL rootPath = ClassLoader.getSystemResource("chunks");
        Chunk chunk = new Chunk(new Path("/a/b/directory"), 0);
        File file = ChunkUtil.parseChunk(rootPath.getFile(), chunk);
        System.out.println(file);
    }
}
