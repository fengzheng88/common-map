package com.map.pojo.gaode;

import lombok.Data;

@Data
public class GaodeReverseStreetNumber {
    private String number; //门牌号
    private String location; //坐标点 经度，纬度
    private String direction; //方向
    private String distance; //距离
    private String street; //街道名称
}
