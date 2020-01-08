package com.map.pojo;

import lombok.Data;

/**
 * X地图点位信息
 */
@Data
public class CoordinatePoint {

    /**
     * 经度
     */
    protected String longitude;
    /**
     * 纬度
     */
    protected String latitude;
    /**
     * 时间
     */
    protected String timestamp;

    /**
     * poi名称
     */
    private String name;

    /**
     * poi所在地址
     */
    private String address;

    /**
     * 所属省份
     */
    private String province;

    /**
     * 所属城市
     */
    private String city;

    /**
     * 所属区县
     */
    private String area;

}
