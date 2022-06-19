package com.washserver.sql;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.washserver.exception.DataWashException;
import com.washserver.sql.model.ResultListModel;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 每一个数据源都对应一个操作他的DataSourceManager对象
 */
public class DataSourceManager {
    private static final Logger log = LoggerFactory.getLogger(DataSourceManager.class);
    /**
     * 用户配置的数据源名称
     */
    private String sourceName;
    private final static Map<String,DataSource> dataSourcePool=new HashMap<>();
    private final static Map<String, DataSourceManager> dataSourceManagerPool =new HashMap<>();
    /**
     * 驼峰映射处理器
     */
    private final BeanProcessor beanProcessor=new GenerousBeanProcessor();
    private final RowProcessor processor=new BasicRowProcessor(beanProcessor);

    private DataSourceManager(){}
    private DataSourceManager(String sourceName){this.sourceName=sourceName;}

    /**
     * 注册数据库DataSource和对应的DbUtils实例
     * @param sourceName 数据源名称
     * @param config 数据源配置
     * @throws Exception druid异常
     */
    public static void regist(String sourceName, Map<Object,Object> config) throws Exception {
        DataSource dataSource = dataSourcePool.getOrDefault(sourceName, null);
        if(dataSource==null){
            dataSource = DruidDataSourceFactory.createDataSource(config);
            //测试连接
            Connection conn = dataSource.getConnection();
            conn.close();
            dataSourcePool.put(sourceName,dataSource);
        }
        DataSourceManager dataSourceManager =new DataSourceManager(sourceName);
        dataSourceManagerPool.put(sourceName, dataSourceManager);
        log.info("注册数据源{}成功",sourceName);
    }

    /**
     * 获取某个数据源对应的DbUtils实例
     * @param sourceName 数据源名称
     * @return DbUtils实例
     */
    public static DataSourceManager getInstance(String sourceName){
        DataSourceManager dataSourceManager = dataSourceManagerPool.getOrDefault(sourceName, null);
        if(dataSourceManager ==null){
            throw new DataWashException("数据源未注册");
        }
        return dataSourceManager;
    }


    public ResultListModel findList(String sql, Object... params) throws SQLException {
        DataSource dataSource = dataSourcePool.get(sourceName);
        QueryRunner queryRunner=new QueryRunner(dataSource);
        return queryRunner.query(sql,new ResultListHandler(),params);
    }

    /**
     * 查询单条数据
     */
    public <T> T findOne(String sql,Class<? extends T> type,Object... params) throws SQLException {
        DataSource dataSource = dataSourcePool.get(sourceName);
        QueryRunner queryRunner=new QueryRunner(dataSource);
        return queryRunner.query(sql,new BeanHandler<>(type,processor),params);
    }

    /**
     * 查询list
     */
    public <T> List<T> findList(String sql,Class<? extends T> type,Object... params) throws SQLException {
        DataSource dataSource = dataSourcePool.get(sourceName);
        QueryRunner queryRunner=new QueryRunner(dataSource);
        return queryRunner.query(sql,new BeanListHandler<>(type,processor),params);
    }

    /**
     * 增、改、查
     */
    public int saveOrUpdate(String sql,Object... params) throws SQLException {
        DataSource dataSource = dataSourcePool.get(sourceName);
        QueryRunner queryRunner=new QueryRunner(dataSource);
        return queryRunner.update(sql,params);
    }

    /**
     * 批量增、改、查
     */
    public int[] batch(String sql, Object[][] params) throws SQLException {
        DataSource dataSource = dataSourcePool.get(sourceName);
        QueryRunner queryRunner=new QueryRunner(dataSource);
        return queryRunner.batch(sql, params);
    }


}
