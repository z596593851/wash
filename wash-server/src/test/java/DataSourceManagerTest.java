import com.alibaba.fastjson2.JSONObject;
import com.washserver.sql.DataSourceManager;
import com.washserver.sql.model.ResultListModel;
import com.washserver.sql.model.Stu;
import org.junit.Before;
import org.junit.Test;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSourceManagerTest {

//    @Before
    public void init() throws Exception {
        Map<Object,Object> config=new HashMap<>();
        config.put("driverClassName","com.mysql.cj.jdbc.Driver");
        config.put("url","jdbc:mysql://localhost:3306/sys?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull");
        config.put("username","root");
        config.put("password","19951127");
        config.put("initialSize","5");
        config.put("maxActive","5");
        config.put("maxWait","3000");
        DataSourceManager.regist("sys",config);
    }

    @Before
    public void initTest() throws Exception {
        Map<Object,Object> config=new HashMap<>();
        config.put("driverClassName","com.mysql.cj.jdbc.Driver");
        config.put("url","jdbc:mysql://localhost:3306/localdb?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull");
        config.put("username","root");
        config.put("password","root");
        config.put("initialSize","5");
        config.put("maxActive","5");
        config.put("maxWait","3000");
        DataSourceManager.regist("localdb",config);
    }


    @Test
    public void dyn() throws SQLException {
        String sql="select * from stu";
        DataSourceManager dataSourceManager = DataSourceManager.getInstance("localdb");
        ResultListModel result = dataSourceManager.findList(sql);
        System.out.println(JSONObject.toJSONString(result));
    }

    @Test
    public void findOneTest() throws SQLException {
        int id=1;
        String sql="select * from stu where id=?";
        DataSourceManager dataSourceManager = DataSourceManager.getInstance("sys");
        Stu stu = dataSourceManager.findOne(sql, Stu.class, id);
        System.out.println(stu);
    }

    @Test
    public void findListTest() throws SQLException {
        String sql="select * from stu";
        DataSourceManager dataSourceManager = DataSourceManager.getInstance("sys");
        List<Stu> result = dataSourceManager.findList(sql,Stu.class);
        System.out.println(JSONObject.toJSONString(result));
    }

    @Test
    public void saveTest() throws SQLException {
        String sql="insert into stu(stu_name,age,sex) values(?,?,?)";
        DataSourceManager dataSourceManager = DataSourceManager.getInstance("sys");
        int result = dataSourceManager.saveOrUpdate(sql, "test1", 10, "f");
        System.out.println(result);
    }

    @Test
    public void updateTest() throws SQLException {
        String sql="update stu set stu_name=? where id=?";
        DataSourceManager dataSourceManager = DataSourceManager.getInstance("sys");
        int result = dataSourceManager.saveOrUpdate(sql, "test2", 7);
        System.out.println(result);
    }

    @Test
    public void batchInsertTest() throws SQLException {
        String sql="insert into stu(stu_name,age,sex) values(?,?,?)";
        Object[][]param=new Object[][]{{"test5", 5, "f"},{"test6", 6, "f"}};

        DataSourceManager dataSourceManager = DataSourceManager.getInstance("sys");
        int[] result = dataSourceManager.batch(sql, param);
        System.out.println(JSONObject.toJSONString(result));
    }

    @Test
    public void batchUpdateTest() throws SQLException {
        String sql="update stu set stu_name=? where id=?";
        Object[][]param=new Object[][]{{"test8",8},{"test9",9}};

        DataSourceManager dataSourceManager = DataSourceManager.getInstance("sys");
        int[] result = dataSourceManager.batch(sql, param);
        System.out.println(JSONObject.toJSONString(result));
    }

}
