package com.map.pojo.baidu;

import lombok.Data;

@Data
public class BaiduReverseAdressComponent {
    private String country; //国家
    private String province; //省名
    private String city; //城市名
    private String district; //区县名
    private String town; //乡镇名
    private String town_code; //乡镇id
    private String street; //街道名
    private String street_number; //街道门牌号
    private String adcode; //行政区划代码
    private String country_code; //国家代码
    private String direction; //相对当前坐标点的方向，当有门牌号的时候返回数据
    private String distance; //相对当前坐标点的距离，当有门牌号的时候返回数据
}
