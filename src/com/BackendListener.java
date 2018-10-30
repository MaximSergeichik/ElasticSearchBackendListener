package com;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.backend.AbstractBackendListenerClient;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;
import org.slf4j.LoggerFactory;


public class BackendListener extends AbstractBackendListenerClient 
{
	private static final String ES_HOST = "es.host";
	private static final String ES_PROTOCOL = "es.protocol";
	private static final String ES_PORT = "es.port";
	private static final String ES_INDEX = "ex.index";
	private static final String ES_DOC = "es.document";
	private static final String JMETER_RESPONSE = "saveResponseData";
	private static final String COUNT_OF_ATTEMPTS = "attemptsCount";
	private static final long SLEEP_TIME=10000;
	private static final String PROJECT = "projectField";
	

	private static String protocol; 
	private static String host;
	private static String port;
	private static String index;
	private static String document;
	private static int countOfAttempts;
	private static Boolean saveResponseData;
	private static String project;
	
	@Override
	public void setupTest(BackendListenerContext arg1)
	{
		Util.setLogger(LoggerFactory.getLogger(BackendListener.class));
		
		Util.writeLog("BackendListener initialization", Util.LogLevel.info);
		
		host = arg1.getParameter(ES_HOST);
		protocol = arg1.getParameter(ES_PROTOCOL);
		port = arg1.getParameter(ES_PORT);
		index = arg1.getParameter(ES_INDEX);
		document = arg1.getParameter(ES_DOC);
		countOfAttempts = arg1.getIntParameter(COUNT_OF_ATTEMPTS);
		saveResponseData = Boolean.valueOf(arg1.getParameter(JMETER_RESPONSE));
		project = arg1.getParameter(PROJECT);
		
		try {
			if (WorkWithServer.doHead(protocol+"://"+host+":"+port))
			{
				Util.writeLog("ElasticSearch server is available", Util.LogLevel.info);
			} else {
				Util.writeLog("BackendListener initialization failed", Util.LogLevel.error);
			}
		} catch(Exception e)
		{
			Util.writeLog(ExceptionUtils.getStackTrace(e), Util.LogLevel.error);
			Util.writeLog("BackendListener initialization failed", Util.LogLevel.info);
		}
		Util.writeLog("BackendListener initialization successful", Util.LogLevel.info);
		
	}
	
	@Override
	public Arguments getDefaultParameters()
	{
		Arguments par = new Arguments();
		par.addArgument(ES_PROTOCOL, "http");
		par.addArgument(ES_HOST, "localhost");
		par.addArgument(ES_PORT, "9200");
		par.addArgument(ES_INDEX, "jmeter");
		par.addArgument(ES_DOC, "test");
		par.addArgument(JMETER_RESPONSE, "false");
		par.addArgument(COUNT_OF_ATTEMPTS, "1");
		par.addArgument(PROJECT, "");
		
		return par;
	}

	@Override
	public void handleSampleResults(List<SampleResult> arg0, BackendListenerContext arg1) {
		String body = "";
		for(int i=0;i<arg0.size();i++)
		{
			SampleResult sample = arg0.get(i);
			String json = Util.getJsonFromSample(sample, saveResponseData, project);
			String action = "{\"index\": {\"_type\":\""+document+"\"}}";
			body+=action+"\n"+json+"\n";
		}
		String url = protocol+"://"+host+":"+port+"/"+index+"/_bulk";
		
		int j=0;
		
		while(j<countOfAttempts){
			if(WorkWithServer.doPost(url, body))
			{
				break;
			}
			else
			{
				j++;
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					Util.writeLog(ExceptionUtils.getStackTrace(e), Util.LogLevel.error);
				}
			}
		}
	}
}
