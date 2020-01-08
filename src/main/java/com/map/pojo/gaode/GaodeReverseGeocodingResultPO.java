package com.map.pojo.gaode;

import lombok.Data;

/**
 * 逆地理编码
 */
@Data
public class GaodeReverseGeocodingResultPO {
    private GaodeReverseAdressComponent addressComponent;
    private String formatted_address;

}
