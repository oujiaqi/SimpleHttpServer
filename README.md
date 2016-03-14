# SimpleHttpServer
A simple http server in java. Only implement GET request.

### 程序运行环境
Linux或者基于Unix操作系统 + Java 1.8+
### 描述
本程序由三个类组成，分别是StreamSocket，MultiThreadHTTPServer
和HttpHandlerThread类。其中StreamSocket是服务器嵌套字处理类，接受
和发送的主要接口。MutiThreadHTTPServer类是该服务器的接口，也即是main
函数，在此函数新建监听socket，负责接受请求，每当有一个请求到来，会自动
新建一个线程进行处理。HttpHanderThread类继承了Runnable类，Runnable
接口，该类是实现线程的需要的。重载了里面的run函数，对于发来的请求进行分
析判断，并进行相应的数据处理。
### 编译程序
```bash
javac *.java
```
### 运行程序
```bash
java MutiThreadHTTPServer <webroot> [port]

```
其中<webroot>为必备参数，此参数为web服务器的根路径。而port为
服务器的指定端口，默认端口为8000