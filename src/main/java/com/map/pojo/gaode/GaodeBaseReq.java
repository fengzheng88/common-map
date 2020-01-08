package com.map.pojo.gaode;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 百度请求对象共通参数
 */
@Data
public class GaodeBaseReq {
    @NotEmpty(message = "访问密钥不能为空")
    private String key;
    private String output = "JSON"; //JSON/XML
}
