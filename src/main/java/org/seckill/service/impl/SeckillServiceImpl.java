package org.seckill.service.impl;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import sun.text.resources.cldr.saq.FormatData_saq;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhk on 17/5/23.
 */
@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;
    private final String salt = "∂ßƒ345>££¢$%^^HJKdf/.'dfs";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(Long seckillId) {
/**
 *  优化点：缓存优化,maintain the consistency of seckill on the base of expires
 *  get from cache
 *  if null
 *     get db
 *     else
 *     put cache
 *     locgoin
 *通过redis缓存，先访问redis，再访问数据库，存入redis缓存
 */
        Exposer exposer;
        //1.access the redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            //2.if null, access the database
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                exposer = new Exposer(false, seckillId);
                return exposer;
            } else {
                //3.put it in the redis
                redisDao.putSeckill(seckill);
            }
        }

        long now = new Date().getTime();
        long start = seckill.getBeginTime().getTime();
        long end = seckill.getEndTime().getTime();
        if (now < start || now > end) {
            exposer = new Exposer(false, seckillId, now, start, end);
            return exposer;
        }
        String md5 = getMd5(seckillId);
        exposer = new Exposer(true, md5, seckillId);
        return exposer;
    }

    String getMd5(long seckillId) {
        String base = seckillId + "/" + salt;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }

    /**
     * 减库存，插入秒杀信息
     */
    @Override
    @Transactional
    public SeckillExecution executeSeckill(Long seckillId, Long userPhone, String md5)
            throws RepeatKillException, SeckillCloseException, SeckillException {
        if (md5 == null || !md5.equals(getMd5(seckillId))) {
            throw new SeckillException("data rewrite");
        } else if (userPhone == null) {
            throw new SeckillException("userPhone is null");
        } else {
            try {

                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeat");
                } else {
                    //reduce the lock time of the update operation
                    int updateCount = seckillDao.reduceNumber(seckillId, new Date());
                    if (updateCount <= 0) {
                        throw new SeckillCloseException("seckill close");

                    }

                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }


            } catch (SeckillCloseException e1) {

                throw e1;
            } catch (RepeatKillException e2) {

                throw e2;
            } catch (Exception e3) {

                logger.error(e3.getMessage(), e3);
                //编译器异常变为运行期异常
                throw new SeckillException("inner error: " + e3.getMessage());
            }


        }
    }

    @Override
    public SeckillExecution executeSeckillProcedure(Long seckillId, Long userPhone, String md5) {
        //String equals to judge the content equal or not
        if (md5 == null || !md5.equals(getMd5(seckillId))) {
            return new SeckillExecution(seckillId, SeckillStateEnum.DATA_REWRITE);

        } else {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("seckillId", seckillId);
            map.put("userPhone", userPhone);
            map.put("result", null);

            try {

                seckillDao.executeSeckill(map);
                int result = MapUtils.getInteger(map, "result", -2);

                if (result == 1) {

                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);

                } else {

                    return new SeckillExecution(seckillId, SeckillStateEnum.stateOf(result));

                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
                return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);

            }


        }

    }
}
