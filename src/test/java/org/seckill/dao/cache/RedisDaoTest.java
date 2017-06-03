package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zhk on 17/6/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    @Autowired
    SeckillDao seckillDao;
    @Autowired
    RedisDao redisDao;

    @Test
    public void getAndSetSeckill() throws Exception {
        long seckillId = 1002;
        Seckill seckill = redisDao.getSeckill(seckillId);
        System.out.println(seckill);
        if (seckill == null) {
            Seckill seckill1 = seckillDao.queryById(seckillId);
            String result = redisDao.putSeckill(seckill1);
            System.out.println(result);
            seckill1 = redisDao.getSeckill(seckillId);
            System.out.println(seckill1);

        }


    }

}