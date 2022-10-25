package com.ball.app.controller.match;

import com.ball.app.controller.match.vo.ScheduleDetailResp;
import com.ball.app.controller.match.vo.ScheduleReq;
import com.ball.app.controller.match.vo.ScheduleResp;
import com.ball.base.util.BeanUtil;
import com.ball.base.util.DateUtil;
import com.ball.biz.match.entity.Schedules;
import com.ball.biz.match.service.ISchedulesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author fanyongpeng
 * @date 10/25/22
 **/
@Api(tags = "赛程")
@RestController
@RequestMapping("/app/schedule")
public class ScheduleController {

    @Autowired
    ISchedulesService iSchedulesService;

    @ApiOperation("查询赛程")
    @RequestMapping(value = "query",method = {RequestMethod.GET,RequestMethod.POST})
    public List<ScheduleResp> query(@RequestBody @Valid ScheduleReq req) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        if (req.getDate() != null ) {
            startDate =  req.getDate().withHour(0).withMinute(0).withSecond(0);
            endDate = startDate.plusDays(1);
        } else {
            startDate =  LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            endDate = startDate.plusDays(7);
        }

        List<Schedules> schedules = iSchedulesService.lambdaQuery().between(Schedules::getMatchDate, startDate, endDate).ge(Schedules::getStatus, 0).list();

        return (schedules.stream().collect(Collectors.groupingBy(s-> DateUtil.formatWeek(s.getMatchDate()))))
                .entrySet().stream().map(c-> {
                    ScheduleResp s = new ScheduleResp();
                    s.setDate(c.getKey());
                    s.setList(c.getValue().stream().map(d-> BeanUtil.copy(d, ScheduleDetailResp.class)).collect(Collectors.toList()));
                    return s;
                }).collect(Collectors.toList());
    }
}
