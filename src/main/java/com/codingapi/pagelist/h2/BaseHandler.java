package com.codingapi.pagelist.h2;

/**
 * @author lorne
 * @date 2019-09-11
 * @description
 */
public class BaseHandler extends Handler {

    @Override
    public String request(String type) {
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
        if (getNextHandler()!=null){
            return getNextHandler().request(type);
        }
        return null;
    }
}
