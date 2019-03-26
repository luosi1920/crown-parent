package com.yibai.crown.provider.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 服务间分页请求
 */
@Setter
@Getter
public abstract class PageRequestDTO implements Serializable {
    private static final long serialVersionUID = 8781056101945517980L;

    protected int pageNumber = 1;
    protected int pageSize = 20;

    public int getOffset() {
        return (pageNumber - 1) * pageSize;
    }

}
