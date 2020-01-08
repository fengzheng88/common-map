package com.map.pojo;

import com.map.common.util.ApplicationContextUtil;
import com.map.service.MapService;
import com.map.service.impl.BaiduMapServiceImpl;
import com.map.service.impl.GaodeMapServiceImpl;

/**
 * 第三方地图简单工厂
 */
public class MapSimpleFactory {

    public static MapService getInstance(String third) {
        if ("baidu".equalsIgnoreCase(third)) {
            return ApplicationContextUtil.getBean(BaiduMapServiceImpl.class);
        } else if ("gaode".equalsIgnoreCase(third)) {
            return ApplicationContextUtil.getBean(GaodeMapServiceImpl.class);
        } else {
            throw new RuntimeException("不支持该第三方地图");
        }
    }
}
