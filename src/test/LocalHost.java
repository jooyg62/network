package test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			InetAddress inetAddress = InetAddress.getLocalHost();
			
			String hostName = inetAddress.getHostName();
			String hostAddress = inetAddress.getHostAddress();
			
			System.out.println("HostName : " + hostName + ", HostAddress : " + hostAddress);
			
			byte[] addresses = inetAddress.getAddress();
			for( byte address : addresses ) {
				System.out.println( address & 0x000000ff );
			}
			
			/*
			InetAddress[] inetAddresses = inetAddress.getAllByName(hostName);
			
			for(InetAddress addr : inetAddresses) {
				System.out.println( addr.getHostAddress() );
			}
			*/
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
