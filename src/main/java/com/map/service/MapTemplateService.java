package com.map.service;

import com.map.common.enums.ParamEnumTypeEnum;
import com.map.common.util.HttpClientUtil;
import com.map.pojo.MapQuery;
import com.map.pojo.CoordinatePoint;

import java.util.List;
import java.util.Map;

/**
 * 地图模板
 */
public abstract class MapTemplateService implements MapService {

    @Override
    public CoordinatePoint transferAddressToXPoint(MapQuery query) {
        //query = getCoordinateTransfer(query);
        Map<ParamEnumTypeEnum, Object> addressTOPointMap = this.beforeTransferAddressToGPSPoint(query, null);
        Object address2PointObj = doHttpRequest(addressTOPointMap);
        return afterTransferAddressToGPSPoint(address2PointObj);
    }

    @Override
    public CoordinatePoint transferXPointToAddress(MapQuery query) {
        //query = getCoordinateTransfer(query);
        Map<ParamEnumTypeEnum, Object> addressTOPointMap = this.beforeTransferGPSPointToAddress(query, null);
        Object obj = doHttpRequest(addressTOPointMap);
        return afterTransferGPSPointToAddress(obj);
    }

    @Override
    public List<CoordinatePoint> getServiceStationListByX(MapQuery query) {
        //query = getCoordinateTransfer(query);
        Map<ParamEnumTypeEnum, Object> addressTOPointMap = this.beforeGetServiceStationList(query, null);
        Object obj = doHttpRequest(addressTOPointMap);
        return afterGetServiceStationList(obj);
    }


    /**
     * 获取POI点前置动作
     *
     * @param query
     * @return
     */
    protected abstract Map<ParamEnumTypeEnum, Object> beforeGetServiceStationList(MapQuery query, String coordtype);

    /**
     * 获取POI点后置动作
     *
     * @param obj
     * @return
     */
    protected abstract List<CoordinatePoint> afterGetServiceStationList(Object obj);

    /**
     * 是否调用坐标转换
     *
     * @param query
     * @return
     */
    private MapQuery getCoordinateTransfer(MapQuery query) {
        if (needTransfer()) {
            Map<ParamEnumTypeEnum, Object> enumTypeObjectMap = this.beforeTransferPoint(query);
            Object o = doHttpRequest(enumTypeObjectMap);
            query = afterTransferPoint(query, o);
        }
        return query;
    }

    /**
     * 正地理编码前置动作
     *
     * @param query
     * @return
     */
    protected abstract Map<ParamEnumTypeEnum, Object> beforeTransferAddressToGPSPoint(MapQuery query, String coordtype);

    /**
     * 正地理编码后置动作
     *
     * @param address2PointObj
     * @return
     */
    protected abstract CoordinatePoint afterTransferAddressToGPSPoint(Object address2PointObj);

    /**
     * 逆地理位置前置动作
     *
     * @param query
     * @return
     */
    protected abstract Map<ParamEnumTypeEnum, Object> beforeTransferGPSPointToAddress(MapQuery query, String coordtype);

    /**
     * 逆地理位置后置动作
     *
     * @param obj
     * @return
     */
    protected abstract CoordinatePoint afterTransferGPSPointToAddress(Object obj);

    /**
     * 坐标转换前置动作
     *
     * @param query
     * @return
     */
    protected abstract Map<ParamEnumTypeEnum, Object> beforeTransferPoint(MapQuery query);

    /**
     * 坐标转换后置动作
     *
     * @param query
     * @param o
     * @return
     */
    protected abstract MapQuery afterTransferPoint(MapQuery query, Object o);

    public Object doHttpRequest(Map<ParamEnumTypeEnum, Object> map) {
        return HttpClientUtil.doGet((String) map.get(ParamEnumTypeEnum.URL), map.get(ParamEnumTypeEnum.REQUEST), (Class) map.get(ParamEnumTypeEnum.RESPONSE));
    }

    /**
     * 是否需要调用坐标转换接口
     *
     * @return
     */
    protected abstract boolean needTransfer();

}

