package com.map.common.util;

import com.map.common.enums.CoordinateTypeEnum;
import com.map.service.MapService;
import com.map.service.impl.BaiduMapServiceImpl;
import com.map.service.impl.GaodeMapServiceImpl;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 第三方地图简单工厂
 */
public class MapSimpleFactory {

    static ConcurrentHashMap<CoordinateTypeEnum, MapService> map = new ConcurrentHashMap();

    /**
     * 上下文存在就返回，不存在就先创建再返回
     * @param coordinateType
     * @return
     */
    public static MapService getInstance(CoordinateTypeEnum coordinateType) {
        MapService mapService = map.get(coordinateType);
        if(mapService == null){
            if (coordinateType == CoordinateTypeEnum.BAIDU) {
                mapService = ApplicationContextUtil.getBean(BaiduMapServiceImpl.class);
            } else if (coordinateType == CoordinateTypeEnum.GAODE) {
                mapService = ApplicationContextUtil.getBean(GaodeMapServiceImpl.class);
            } else {
                throw new RuntimeException("不支持该第三方地图");
            }
        }
        return mapService;
    }
}
