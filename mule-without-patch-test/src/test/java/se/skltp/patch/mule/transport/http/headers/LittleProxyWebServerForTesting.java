package se.skltp.patch.mule.transport.http.headers;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.ServerCookieEncoder;

import java.io.UnsupportedEncodingException;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

/**
 * Using a webserver that lets us add multiple Set-Cookie-headers with different
 * case in the response.
 * <p>
 * Note: tried jetty and JDK contained com.sun.net.httpserver.HttpServer, but
 * they are to rigid for this test and does not allow setting Set-Cookie headers
 * with different case.
 * </p>
 * <p>Ref: https://github.com/adamfisk/LittleProxy</p>
 * <p>Test using curl:</p>
 * <pre>
 * curl -v http://localhost:8082/test/headers
 * </pre>
 * 
 * @author hakan
 */
public class LittleProxyWebServerForTesting {
	private static final int SERVER_PORT = 8082;

	public static void main(String[] args) {
		LittleProxyWebServerForTesting serverImpl = new LittleProxyWebServerForTesting();
		serverImpl.createAndRunWebServer();
	}

	public void createAndRunWebServer() {
		HttpProxyServer server = DefaultHttpProxyServer.bootstrap()
				.withPort(8082)
				.withFiltersSource(new HttpFiltersSourceAdapter() {

					public HttpFilters filterRequest(
							HttpRequest originalRequest,
							ChannelHandlerContext ctx) {

						return new HttpFiltersAdapter(originalRequest) {
							@Override
							public HttpResponse clientToProxyRequest(
									HttpObject httpObject) {
System.out.println("### received request");
								// TODO: implement your filtering here
								// return null;
								ByteBuf buffer = null;
								try {
									buffer = Unpooled
											.wrappedBuffer("hello from server"
													.getBytes("UTF-8"));
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								HttpResponse response = new DefaultFullHttpResponse(
										HttpVersion.HTTP_1_1,
										HttpResponseStatus.OK, buffer);
								HttpHeaders.setContentLength(response,
										buffer.readableBytes());
								HttpHeaders.setHeader(response,
										HttpHeaders.Names.CONTENT_TYPE,
										"text/html");
// test: add Set-Cookie-headers with different case
								addSetCookieHeadersWithDifferentCaseForTesting(response);
System.out.println("### returning response");
								return response;
							}

							@Override
							public HttpObject serverToProxyResponse(
									HttpObject httpObject) {
								// TODO: implement your filtering here
								return httpObject;
							}
						};
					}
				}).start();
	}
	
	private void addSetCookieHeadersWithDifferentCaseForTesting(HttpResponse response) {
		//HttpHeaders.addHeader(response, HttpHeaders.Names.SET_COOKIE, ServerCookieEncoder.encode(
		//				"header-name", HttpHeaders.Names.SET_COOKIE));
		HttpHeaders.addHeader(response, "SET-COOKIE", ServerCookieEncoder.encode(
						"header-name-is-uppercase", "SET-COOKIE-UPPERCASE-1"));
		HttpHeaders.addHeader(response, "Set-Cookie", ServerCookieEncoder.encode(
						"header-name-is-camelcase", "Set-Cookie-CamelCase-1"));		
	}
}
