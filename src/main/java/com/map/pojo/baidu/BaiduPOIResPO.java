package com.map.pojo.baidu;

import lombok.Data;

import java.util.List;

/**
 * 百度poi返回对象
 */
@Data
public class BaiduPOIResPO extends BaiduBaseRes {
    //private int total; //POI检索总数，开发者请求中设置了page_num字段才会出现total字段。出于数据保护目的，单次请求total最多为400。
    private List<BaiduPOIResultPO> results;
}
