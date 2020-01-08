package com.map.pojo.baidu;

import lombok.Data;

/**
 * 百度poi返回对象
 */
@Data
public class BaiduPOIResultPO {
    private String name; //poi名称
    private Coordinate location; //poi经纬度坐标
    private String address; //poi地址信息
    private String province;// 所属省份
    private String city;// 所属城市
    private String area; //所属区县
    private String telephone; //poi电话信息
    private String uid; //poi的唯一标示，可用于详情检索
    private String street_id; //街景图id
    private String detail; //是否有详情页：1有，0没有
}
