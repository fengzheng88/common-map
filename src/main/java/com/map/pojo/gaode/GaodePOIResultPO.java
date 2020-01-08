package com.map.pojo.gaode;

import lombok.Data;

/**
 * 高德POI返回结果
 */
@Data
public class GaodePOIResultPO {
    private String id;//		唯一ID
    private String parent;//		父POI的ID	当前POI如果有父POI，则返回父POI的ID。可能为空
    private String name;//		名称
    private String type;//	兴趣点类型	顺序为大类、中类、小类 例如：餐饮服务;中餐厅;特色/地方风味餐厅
    private String typecode;//兴趣点类型编码	例如：050118
    private String address;//地址	东四环中路189号百盛北门
    private String location;//纬度,经度	格式：X,Y
    private String distance;//离中心点距离	单位：米；仅在周边搜索的时候有值返回
//    private String tel;//POI的电话
    private String pname;//POI所在省份名称	若是直辖市的时候，此处直接显示市名，例如北京市
    private String cityname;//城市名	 若是直辖市的时候，此处直接显示市名，例如北京市 
    private String adname;//区域名称	区县级别的返回，例如朝阳区

}
