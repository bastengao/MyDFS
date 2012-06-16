package com.gaohui.rpc.codec;

/**
 * 编码转换类。负责将对象编码为字节数组，同时相反将字节数组解码为对象
 * <p> 实现都应该保证线程安全
 * User: Administrator
 * Date: 11-1-15 Time: 下午11:45
 *
 * @param <T>
 * @author Basten Gao
 */
public interface Codec<T> {

    byte[] encode(T t);

    T decode(byte[] data);
}
