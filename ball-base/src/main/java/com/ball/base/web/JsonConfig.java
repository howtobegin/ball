package com.ball.base.web;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.ball.base.util.DateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 */
public class JsonConfig {


    private JsonConfig() {
    }

    public static FastJsonConfig getFastJsonConfig() {
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.SortField,
                SerializerFeature.MapSortField,
                SerializerFeature.WriteNullListAsEmpty
        );
        ValueFilter filter = (object, name, value) -> {
            if(value instanceof BigDecimal){
                return ((BigDecimal) value).stripTrailingZeros().toPlainString();
            } else if (value instanceof LocalDateTime) {
                return DateUtil.getTime((LocalDateTime) value);
            } else if (value instanceof LocalDate) {
                return DateUtil.getTime((LocalDate) value);
            }
            return value;
        };

        fastJsonConfig.setSerializeFilters(filter);
        return fastJsonConfig;
    }
}
