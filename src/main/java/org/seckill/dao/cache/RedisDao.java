package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by zhk on 17/6/1.
 */
public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JedisPool jedisPool;

    public RedisDao(String ip, int port) {
        this.jedisPool = new JedisPool(ip, port);

    }

    private RuntimeSchema<Seckill> runtimeSchema = RuntimeSchema.createFrom(Seckill.class);

    public Seckill getSeckill(long seckillId) {
        //redis操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                /**
                 * 实现自定义序列化
                 * 1、从jedis中获得对象字节码 2、通过protostuff反序列化转化成对象
                 * protostuff
                 */
                String key = "seckill:" + seckillId;
                byte[] bytes = jedis.get(key.getBytes());
                //缓存重新获得
                if (bytes != null) {
                    /**
                     *获得对象，用bytes按照runtimeSchema进行初始化反序列化
                     */
                    Seckill seckill = runtimeSchema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, runtimeSchema);
                    return seckill;
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
                jedis.close();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return null;

    }

    public String putSeckill(Seckill seckill) {
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                //序列化 set Object(seckill) -> byte[]
                String key = "seckill:" + seckill.getSeckillId();
                byte[] keyBytes = key.getBytes();
                byte[] seckillBytes = ProtostuffIOUtil.toByteArray(seckill, runtimeSchema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int expires = 60 * 60;
                String result=jedis.setex(keyBytes, expires, seckillBytes);
                return result;
            } finally {
                jedis.close();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

}
