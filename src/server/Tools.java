package server;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class Tools {
	public static InetAddress getAddress(String serverIP){
		InetAddress serverAddr = null;
		try {
			serverAddr = InetAddress.getByName(serverIP);
		} catch (UnknownHostException e){
			return serverAddr;
		}
		return serverAddr;
	}
	
	public static Properties loadConfigurations(String configFilePath){
		try {
			InputStream inputStream = Server.class.getResourceAsStream(configFilePath);
			Properties prop = new Properties();
			prop.load(inputStream);
			return prop;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
