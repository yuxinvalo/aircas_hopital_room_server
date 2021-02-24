package test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class TestClient implements Runnable {
	int i = 0;
	public static void main(String[] args) {
	        for(int i = 0; i < 5; ++i){
	        	System.out.println("Index: " + i);
	        	TestClient tc = new TestClient();
	        	tc.i = 0;
	        	tc.run();
	        	TestClient tc1 = new TestClient();
	        	tc1.i = 1;
	        	tc1.run();
	        	TestClient tc2 = new TestClient();
	        	tc2.i = 2;
	        	tc2.run();
	        	TestClient tc3 = new TestClient();
	        	tc3.i = 3;
	        	tc3.run();
	        }
//		for (int i = 0; i < 100; ++i){
//			String res = TestClient.buildRandomDeviceStatus();
//			System.out.println(res);
//		}
		
	 }
	
	public static String buildRandomDeviceStatus(){
		String res = "";
		String[] IDs = {"TCB-A1", "TCB-A2", "TCB-A3", "TCB-A4", "TCB-A5", "TCB-A6", "TCB-A7", "TCB-B1", "TCB-B2"};
		Random rand = new Random();
		int i = rand.nextInt(8);
		int progress = rand.nextInt(100);
		res = res + IDs[i] + "," + rand.nextInt(80) + "," + progress + "," + rand.nextInt(100) + "," ;
		String ip = "192.168.0." + transformIP(IDs[i].substring(4, 6));
		res = res + ip + ",";
		if (progress > 10){
			res = res + "0";
		} else {
			res = res + "1";
		}
		return res;
	}
	
	public static String transformIP(String ID){
		String res = "";
		if(ID.charAt(0) == 'A'){
			res = res + "1";
		} else {
			res = res + "2";
		}
		res = res + ID.charAt(1);
		return res;
	}

	@Override
	public void run() {
		try {
            Socket socket=new Socket("127.0.0.1", 8082);
            OutputStream outputStream=socket.getOutputStream();
            PrintWriter printWriter=new PrintWriter(outputStream);
            String status = buildRandomDeviceStatus();
            System.out.println("THREAD: " + i + " send status: " + status);
            printWriter.write("$" + status + "#");
            printWriter.flush();
            printWriter.close();
            outputStream.close();
            socket.close();
            Thread.sleep(2000);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
