package com.map.pojo.baidu;

import lombok.Data;

/**
 * 百度坐标转换请求对象
 */
@Data
public class BaiduTransferReqPO extends BaiduBaseReq {
    private String coords; //源坐标，多组坐标以";"分隔 lng经度，lat纬度
    private int from = 1; // 源坐标类型 1:GPS设备获取的角度坐标，WGS84坐标 5:百度地图采用的经纬度坐标
    private int to = 5; // 目标坐标类型 5:bd09ll 百度经纬度坐标
}
