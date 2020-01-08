package com.map.pojo.baidu;

import lombok.Data;

/**
 * 坐标
 */
@Data
public class Coordinate {
    //transfer
    private float x; //经度
    private float y; //纬度

    //POI
    private float lng;//经度
    private float lat;//纬度
}
