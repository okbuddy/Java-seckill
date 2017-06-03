CREATE DATABASE seckill;
USE seckill;
-- 创建表
CREATE TABLE seckill (
  seckill_id  BIGINT       NOT NULL AUTO_INCREMENT
  COMMENT '商品id',
  name        VARCHAR(120) NOT NULL
  COMMENT '商品名称',
  number      INT          NOT NULL
  COMMENT '商品数量',
  create_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '秒杀创建时间',
  begin_time  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '秒杀开始时间',
  end_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '秒杀结束时间',
  PRIMARY KEY (seckill_id),
  KEY idx_begin_time(begin_time),
  KEY idx_end_time(end_time),
  KEY idx_create_time(create_time)
)
  ENGINE = innodb
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8
  COMMENT = '秒杀库存库';

-- 初始化数据
INSERT INTO
  seckill (name, number, begin_time, end_time)
VALUES
  ('p1', 100, '2017-05-21 11:06:48', '2017-05-22 11:06:48'),
  ('p2', 200, '2017-05-21 11:06:48', '2017-05-22 11:06:48'),
  ('p3', 300, '2017-05-21 11:06:48', '2017-05-22 11:06:48'),
  ('p4', 400, '2017-05-21 11:06:48', '2017-05-22 11:06:48');

-- 秒杀成功明细表
CREATE TABLE success_killed (
  seckill_id  BIGINT    NOT NULL
  COMMENT '商品id',
  user_phone  BIGINT    NOT NULL
  COMMENT '用户号码',
  state       TINYINT   NOT NULL DEFAULT -1
  COMMENT '秒杀状态，-1：失败，0：成功，1：付款，2：发货',
  create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
  COMMENT '明细创建时间',
  PRIMARY KEY (seckill_id, user_phone),
  KEY idx_create_time(create_time)
)
  ENGINE = innodb
  DEFAULT CHARSET = utf8
  COMMENT = '秒杀成功明细表';

SELECT *
FROM seckill;