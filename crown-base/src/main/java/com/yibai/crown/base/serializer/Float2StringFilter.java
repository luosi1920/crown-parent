package com.yibai.crown.base.serializer;

import com.alibaba.fastjson.serializer.ValueFilter;

import java.math.BigDecimal;

/**
 * 2018/2/20.
 */
public class Float2StringFilter implements ValueFilter {

    @Override
    public Object process(Object object, String name, Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toPlainString();
        }
        if (value instanceof Float || value instanceof Double) {
            return value.toString();
        }
        return value;
    }

}
