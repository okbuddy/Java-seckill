package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by zhk on 17/5/24.
 */
@Controller
@RequestMapping(value = "/seckill")
public class ServiceController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SeckillService seckillService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getSeckillList(Model model) {
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";
    }

    @RequestMapping(value = "/{seckillId}/detail")
    public String getById(@PathVariable("seckillId") Long seckillId, Model model) {
        if (seckillId == null) {
            return "redirect:/seckill/list";

        }
        Seckill seckill = seckillService.getById(seckillId);
        if (seckill == null) {
            return "forward:/seckill/list";

        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    //    ajax json
    @RequestMapping(value = "/{seckillId}/exposer", method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result = new SeckillResult<Exposer>(true, "init result");
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, "error:" + e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{seckillId}/{md5}/execution", method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable Long seckillId,
                                                   @PathVariable String md5,
                                                   @CookieValue(value = "killPhone") Long userPhone) {
        //userPhone为空的验证改在serviceImpl中完成
        //主动检测runtimeException，对异常情况返回不同seckillExecution
        SeckillExecution seckillExecution;
//        try {

        seckillExecution = seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
        return new SeckillResult<SeckillExecution>(true, seckillExecution);
//        }

//        catch (SeckillCloseException e) {
//            seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.END);
//            return new SeckillResult<SeckillExecution>(true, seckillExecution);
//        } catch (RepeatKillException e) {
//            seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
//            return new SeckillResult<SeckillExecution>(true, seckillExecution);
//
//        } catch (SeckillException e) {
//            logger.error(e.getMessage(), e);
//            seckillExecution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
//            return new SeckillResult<SeckillExecution>(true, seckillExecution);
//
//        }


    }

    @RequestMapping(value = "/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time() {
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }

}
