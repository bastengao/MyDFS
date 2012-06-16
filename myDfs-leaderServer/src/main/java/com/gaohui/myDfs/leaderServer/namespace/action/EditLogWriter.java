package com.gaohui.myDfs.leaderServer.namespace.action;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * User: Administrator
 * Date: 11-4-25 Time: 下午10:36
 *
 * @author Basten Gao
 */
public class EditLogWriter {
    private ObjectOutputStream outputStream = null;

    public EditLogWriter() throws IOException {
        outputStream = new ObjectOutputStream(new FileOutputStream(EditLogReader.EDIT_LOG)); //ClassLoader.getSystemResource("edit.log").getFile()
    }

    public void recordAction(NamespaceAction action) throws IOException {
        outputStream.writeObject(action);
    }


    public void close() throws IOException {
        outputStream.close();
    }
}
