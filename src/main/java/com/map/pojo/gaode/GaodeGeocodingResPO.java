package com.map.pojo.gaode;

import lombok.Data;

import java.util.List;

/**
 * 正地理编码
 */
@Data
public class GaodeGeocodingResPO extends GaodeBaseRes {
    private int count; //总数
    private List<GaodeGeocodingResultPO> geocodes;

}
