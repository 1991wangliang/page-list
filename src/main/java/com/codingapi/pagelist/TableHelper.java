package com.codingapi.pagelist;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.pagelist.h2.BaseHandler;
import com.codingapi.pagelist.h2.Handler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

    @Autowired(required = false)
    private Handler selfHandler;

    public SqlParam parser(String initCmd, Object object) {
        Map<String,Object> map = (Map) JSONObject.toJSON(object);
        List<Object> params = new ArrayList<>();
        SqlParam sqlParam = new SqlParam();

        String regex = "\\#\\{([^}]*)\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(initCmd);
        while (matcher.find()){
            String key = matcher.group(1);
            Object val = map.get(key);
            params.add(val);
            initCmd = initCmd.replace("#{" + key + "}", "?");
        }
        sqlParam.setSql(initCmd);
        sqlParam.setParams(params.toArray());
        return sqlParam;
    }

    public  SqlParam createInsertSql(String name,Object object) {
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
        return parser(sb.toString(),object);
    }


    public  String createTableSql(String name,  Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS  ");
        sb.append(name);
        sb.append("(");
        Field[] fields =  clazz.getDeclaredFields();
        int filedLength = fields.length;
        for (int i=0;i<filedLength;i++){
            Field field = fields[i];
            if(i==filedLength-1){
                sb.append(getColumnLine(field.getName(), field.getType()));
            }else {
                sb.append(getColumnLine(field.getName(), field.getType())+",");
            }
        }
        sb.append(")");
        return sb.toString().toUpperCase();
    }

    private  String getColumn(String name){
        return name.toUpperCase();
    }

    private  String getColumnLine(String name,Class<?> type){

        String typeName = java2h2handler( type.getName());

        return String.format("%s %s",name.toUpperCase(),typeName);
    }

    private String java2h2handler(String type) {
        Handler handler = new BaseHandler();
        handler.setNextHandler(selfHandler);
        String val =  handler.request(type);
        log.debug("type:{},val:{}",type,val);
        return val;
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
