package com.codingapi.pagelist.h2;

/**
 * @author lorne
 * @date 2019-09-11
 * @description
 */
public interface ColumnHandler {

    String request(String name,String type,String...columns);
}
