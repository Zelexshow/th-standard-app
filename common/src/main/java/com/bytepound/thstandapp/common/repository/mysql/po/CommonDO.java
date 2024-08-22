package com.bytepound.thstandapp.common.repository.mysql.po;

import lombok.Data;

import java.util.Date;

/**
 * 通用do
 * @param <T>
 */
@Data
public class CommonDO <T>{
    private T id;
    private Date gmtCreateTime;
    private Date gmtModTime;
    private boolean isDelete;

    public void initNow(){
        Date date = new Date();
        setGmtCreateTime(date);
        setGmtModTime(date);
        setDelete(false);
    }
}
