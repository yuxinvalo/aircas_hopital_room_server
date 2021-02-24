package server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

public class Server {
	private InetAddress serverAddr;
	private int port;
	private int maxQueue;
	private int maxThread;
	private char suffix;
	private char prefix;
	public final static Logger log = Logger.getLogger(Server.class);
	
	public Server(int port, String ip, int maxQueue, int maxThread, char suffix, char prefix){
		this.serverAddr = Tools.getAddress(ip);
		if (serverAddr == null){
			this.serverAddr = Tools.getAddress("127.0.0.1");
		}
		this.port = port;
		this.maxQueue = maxQueue;
		this.maxThread = maxThread;
		this.suffix = suffix;
		this.prefix = prefix;
		log.info("Init Server done...");
	}
	
	public void startServer(){
		try(ServerSocket serverSocket = new ServerSocket(this.port, this.maxQueue, this.serverAddr)){
			log.info("Generate Server done, Server is running...");
			Executor executor = Executors.newFixedThreadPool(this.maxThread);
			while (true){
				try{
					Socket connection = serverSocket.accept();
					log.info("Got socket from : " + connection.getInetAddress());
					executor.execute(new ServiceTask(connection, prefix, suffix, log));
				} catch (Exception e){
					log.error("Start server exception caused by: ", e.fillInStackTrace());
				}
			}
		} catch (IOException e){
			log.error("Create server exception!", e.fillInStackTrace());
		}
	}
	
	public static void main(String[] args) throws IOException{
		try{
			String configFilePath = "../server.properties";
			Properties prop = Tools.loadConfigurations(configFilePath);
			int port = Integer.parseInt(prop.getProperty("PORT"));
			String ip = prop.getProperty("IP", "127.0.0.1");
			int maxQueue = Integer.parseInt(prop.getProperty("MAX_QUEUE", "10"));
			int maxThread = Integer.parseInt(prop.getProperty("MAX_THREAD", "50"));
			char prefix = prop.getProperty("MSG_PREFIX", "$").charAt(0);
			char suffix = prop.getProperty("MSG_SUFFIX", "#").charAt(0);
			Server server = new Server(port, ip, maxQueue, maxThread, suffix, prefix);
			server.startServer();
		} catch (Exception e){
			log.error("Parse configuration exception!", e.fillInStackTrace());
		}
		
	}

}
