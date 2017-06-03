package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by zhk on 17/5/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledDaoTest {
    @Resource
    SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() throws Exception {
            long id=1001;
            long phone=12326665984L;
            int count=successKilledDao.insertSuccessKilled(id,phone);
        System.out.println("count: "+count);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long id=1001;
        long phone=12326665984L;
        SuccessKilled successKilled= successKilledDao.queryByIdWithSeckill(id,phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());

    }

}