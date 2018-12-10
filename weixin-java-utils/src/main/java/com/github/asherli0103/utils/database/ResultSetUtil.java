/*
 * Copyright  (c) 2017. By AsherLi0103
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.github.asherli0103.utils.database;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author AsherLi0103
 * @version 1.0.00
 */
public class ResultSetUtil {

    public static List<Map<String, Object>> toList(ResultSet rs) {
        Map<String, Object> record = null;
        List<String> columnNameList = new ArrayList<>();
        List<Map<String, Object>> recordSet = new ArrayList<>();
        try {
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                String columnName = rs.getMetaData().getColumnName(i)
                        .toLowerCase();
                columnNameList.add(columnName);
            }
            while (rs.next()) {
                record = new HashMap<>();
                for (String columnName : columnNameList)
                    record.put(columnName, rs.getObject(columnName));

                recordSet.add(record);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return recordSet;
    }

    public static <T> List<T> toList(ResultSet rs, Class<T> classType) {
        List<String> columnNameList = new ArrayList<>();
        List<T> entityList = null;
        try {
            entityList = new ArrayList<>();
            Field[] fields = classType.getDeclaredFields();

            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                String columnName = rs.getMetaData().getColumnName(i).toUpperCase();
                columnNameList.add(columnName);
            }
            while (rs.next()) {
                T entity = classType.newInstance();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    if (columnNameList.contains(fieldName.toUpperCase())) {
                        DataBaseUtil.setProperty(entity, fieldName, rs.getObject(fieldName));
                    }
                }
                entityList.add(entity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return entityList;
    }

    public static Object toSingleResult(ResultSet rs) {
        Object result = null;
        try {
            while (rs.next()) {
                result = rs.getObject(1);
                break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public static List toArrayList(ResultSet rs) {
        List arrayList = null;
        try {
            arrayList = new ArrayList();
            int iCol = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] objArray = new Object[iCol];
                for (int i = 1; i <= iCol; i++) {
                    objArray[i - 1] = rs.getObject(i);
                }
                arrayList.add(objArray);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return arrayList;
    }

    public static Map<String, Object> toHashMap(ResultSet rs) {
        List<String> columnNameList = new ArrayList<>();
        Map<String, Object> record = new HashMap<>();
        try {
            int iRow = 0;
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                String columnName = rs.getMetaData().getColumnName(i)
                        .toLowerCase();
                columnNameList.add(columnName);
            }
            while (rs.next()) {
                if (iRow > 1)
                    throw new RuntimeException(
                            "返回Map<String,Object>类型只能存放一条记录！");
                for (String columnName : columnNameList)
                    record.put(columnName, rs.getObject(columnName));
                iRow++;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return record;
    }

    public static <T> T toBean(ResultSet rs, Class<T> classType) {
        T entity = null;
        List<String> ColumnNameList = new ArrayList<>();
        try {
            entity = classType.newInstance();
            Field[] fields = classType.getDeclaredFields();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                String columnName = rs.getMetaData().getColumnName(i)
                        .toUpperCase();
                ColumnNameList.add(columnName);
            }
            while (rs.next()) {
                for (Field field : fields) {
                    String fieldName = field.getName();
                    if (ColumnNameList.contains(fieldName.toUpperCase())) {
                        DataBaseUtil.setProperty(entity, fieldName, rs.getObject(fieldName));
                    }
                }
                break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return entity;
    }
}
