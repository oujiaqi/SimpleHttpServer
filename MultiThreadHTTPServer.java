// 基于命令行界面的HTTP服务程序
import java.net.ServerSocket;

public class MultiThreadHTTPServer {
	public static void main(String args[]) {
		int port = 8000; //监听端口
		String webRoot = "/www"; //根目录
		ServerSocket listenSocket; //监听套接字
		try {
			if (args.length == 0) {
				System.out.println("用法：java MultiThreadHTTPServer <webroot> [port]");
				return;
			}
			if (args.length == 1) {
				webRoot = args[0];
			}
			if (args.length == 2) {
				webRoot = args[0];
				try {
					port = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					System.out.println("参数2请输入0~65535的正整数");
					return;
				}
			}
			listenSocket = new ServerSocket(port);
			System.out.println("\nHttp服务器程序正在端口" + listenSocket.getLocalPort()
					+ "处运行");
			while (true) {
				System.out.println("等待连接...");
				StreamSocket client = new StreamSocket(listenSocket.accept());
				System.out.println("客户端" + client.getSocket().getInetAddress()
						+ ":" + client.getSocket().getPort() + "已建立连接！");
				HttpHandlerThread handlerThread = new HttpHandlerThread(
						webRoot, client);
				Thread thread = new Thread(handlerThread);
				thread.start();
			}
		} catch (Exception e) {
			System.out.println("\nHttp服务器程序发生异常！已退出！");
		}
	}
}
