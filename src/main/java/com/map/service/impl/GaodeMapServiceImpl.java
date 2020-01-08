package com.map.service.impl;

import com.map.common.enums.ParamEnumType;
import com.map.common.util.CoordinatePointUtil;
import com.map.pojo.MapQuery;
import com.map.pojo.CoordinatePoint;
import com.map.pojo.gaode.*;
import com.map.service.MapService;
import com.map.service.MapTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 高德地图
 */
@Service
public class GaodeMapServiceImpl extends MapTemplateService implements MapService {

    //@Value("baidu.url")
    private String url_poi = "https://restapi.amap.com/v3/place/around";
    private String url_transfer = "https://restapi.amap.com/v3/assistant/coordinate/convert";
    private String url_geocoding = "https://restapi.amap.com/v3/geocode/geo";
    private String url_reverse_geocoding = "https://restapi.amap.com/v3/geocode/regeo";

    //@Value("baidu.key")
    private String key = "高德key";

    /**
     * 入参地址->gcj02->wgs84
     * @param query 地址描述
     * @return
     */
    @Override
    public CoordinatePoint transferAddressToGPSPoint(MapQuery query) {
        Map<ParamEnumType, Object> wgs84ll = this.beforeTransferAddressToGPSPoint(query, null);
        Object o = doHttpRequest(wgs84ll);
        CoordinatePoint gpsPointDetail = this.afterTransferAddressToGPSPoint(o);
        double[] doubles = CoordinatePointUtil.gcj02_To_Gps84(Double.valueOf(gpsPointDetail.getLatitude()), Double.valueOf(gpsPointDetail.getLongitude()));
        gpsPointDetail.setLatitude(String.valueOf(doubles[0]));
        gpsPointDetail.setLongitude(String.valueOf(doubles[1]));
        return gpsPointDetail;
    }

    /**
     * gps->gcg02
     * @param query GPS点数据
     * @return
     */
    @Override
    public CoordinatePoint transferGPSPointToAddress(MapQuery query) {
        if (query == null || query.getCoordinatePoint() == null) {
            throw new RuntimeException("基准点坐标不能为空");
        }

        double[] doubles = CoordinatePointUtil.gps84_To_Gcj02(Double.valueOf(query.getCoordinatePoint().getLatitude()), Double.valueOf(query.getCoordinatePoint().getLongitude()));
        query.getCoordinatePoint().setLatitude(String.valueOf(doubles[0]));
        query.getCoordinatePoint().setLongitude(String.valueOf(doubles[1]));

        Map<ParamEnumType, Object> wgs84ll = this.beforeTransferGPSPointToAddress(query, null);
        Object o = doHttpRequest(wgs84ll);
        return this.afterTransferGPSPointToAddress(o);
    }

    /**
     * gps->gcj02->gps
     * @param query 查询条件
     * @return
     */
    @Override
    public List<CoordinatePoint> getServiceStationList(MapQuery query) {
        if (query == null || query.getCoordinatePoint() == null || StringUtils.isEmpty(query.getQueryName())) {
            throw new RuntimeException("POI查询关键字丶基准点坐标不能为空");
        }

        double[] doubles = CoordinatePointUtil.gps84_To_Gcj02(Double.valueOf(query.getCoordinatePoint().getLatitude()), Double.valueOf(query.getCoordinatePoint().getLongitude()));
        query.getCoordinatePoint().setLatitude(String.valueOf(doubles[0]));
        query.getCoordinatePoint().setLongitude(String.valueOf(doubles[1]));

        Map<ParamEnumType, Object> wgs84ll = this.beforeGetServiceStationList(query, null);
        Object o = doHttpRequest(wgs84ll);
        List<CoordinatePoint> gpsPointList = this.afterGetServiceStationList(o);
        gpsPointList.forEach(x -> {
            double[] doubles2 = CoordinatePointUtil.gcj02_To_Gps84(Double.valueOf(x.getLatitude()), Double.valueOf(x.getLongitude()));
            x.setLatitude(String.valueOf(doubles2[0]));
            x.setLongitude(String.valueOf(doubles2[1]));
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

    @Override
    protected Map<ParamEnumType, Object> beforeGetServiceStationList(MapQuery query, String coordtype) {
        if (query == null || query.getCoordinatePoint() == null || StringUtils.isEmpty(query.getQueryName())) {
            throw new RuntimeException("POI查询关键字丶基准点坐标不能为空");
        }
        Map<ParamEnumType, Object> map = new HashMap<>();
        map.put(ParamEnumType.URL, url_poi);
        GaodePOIReqPO gaodePOIReqPO = new GaodePOIReqPO();
        gaodePOIReqPO.setKeywords(query.getQueryName());
        gaodePOIReqPO.setRadius(query.getRange());
        String longitude = query.getCoordinatePoint().getLongitude();
        String latitude = query.getCoordinatePoint().getLatitude();
        if(StringUtils.isEmpty(longitude) || StringUtils.isEmpty(latitude)){
            throw new RuntimeException("POI查询关键字丶基准点坐标不能为空");
        }
        BigDecimal lngbd = new BigDecimal(longitude);
        lngbd = lngbd.setScale(6, RoundingMode.UNNECESSARY);
        BigDecimal latbd = new BigDecimal(latitude);
        latbd = latbd.setScale(6);

        gaodePOIReqPO.setLocation( lngbd + "," + latbd);
        gaodePOIReqPO.setKey(key);
        map.put(ParamEnumType.REQUEST, gaodePOIReqPO);
        map.put(ParamEnumType.RESPONSE, GaodePOIResPO.class);
        return map;
    }

    @Override
    protected List<CoordinatePoint> afterGetServiceStationList(Object obj) {
        GaodePOIResPO po = (GaodePOIResPO) obj;
        if (po == null || po.getStatus() == 0 || po.getPois() == null || po.getPois().size() == 0) {
            throw new RuntimeException("查询不到结果，请调整请求参数");
        }
        List<CoordinatePoint> list = new ArrayList<>();
        po.getPois().forEach(x -> {
            CoordinatePoint gpsPoint = new CoordinatePoint();
            String location = x.getLocation();
            if (!StringUtils.isEmpty(location)) {
                gpsPoint.setLongitude(location.substring(0, location.indexOf(",")));
                gpsPoint.setLatitude(location.substring(location.indexOf(",") + 1));
            }

            gpsPoint.setAddress(x.getAddress());
            gpsPoint.setName(x.getName());
            gpsPoint.setCity(x.getCityname());
            gpsPoint.setProvince(x.getPname());
            gpsPoint.setArea(x.getAdname());
            list.add(gpsPoint);
        });

        return list;
    }

    /**
     * 正地理编码前置动作
     * @param query
     * @param coordtype 都为空
     * @return 返回国测gcj02
     */
    @Override
    protected Map<ParamEnumType, Object> beforeTransferAddressToGPSPoint(MapQuery query, String coordtype) {

        if (query == null || StringUtils.isEmpty(query.getAddress())) {
            throw new RuntimeException("地址不能为空");
        }

        Map<ParamEnumType, Object> map = new HashMap<>();
        map.put(ParamEnumType.URL, url_geocoding);
        GaodeGeocodingReqPO gaodeGeocodingReqPO = new GaodeGeocodingReqPO();
        gaodeGeocodingReqPO.setAddress(query.getAddress());
        gaodeGeocodingReqPO.setKey(key);
        map.put(ParamEnumType.REQUEST, gaodeGeocodingReqPO);
        map.put(ParamEnumType.RESPONSE, GaodeGeocodingResPO.class);
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
        CoordinatePoint gpsPointDetail = new CoordinatePoint();
        GaodeGeocodingResPO po = (GaodeGeocodingResPO) address2PointObj;
        if (po == null || po.getStatus() == 0 || po.getCount() == 0) {
            throw new RuntimeException("查询不到结果,请调整请求参数");
        }

        String location = po.getGeocodes().get(0).getLocation();
        if (!StringUtils.isEmpty(location)) {
            gpsPointDetail.setLongitude(location.substring(0, location.indexOf(",")));
            gpsPointDetail.setLatitude(location.substring(location.indexOf(",") + 1));
        }
        return gpsPointDetail;
    }

    /**
     * 逆地理编码前置动作
     *
     * @param query
     * @return
     */
    @Override
    protected Map<ParamEnumType, Object> beforeTransferGPSPointToAddress(MapQuery query, String coordtype) {

        if (query == null || query.getCoordinatePoint() == null) {
            throw new RuntimeException("基准点坐标不能为空");
        }

        Map<ParamEnumType, Object> map = new HashMap<>();
        map.put(ParamEnumType.URL, url_reverse_geocoding);
        GaodeReverseGeocodingReqPO gaodeReverseGeocodingReqPO = new GaodeReverseGeocodingReqPO();
        CoordinatePoint gpsPoint = query.getCoordinatePoint();
        gaodeReverseGeocodingReqPO.setLocation(gpsPoint.getLongitude() + "," + gpsPoint.getLatitude());
        gaodeReverseGeocodingReqPO.setKey(key);
        gaodeReverseGeocodingReqPO.setRadius(query.getRange());
        map.put(ParamEnumType.REQUEST, gaodeReverseGeocodingReqPO);
        map.put(ParamEnumType.RESPONSE, GaodeReverseGeocodingResPO.class);
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
        GaodeReverseGeocodingResPO po = (GaodeReverseGeocodingResPO) obj;
        if (po == null || po.getStatus() == 0 || po.getRegeocode() == null || StringUtils.isEmpty(po.getRegeocode().getFormatted_address())) {
            throw new RuntimeException("输入的坐标不正确，请重新输入");
        }
        CoordinatePoint gpsPointDetail = new CoordinatePoint();
        gpsPointDetail.setAddress(po.getRegeocode().getFormatted_address());

        if (po.getRegeocode().getAddressComponent() != null) {
            gpsPointDetail.setProvince(po.getRegeocode().getAddressComponent().getProvince());
            gpsPointDetail.setCity(po.getRegeocode().getAddressComponent().getCity());
            gpsPointDetail.setArea(po.getRegeocode().getAddressComponent().getDistrict());

            if (po.getRegeocode().getAddressComponent().getStreetNumber() != null) {
                String location = po.getRegeocode().getAddressComponent().getStreetNumber().getLocation();
                gpsPointDetail.setLongitude(location.substring(0, location.indexOf(",")));
                gpsPointDetail.setLatitude(location.substring(location.indexOf(",") + 1));
            }

        }
        return gpsPointDetail;
    }

    /**
     * 坐标转换前置动作
     *
     * @param query
     * @return
     */
    @Override
    protected Map<ParamEnumType, Object> beforeTransferPoint(MapQuery query) {
        if (query == null || query.getCoordinatePoint() == null) {
            throw new RuntimeException("基准点坐标不能为空");
        }
        Map<ParamEnumType, Object> map = new HashMap<>();
        map.put(ParamEnumType.URL, url_transfer);
        GaodeTransferReqPO gaodeTransferReqPO = new GaodeTransferReqPO();
        CoordinatePoint gpsPoint = query.getCoordinatePoint();
        gaodeTransferReqPO.setLocations(gpsPoint.getLongitude() + "," + gpsPoint.getLatitude());
        gaodeTransferReqPO.setKey(key);
        map.put(ParamEnumType.REQUEST, gaodeTransferReqPO);
        map.put(ParamEnumType.RESPONSE, GaodeTransferResPO.class);

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
        GaodeTransferResPO po = (GaodeTransferResPO) o;
        if (po == null || StringUtils.isEmpty(po.getLocations())) {
            throw new RuntimeException("输入的坐标不正确，请重新输入");
        }
        String location = po.getLocations();
        query.getCoordinatePoint().setLatitude(location.substring(location.indexOf(",") + 1));
        query.getCoordinatePoint().setLongitude(location.substring(0, location.indexOf(",")));

        return query;
    }

    @Override
    protected boolean needTransfer() {
        return true;
    }
}
