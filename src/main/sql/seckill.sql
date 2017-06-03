DELIMITER //

CREATE PROCEDURE seckill.executeSeckill(IN  v_seckill_id BIGINT, IN v_user_phone BIGINT,
                                        OUT result       INT
)
  BEGIN
    DECLARE insertCount INT;
    DECLARE updateCount INT;
    DECLARE createdTime TIMESTAMP;
    SET createdTime = now();

    START TRANSACTION;
    INSERT IGNORE INTO success_killed (seckill_id, user_phone, state, create_time) VALUES (v_seckill_id, v_user_phone,
                                                                                           1, createdTime
    );
    SELECT row_count()
    INTO insertCount;
    IF (insertCount = 0)
    THEN
      #       repeat seckill
      ROLLBACK;
      SET result = -1;
    ELSEIF (insertCount < 0)
      THEN
        #         inner error
        ROLLBACK;
        SET result = -2;
    ELSEIF (insertCount = 1)
      THEN
        #       update the seckill table

        UPDATE seckill
        SET number = number - 1
        WHERE
          seckill_id = v_seckill_id AND
          createdTime > seckill.begin_time AND
          createdTime < seckill.end_time AND
          seckill.number > 0;
        SELECT row_count()
        INTO updateCount;
        IF (updateCount = 0)
        THEN
          #         seckill close
          ROLLBACK;
          SET result = 0;
        ELSEIF (updateCount < 0)
          THEN
            #             inner update error
            ROLLBACK;
            SET result = -2;
        ELSE
          #           insert and update successfully
          COMMIT;
          SET result = 1;
        END IF;
    END IF;
  END;
//

# after create procedure
DELIMITER ;
SET @result = -3;
CALL executeSeckill(1001, 11111222223, @result);
SELECT @result;


