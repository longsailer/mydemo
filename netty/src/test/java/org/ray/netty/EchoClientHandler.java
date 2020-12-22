package org.ray.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * EchoClientHandler.java
 * <br><br>
 * [write note]
 * @author: ray
 * @date: 2020年12月11日
 */
@Sharable
public class EchoClientHandler extends ChannelInboundHandlerAdapter{
	
	private Logger log = LoggerFactory.getLogger(EchoClientHandler.class);
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Client received: " + ctx.channel().id().asShortText() + "\r\n" + in.toString(CharsetUtil.UTF_8));        //2
        //ctx.write(HttpResponseStatus.OK);                            //3
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	ByteBuf msg = Unpooled.copiedBuffer("success", CharsetUtil.UTF_8); 
        ctx.writeAndFlush(msg)//4
        .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause) {
    	log.error("连接【"+ctx.channel().id().asShortText()+"】发生错误，详情：", cause);                //5
        ctx.close();                            //6
    }
}

