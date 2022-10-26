package com.ball.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ball.common.dao.mapper.MessageTemplateMapper;
import com.ball.common.dao.po.MessageTemplate;
import com.ball.common.service.IMessageTemplateService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 短信邮件模板表 服务实现类
 * </p>
 *
 * @author JimChery
 * @since 2021-04-14
 */
@Service
public class MessageTemplateServiceImpl extends ServiceImpl<MessageTemplateMapper, MessageTemplate> implements IMessageTemplateService {

}
