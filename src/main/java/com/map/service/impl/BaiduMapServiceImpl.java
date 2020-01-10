package com.map.service.impl;

import com.map.common.config.MapConfig;
import com.map.common.enums.ParamEnumTypeEnum;
import com.map.common.util.CoordinatePointUtil;
import com.map.pojo.MapQuery;
import com.map.pojo.CoordinatePoint;
import com.map.pojo.baidu.*;
import com.map.service.MapService;
import com.map.service.MapTemplateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 百度地图
 */
@Service
public class BaiduMapServiceImpl extends MapTemplateService implements MapService {

    /**
     * 以下@Value可以用MapConfig替换
     * @see com.map.common.config.MapConfig
     */
    @Value("${map.baidu.urlPoi}")
    private String url_poi;
    @Value("${map.baidu.urlTransfer}")
    private String url_transfer;
    @Value("${map.baidu.urlGeocoding}")
    private String url_geocoding;
    @Value("${map.baidu.urlReverseGeocoding}")
    private String url_reverse_geocoding;

    @Value("${map.baidu.key}")
    private String key;

    @Resource
    MapConfig mapConfig;

    @PostConstruct
    public void init(){
        System.out.println(mapConfig.getCoordinateType());
        System.out.println(mapConfig.getBaiduMapConfig().getKey());
        System.out.println(mapConfig.getBaiduMapConfig().getUrlGeocoding());
        System.out.println(mapConfig.getBaiduMapConfig().getUrlPoi());
        System.out.println(mapConfig.getBaiduMapConfig().getUrlReverseGeocoding());
        System.out.println(mapConfig.getBaiduMapConfig().getUrlTransfer());
    }

    /**
     * 地址->gps
     *
     * @param query 地址描述
     * @return
     */
    @Override
    public CoordinatePoint transferAddressToGPSPoint(MapQuery query) {
        Map<ParamEnumTypeEnum, Object> wgs84ll = this.beforeTransferAddressToGPSPoint(query, "wgs84ll");
        Object o = doHttpRequest(wgs84ll);
        return this.afterTransferAddressToGPSPoint(o);
    }

    /**
     * 入参GPS->bd09->出参GPS
     *
     * @param query GPS点数据
     * @return
     */
    @Override
    public CoordinatePoint transferGPSPointToAddress(MapQuery query) {
        Map<ParamEnumTypeEnum, Object> wgs84ll = this.beforeTransferGPSPointToAddress(query, "wgs84ll");
        Object o = doHttpRequest(wgs84ll);
        CoordinatePoint CoordinatePoint = this.afterTransferGPSPointToAddress(o);
        double[] doubles = CoordinatePointUtil.bd09_To_gps84(Double.valueOf(CoordinatePoint.getLatitude()), Double.valueOf(CoordinatePoint.getLongitude()));
        CoordinatePoint.setLatitude(String.valueOf(doubles[0]));
        CoordinatePoint.setLongitude(String.valueOf(doubles[1]));
        return CoordinatePoint;
    }

    /**
     * 入参GPS->bd09->出参GPS
     *
     * @param query 查询条件
     * @return
     */
    @Override
    public List<CoordinatePoint> getServiceStationList(MapQuery query) {
        Map<ParamEnumTypeEnum, Object> wgs84ll = this.beforeGetServiceStationList(query, "1");
        Object o = doHttpRequest(wgs84ll);
        List<CoordinatePoint> gpsPointList = this.afterGetServiceStationList(o);
        gpsPointList.forEach(x -> {
            double[] doubles = CoordinatePointUtil.bd09_To_gps84(Double.valueOf(x.getLatitude()), Double.valueOf(x.getLongitude()));
            x.setLatitude(String.valueOf(doubles[0]));
            x.setLongitude(String.valueOf(doubles[1]));
        });
        return gpsPointList;
    }

    @Override
    public CoordinatePoint transferAddressToXPoint(MapQuery query) {
        return super.transferAddressToXPoint(query);
    }

    @Override
    public CoordinatePoint transferXPointToAddress(MapQuery query) {
        return super.transferXPointToAddress(query);
    }

    @Override
    public List<CoordinatePoint> getServiceStationListByX(MapQuery query) {
        return super.getServiceStationListByX(query);
    }

    /**
     * @param query
     * @param coordtype ifnull->请求location为百度坐标
     * @return
     */
    @Override
    protected Map<ParamEnumTypeEnum, Object> beforeGetServiceStationList(MapQuery query, String coordtype) {
        if (query == null || query.getCoordinatePoint() == null || StringUtils.isEmpty(query.getQueryName())) {
            throw new RuntimeException("POI查询关键字丶基准点坐标不能为空");
        }
        Map<ParamEnumTypeEnum, Object> map = new HashMap<>();
        map.put(ParamEnumTypeEnum.URL, url_poi);
        BaiduPOIReqPO baiduPOIReqPO = new BaiduPOIReqPO();
        baiduPOIReqPO.setQuery(query.getQueryName());
        baiduPOIReqPO.setRadius(String.valueOf(query.getRange()));
        baiduPOIReqPO.setLocation(query.getCoordinatePoint().getLatitude() + "," + query.getCoordinatePoint().getLongitude());
        baiduPOIReqPO.setAk(key);
        baiduPOIReqPO.setCoord_type(coordtype);
        map.put(ParamEnumTypeEnum.REQUEST, baiduPOIReqPO);
        map.put(ParamEnumTypeEnum.RESPONSE, BaiduPOIResPO.class);
        return map;
    }

    @Override
    protected List<CoordinatePoint> afterGetServiceStationList(Object obj) {
        BaiduPOIResPO po = (BaiduPOIResPO) obj;
        if (po == null || po.getStatus() != 0 || po.getResults() == null || po.getResults().size() == 0) {
            throw new RuntimeException("查询不到结果，请调整请求参数");
        }
        List<CoordinatePoint> list = new ArrayList<>();
        po.getResults().forEach(x -> {
            CoordinatePoint gpsPoint = new CoordinatePoint();
            gpsPoint.setLongitude(String.valueOf(x.getLocation().getLng()));
            gpsPoint.setLatitude(String.valueOf(x.getLocation().getLat()));
            gpsPoint.setAddress(x.getAddress());
            gpsPoint.setName(x.getName());
            gpsPoint.setCity(x.getCity());
            gpsPoint.setProvince(x.getProvince());
            gpsPoint.setArea(x.getArea());
            list.add(gpsPoint);
        });

        return list;
    }

    /**
     * 正地理编码前置动作
     *
     * @param query
     * @param coordtype ifnull->返回location为百度坐标
     * @return
     */
    @Override
    protected Map<ParamEnumTypeEnum, Object> beforeTransferAddressToGPSPoint(MapQuery query, String coordtype) {

        if (query == null || StringUtils.isEmpty(query.getAddress())) {
            throw new RuntimeException("地址不能为空");
        }

        Map<ParamEnumTypeEnum, Object> map = new HashMap<>();
        map.put(ParamEnumTypeEnum.URL, url_geocoding);
        BaiduGeocodingReqPO baiduGeocodingReqPO = new BaiduGeocodingReqPO();
        baiduGeocodingReqPO.setAddress(query.getAddress());
        baiduGeocodingReqPO.setAk(key);
        baiduGeocodingReqPO.setRet_coordtype(coordtype);
        map.put(ParamEnumTypeEnum.REQUEST, baiduGeocodingReqPO);
        map.put(ParamEnumTypeEnum.RESPONSE, BaiduGeocodingResPO.class);
        return map;
    }

    /**
     * 正地理编码后置动作
     *
     * @param address2PointObj
     * @return
     */
    @Override
    protected CoordinatePoint afterTransferAddressToGPSPoint(Object address2PointObj) {
        CoordinatePoint CoordinatePoint = new CoordinatePoint();
        BaiduGeocodingResPO po = (BaiduGeocodingResPO) address2PointObj;
        if (po == null || po.getStatus() != 0 || po.getResult() == null || po.getResult().getLocation() == null) {
            throw new RuntimeException("查询不到结果,请调整请求参数");
        }

        CoordinatePoint.setLongitude(String.valueOf(po.getResult().getLocation().getLng()));
        CoordinatePoint.setLatitude(String.valueOf(po.getResult().getLocation().getLat()));

        return CoordinatePoint;
    }

    /**
     * 逆地理编码前置动作(返回百度坐标)
     *
     * @param query
     * @param coordtype ifnull->请求location为百度坐标
     * @return
     */
    @Override
    protected Map<ParamEnumTypeEnum, Object> beforeTransferGPSPointToAddress(MapQuery query, String coordtype) {

        if (query == null || query.getCoordinatePoint() == null) {
            throw new RuntimeException("基准点坐标不能为空");
        }

        Map<ParamEnumTypeEnum, Object> map = new HashMap<>();
        map.put(ParamEnumTypeEnum.URL, url_reverse_geocoding);
        BaiduReverseGeocodingReqPO baiduReverseGeocodingReqPO = new BaiduReverseGeocodingReqPO();
        CoordinatePoint gpsPoint = query.getCoordinatePoint();
        baiduReverseGeocodingReqPO.setLocation(gpsPoint.getLatitude() + "," + gpsPoint.getLongitude());
        baiduReverseGeocodingReqPO.setAk(key);
        baiduReverseGeocodingReqPO.setRadius(String.valueOf(query.getRange()));
        baiduReverseGeocodingReqPO.setCoordtype(coordtype);
        map.put(ParamEnumTypeEnum.REQUEST, baiduReverseGeocodingReqPO);
        map.put(ParamEnumTypeEnum.RESPONSE, BaiduReverseGeocodingResPO.class);
        return map;
    }

    /**
     * 逆地理编码后置动作
     *
     * @param obj
     * @return
     */
    @Override
    protected CoordinatePoint afterTransferGPSPointToAddress(Object obj) {
        BaiduReverseGeocodingResPO po = (BaiduReverseGeocodingResPO) obj;
        if (po == null || po.getStatus() != 0 || po.getResult() == null || StringUtils.isEmpty(po.getResult().getFormatted_address())) {
            throw new RuntimeException("输入的坐标不正确，请重新输入");
        }
        CoordinatePoint CoordinatePoint = new CoordinatePoint();
        CoordinatePoint.setAddress(po.getResult().getFormatted_address());
        CoordinatePoint.setLatitude(String.valueOf(po.getResult().getLocation().getLat()));
        CoordinatePoint.setLongitude(String.valueOf(po.getResult().getLocation().getLng()));
        if (po.getResult().getAddressComponent() != null) {
            CoordinatePoint.setProvince(po.getResult().getAddressComponent().getProvince());
            CoordinatePoint.setCity(po.getResult().getAddressComponent().getCity());
            CoordinatePoint.setArea(po.getResult().getAddressComponent().getDistrict());
        }
        return CoordinatePoint;
    }

    /**
     * 坐标转换前置动作
     * 默认GPS->百度
     *
     * @param query
     * @return
     */
    @Override
    protected Map<ParamEnumTypeEnum, Object> beforeTransferPoint(MapQuery query) {
        if (query == null || query.getCoordinatePoint() == null) {
            throw new RuntimeException("基准点坐标不能为空");
        }
        Map<ParamEnumTypeEnum, Object> map = new HashMap<>();
        map.put(ParamEnumTypeEnum.URL, url_transfer);
        BaiduTransferReqPO baiduTransferReqPO = new BaiduTransferReqPO();
        CoordinatePoint gpsPoint = query.getCoordinatePoint();
        baiduTransferReqPO.setCoords(gpsPoint.getLongitude() + "," + gpsPoint.getLatitude());
        baiduTransferReqPO.setAk(key);
        map.put(ParamEnumTypeEnum.REQUEST, baiduTransferReqPO);
        map.put(ParamEnumTypeEnum.RESPONSE, BaiduTransferResPO.class);

        return map;
    }

    /**
     * 坐标转换后置动作
     *
     * @param query
     * @param o
     * @return
     */
    @Override
    protected MapQuery afterTransferPoint(MapQuery query, Object o) {
        BaiduTransferResPO po = (BaiduTransferResPO) o;

        if (po == null || po.getResult() == null || po.getResult().size() == 0) {
            throw new RuntimeException("输入的坐标不正确，请重新输入");
        }

        query.getCoordinatePoint().setLatitude(String.valueOf(po.getResult().get(0).getY()));
        query.getCoordinatePoint().setLongitude(String.valueOf(po.getResult().get(0).getX()));

        return query;
    }

    @Override
    protected boolean needTransfer() {
        return true;
    }
}
