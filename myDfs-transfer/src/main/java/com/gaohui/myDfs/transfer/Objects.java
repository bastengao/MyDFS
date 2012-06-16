package com.gaohui.myDfs.transfer;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * User: Administrator
 * Date: 11-3-3 Time: 下午6:54
 *
 * @author Basten Gao
 */
public final class Objects {

    protected Objects() {
    }

    /**
     * 将字节利用序列化读为Object对象
     *
     * @param data
     * @return
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
    public static Object readObject(byte[] data) {
        ObjectInput input = null;
        try {
            input = new ObjectInputStream(new ByteArrayInputStream(data));
            return input.readObject();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将java对象进行序列化，并返回序列化数组
     *
     * @param object
     * @return
     * @throws IOException
     */
    public static byte[] writeObject(Object object) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(array);
            objectOutputStream.writeObject(object);
            objectOutputStream.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        try {
            return array.toByteArray();
        } finally {
            try {
                array.close();
            } catch (IOException e) {
                e.printStackTrace();   //TODO
            }
        }
    }
}
