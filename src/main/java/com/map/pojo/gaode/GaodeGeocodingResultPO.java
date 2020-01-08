package com.map.pojo.gaode;

import lombok.Data;

/**
 * 高德POI返回结果
 */
@Data
public class GaodeGeocodingResultPO {
    private String formatted_address;// 结构化地址信息	省份＋城市＋区县＋城镇＋乡村＋街道＋门牌号码
    private String country;// 		国家	国内地址默认返回中国
    private String province;// 		地址所在的省份名	例如：北京市。此处需要注意的是，中国的四大直辖市也算作省级单位。
    private String city;// 		地址所在的城市名	例如：北京市
    private String citycode;// 		城市编码	例如：010
    private String district;// 		地址所在的区	例如：朝阳区
//    private String street;// 		街道	例如：阜通东大街
//    private String number;// 		门牌	例如：6号
    private String adcode;// 		区域编码	例如：110101
    private String location;// 	坐标点	经度，纬度
    private String level;//  匹配级别	参见下方的地理编码匹配级别列表
}
