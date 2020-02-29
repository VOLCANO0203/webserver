package cn.volcano.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * ��JDBC�Ĺ�����
 *
 */
public class JDBCUtils {
	//˽�л����캯��������޷�ֱ��new����
	private JDBCUtils() {}
	
	//�������ӳض���
	private static ComboPooledDataSource pool = new ComboPooledDataSource();
	
	/**
	 * �����ݿ����ӳ��л�ȡһ�����Ӷ���
	 * @return Connection����
	 * @throws Exception 
	 */
	//�ṩ��̬��getConnection����
	public static Connection getConnection() throws Exception {
		
		return pool.getConnection();
	}
	
	//�ṩ��̬close����
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
