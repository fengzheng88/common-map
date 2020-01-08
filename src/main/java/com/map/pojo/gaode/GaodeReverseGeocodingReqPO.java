package com.map.pojo.gaode;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 高德地图逆地理编码
 */
@Data
public class GaodeReverseGeocodingReqPO extends GaodeBaseReq{
    @NotEmpty(message = "基站点不能为空")
    private String location; //经度，纬度
    private Long radius; //搜索半径
}
