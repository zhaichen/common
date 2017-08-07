package io.github.zhaichen.util;


import io.github.zhaichen.annotation.ColumnLabel;
import io.github.zhaichen.annotation.Table;
import io.github.zhaichen.model.BaseModel;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created with com.ptmind.datadeck.app.user.util.
 * Author: ZhaiChen
 * Date: 2017/8/3 17:18
 * Description:
 */
public class DaoUtils {


    private static ResultSet resultSet;
    private static Class<Object> clazz;

    /**
     * 根据实体生成对应的SQL信息和数据信息  如 insert into tables(id,name,value) values(?,?,?)
     * @param baseModel
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String,List<Object>> getInsertMetaData(BaseModel baseModel) throws IllegalAccessException {
        String tableName = getTableName(baseModel);
        if(StringUtils.isBlank(tableName)){
            throw  new RuntimeException("Model @Table not found.");
        }
        Map<String,Object> dataMap = getColumnMap(baseModel);
        if(MapUtils.isEmpty(dataMap)){
            throw  new RuntimeException("Model @ColumnLabel not found.");
        }

        List<Object> valueList = new ArrayList<>();
        Map<String,List<Object>> resultMap = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(tableName).append(" (");

        dataMap.forEach((k,v)->{
            sb.append(k).append(",");
            valueList.add(v);
        });
        sb.deleteCharAt(sb.length()-1);
        sb.append(") VALUES (");


        IntStream.range(0,valueList.size()).forEach((i)->{
            sb.append("?").append(",");
        });
        sb.deleteCharAt(sb.length()-1);
        sb.append(")");
        resultMap.put(sb.toString(),valueList);
        return resultMap;
    }



    public static <T> T getRowMapper(ResultSet resultSet,Class<T> clazz) throws IllegalAccessException, InstantiationException, SQLException, NoSuchMethodException, InvocationTargetException {
        T t = clazz.newInstance();
        Class dataObj = t.getClass();
        Field[] fields = dataObj.getDeclaredFields();

        for (Field field : fields) {
            if(field.isAnnotationPresent(ColumnLabel.class)){
                ColumnLabel columnLabel = field.getAnnotation(ColumnLabel.class);
                String columnName = columnLabel.value();
                String type = field.getGenericType().getTypeName();
                if(type.equals("java.lang.String")){

                    String  colmnValue = resultSet.getString(columnName);
                    char[] fieldArray = field.getName().toCharArray();
                    fieldArray[0] = Character.toUpperCase(fieldArray[0]);
                    String mehodName = "set" + new String(fieldArray);
                    Method method = dataObj.getMethod(mehodName,String.class);
                    method.invoke(t,colmnValue);

                }else if(type.equals("java.lang.Integer")){

                    int  colmnValue = resultSet.getInt(columnName);
                    char[] fieldArray = field.getName().toCharArray();
                    fieldArray[0] = Character.toUpperCase(fieldArray[0]);
                    String mehodName = "set" + new String(fieldArray);
                    Method method = dataObj.getMethod(mehodName,Integer.class);
                    method.invoke(t,colmnValue);

                }else if(type.equals("java.lang.Long")){

                    Long  colmnValue = resultSet.getLong(columnName);
                    char[] fieldArray = field.getName().toCharArray();
                    fieldArray[0] = Character.toUpperCase(fieldArray[0]);
                    String mehodName = "set" + new String(fieldArray);
                    Method method = dataObj.getMethod(mehodName,Long.class);
                    method.invoke(t,colmnValue);

                }else if(type.equals("java.lang.Double")){

                    Double  colmnValue = resultSet.getDouble(columnName);
                    char[] fieldArray = field.getName().toCharArray();
                    fieldArray[0] = Character.toUpperCase(fieldArray[0]);
                    String mehodName = "set" + new String(fieldArray);
                    Method method = dataObj.getMethod(mehodName,Double.class);
                    method.invoke(t,colmnValue);

                }else if(type.equals("java.lang.Boolean")){

                    Boolean  colmnValue = resultSet.getBoolean(columnName);
                    char[] fieldArray = field.getName().toCharArray();
                    fieldArray[0] = Character.toUpperCase(fieldArray[0]);
                    String mehodName = "set" + new String(fieldArray);
                    Method method = dataObj.getMethod(mehodName,Boolean.class);
                    method.invoke(t,colmnValue);

                }else if(type.equals("java.lang.Date")){

                    Date colmnValue = resultSet.getDate(columnName);
                    char[] fieldArray = field.getName().toCharArray();
                    fieldArray[0] = Character.toUpperCase(fieldArray[0]);
                    String mehodName = "set" + new String(fieldArray);
                    Method method = dataObj.getMethod(mehodName,Date.class);
                    method.invoke(t,colmnValue);

                }else if(type.equals("java.lang.Short")){

                    Short colmnValue = resultSet.getShort(columnName);
                    char[] fieldArray = field.getName().toCharArray();
                    fieldArray[0] = Character.toUpperCase(fieldArray[0]);
                    String mehodName = "set" + new String(fieldArray);
                    Method method = dataObj.getMethod(mehodName,Short.class);
                    method.invoke(t,colmnValue);

                }else {
                    throw new RuntimeException(clazz.getName()+"--->"+field.getName()+" 未知的映射类型:"+type);
                }
            }
        }

        return t;
    }

    private static String getTableName(BaseModel baseModel){
        Class cla = baseModel.getClass();
        if(cla.isAnnotationPresent(Table.class)){
            Table table = (Table) cla.getAnnotation(Table.class);
            return table.value();
        }
        return "";
    }

    private static Map<String,Object> getColumnMap(BaseModel baseModel) throws IllegalAccessException {
        Map<String,Object> dataMap = new HashMap<>();
        Class cla = baseModel.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field : fields) {
            if(field.isAnnotationPresent(ColumnLabel.class)){
                ColumnLabel columnLabel = field.getAnnotation(ColumnLabel.class);
                field.setAccessible(true);
                Object value = field.get(baseModel);
                if(value != null){
                    String columnName = columnLabel.value();
                    dataMap.put(columnName,value);
                }
            }
        }
        return dataMap;
    }


    private DaoUtils() {
    }
}
