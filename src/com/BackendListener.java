package com;

import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.backend.AbstractBackendListenerClient;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BackendListener extends AbstractBackendListenerClient 
{
	private static String ES_HOST = "es.host";
	private static String ES_PROTOCOL = "es.protocol";
	private static String ES_PORT = "es.port";
	private static String ES_INDEX = "ex.index";
	private static String ES_DOC = "es.document";
	private static String JMETER_RESPONSE = "saveResponseData";
	private static String COUNT_OF_ATTEMPTS = "attemptsCount";
	private static long SLEEP_TIME=10000;
	private static String PROJECT = "projectField";
	
	private static Logger LOGGER = LoggerFactory.getLogger(BackendListener.class);
	
	@Override
	public void setupTest(BackendListenerContext arg1)
	{
		LOGGER.info("BackendListener initialization");
		String host = arg1.getParameter(ES_HOST);
		String protocol = arg1.getParameter(ES_PROTOCOL);
		String port = arg1.getParameter(ES_PORT);
		
		try {
			if (WorkWithServer.doHead(protocol+"://"+host+":"+port, LOGGER))
			{
				LOGGER.info("ElasticSearch server is available");
			} else {
				LOGGER.error("BackendListener initialization failed");
			}
		} catch(Exception e)
		{
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			LOGGER.error("BackendListener initialization failed");
		}
		LOGGER.info("BackendListener initialization successful");
		
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

	//@SuppressWarnings("unchecked")
	@Override
	public void handleSampleResults(List<SampleResult> arg0, BackendListenerContext arg1) {
		String body = "";
		for(int i=0;i<arg0.size();i++)
		{
			SampleResult sample = arg0.get(i);
			String json = Util.getJsonFromSample(sample, arg1.getParameter(JMETER_RESPONSE), arg1.getParameter(PROJECT));
			String action = "{\"index\": {\"_type\":\""+arg1.getParameter(ES_DOC)+"\"}}";
			body+=action+"\n"+json+"\n";	
		}
		String url = arg1.getParameter(ES_PROTOCOL)+"://"+arg1.getParameter(ES_HOST)+":"+arg1.getParameter(ES_PORT)+"/"+arg1.getParameter(ES_INDEX)+"/_bulk";
		
		int j=0;
		int count = arg1.getIntParameter(COUNT_OF_ATTEMPTS);
		while(j<count){
			if(WorkWithServer.doPost(url, body, LOGGER))
			{
				break;
			}
			else
			{
				j++;
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					LOGGER.error(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}
}
