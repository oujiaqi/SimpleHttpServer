import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.util.StringTokenizer;

// 处理一个HTTP请求线程
public class HttpHandlerThread implements Runnable {
	private final static String CRLF = System.getProperty("line.separator");
	private StreamSocket client;
	private String root;
	private String clientInetAddress;
	private int clientPort;

	// 创建一条HTTP服务线程
	public HttpHandlerThread(String webRoot, StreamSocket client) {
		this.client = client;
		this.root = webRoot;
		this.clientInetAddress = client.getSocket().getInetAddress().toString();
		clientPort = client.getSocket().getPort();
	}

	// 处理HTTP请求
	public void run() {
		String statusLine = null;
		String entityBody = null;
		// 读取并显示 HTTP客户端提交的请求信息
		try {
			while (true) {
				String headerLine = client.receiveMessage();
				if (headerLine.trim().equals("")) {
					client.close();
					System.out.println("会话结束！");
					break;
				} else {
					if (headerLine.length() != 0)
						System.out.println("客户端的请求是："+headerLine);
					StringTokenizer s = new StringTokenizer(headerLine);
					if ((s.countTokens() >= 2) && s.nextToken().equals("GET")) {
						// 从请求中取出文档的文件名，支持默认索引文件
						String filename = s.nextToken();
						if (filename.startsWith("/"))
							filename = filename.substring(1);
						if (filename.endsWith("/"))
							filename += "index.html";
						if (filename.equals(""))
							filename += "index.html";

						// add root
						filename = root + filename;

						// 打开所请求的文件
						FileInputStream fis = null;
						boolean fileExists = true;
						try {
							fis = new FileInputStream(filename);
							fis.close();
						} catch (FileNotFoundException e) {
							fileExists = false;
						}

						// 读取文件中的内容并写到socket的输出流
						if (fileExists) {
							statusLine = "HTTP/1.0 200 OK" + CRLF;
						} else {
							statusLine = "HTTP/1.0 404 Not Found" + CRLF;
							entityBody = "<HTML>" + "<HEAD>" + CRLF
									+ "<TITLE>404 Not Found</TITLE>" + CRLF
									+ "</HEAD><BODY>" + CRLF + "<h1>Not Found</h1>"
									+ CRLF + "<p>The request URI " + filename
									+ " was not found on this server.</p>" + CRLF
									+ "</BODY></HTML>" + CRLF;
						}
						client.sendMessage(statusLine);
						client.sendMessage(CRLF);
						if (fileExists) {
							client.sendBytes(filename);
							client.sendMessage(CRLF);
							client.sendMessage(CRLF);
						} else {
							client.sendMessage(entityBody);
							client.sendMessage(CRLF);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("客户端" + clientInetAddress + ":" + clientPort + "已断开");
	}
}
