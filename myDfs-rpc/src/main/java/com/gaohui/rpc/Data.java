package com.gaohui.rpc;

/**
 * User: Administrator
 * Date: 11-1-5 Time: 下午4:31
 *
 * @author Bastengao
 */

/**
 * 数据
 * <P>Immutable
 */
public class Data {

    private final Class type;
    private final Object data;

    /**
     * 构造Data,此类是不可变的。对传入的data会对他进行复制，而不是直接引用。
     *
     * @param type
     * @param data
     */
    public Data(Class type, Object data) {
        this.type = type;
        this.data = data;
    }

    /**
     * 返回data的副本
     *
     * @return
     */
    public Object getData() {
        return data;
    }

    public Class getType() {
        return type;
    }
}
