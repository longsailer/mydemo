package org.ray.rpc.client;

import java.io.Serializable;

/**
 * ProviderInstance.java
 * <br><br>
 * 服务实例
 * @author: ray
 * @date: 2020年12月29日
 */
public class ProviderInstance implements Serializable {

	/**
	 * serialVersionUID
	 * long
	 */
	private static final long serialVersionUID = 1L;

	private String serviceId;
	
	private String host;
	
	private int port;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}

