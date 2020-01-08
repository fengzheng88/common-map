package com.map.pojo.gaode;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 高德地图POI请求-周边
 */
@Data
public class GaodePOIReqPO extends GaodeBaseReq {
    //@NotEmpty(message = "查询关键字不能为空")
    private String keywords;
    @NotEmpty(message = "中心点坐标不能为空，经度在前纬度在后，小数不得超过6位")
    private String location; //经度,纬度
    private Long radius;//查询半径，取值范围0-50000米，默认3000
    private String types; //查询POI类型
    private String city; //查询城市
    private String citylimit;//仅返回指定城市数据,默认false
    private int children;//0:子POI都会显示，默认 1：子POI会归到父POI之中
    private int offset = 20; //每页记录数据，默认20，建议不超过25
    private int page = 1; //当前页数，默认1，最大100
    private String sortrule;//规定返回结果的排序规则，按距离：distance 综合：weight

}
