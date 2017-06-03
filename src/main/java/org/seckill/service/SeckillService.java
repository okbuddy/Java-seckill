package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * Created by zhk on 17/5/23.
 */
public interface SeckillService {
    List<Seckill> getSeckillList();
    Seckill getById(long seckillId);

    /**
     * 暴露秒杀接口
     * 或者输出秒杀时间
     * @param seckillId
     * @return
     */
    Exposer exportSeckillUrl(Long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(Long seckillId, Long userPhone, String md5)
    throws RepeatKillException,SeckillCloseException,SeckillException;

    SeckillExecution executeSeckillProcedure(Long seckillId,Long userPhone,String md5);
}
