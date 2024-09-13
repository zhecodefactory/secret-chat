package com.itchat.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

/**
 * @author 王哲
 * @Contact 1121586359@qq.com
 * @ClassName HttpHandler.java
 * @create 2024年09月13日 下午2:57
 * @Description 自定义的管道处理器
 * @Version V1.0
 */
@Slf4j
public class HttpHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext context, HttpObject http) throws Exception {
        // 获取channel
        Channel channel = context.channel();

        // 打印客户端的远程地址
        log.info("客户端远程地址为{}", channel.remoteAddress());

        // 通过缓冲区定义发送的消息，读写数据都是通过缓冲区进行数据交换的
        ByteBuf content = Unpooled.copiedBuffer("hello 王青玄~", CharsetUtil.UTF_8);

        // 构建Http的响应
        FullHttpResponse response = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                content);
        // 为Http客户端的响应添加数据类型和数据长度
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

        // 将响应数据写入缓冲区刷新到客户端
        context.writeAndFlush(response);
    }
}
