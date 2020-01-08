package com.map.pojo.gaode;

import lombok.Data;

import java.util.List;

/**
 * 高德地图POI请求
 */
@Data
public class GaodePOIResPO extends GaodeBaseRes  {
    private int count; //搜索方案数目(最大值为1000)
    private List<GaodePOIResultPO> pois;

}
