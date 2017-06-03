package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhk on 17/5/21.
 */
public interface SeckillDao {
    /**
     * @param seckillId
     * @param createTime
     * @return 如果int>1，表示更新的记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);

    /**
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

    void executeSeckill(Map<String,Object> map);

}
