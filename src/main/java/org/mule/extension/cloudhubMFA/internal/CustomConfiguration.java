package org.mule.extension.cloudhubMFA.internal;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.mule.runtime.extension.api.annotation.Operations;


@Operations(CustomOperations.class)
public class CustomConfiguration {
	@Parameter
    @Summary("Enter clientID received from connected APP")
	private String clientID;
	@Parameter
	@Summary("Enter clientSecret received from connected APP")
	private String clientSecret;
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	@Parameter
	@Summary("Enter anypoint platform environment Id")
	private String envId;
	public String getEnvId() {
		return envId;
	}
	public void setEnvId(String envId) {
		this.envId = envId;
	}

}
