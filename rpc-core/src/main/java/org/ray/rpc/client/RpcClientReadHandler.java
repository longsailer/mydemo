package org.ray.rpc.client;

import java.util.concurrent.Callable;

import org.ray.rpc.core.JsonUtils;
import org.ray.rpc.core.RPCResponseFactory;
import org.ray.rpc.core.bean.RpcResponseBean;
import org.ray.rpc.tcp.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * EchoClientHandler.java
 * <br><br>
 * @author: ray
 * @date: 2020年12月11日
 */
@Sharable
public class RpcClientReadHandler<T> extends ChannelInboundHandlerAdapter implements Callable<RpcResponseBean<T>>{
	
	private Logger log = LoggerFactory.getLogger(RpcClientReadHandler.class);
	
	private ChannelHandlerContext ctx;
	
	private RpcRequest request;
	
	private RpcResponseBean<String> response;
	
	private TypeReference<T> returnType;
	
	private long maxWaitTime = 30000;
	
	public RpcClientReadHandler(RpcRequest request, TypeReference<T> returnType){
		this.request = request;
		this.returnType = returnType;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		this.ctx = ctx;
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        String result = in.toString(CharsetUtil.UTF_8);
        System.out.println("Client received: " + ctx.channel().id().asShortText() + "\r\n" + result);        //2
        try {
			this.response = JsonUtils.jsonToClazz(result, new TypeReference<RpcResponseBean<String>>(){});
			ctx.close();
		} catch (JsonMappingException e) {
			log.error("结果格式解析错误", e);
		} catch (JsonProcessingException e) {
			log.error("结果格式解析错误", e);
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause) {
    	log.error("连接【"+ctx.channel().id().asShortText()+"】发生错误，详情：", cause);                //5
        ctx.close();                            //6
    }

	@SuppressWarnings("unchecked")
	@Override
	public RpcResponseBean<T> call() throws Exception {
		try{
			String requestJson = JsonUtils.clazzToJson(this.request);
			ByteBuf requestBuf = Unpooled.copiedBuffer(requestJson, CharsetUtil.UTF_8);
			ctx.writeAndFlush(requestBuf).sync();
			long time = 1000;
			while(time <= maxWaitTime){
				if(response != null){
					break;
				}
				Thread.sleep(100);
				time += 100;
			}
			if(String.class.equals(returnType.getType())){
				return (RpcResponseBean<T>) response;
			}
			return RPCResponseFactory.processResponse(response, returnType);
		}finally{
			ctx.close();
		}
	}

	public long getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(long maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	public TypeReference<T> getReturnType() {
		return returnType;
	}

	public void setReturnType(TypeReference<T> returnType) {
		this.returnType = returnType;
	}
}

