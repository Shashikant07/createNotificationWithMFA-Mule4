package org.mule.extension.cloudhubMFA.internal;
import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.Alias;
import org.slf4j.*;
import org.slf4j.LoggerFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.*;
public class CustomOperations {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomOperations.class);
	
	@MediaType(value = ANY,strict = false)
	@Alias("CreateNotificationWithMFA")
	public void getCloudhubToken(@Config CustomConfiguration c,@ParameterGroup(name="cloudhub Notification Details") CustomParameter p) throws ClientProtocolException, IOException, CustomExceptions {
		LOGGER.debug("Started token retrieving process.........");
		String response = null;
		String Protocol = "https://";
		String host = "anypoint.mulesoft.com";
		String Path = "/accounts/api/v2/oauth2/token";
		String payload  = "client_id=" + c.getClientID() +"&" + "client_secret=" + c.getClientSecret() +"&"+"grant_type=client_credentials";
		String envId = c.getEnvId();
		
		/*Method call for retrieving token*/
		response = getToken(Protocol, host, Path, payload);
		
		/*Converting the retrieved token into Json*/
		JSONObject jsonObj = new JSONObject(response);
		LOGGER.debug("Token retrieved successfully.........");
		
		/*preparing the token for headers.*/
		String token = "Bearer " + jsonObj.getString("access_token").toString();
		
		/*building the Json-string*/
		String JsonString = "{" + "\"domain\"" + ":" + "\""+p.getDomain() +"\","
				 + "\"message\"" + ":"  + "\"" + p.getMessage().replace("\n", "\\n") + "\"" + ","
						+ "\"priority\" :" + "\"" +p.getPriority() + "\"," + "\"transactionId\" :" + "\"" + p.getTransactionId() +"\""+ " } ";
		
		/*Method call for creating alerts on cloudHub*/
			AlertCreationOnCloudHub(token,envId,JsonString);	
		
				
	}
	
	/*Method for generating Alerts on cloudHub*/
	private void AlertCreationOnCloudHub (String token,String envId,String JsonString) throws ClientProtocolException, IOException, CustomExceptions{
		LOGGER.debug("Started alert creation process to cloudHub");
		String Protocol = "https://";
	    String host = "anypoint.mulesoft.com";
	    String path = "/cloudhub/api/notifications";
	    HttpClient httpClient = HttpClientBuilder.create().build();
	    HttpResponse response = null;
			HttpPost request = new HttpPost(Protocol + host + path);
		    StringEntity payload = new StringEntity(JsonString);
		    payload.setContentType("application/json");
		    request.addHeader("content-type", "application/json");
		    request.addHeader("Authorization", token);
		    request.addHeader("X-ANYPNT-ENV-ID", envId);
		    request.setEntity(payload);
		    response = httpClient.execute(request);
		    int responseCode =  response.getStatusLine().getStatusCode();
			
			 if(responseCode == 403){
				 ExceptionHandling e = new ExceptionHandling();
			     e.environmentId();
			    }
			
		
		LOGGER.debug("Successfully generated the alert on cloudHub");
	    }
	/*Method for getting access_token*/
	private String getToken(String Protocol,String host,String Path,String payload) throws IOException, CustomExceptions {
		String response = null;
		
		URL url = new URL(Protocol + host + Path);
		URLConnection con = url.openConnection();
		con.setDoOutput(true);
		con.addRequestProperty("User-Agent", "Mozilla");
		HttpsURLConnection https = (HttpsURLConnection) con;
		https.setRequestMethod("POST");
		https.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		byte[] postData = payload.getBytes("utf-8");
		int postDataLength = postData.length;
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			   wr.write(postData,0,postDataLength);
			
		int status = ((HttpURLConnection) con).getResponseCode();
		if(status == 401) {
			ExceptionHandling e = new ExceptionHandling();
		    e.connectAppCredentials();
		}
		response = getHttpResponse(https);
		
		
		return response;
	}
	
	/*Method for building response*/
	private String getHttpResponse(URLConnection con) throws UnsupportedEncodingException, IOException{
		  StringBuilder response = null;
		  try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"))){
				response = new StringBuilder();
				String responseLine = null;
		 		while((responseLine = br.readLine())!=null){
					response.append(responseLine.trim());
				}
			}
		  return response.toString();
		}	

}

/*Exception handling*/ 
@SuppressWarnings("serial")
class CustomExceptions  extends Exception
{

	public CustomExceptions (String str)
  {
      /*calling the constructor of parent Exception*/
      super(str);
  }
}
class ExceptionHandling{
	 void environmentId() throws CustomExceptions{
	  throw new CustomExceptions("Invalid Environment Id");
	 }
	 
	 void connectAppCredentials() throws CustomExceptions{
		  throw new CustomExceptions("Invalid ClientId/ClientSecret");
		 }
	}