package http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class RequestHandler extends Thread {
	private static String documentRoot = "";
	
	static {
		try {
			documentRoot = new File(RequestHandler.class.
					getProtectionDomain().
					getCodeSource().
					getLocation().
					toURI()).
					getPath();
			documentRoot += "/webapp";  
			System.out.println("---->" + documentRoot);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private Socket socket;
	
	public RequestHandler( Socket socket ) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			// get IOStream

			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = ( InetSocketAddress )socket.getRemoteSocketAddress();
			consoleLog( "connected from " + inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort() );

			// get IOStream
			OutputStream outputStream = socket.getOutputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			
			String request = null;
			
			while(true) {
				String line = br.readLine();
				
				//브라우저가 연결을 끊으면...
				if(line == null) {
					break;
				}
				
				// Request Header만 읽음
				if("".equals(line)) {
					break;
				}
				
				// Header의 첫번째 라인만 처리
				if(request == null) {
					request = line;
					break;
				}
				
			}			
			
			String[] tokens = request.split(" ");
			if("GET".equals(tokens[0])) {
				consoleLog("request:" + tokens[1]);
				responseStaticResource(outputStream, tokens[1], tokens[2]);
			} else { // POST, PUT, DELETE, OPTIONS, CONNECT, HEAD etc..
					 // 와 같은 Method는 무시
				
				/* 응답 예시
				 * HTTP/1.1 400 Bad Request\r\n
				 * Content-Type:text/html; charset=utf-8\r\n
				 * \r\n
				 * HTML 에러 문서(./webapp.error/400.html)
				 */
				response400Error(outputStream, tokens[2]);
			}
			
			// 예제 응답입니다.
			// 서버 시작과 테스트를 마친 후, 주석 처리 합니다.
//			outputStream.write( "HTTP/1.1 200 OK\r\n".getBytes( "UTF-8" ) );
//			outputStream.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
//			outputStream.write( "\r\n".getBytes() );
//			outputStream.write( "<h1>이 페이지가 잘 보이면 실습과제 SimpleHttpServer를 시작할 준비가 된 것입니다.</h1>".getBytes( "UTF-8" ) );

		} catch( Exception ex ) {
			consoleLog( "error:" + ex );
		} finally {
			// clean-up
			try{
				if( socket != null && socket.isClosed() == false ) {
					socket.close();
				}
				
			} catch( IOException ex ) {
				consoleLog( "error:" + ex );
			}
		}			
	}

	public void responseStaticResource(OutputStream outputStream, String url, String protocol) throws IOException {
		
		if("/".equals(url)) {
			url = "/index.html";
		}
		
		File file = new File( documentRoot + url);
		if(file.exists() == false) {
			/* 응답 예시
			 * HTTP/1.1 404 File Not Found
			 * Content-Type:text/html; charset=utf-8\r\n
			 * \r\n
			 * HTML 에러 문서(./webapp.error/404.html)
			 */
			response404Error(outputStream, protocol);
			
			return;
		}
		
		//nio
		byte[] body = Files.readAllBytes(file.toPath());
		String contentType = Files.probeContentType( file.toPath() );
		
		//응답
		outputStream.write( (protocol + " 200 OK\r\n").getBytes( "UTF-8" ) );
		outputStream.write( ("Content-Type:" + contentType + "; charset=utf-8\r\n").getBytes( "UTF-8" ) ); //텍스트는 encoding, 이미지는 length를 알려줘야 한다.
		outputStream.write( "\r\n".getBytes() );
		outputStream.write( body );
	}
	
	/**
	 * ----------------------------------
	 * |	404 Error Page Response		|
	 * ----------------------------------
	 * @param outputStream
	 * @param protocol
	 * @throws IOException
	 */
	public void response404Error(OutputStream outputStream, String protocol) throws IOException {
		
		File file = new File(documentRoot + "/error/404.html");
		byte[] body = Files.readAllBytes(file.toPath());
		
		outputStream.write( (protocol +" 404 File Not Found\r\n").getBytes( "UTF-8" ) );
		outputStream.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
		outputStream.write( "\r\n".getBytes() );
		outputStream.write( body );
	}
	
	/**
	 * ----------------------------------
	 * |	400 Error Page Response		|
	 * ----------------------------------
	 * @param outputStream
	 * @param protocol
	 * @throws IOException
	 */
	public void response400Error(OutputStream outputStream, String protocol) throws IOException {
		
		File file = new File(documentRoot + "/error/400.html");
		byte[] body = Files.readAllBytes(file.toPath());
		
		outputStream.write( (protocol +" 400 Bad Request\r\n").getBytes( "UTF-8" ) );
		outputStream.write( "Content-Type:text/html; charset=utf-8\r\n".getBytes( "UTF-8" ) );
		outputStream.write( "\r\n".getBytes() );
		outputStream.write( body );
	}
	
	public void consoleLog( String message ) {
		System.out.println( "[RequestHandler#" + getId() + "] " + message );
	}
}
