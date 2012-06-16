package com.gaohui.myDfs.storageServer;

import com.gaohui.myDfs.core.Chunk;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Set;

/**
 * User: Administrator
 * Date: 11-2-11 Time: 下午9:26
 *
 * @author Basten Gao
 */
public class ScannerTest {
    @Test
    public void test() {
        URL url = ClassLoader.getSystemResource("chunks");
        Set<Chunk> chunks = Scanner.getChunks(url.getFile());
        System.out.println(chunks);
    }
}
