package org.ray.rpc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.ray.rpc.core.JsonUtils;
import org.ray.rpc.core.bean.RpcResponseBean;
import org.ray.rpc.tcp.RpcRequest;
import org.ray.rpc.tcp.RpcResponse;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * RpcClient.java <br>
 * <br>
 * RPC客户端API
 * 
 * @author: ray
 * @date: 2020年12月28日
 */
public class TcpRpcClient implements RpcClient {
	private String clusterHost;
	private int clusterPort;
	private RpcIOChannelFactory factory = RpcIOChannelFactory.getInstance();
	private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public TcpRpcClient(String clusterHost, int clusterPort) {
		this.clusterHost = clusterHost;
		this.clusterPort = clusterPort;
	}

	public <T> RpcResponse<T> call(RpcRequest request, TypeReference<T> returnType)
			throws IllegalStateException, InterruptedException, ExecutionException, IOException {
		ProviderInstance pi = findOneProviderInstance(request.getApplicationName());
		if (pi == null || pi.getHost() == null || "".equals(pi.getHost())) {
			throw new NoRouteToHostException("无可用的服务,请确保服务名正确");
		}
		RpcClientReadHandler<T> readHandler = new RpcClientReadHandler<T>(request, returnType);
		factory.build(pi.getHost(), pi.getPort(), readHandler).connect();
		Future<RpcResponseBean<T>> callResult = executorService.submit(readHandler);
		RpcResponseBean<T> response = callResult.get();
		return response;
	}

	public <T> T callForObject(RpcRequest request, TypeReference<T> returnType)
			throws IllegalStateException, InterruptedException, ExecutionException, IOException {
		RpcResponse<T> response = this.call(request, returnType);
		if(response.getStatus() != 200){
			throw new ExecutionException(new Exception(response.getMsg()));
		}
		return response.getData();
	}

	private ProviderInstance findOneProviderInstance(String appName) throws IOException {
		String result = "";
		String sendUrl = String.format("http://%s:%s/cluster?appName=%s", this.clusterHost, this.clusterPort, appName);
		HttpURLConnection connection = null;
		BufferedReader reader = null;
		try {
			URL postUrl = new URL(sendUrl);
			// 打开连接
			connection = (HttpURLConnection) postUrl.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(30000);
			connection.setReadTimeout(30000);
			connection.connect();
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			return JsonUtils.jsonToClazz(result, ProviderInstance.class);
		} catch (Exception e) {
			throw new IOException("请求服务时发生错误", e);
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
