package com.map.pojo.gaode;

import lombok.Data;

/**
 * 高德地图转换请求对象
 */
@Data
public class GaodeTransferReqPO extends GaodeBaseReq {
    private String locations;//经度，纬度。 多个用"|"分格
    private String coordsys = "gps";//原坐标系 gps/mapbar/baidu/autonavi
}
