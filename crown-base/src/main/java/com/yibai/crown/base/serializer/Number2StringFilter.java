package com.yibai.crown.base.serializer;

import com.alibaba.fastjson.serializer.ValueFilter;

import java.math.BigDecimal;

/**
 * Created by dell on 2017/6/6.
 */
public class Number2StringFilter implements ValueFilter {

    @Override
    public Object process(Object object, String name, Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toPlainString();
        }
        if (value instanceof Number) {
            return value.toString();
        }
        return value;
    }

}