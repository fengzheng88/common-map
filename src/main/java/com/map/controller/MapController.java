package com.map.controller;

import com.map.common.enums.CoordinateTypeEnum;
import com.map.pojo.MapQuery;
import com.map.pojo.CoordinatePoint;
import com.map.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class MapController {

    ConcurrentHashMap<CoordinateTypeEnum, MapService> concurrentHashMap = new ConcurrentHashMap<>();

    @Resource
    MapService baiduMapServiceImpl;

    @Resource
    MapService gaodeMapServiceImpl;

    /**
     * 默认使用@Primary注解修饰的地图实例
     * @see com.map.service.impl.DefaultMapServiceImpl
     */
    @Autowired
    private MapService mapService;

    @PostConstruct
    public void init(){
        concurrentHashMap.put(CoordinateTypeEnum.BAIDU, baiduMapServiceImpl);
        concurrentHashMap.put(CoordinateTypeEnum.GAODE, gaodeMapServiceImpl);
    }


    /**
     * 示例：http://localhost:8080/querymap/baidu/poi
     * body示例：
     * {
     * 	"coordinatePoint":{
     * 		"longitude":"121.44056",
     * 		"latitude":"31.164404"
     *        },
     * 	"queryName":"停车场"
     * }
     * @param query  请求对象
     * @param coordinateType 地图类型
     * @param method 调用方法
     * @return
     */
    @RequestMapping("/queryMap/{coordinateType}/{method}")
    @ResponseBody
    public Object querymap(@RequestBody MapQuery query, @PathVariable("coordinateType") String coordinateType, @PathVariable("method") String method) {

        /**
         * @Author xiedong say 实例放入map更香
         *
         */
        MapService instance = concurrentHashMap.getOrDefault(CoordinateTypeEnum.valueOf(coordinateType.toUpperCase()), mapService);

        //MapService instance = MapSimpleFactory.getInstance(CoordinateType.valueOf(coordinateType.toUpperCase()));

        //这个可以提取到service处理，这边就不弄了
        switch (method) {
            case "gps":
                CoordinatePoint gpsPointDetail = instance.transferAddressToXPoint(query);
                return gpsPointDetail;
            case "address":
                CoordinatePoint gpsPointDetail1 = instance.transferXPointToAddress(query);
                return gpsPointDetail1;
            case "poi":
                List<CoordinatePoint> serviceStationList = instance.getServiceStationListByX(query);
                return serviceStationList;
            default:
                throw new RuntimeException("不支持该方法");
        }
    }
}
