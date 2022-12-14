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
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Comparator;
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
    @PostMapping(value = "query")
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

        List<Schedules> schedules = iSchedulesService.lambdaQuery().between(Schedules::getMatchDate, startDate, endDate)
                .and(i-> i.eq(Schedules::getHasOdds,true).or().eq(Schedules::getHasOddsScore,true))
                .ge(Schedules::getStatus, 0).list();

        List<ScheduleResp> res = (schedules.stream().collect(Collectors.groupingBy(s-> DateUtil.formatWeek(s.getMatchDate()))))
                .entrySet().stream().map(c-> {
                    ScheduleResp s = new ScheduleResp();
                    s.setDate(c.getKey());

                    s.setList(c.getValue().stream().map(d-> {
                        ScheduleDetailResp detailResp = BeanUtil.copy(d, ScheduleDetailResp.class);
                        if(StringUtil.isNotBlank(detailResp.getAwayNameZh()) && StringUtil.isNotBlank(detailResp.getHomeNameZh())) {
                            detailResp.setAwayName(String.format("%s(%s)",detailResp.getAwayNameZh(),detailResp.getAwayName()));
                            detailResp.setHomeName(String.format("%s(%s)",detailResp.getHomeNameZh(),detailResp.getHomeName()));
                        }
                        return detailResp;
                    }).collect(Collectors.toList()));
                    s.getList().sort(new Comparator<ScheduleDetailResp>() {
                        @Override
                        public int compare(ScheduleDetailResp o1, ScheduleDetailResp o2) {
                            return  o1.getMatchDate().compareTo(o2.getMatchDate()) ;
                        }
                    });
                    s.setTime(s.getList().get(0).getMatchDate().withHour(0).withMinute(0).withSecond(0));
                    return s;
                }).collect(Collectors.toList());
        res.sort(new Comparator<ScheduleResp>() {
            @Override
            public int compare(ScheduleResp o1, ScheduleResp o2) {
                return o1.getTime().compareTo(o2.getTime()) ;
            }
        });
        return res;
    }
}
