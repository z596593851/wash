package com.washserver.sql;

import com.washserver.sql.model.ResultListModel;
import org.apache.commons.dbutils.ResultSetHandler;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResultListHandler implements ResultSetHandler<ResultListModel> {
    @Override
    public ResultListModel handle(ResultSet rs) throws SQLException {
        if(!rs.next()){
            return null;
        }else{
            ResultSetMetaData rsmd = rs.getMetaData();
            //列数
            int cols = rsmd.getColumnCount();
            List<ResultListModel.ColumnDes> des=new ArrayList<>(cols);
            List<Object[]> value=new ArrayList<>();

            for(int col = 1; col <= cols; ++col) {
                ResultListModel.ColumnDes columnDes =new ResultListModel.ColumnDes();
                columnDes.setColumnName(rsmd.getColumnName(col));
                columnDes.setColumnType(rsmd.getColumnType(col));
                columnDes.setColumnTypeName(rsmd.getColumnClassName(col));
                des.add(columnDes);
            }
            do{
                Object[] array=new Object[cols];
                for(int col = 1; col <= cols; ++col) {
                    array[col-1]=rs.getObject(col);
                }
                value.add(array);
            }while(rs.next());
            return new ResultListModel(cols,des,value);
        }
    }
}
