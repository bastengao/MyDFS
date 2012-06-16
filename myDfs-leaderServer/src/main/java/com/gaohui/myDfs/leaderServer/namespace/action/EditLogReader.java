package com.gaohui.myDfs.leaderServer.namespace.action;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Administrator
 * Date: 11-4-25 Time: 下午11:38
 *
 * @author Basten Gao
 */
public class EditLogReader {


    public static final String EDIT_LOG = "namespace/edit.log";

    /**
     * @throws IOException
     */
    public EditLogReader() throws IOException {

    }

    /**
     * 返回action 的list,如果没有action,返回空的list.
     *
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public List<NamespaceAction> getActions() throws ClassNotFoundException, IOException {
        ensureFileExists();
        List<NamespaceAction> namespaceActions = new LinkedList<NamespaceAction>();
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(EDIT_LOG));  //ClassLoader.getSystemResource("edit.log").getFile()
            for (NamespaceAction action = (NamespaceAction) inputStream.readObject(); action != null; action = (NamespaceAction) inputStream.readObject()) {
                namespaceActions.add(action);
            }
        } catch (EOFException ex) {
            //if reach end of file ,do nothing
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return namespaceActions;
    }


    private void ensureFileExists() {
        File imageDirectory = new File("namespace");
        if (!imageDirectory.exists()) {
            imageDirectory.mkdir();
        }
        File imageFile = new File(EDIT_LOG);
        if (!imageFile.exists()) {
            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
