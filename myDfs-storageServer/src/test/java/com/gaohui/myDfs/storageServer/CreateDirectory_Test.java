package com.gaohui.myDfs.storageServer;

import java.io.File;

/**
 * User: Administrator
 * Date: 11-5-2 Time: 下午3:08
 *
 * @author Basten Gao
 */
public class CreateDirectory_Test {
    public static void main(String[] args) {
        File file = new File("image");
        file.mkdir();
    }
}
