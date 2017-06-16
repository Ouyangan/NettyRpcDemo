package com.hunt.util;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ouyangan on 2017/6/14.
 */
public class SerializationUtil {
    private static final Logger log = LoggerFactory.getLogger(SerializationUtil.class);
    //缓存
    private static final Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();
    //可以使用缓存的Objenesis
    private static Objenesis objenesis = new ObjenesisStd(true);


    private static <T> Schema<T> getSchema(Class<T> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
        if (schema == null) {
            schema = RuntimeSchema.createFrom(cls);
            cachedSchema.put(cls, schema);
        }
        return schema;
    }

    public static <T> byte[] serialize(T t) {
        Class<T> cls = (Class<T>) t.getClass();
        LinkedBuffer allocate = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        Schema<T> schema = getSchema(cls);
        byte[] bytes;
        try {
            bytes = ProtobufIOUtil.toByteArray(t, schema, allocate);
        } finally {
            allocate.clear();
        }
        log.debug("序列化{}", cls.getSimpleName());
        return bytes;
    }

    public static <T> T deserialize(byte[] bytes, Class<T> cls) {
        T message = objenesis.newInstance(cls);
        Schema<T> schema = getSchema(cls);
        ProtobufIOUtil.mergeFrom(bytes, message, schema);
        log.debug("反序列化{}", cls.getSimpleName());
        return message;
    }

}
