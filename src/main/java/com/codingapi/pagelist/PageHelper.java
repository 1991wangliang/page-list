package com.codingapi.pagelist;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * @author lorne
 * @date 2019-09-11
 */
@Slf4j
public class PageHelper {


    private TableHelper tableHelper;

    private H2Helper h2Helper;


    public PageHelper(TableHelper tableHelper, H2Helper h2Helper) {
        this.tableHelper = tableHelper;
        this.h2Helper = h2Helper;
    }

    public void initData(String name, Class table, List list) throws SQLException {
        String sql = tableHelper.createTableSql(name, table);

        int res = h2Helper.update(sql);
        log.debug("table-sql->{},state:{}",sql,res);

        for(Object obj:list){
            TableHelper.SqlParam sqlParam = tableHelper.createInsertSql(name,obj);
            h2Helper.update(sqlParam.getSql(),sqlParam.getParams());
        }
    }


    public <T> List<T> query(String sql, ResultSetHandler<List<T>> resultSetHandler) throws SQLException {
        return h2Helper.query(sql,resultSetHandler);
    }


    public void dropTable(String name) throws SQLException{
        h2Helper.update("drop table "+name);
    }
}
