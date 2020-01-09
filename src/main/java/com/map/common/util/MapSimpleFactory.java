package com.map.common.util;

import com.map.common.enums.CoordTypeEnum;
import com.map.common.util.ApplicationContextUtil;
import com.map.service.MapService;
import com.map.service.impl.BaiduMapServiceImpl;
import com.map.service.impl.GaodeMapServiceImpl;

/**
 * 第三方地图简单工厂
 */
public class MapSimpleFactory {

    public static MapService getInstance(CoordTypeEnum coordType) {
        if (coordType == CoordTypeEnum.BAIDU) {
            return ApplicationContextUtil.getBean(BaiduMapServiceImpl.class);
        } else if (coordType == CoordTypeEnum.GAODE) {
            return ApplicationContextUtil.getBean(GaodeMapServiceImpl.class);
        } else {
            throw new RuntimeException("不支持该第三方地图");
        }
    }
}
