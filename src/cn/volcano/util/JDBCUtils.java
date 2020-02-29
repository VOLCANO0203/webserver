package cn.volcano.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 是JDBC的工具类
 *
 */
public class JDBCUtils {
	//私有化构造函数，外界无法直接new对象
	private JDBCUtils() {}
	
	//创建连接池对象
	private static ComboPooledDataSource pool = new ComboPooledDataSource();
	
	/**
	 * 从数据库连接池中获取一个连接对象
	 * @return Connection对象
	 * @throws Exception 
	 */
	//提供静态的getConnection方法
	public static Connection getConnection() throws Exception {
		
		return pool.getConnection();
	}
	
	//提供静态close方法
	public static void close(Connection con, Statement st, ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				rs = null;
			}
		}
		if(st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				st = null;
			}
		}
		if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally {
				con = null;
			}
		}
	}	
}
