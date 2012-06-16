package com.gaohui.rpc.codec.xml.xstream;

import com.gaohui.rpc.codec.Codec;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 利用XStream的编码器
 * User: Administrator
 * Date: 11-1-16 Time: 上午10:03
 *
 * @param <T> 被解析的类型
 * @author Basten Gao
 */
public class XStreamCodec<T> implements Codec<T> {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(XStreamCodec.class);

    @Override
    public byte[] encode(T t) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        XStream xStream = new XStream();
        xStream.toXML(t, buffer);
        LOGGER.debug("{}", new String(buffer.toByteArray()));
        return buffer.toByteArray();
    }

    @Override
    public T decode(byte[] data) {
        LOGGER.debug("{}", new String(data));
        ByteArrayInputStream buffer = new ByteArrayInputStream(data);
        XStream xStream = new XStream();
        return (T) xStream.fromXML(buffer);
    }
}
