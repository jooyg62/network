package echo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {
	
	private static final int 	PORT = 	7000; 

	public static void main(String[] args) {

		ServerSocket ss = null;
		
		try {
			//1. 서버 소켓 생성
			ss = new ServerSocket();
			
			//2. 바인딩(binding)
			ss.bind(new InetSocketAddress("0.0.0.0", PORT)); //"0.0.0.0" 이였다가 클라이언트가 찌를 때 ip가 바뀐다.
			log("Server starts...[port:" + PORT + "]");
			
			while(true) {
				//3. accept
				Socket socket 					= ss.accept(); //blocking
				
				Thread thread = new EchoServerReceiveThread(socket);
				thread.start();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ss != null && !ss.isClosed()) {					
					ss.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void log(String log) {
		System.out.println("[server#"+ Thread.currentThread().getId() + "] " + log);
	}
	


}
