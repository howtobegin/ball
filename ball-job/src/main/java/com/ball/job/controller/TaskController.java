package com.ball.job.controller;

import com.ball.job.base.ScheduleBaseService;
import com.ball.job.base.TaskConfig;
import com.ball.job.controller.vo.JobBaseVO;
import com.ball.job.controller.vo.JobConfigVO;
import com.ball.job.controller.vo.UpdateJobConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author JimChery
 */
@Api(tags = "任务")
@RestController
@RequestMapping("/job/config/")
public class TaskController {

    @Autowired
    private ScheduleBaseService scheduleBaseService;

    @ApiOperation("所有任务信息")
    @GetMapping("all")
    public List<JobConfigVO> getAllJob() {
        return TaskConfig.getTasks().stream().map(JobConfigVO::new).collect(Collectors.toList());
    }

    @ApiOperation("修改trigger类型任务的表达式")
    @PostMapping("update")
    public void updateJob(@RequestBody @Valid UpdateJobConfigVO update) {
         scheduleBaseService.changeTask(update.getJobClass(), update.getExpression());
    }

    @ApiOperation("禁用任务")
    @PostMapping("disable")
    public void disableJob(@RequestBody @Valid JobBaseVO req) {
        scheduleBaseService.disableTask(req.getJobClass());
    }

    @ApiOperation("启用任务")
    @PostMapping("enable")
    public void enableJob(@RequestBody @Valid JobBaseVO req) {
        scheduleBaseService.enableTask(req.getJobClass());
    }

    @ApiOperation("执行任务")
    @PostMapping("run")
    public void runJob(@RequestBody @Valid JobBaseVO req) {
        scheduleBaseService.runTask(req.getJobClass());
    }
}
