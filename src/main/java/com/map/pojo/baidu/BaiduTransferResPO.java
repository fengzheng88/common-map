package com.map.pojo.baidu;

import lombok.Data;

import java.util.List;

/**
 * 百度坐标转换返回对象
 */
@Data
public class BaiduTransferResPO extends BaiduBaseRes {
    private List<Coordinate> result;//转换结果
}
