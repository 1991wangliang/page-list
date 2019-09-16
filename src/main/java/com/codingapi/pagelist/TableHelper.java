package com.codingapi.pagelist;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.pagelist.h2.ColumnHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lorne
 * @date 2019-09-11
 * @description
 */
@Slf4j
public class TableHelper {

    @Autowired
    private ColumnHandler columnHandler;

    public SqlParam parser(String initCmd, Object object,String ... columns) {
        Map<String,Object> map = (Map) JSONObject.toJSON(object);
        List<Object> params = new ArrayList<>();
        SqlParam sqlParam = new SqlParam();

        String regex = "\\#\\{([^}]*)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(initCmd);
        while (matcher.find()){
            String key = matcher.group(1);
            Object val = map.get(key);
            if(Arrays.asList(columns).contains(key)){
                val = val.toString();
            }
            params.add(val);
            initCmd = initCmd.replace("#{" + key + "}", "?");
        }
        sqlParam.setSql(initCmd);
        sqlParam.setParams(params.toArray());
        return sqlParam;
    }

    public  SqlParam createInsertSql(String name,Object object,String ... columns) {
        Class clazz = object.getClass();
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ");
        sb.append(name);
        sb.append("(");
        Field[] fields =  clazz.getDeclaredFields();
        int filedLength = fields.length;
        for (int i=0;i<filedLength;i++){
            Field field = fields[i];
            if(i==filedLength-1){
                sb.append(getColumn(field.getName()));
            }else {
                sb.append(getColumn(field.getName())+",");
            }
        }
        sb.append(")");
        sb.append(" values(");
        for (int i=0;i<filedLength;i++){
            Field field = fields[i];
            if(i==filedLength-1){
                sb.append("#{"+field.getName()+"}");
            }else {
                sb.append("#{"+field.getName()+"},");
            }
        }
        sb.append(")");
        return parser(sb.toString(),object,columns);
    }


    public  String createTableSql(String name,  Class<?> clazz,String...columns) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS  ");
        sb.append(name);
        sb.append("(");
        Field[] fields =  clazz.getDeclaredFields();
        int filedLength = fields.length;
        for (int i=0;i<filedLength;i++){
            Field field = fields[i];
            if(i==filedLength-1){
                sb.append(getColumnLine(field.getName(), field.getType(),columns));
            }else {
                sb.append(getColumnLine(field.getName(), field.getType(),columns)+",");
            }
        }
        sb.append(")");
        return sb.toString().toUpperCase();
    }

    private  String getColumn(String name){
        return name.toUpperCase();
    }

    private  String getColumnLine(String name,Class<?> type,String ... columns){
        String typeName =  columnHandler.request(name,type.getName(),columns);
        return String.format("%s %s",name.toUpperCase(),typeName);
    }




    public String createClearSql(String name) {
        return "truncate "+name;
    }


    @Data
    public static class SqlParam{
        String sql;
        Object[] params;
    }

}
