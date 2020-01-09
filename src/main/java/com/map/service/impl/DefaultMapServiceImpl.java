package com.map.service.impl;

import com.map.common.enums.CoordTypeEnum;
import com.map.common.util.MapSimpleFactory;
import com.map.pojo.CoordinatePoint;
import com.map.pojo.MapQuery;
import com.map.service.MapService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Primary 优先使用该注解下的实例
 * 为啥要弄这个类呢？为了使用配置来指定使用哪种具体的实例,且对后端也是无感知的(使用MapService时无须指定哪种实例)
 * @see com.map.service.impl.BaiduMapServiceImpl
 * @see com.map.service.impl.GaodeMapServiceImpl
 */
@Primary
@Service
public class DefaultMapServiceImpl implements MapService {

    @Value("${map.coordType}")
    private String coordType;

    private MapService actualMapService;

    /**
     * TODO 当前类需要依赖BaiduMapServiceImpl、GaodeMapServiceImpl的实例的创建
     * TODO 使用@DependsOn有缺陷，如果新增GoogleMapServiceImpl,此时未加入@DependsOn是否存在NullPointerException风险
     * TODO 如何做到无任何感知，有待研究
     *
     */
    @PostConstruct
    public void init(){
        actualMapService = MapSimpleFactory.getInstance(CoordTypeEnum.valueOf(coordType.toUpperCase()));
    }

    @Override
    public CoordinatePoint transferAddressToGPSPoint(MapQuery query) {
        MapService instance = MapSimpleFactory.getInstance(CoordTypeEnum.valueOf(coordType.toUpperCase()));
        return instance.transferAddressToGPSPoint(query);
    }

    @Override
    public CoordinatePoint transferGPSPointToAddress(MapQuery query) {
        MapService instance = MapSimpleFactory.getInstance(CoordTypeEnum.valueOf(coordType.toUpperCase()));
        return instance.transferGPSPointToAddress(query);
    }

    @Override
    public List<CoordinatePoint> getServiceStationList(MapQuery query) {
        MapService instance = MapSimpleFactory.getInstance(CoordTypeEnum.valueOf(coordType.toUpperCase()));
        return instance.getServiceStationList(query);
    }

    @Override
    public CoordinatePoint transferAddressToXPoint(MapQuery query) {
        MapService instance = MapSimpleFactory.getInstance(CoordTypeEnum.valueOf(coordType.toUpperCase()));
        return instance.transferAddressToXPoint(query);
    }

    @Override
    public CoordinatePoint transferXPointToAddress(MapQuery query) {
        MapService instance = MapSimpleFactory.getInstance(CoordTypeEnum.valueOf(coordType.toUpperCase()));
        return instance.transferXPointToAddress(query);
    }

    @Override
    public List<CoordinatePoint> getServiceStationListByX(MapQuery query) {
        MapService instance = MapSimpleFactory.getInstance(CoordTypeEnum.valueOf(coordType.toUpperCase()));
        return instance.getServiceStationListByX(query);
    }
}
