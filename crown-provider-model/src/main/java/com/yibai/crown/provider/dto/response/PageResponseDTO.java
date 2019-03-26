package com.yibai.crown.provider.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务间分页查询结果
 */
@Getter
@Setter
public class PageResponseDTO<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 3254297929154173055L;

    private int totalCount = 0;
    private int totalPage = 1;
    private List<T> items = new ArrayList<>();

    public void measureTotalPage(int totalCount, int pageSize) {
        this.totalCount = totalCount;
        if (totalCount < 1) {
            return;
        }
        this.totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
    }

}
