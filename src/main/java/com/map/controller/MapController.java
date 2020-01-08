package com.map.controller;

import com.map.pojo.MapQuery;
import com.map.pojo.MapSimpleFactory;
import com.map.pojo.CoordinatePoint;
import com.map.service.MapService;
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
     * @param third  第三方名称
     * @param method 调用方法
     * @return
     */
    @RequestMapping("/querymap/{third}/{method}")
    @ResponseBody
    public Object querymap(@RequestBody MapQuery query, @PathVariable("third") String third, @PathVariable("method") String method) {

        MapService instance = MapSimpleFactory.getInstance(third);

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
