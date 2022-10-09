package com.ball.boss.controller.system;


import com.ball.base.model.PageResult;
import com.ball.base.util.BeanUtil;
import com.ball.boss.controller.system.vo.DictionaryBaseVo;
import com.ball.boss.controller.system.vo.DictionarySimpleVo;
import com.ball.boss.controller.system.vo.request.DictionaryPagingRequestVo;
import com.ball.boss.controller.system.vo.request.DictionaryUpdateVo;
import com.ball.boss.controller.system.vo.response.DictionaryResponseVo;
import com.ball.boss.dao.entity.BossCode;
import com.ball.boss.service.system.CodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Api(tags = "字典接口")
@RestController
@RequestMapping("/boss/system/dictionary")
public class DictionaryController {

    @Autowired
    private CodeService codeService;

    @ApiOperation("新增字典")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@RequestBody @Valid DictionaryBaseVo dictionary) {
        return codeService.add(BeanUtil.copy(dictionary, BossCode.class));
    }

    @ApiOperation("修改字典")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void update(@RequestBody @Valid DictionaryUpdateVo dictionary) {
        codeService.update(BeanUtil.copy(dictionary, BossCode.class));
    }

    @ApiOperation("删除字典")
    @ApiImplicitParam(value = "字典编号", name = "codeId", required = true)
    @RequestMapping(value = "/delete", method = {RequestMethod.POST, RequestMethod.GET} )
    public void delete(@RequestParam String codeId) {
        codeService.delete(codeId);
    }

    @ApiOperation("根据对照字段查询字典")
    @ApiImplicitParam(value = "对照字段", name = "field", required = true)
    @RequestMapping(value = "/getByField", method = RequestMethod.GET )
    public List<DictionarySimpleVo> getByField(@RequestParam String field) {
        List<BossCode> codes = codeService.queryByField(field);
        if (CollectionUtils.isEmpty(codes)) {
            return new ArrayList<>();
        }
        return codes.stream().map(o -> BeanUtil.copy(o, DictionarySimpleVo.class)).collect(Collectors.toList());
    }

    @ApiOperation("查询所有字典信息")
    @RequestMapping(value = "/getAll", method = RequestMethod.GET )
    public List<DictionarySimpleVo> getAll() {
        List<BossCode> codes = codeService.queryAll();
        if (CollectionUtils.isEmpty(codes)) {
            return new ArrayList<>();
        }
        return codes.stream().map(o -> BeanUtil.copy(o, DictionarySimpleVo.class)).collect(Collectors.toList());
    }

    @ApiOperation("根据字典编号查询字典信息")
    @ApiImplicitParam(value = "字典编号", name = "codeId", required = true)
    @RequestMapping(value = "/getById", method = RequestMethod.GET )
    public DictionaryResponseVo getById(@RequestParam String codeId) {
        return BeanUtil.copy(codeService.queryById(codeId), DictionaryResponseVo.class);
    }


    @ApiOperation("分页查询字典信息")
    @RequestMapping(value = "/queryPaging", method = RequestMethod.POST )
    public PageResult<DictionaryResponseVo> queryPaging(@RequestBody @Valid DictionaryPagingRequestVo request) {
        PageResult<BossCode> queryResult = codeService.queryPaging(BeanUtil.copy(request, BossCode.class));
        PageResult<DictionaryResponseVo> returnValue = new PageResult<>(null, queryResult.getTotalNum(), request.getPageIndex(), request.getPageSize());
        if (CollectionUtils.isEmpty(queryResult.getRows())) {
            returnValue.setRows(new ArrayList<>());
        }
        returnValue.setRows(queryResult.getRows().stream().map(o -> BeanUtil.copy(o, DictionaryResponseVo.class)).collect(Collectors.toList()));
        return returnValue;
    }
}
