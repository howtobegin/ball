package com.ball.common.service;

import com.ball.base.util.BizAssert;
import com.ball.base.util.SimpleCodeUtil;
import com.ball.common.dao.po.MessageTemplate;
import com.ball.common.enums.MessageMediaEnum;
import com.ball.common.enums.VerifyCodeEnum;
import com.ball.common.exception.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 短信邮件服务
 * @author littlehow
 */
@Service
@Slf4j
public class MessageService {
    @Autowired
    private IMessageTemplateService messageTemplateService;

    @Autowired
    private SmsPaasooService smsService;

    @Autowired
    private EmailGunService emailService;

    @Value("${message.verify.enabled:true}")
    private boolean messageVerify;

    @Value("${message.send.enabled:true}")
    private boolean messageSend;

    @Value("${message.send.distance:60}")
    private int distance;

    @Value("${message.verify.code.length:6}")
    private int verifyCodeLength;

    @Value("${message.verify.code.expired:10}")
    private int codeExpiredMinutes;

    private Map<String, String> whiteMap = new HashMap<>();

    @Value("${message.verify.white:}")
    public void setWhiteMap(String value) {
        log.info("white list change to {}", value);
        whiteMap.clear();
        if (StringUtils.hasText(value)) {
            String[] infos = value.split(";");
            for (String info : infos) {
                String[] phoneCode = info.split(",");
                whiteMap.put(phoneCode[0], phoneCode[1]);
            }
        }
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String CODE_KEY_PREFIX = "message:";

    public void sendVerifyCode(String target, String lang, MessageMediaEnum messageMediaEnum) {
        sendVerifyCode(target, lang, messageMediaEnum, VerifyCodeEnum.VERIFY_CODE.name());
    }

    public void sendVerifyCode(String target, String lang, MessageMediaEnum messageMediaEnum, String code) {
        if (!messageSend) {
            return;
        }
        // 判断target是否发送过
        BizAssert.isTrue(Boolean.FALSE.equals(redisTemplate.hasKey(getExpiredKey(target))),
                CommonErrorCode.MESSAGE_SEND_FREQUENTLY);

        // 首先查询模板信息
        MessageTemplate messageTemplate = messageTemplateService.lambdaQuery()
                .eq(MessageTemplate::getTemplateCode, code)
                .eq(MessageTemplate::getMedia, messageMediaEnum.v)
                .eq(MessageTemplate::getLang, lang).one();
        BizAssert.notNull(messageTemplate, CommonErrorCode.MESSAGE_TEMPLATE_NOT_EXISTS);
        String verifyCode = SimpleCodeUtil.getVerifyCode(verifyCodeLength);
        log.info("send message target={}, code={}", target, code);
        // 设置code
        redisTemplate.opsForValue().set(getCodeKey(target), verifyCode, codeExpiredMinutes, TimeUnit.MINUTES);
        // 内容渲染
        String content = messageTemplate.getContent().replace("${code}", verifyCode);
        // 发送手机
        if (messageMediaEnum.isPhone()) {
            if (!target.startsWith("+")) {
                target = "+" + target;
            }
            smsService.send(target, content);
        } else {
            // 发送邮件
            emailService.send(target, messageTemplate.getSubject(), content);
        }
        redisTemplate.opsForValue().set(getExpiredKey(target), "1", distance, TimeUnit.SECONDS);
    }

    /**
     * 验证验证码信息
     * @param target -
     * @param code   -
     */
    public void verifyCode(String target, String code) {
        if (!messageVerify) {
            return;
        }
        if (whiteCheck(target, code)) {
            return;
        }
        String key = getCodeKey(target);
        String codeCache = redisTemplate.opsForValue().get(key);
        BizAssert.notNull(codeCache, CommonErrorCode.MESSAGE_CODE_EXPIRED);
        BizAssert.isTrue(code.equals(codeCache), CommonErrorCode.MESSAGE_CODE_ERROR);
        // 验证成功，删除验证码
        redisTemplate.delete(key);
    }

    /**
     * 验证白名单
     * @param target -
     * @param code -
     * @return -
     */
    private boolean whiteCheck(String target, String code) {
        return whiteMap.containsKey(target) && code.equals(whiteMap.get(target));
    }

    private String getExpiredKey(String target) {
        return CODE_KEY_PREFIX + "expired:" + target;
    }

    private String getCodeKey(String target) {
        return CODE_KEY_PREFIX + "verify:" + target;
    }
}
