package com.map.pojo.gaode;

import lombok.Data;

@Data
public class GaodeBaseRes {
    private int status; //1成功 0失败
    private String info; //status为0时，info返回错误原因;否则返回”OK“
}
