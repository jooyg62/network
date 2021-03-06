package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {

	public static void main(String[] args) {
		
		ServerSocket ss = null;
		
		try {
			//1. 서버 소켓 생성
			ss = new ServerSocket();
			
			//1-1. Time-Wait 시간에 소켓에 포트번호 할당을 가능하게 하기 위해서
			ss.setReuseAddress(true);
			
			//2. 바인딩(binding)
			//	 : Socket에 SocketAddress(IPAddress + Port)를 바인딩 한다.
			
//			InetAddress inetAddress 	= InetAddress.getLocalHost();
//			String localHost 			= inetAddress.getHostAddress();
//			ss.bind(new InetSocketAddress(inetAddress, 5000));
			ss.bind(new InetSocketAddress("0.0.0.0", 5001)); //"0.0.0.0" 이였다가 클라이언트가 찌를 때 ip가 바뀐다.
			
			//3. accept
			//	 : 클라이언트의 연결요청을 기다림.
			Socket socket 					= ss.accept(); //blocking
			
			InetSocketAddress irsa 			= (InetSocketAddress) socket.getRemoteSocketAddress();
			
			String 	remoteHostAddress 		= irsa.getAddress().getHostAddress();
			int 	remotePort 				= irsa.getPort();
			
			System.out.println(
					"[server] connected by client[" + 
					remoteHostAddress +	":" + 
					remotePort +
					"]");
			
			try {
				
				//4. IOStream
				InputStream 	is = socket.getInputStream();
				OutputStream 	os = socket.getOutputStream();
				
				while(true) {
					//5. 데이터 읽기
					byte[] buffer = new byte[256];
					int readByteCount = is.read(buffer); //blocking
					if(readByteCount == -1) {
						//클라이언트 정상종료
						//close() 메소드 호출을 통해서
						System.out.println("[server] closed by client");
						break;
					}
					
					String data = new String(buffer, 0, readByteCount, "utf-8");
					System.out.println("[server] received:" + data);
					
					//6. 데이터 쓰기
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					os.write(data.getBytes("utf-8"));
				}
			
			} catch(SocketException e) {
				System.out.println("[server] sudden closed by client");
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(socket != null && !socket.isClosed()) {
						socket.close();											
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
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

}
