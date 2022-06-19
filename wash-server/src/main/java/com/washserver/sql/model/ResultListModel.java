package com.washserver.sql.model;

import java.util.List;

/**
 * {
 *     "des":[
 *         {
 *             "columnName":"id",
 *             "columnType":4,
 *             "columnTypeName":"java.lang.Integer"
 *         },
 *         {
 *             "columnName":"name",
 *             "columnType":12,
 *             "columnTypeName":"java.lang.String"
 *         }
 *     ],
 *     "size":2,
 *     "value":[
 *         [
 *             1,
 *             "tom"
 *         ],
 *         [
 *             2,
 *             "jim"
 *         ]
 *     ]
 * }
 */
public class ResultListModel {
    private int size;
    private List<ColumnDes> des;
    private List<Object[]> value;

    public ResultListModel(){}

    public ResultListModel(int size, List<ColumnDes> des, List<Object[]> value) {
        this.size = size;
        this.des = des;
        this.value = value;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<ColumnDes> getDes() {
        return des;
    }

    public void setDes(List<ColumnDes> des) {
        this.des = des;
    }

    public List<Object[]> getValue() {
        return value;
    }

    public void setValue(List<Object[]> value) {
        this.value = value;
    }

    public static class ColumnDes {
        private String columnName;
        private int columnType;
        private String columnTypeName;

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public int getColumnType() {
            return columnType;
        }

        public void setColumnType(int columnType) {
            this.columnType = columnType;
        }

        public String getColumnTypeName() {
            return columnTypeName;
        }

        public void setColumnTypeName(String columnTypeName) {
            this.columnTypeName = columnTypeName;
        }
    }
}
