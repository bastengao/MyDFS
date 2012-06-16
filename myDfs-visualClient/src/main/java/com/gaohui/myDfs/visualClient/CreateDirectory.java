package com.gaohui.myDfs.visualClient;

import java.io.File;

/**
 * User: Administrator
 * Date: 11-5-2 Time: 下午3:14
 *
 * @author Basten Gao
 */
public class CreateDirectory {
    public static void main(String[] args) {
        File file = new File("image");
        file.mkdir();
    }
}
