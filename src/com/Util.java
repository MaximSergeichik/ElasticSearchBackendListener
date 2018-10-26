package com;

import org.apache.jmeter.samplers.SampleResult;
import org.json.JSONObject;



public class Util {
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
