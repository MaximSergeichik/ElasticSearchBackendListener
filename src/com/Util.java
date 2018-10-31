package com;

import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;

import net.minidev.json.JSONObject;

public class Util {

	private Util() {
	}

	enum LogLevel {
		info, error
	}

	public static void writeLog(String message, LogLevel logLevel, Logger LOGGER) {
		switch (logLevel) {
		case info: {
			LOGGER.info(message);
			break;
		}
		case error: {
			LOGGER.error(message);
			break;
		}
		}
	}

	private static boolean isTransaction(SampleResult sample) {
		return !(sample instanceof org.apache.jmeter.protocol.http.sampler.HTTPSampleResult);
	}

	public static String getJsonFromSample(SampleResult sample, boolean saveResponse, String project) {
		JSONObject json = new JSONObject();
		json.put("TransactionName", sample.getSampleLabel());
		json.put("ResponseTime", sample.getTime());
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
		json.put("Project", project);
		json.put("IsTransaction", isTransaction(sample));

		if (saveResponse) {
			json.put("ResponseData", sample.getResponseDataAsString());
		}

		String result = json.toString();
		return result;
	}

}
