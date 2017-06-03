package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

import java.util.List;

/**
 * Created by zhk on 17/5/21.
 */
public interface  SuccessKilledDao {
    /**
     *
     * @param seckill
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);


    /**
     * 根据id查询successKilled并携带秒杀产品对象实体
     * @param seckillId
     * @return
     */
    SuccessKilled  queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);

}
