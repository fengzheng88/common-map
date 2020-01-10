package com.map.service.impl;

import com.map.common.enums.CoordinateTypeEnum;
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
 * 为啥要弄这个类呢？为了使用配置来指定使用哪种具体的实例,且对其他程序猿也是无感知的(使用@Autowird MapService mapService时无须指定哪种实例,无需管具体有多少实例)
 * @see com.map.service.impl.BaiduMapServiceImpl
 * @see com.map.service.impl.GaodeMapServiceImpl
 */
@Primary
@Service
public class DefaultMapServiceImpl implements MapService {

    @Value("${map.coordinateType}")
    private String coordinateType;

    private MapService actualMapService;

    /**
     * 通过配置指定具体实例
     */
    @PostConstruct
    public void init(){
        actualMapService = MapSimpleFactory.getInstance(CoordinateTypeEnum.valueOf(coordinateType.toUpperCase()));
    }

    @Override
    public CoordinatePoint transferAddressToGPSPoint(MapQuery query) {
        return actualMapService.transferAddressToGPSPoint(query);
    }

    @Override
    public CoordinatePoint transferGPSPointToAddress(MapQuery query) {
        return actualMapService.transferGPSPointToAddress(query);
    }

    @Override
    public List<CoordinatePoint> getServiceStationList(MapQuery query) {
        return actualMapService.getServiceStationList(query);
    }

    @Override
    public CoordinatePoint transferAddressToXPoint(MapQuery query) {
        return actualMapService.transferAddressToXPoint(query);
    }

    @Override
    public CoordinatePoint transferXPointToAddress(MapQuery query) {
        return actualMapService.transferXPointToAddress(query);
    }

    @Override
    public List<CoordinatePoint> getServiceStationListByX(MapQuery query) {
        return actualMapService.getServiceStationListByX(query);
    }
}
