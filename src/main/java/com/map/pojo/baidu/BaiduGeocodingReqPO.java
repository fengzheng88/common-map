package com.map.pojo.baidu;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 正地理编码
 */
@Data
public class BaiduGeocodingReqPO extends BaiduBaseReq {
    @NotEmpty(message = "待解析的地址不能为空")
    private String address; //待解析的地址，最多支持84个字节
    private String city; //地址所在的城市名
    private String ret_coordtype = "bd09ll"; //目标坐标类型 wgs84ll/bd09ll

}
