package com.wuzhulin.vo.param;

import lombok.Data;

@Data
public class PageParam {
    private int page = 1;

    private int pageSize = 10;

    private Long categoryId;

    private Long tagId;

    private String year;

    private String month;

    public String getMonth() {
        if (month != null && month.length() == 1) {
            return "0" + this.month;
        }
        return month;
    }
}
