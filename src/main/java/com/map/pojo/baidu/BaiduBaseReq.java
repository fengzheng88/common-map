package com.map.pojo.baidu;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 百度请求对象共通参数
 */
@Data
public class BaiduBaseReq {
    @NotEmpty(message = "访问密钥不能为空")
    private String ak;
    private String output = "json";
}
