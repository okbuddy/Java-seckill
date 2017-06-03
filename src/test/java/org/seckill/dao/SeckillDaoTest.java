package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zhk on 17/5/22.
 */

/**
 * 整合spring和Junit，运行junit时使用spring的Ioc
 * spring-test  junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")

public class SeckillDaoTest {
    @Resource
    SeckillDao seckillDao;
    @Test
    public void reduceNumber() throws Exception {
            long id=1000;
        Date date = new Date();
        int count=seckillDao.reduceNumber(id,date);
        System.out.println("update: "+count);

    }

    @Test
    public void queryById() throws Exception {
        long a=1001;
        Seckill seckill = seckillDao.queryById(a);
        System.out.println(seckill);

    }

    @Test
    public void queryAll() throws Exception {
        List<Seckill> list = seckillDao.queryAll(0,100);
        for (Seckill s :
                list) {
            System.out.println(s.getName());
        }
    }

}