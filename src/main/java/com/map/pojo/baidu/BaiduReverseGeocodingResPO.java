package com.map.pojo.baidu;

import lombok.Data;

/**
 * 逆地理编码返回对象
 */
@Data
public class BaiduReverseGeocodingResPO extends BaiduBaseRes {
    private BaiduReverseGeocodingResultPO result;
}
