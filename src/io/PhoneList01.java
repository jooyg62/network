package io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class PhoneList01 {

	public static void main(String[] args) {
		
		BufferedReader br = null;
		
		try {
			//기반스트림
			//보조스트림1(bytes->char)
			br	=	new BufferedReader(new InputStreamReader(new FileInputStream("phone.txt"), "utf-8"));
			
			//read
			String line = null;
			while((line = br.readLine()) != null) {
//				System.out.println(line);
				
				StringTokenizer st = new StringTokenizer(line, "\t ");
				
//				System.out.println(st.hasMoreElements());
				
				int index = 0;
				while(st.hasMoreElements()) {
					String token = st.nextToken();
					
					System.out.print(token);
					
					if(index == 0) { // 이름
						System.out.print(":");
					} else if(index == 1) { // 전화번호1
						System.out.print("-");
					} else if(index == 2) { // 전화번호2
						System.out.print("-");
					}
					
					index++;
				}
				
				System.out.println("\n");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(br != null) {
					br.close();					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
