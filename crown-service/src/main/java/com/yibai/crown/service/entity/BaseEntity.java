package com.yibai.crown.service.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
public abstract class BaseEntity extends CoreEntity{
    protected Date createTime;
    protected Date updateTime;
}
