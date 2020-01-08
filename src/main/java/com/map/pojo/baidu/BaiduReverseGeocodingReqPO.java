package com.map.pojo.baidu;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 逆地理编码请求对象
 */
@Data
public class BaiduReverseGeocodingReqPO extends BaiduBaseReq {
    @NotEmpty(message = "坐标不能为空")
    private String location; //纬度lat,经度lng
    private String radius; //poi召回半径，允许设置区间为0-1000
    private String coordtype; //原坐标类型
    private String ret_coordtype = "bd09ll"; //目标坐标类型

}
