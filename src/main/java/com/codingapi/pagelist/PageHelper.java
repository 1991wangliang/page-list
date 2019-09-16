package com.codingapi.pagelist;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.columns.IntegerColumnHandler;

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

    /**
     * 加载数据
     * @param name 唯一表名
     * @param table 表存储的数据类型
     * @param list  数据
     * @param columns   需要转换成json字符串的字段
     * @throws SQLException
     */
    public void initData(String name, Class table, List list,String ... columns) throws SQLException {
        String sql = tableHelper.createTableSql(name, table,columns);

        int res = h2Helper.update(sql);
        log.debug("table-sql->{},state:{}",sql,res);

        for(Object obj:list){
            TableHelper.SqlParam sqlParam = tableHelper.createInsertSql(name,obj,columns);
            log.debug("insert-sql->{}",sqlParam);
            h2Helper.update(sqlParam.getSql(),sqlParam.getParams());
        }
    }


    /**
     * 查询数据
     * @param sql   执行的sql
     * @param resultSetHandler 结果解析器
     * @param <T>   List<T>
     * @return  查询以后的结果数据
     * @throws SQLException
     */
    public <T> List<T> query(String sql, ResultSetHandler<List<T>> resultSetHandler) throws SQLException {
        return h2Helper.query(sql,resultSetHandler);
    }


    /**
     * 删除表
     * @param name  唯一key
     * @throws SQLException
     */
    public void dropTable(String name) throws SQLException{
        h2Helper.update("drop table "+name);
    }

    /**
     * 检查是否存在表名
     * @param name table-name
     * @return 是否存在
     * @throws SQLException
     */
    public boolean haveTable(String name) throws SQLException{
        return h2Helper.count("SELECT COUNT(*) AS count FROM information_schema.tables WHERE table_name = ?",name.toUpperCase())>0;
    }
}
