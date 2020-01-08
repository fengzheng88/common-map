package com.map.service;

import com.map.pojo.MapQuery;
import com.map.pojo.CoordinatePoint;

import java.util.List;

/**
 *
 */
public interface MapService {
    /**
     * 地址转换为GPS点
     *
     * @param query 地址描述
     * @return GPS点信息
     */
    CoordinatePoint transferAddressToGPSPoint(MapQuery query);

    /**
     * GPS点转换为地址信息
     *
     * @param query GPS点数据
     * @return 地址信息
     */
    CoordinatePoint transferGPSPointToAddress(MapQuery query);

    /**
     * 获取维修站位点数据
     *
     * @param query 查询条件
     * @return 维修站位点数据列表
     */
    List<CoordinatePoint> getServiceStationList(MapQuery query);

    /**
     * 地址转换为X地图点
     *
     * @param query 地址描述
     * @return X地图信息
     */
    CoordinatePoint transferAddressToXPoint(MapQuery query);

    /**
     * X地图点转换为地址信息
     *
     * @param query GPS点数据
     * @return 地址信息
     */
    CoordinatePoint transferXPointToAddress(MapQuery query);

    /**
     * 获取维修站X地图点数据
     *
     * @param query 查询条件
     * @return 获取POI列表
     */
    List<CoordinatePoint> getServiceStationListByX(MapQuery query);
}
