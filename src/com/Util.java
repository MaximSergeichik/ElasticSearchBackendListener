package com;

import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.jmeter.samplers.SampleResult;
import org.json.JSONObject;
import org.slf4j.Logger;


public class Util {

	public static void sendJSON(String uri, String index, String doc, String body, Logger LOGGER)
	{
		Runnable r = new SendData(uri, index, doc, body, LOGGER);
		new Thread(r).start();
		
	}
	
	public static boolean ping(String uri) throws Exception 
	{
		URL url = new URL(uri);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod("HEAD");
		int response_code = connection.getResponseCode();
		if (response_code == HttpURLConnection.HTTP_OK)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static String getJsonFromSample(SampleResult sample, String saveResponse)
	{
		
		JSONObject json = new JSONObject();
		json.put("TransactionName", sample.getSampleLabel());
		long responseTime = sample.getEndTime()-sample.getStartTime();
		json.put("ResponseTime", responseTime);
		json.put("ConnectTime", sample.getConnectTime());
		json.put("IdleTime", sample.getIdleTime());
		json.put("RequestHeaders", sample.getRequestHeaders());
		json.put("ResponseHeaders", sample.getResponseHeaders());
		json.put("ResponseCode", sample.getResponseCode());
		json.put("ResponseMessage", sample.getResponseMessage());
		json.put("ThreadName", sample.getThreadName());
		json.put("Latency", sample.getLatency());
		json.put("RequestURL", sample.getUrlAsString());
		json.put("@timestamp", sample.getTimeStamp());
		json.put("SentBytes", sample.getSentBytes());
		json.put("ReceivedBytes", sample.getBodySizeAsLong());
		json.put("DataType", sample.getDataType());
		if (saveResponse.toLowerCase().equals("true"))
		{
			json.put("ResponseData", sample.getResponseDataAsString());
		}
		
		String result = json.toString();
		return result;
	}
	
}
