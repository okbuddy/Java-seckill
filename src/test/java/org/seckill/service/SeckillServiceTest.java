package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by zhk on 17/5/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
    //execute procedure to reduce and insert database
    @Test
    public void executeSeckillProcedure() throws Exception {
        long seckillId = 1003;
        long userPhone = 12547896999L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution seckillExecution = seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
            System.out.println(seckillExecution.getStateInfo()+" : "+seckillExecution.getSeckillId());


        }


    }

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        for (Seckill s :
                list) {
            logger.info("seckill: {}", s);
        }
    }

    @Test
    public void getById() throws Exception {
        Seckill seckill = seckillService.getById(1000);
        logger.info("seckill: {}", seckill);
    }

    @Test
    public void exportSeckillUrlandExcuteSeckill() throws Exception {
        long seckillId = 1002;
        long phone = 12548796359L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        System.out.println(exposer);
        if (!exposer.isExposed()) {
            logger.warn("not expose: {}", exposer);

        } else {
            try {

                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, phone, exposer.getMd5());
                logger.info("seckillExecution: {}", seckillExecution);
            } catch (RepeatKillException e1) {
                logger.error("repeat:{}", e1.getMessage());
            } catch (SeckillCloseException e2) {
                logger.error("close:{}", e2.getMessage());
            } catch (SeckillException e3) {

                //数据篡改和其他内部错误如SQL书写错误等
                //其实除了MD5不同导致数据篡改，其他内部错误应该抛出，不能通过测试
                logger.error("seckillException:{}", e3.getMessage());
            }

        }
    }

}