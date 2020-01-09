package com.map.controller;

import com.map.common.enums.CoordTypeEnum;
import com.map.pojo.MapQuery;
import com.map.common.util.MapSimpleFactory;
import com.map.pojo.CoordinatePoint;
import com.map.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MapController {
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
     * @param coordtype 地图类型
     * @param method 调用方法
     * @return
     */
    @RequestMapping("/querymap/{coordtype}/{method}")
    @ResponseBody
    public Object querymap(@RequestBody MapQuery query, @PathVariable("coordtype") String coordtype, @PathVariable("method") String method) {

        MapService instance = MapSimpleFactory.getInstance(CoordTypeEnum.valueOf(coordtype.toUpperCase()));

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


    /**
     * 默认使用@Primary注解修饰的地图实例
     */
    @Autowired
    private MapService mapService;

    /**
     * 使用cloud中的配置来指定使用地图的实例
     * @param query
     * @param method
     * @return
     */
    @RequestMapping("/querymap2/{method}")
    @ResponseBody
    public Object query(@RequestBody MapQuery query, @PathVariable("method") String method){
        switch (method) {
            case "gps":
                CoordinatePoint gpsPointDetail = mapService.transferAddressToXPoint(query);
                return gpsPointDetail;
            case "address":
                CoordinatePoint gpsPointDetail1 = mapService.transferXPointToAddress(query);
                return gpsPointDetail1;
            case "poi":
                List<CoordinatePoint> serviceStationList = mapService.getServiceStationListByX(query);
                return serviceStationList;
            default:
                throw new RuntimeException("不支持该方法");
        }
    }
}
