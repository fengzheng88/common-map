package com.map.pojo.gaode;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 高德地图正地理编码
 */
@Data
public class GaodeGeocodingReqPO extends GaodeBaseReq{
    @NotEmpty(message = "地址不能为空")
    private String address; //batch=true时，用"|"分隔，最多支持10个
    private String city;
    private String batch = "false"; //批量查询控制 true:批量 false:单个

}
