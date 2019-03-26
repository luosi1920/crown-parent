package com.yibai.crown.base.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型
 * Created by fushengcai on 2017/10/11.
 */
@AllArgsConstructor
@Getter
public enum UserTypeEnum {
    PERSONAL((byte) 1, "个人用户"),
    ORGANIZATION((byte) 2, "企业用户");

    private Byte value;
    private String desc;

    public static UserTypeEnum enumOf(Byte value) {
        for (UserTypeEnum type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }

}
