package com.map.pojo.gaode;

import lombok.Data;

/**
 * 逆地理编码
 */
@Data
public class GaodeReverseGeocodingResPO extends GaodeBaseRes {
    private GaodeReverseGeocodingResultPO regeocode;
}
