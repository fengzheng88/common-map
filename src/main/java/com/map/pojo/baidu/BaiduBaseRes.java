package com.map.pojo.baidu;

import lombok.Data;

@Data
public class BaiduBaseRes {
    private int status; //本次API访问状态，如果成功返回0，如果失败返回其他数字。（见服务状态码）
    private String message; //对API访问状态值的英文说明，如果成功返回"ok"，并返回结果字段，如果失败返回错误说明。
}
