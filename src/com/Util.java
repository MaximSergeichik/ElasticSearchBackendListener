package com;

import org.json.*;
import org.apache.jmeter.samplers.SampleResult;

import java.io.IOException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.slf4j.Logger;


public class Util {

	public static boolean sendJSON(String uri, String index, String doc, String body, Logger LOGGER)
	{
		URL url;
		try {
			url = new URL(uri+"/"+index+"/"+doc);
			HttpURLConnection connection;
			connection = (HttpURLConnection)url.openConnection();
			
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			
			BufferedWriter requestWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			requestWriter.write(body);
			requestWriter.close();
			
			String response = "";
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while (responseReader.readLine() != null)
			{
				response += responseReader.readLine();
			}
			responseReader.close();
			
			if (response.contains("\"result\" : \"created\""))
			{
				return true;
			}
			else
			{
				return false;
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.info(body);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.info(body);
		}
		return false;
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
