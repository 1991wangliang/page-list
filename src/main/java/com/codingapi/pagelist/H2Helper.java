package com.codingapi.pagelist;

import com.codingapi.pagelist.h2.H2Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * @author lorne
 * @date 2019-09-11
 */
public class H2Helper {

    private final HikariDataSource hikariDataSource;

    private final QueryRunner queryRunner;

    public H2Helper(H2Config h2Config) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(org.h2.Driver.class.getName());
        hikariConfig.setJdbcUrl(String.format("jdbc:h2:%s", h2Config.getH2path()));
        hikariDataSource = new HikariDataSource(hikariConfig);
        queryRunner = new QueryRunner(hikariDataSource);
    }

    public int update(String sql,Object... params) throws SQLException {
        return queryRunner.update(sql,params);
    }

    public <T> List<T> query(String sql,ResultSetHandler<List<T>> resultSetHandler) throws SQLException {
        return queryRunner.query(sql, resultSetHandler);
    }

}
