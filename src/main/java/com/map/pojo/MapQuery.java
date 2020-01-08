package com.map.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 请求X地图请求对象
 */
@Data
@Accessors(chain = true)
public class MapQuery {

    /**
     * 位置
     */
    private String address;

    /**
     * 查询基准点位
     */
    private CoordinatePoint coordinatePoint;

    /**
     * 查询范围
     */
    private Long range;

    /**
     * POI查询关键字
     */
    private String queryName;

}
