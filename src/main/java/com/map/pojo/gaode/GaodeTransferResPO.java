package com.map.pojo.gaode;

import lombok.Data;

/**
 * 高德地图转换返回对象
 */
@Data
public class GaodeTransferResPO extends GaodeBaseRes {
    private String locations; //多个使用";"分隔
}
