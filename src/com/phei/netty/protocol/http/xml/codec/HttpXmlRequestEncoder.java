package com.phei.netty.protocol.http.xml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.net.InetAddress;
import java.util.List;

/**
 * @author Lilinfeng
 * @date 2014年3月1日
 * @version 1.0
 */
public class HttpXmlRequestEncoder extends
	AbstractHttpXmlEncoder<HttpXmlRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlRequest msg,
	    List<Object> out) throws Exception {
    	//将业务需要的pojo对象通过jibx序列化成XML字符串
		ByteBuf body = encode0(ctx, msg.getBody());
		FullHttpRequest request = msg.getRequest();
		//如果业务侧设置了http消息头则使用业务侧的。
		if (request == null) {
		    request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
			    HttpMethod.GET, "/do", body);
		    HttpHeaders headers = request.headers();
		    headers.set(HttpHeaders.Names.HOST, InetAddress.getLocalHost()
			    .getHostAddress());
		    headers.set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
		    headers.set(HttpHeaders.Names.ACCEPT_ENCODING,
			    HttpHeaders.Values.GZIP.toString() + ','
				    + HttpHeaders.Values.DEFLATE.toString());
		    headers.set(HttpHeaders.Names.ACCEPT_CHARSET,
			    "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		    headers.set(HttpHeaders.Names.ACCEPT_LANGUAGE, "zh");
		    headers.set(HttpHeaders.Names.USER_AGENT,
			    "Netty xml Http Client side");
		    headers.set(HttpHeaders.Names.ACCEPT,
			    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		}
		//Http消息头中设置消息体的长度
		HttpHeaders.setContentLength(request, body.readableBytes());
		out.add(request);
    }

}
