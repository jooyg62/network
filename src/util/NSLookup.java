package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class NSLookup {

	public static void main(String[] args) {
		
		BufferedReader br = null;
		
		try {
			
			br = new BufferedReader(new InputStreamReader(System.in, "utf-8"));
			
			String line = null;
			while((line = br.readLine()) != null) {
				
				if("exit".equals(line)) {
					System.out.println("NSLookup exit!!");
					break;
				}
				
				// NSLookup 출력
				InetAddress[] inetAddress = InetAddress.getAllByName(line);
				
				for(InetAddress addr : inetAddress) {
					System.out.println( line + " : " + addr.getHostAddress() );
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
