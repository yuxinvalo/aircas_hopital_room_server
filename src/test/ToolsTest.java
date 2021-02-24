package test;
import static org.junit.Assert.*;

import java.net.InetAddress;

import org.junit.Test;

import server.Tools;
public class ToolsTest {
	@Test
	public void testGetAddress(){
		String serverIP = "192.168.1.1";
		InetAddress addr = Tools.getAddress(serverIP);
		assertEquals(serverIP, addr.getHostAddress());
		serverIP = "abcde";
		addr = Tools.getAddress(serverIP);
		assertEquals(addr, null);
	}
	
}
