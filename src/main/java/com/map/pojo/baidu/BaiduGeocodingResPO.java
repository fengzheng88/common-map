package com.map.pojo.baidu;

import lombok.Data;

/**
 * 正地理编码
 */
@Data
public class BaiduGeocodingResPO extends BaiduBaseRes {
    private BaiduGeocodingResultPO result;
}
