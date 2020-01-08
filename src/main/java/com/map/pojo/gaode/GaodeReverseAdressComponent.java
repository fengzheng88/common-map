package com.map.pojo.gaode;

import lombok.Data;

@Data
public class GaodeReverseAdressComponent {
    private String province; //省名
    private String city; //城市名
    private String district; //区县名
    private String towncode; //乡镇id
    private String country; //国家

    private GaodeReverseStreetNumber streetNumber;
}
