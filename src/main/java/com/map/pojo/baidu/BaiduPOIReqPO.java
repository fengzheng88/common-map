package com.map.pojo.baidu;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 百度poi请求对象
 */
@Data
public class BaiduPOIReqPO extends BaiduBaseReq{
    @NotEmpty(message = "关键字搜索不能为空")
    private String query; //关键字搜索
    private String tag; //检索分类偏好
    @NotEmpty(message = "圆形区域中心点不能为空")
    private String location; //圆形区域中心点 lat纬度，lng经度
    private String radius; //半径,米
    private String radius_limit = "false"; //是否严格限定召回结果在设置检索半径范围内
    private String scope;//检索结果详细程度1基本，2详细
    private String coord_type; //1:wgs84ll/3:bd09ll
    //private String ret_coordtype; //只有bd09ll/gcj02ll
}
