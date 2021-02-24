package server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

public class ServiceTask implements Runnable{
	private Socket connection;
	private char prefix;
	private char suffix;
	Logger log;
	
	public ServiceTask(Socket connection, char prefix, char suffix, Logger log){
		this.connection = connection;
		this.prefix = prefix;
		this.suffix = suffix;
		this.log = log;
	}
	
	@Override
	public void run() {
		Socket conn = connection;
		StringBuilder recStrBuilder = new StringBuilder();
		boolean isStartRead = false;
		try{
			InputStream in = conn.getInputStream();
			for (int c = in.read(); c != suffix; c = in.read()){
				if (isStartRead){
					recStrBuilder.append((char)c);
					continue;
				}
				if (c == prefix){
					isStartRead = true;
				}
			}
			String recStr = recStrBuilder.toString();
			recStr = recStr + "," + conn.getInetAddress().getHostAddress();
			String[] status = recStr.split(",");
			log.info("GOT MESSAGE: " + recStr);
			// TODO: Save data to SQLSERVER
			if (status.length != 7){
				log.error("Erupted information: " + recStr);
				return;
			} else{
				DBHelper helper = new DBHelper();
				helper.connectDB();
				int count = helper.updateDeviceStatus(status);
				log.info("Insert or update row: " + count);
			}	
		}catch(Exception e){
			log.error("Read device information exception caused by: " + e);
		} finally {
			try{
				if (conn != null){
					conn.close();
				}
			} catch (IOException e){
				log.error("Close socket connection exception caused by: " + e);
			}
		}
	}
	
}
