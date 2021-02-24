package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DBHelper{
	private Connection conn = null;
	private String driver;
	private String dbUrl, dbUser, dbPass;
	private PreparedStatement ps = null;
	public static final String UPDATE_DEVICE_SQL = "UPDATE t_device SET frequency = ? , progress = ?, power = ?, ip_device = ?, ip_router = ?, alarm = ? WHERE id = ?";
	public static final int ID = 0;
	public static final int FREQUENCY = 1;
	public static final int POWER = 3;
	public static final int PROGRESS = 2; 
	public static final int IP_DEVICE = 4;
	public static final int IP_ROUTER = 6;
	public static final int ALARM = 5;
	
	
	Logger log = Logger.getLogger(DBHelper.class);
	
	public DBHelper(String dbUrl, String dbUser, String dbPass, String driver){
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
		this.driver = driver;
	}
	
	public DBHelper() throws Exception{
		Properties prop = Tools.loadConfigurations("../server.properties");
		this.dbUrl = prop.getProperty("DB_URL");
		this.dbUser = prop.getProperty("DB_USER");
		this.dbPass = prop.getProperty("DB_PWD");
		this.driver = prop.getProperty("DB_DRIVER");
	}
	
	public boolean connectDB(){
		try{
			Class.forName(this.driver);
			this.conn = DriverManager.getConnection(this.dbUrl, this.dbUser, this.dbPass);
			if (!this.conn.isClosed()){
				return true;
			}
		}catch (ClassNotFoundException e){
			log.error("Connect DB failed...Class not found", e.fillInStackTrace());
		} catch(SQLException e){
			log.error("Connect DB failed...SQL exception", e.fillInStackTrace());
		}
		return false;
	}
	
	public int updateDeviceStatus(String[] status){
		if (this.conn == null){
			if (!this.connectDB()){
				log.error("Connect to DB failed! It won't save Device status!");
				return 0;
			}
		}
		try {
			this.ps = this.conn.prepareStatement(UPDATE_DEVICE_SQL);
			this.ps.setString(7, status[ID]);
			this.ps.setInt(1, Integer.parseInt(status[FREQUENCY]));
			this.ps.setInt(2, Integer.parseInt(status[PROGRESS]));
			this.ps.setInt(3, Integer.parseInt(status[POWER]));
			this.ps.setString(4, status[IP_DEVICE]);
			this.ps.setString(5, status[IP_ROUTER]);
			this.ps.setInt(6, Integer.parseInt(status[ALARM]));
			int rowCount = this.ps.executeUpdate();
			return rowCount;
		} catch (SQLException e) {
			log.error("Insert action failed! ", e.fillInStackTrace());
			return 0;
		}
		
	}
	
	public void closeConnection(){
		try{
			this.conn.close();
			
		} catch (SQLException e){
			log.error("Close connection failed...SQL exception", e.fillInStackTrace());
		}
	}
	
	
}
