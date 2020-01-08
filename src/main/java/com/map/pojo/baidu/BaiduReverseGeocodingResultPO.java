package com.map.pojo.baidu;

import lombok.Data;

/**
 * 逆地理编码返回对象
 */
@Data
public class BaiduReverseGeocodingResultPO {
    private Coordinate location;
    private String formatted_address;
    private String business;
    private BaiduReverseAdressComponent addressComponent;
}
