package com.codingapi.pagelist.h2;

import java.util.Arrays;

/**
 * @author lorne
 * @date 2019-09-11
 * @description
 */
public class MySelfHandler implements ColumnHandler {

    @Override
    public String request(String name,String type,String...columns) {
        if(Arrays.asList(columns).contains(name)){
            return "varchar(5000)";
        }
        if("java.lang.String".equals(type)){
            return "varchar(100)";
        }
        if("int".equals(type)){
            return "int(5)";
        }
        if("java.util.Date".equals(type)){
            return "datetime";
        }
        if("java.lang.Long".equals(type)){
            return "bigint";
        }
        return null;
    }
}
