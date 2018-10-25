package com;

import java.util.List;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.visualizers.backend.AbstractBackendListenerClient;
import org.apache.jmeter.visualizers.backend.BackendListenerContext;
import org.slf4j.*;



public class BackendListener extends AbstractBackendListenerClient 
{
	private static String ES_HOST = "es.host";
	private static String ES_PROTOCOL = "es.protocol";
	private static String ES_PORT = "es.port";
	private static String ES_INDEX = "ex.index";
	private static String ES_DOC = "es.document";
	private static String JMETER_RESPONSE = "saveResponseData";
	
	private static Logger LOGGER = LoggerFactory.getLogger(BackendListener.class);
	
	@Override
	public void setupTest(BackendListenerContext arg1)
	{
		
	}
	
	@Override
	public Arguments getDefaultParameters()
	{
		Arguments par = new Arguments();
		par.addArgument(ES_PROTOCOL, "http");
		par.addArgument(ES_HOST, "localhost");
		par.addArgument(ES_PORT, "9200");
		par.addArgument(ES_INDEX, "_jmeter");
		par.addArgument(ES_DOC, "_test");
		par.addArgument(JMETER_RESPONSE, "false");
		
		
		return par;
	}

	@Override
	public void handleSampleResults(List<SampleResult> arg0, BackendListenerContext arg1) {
		// TODO Auto-generated method stub
		for(int i=0;i<arg0.size();i++)
		{
			SampleResult sample = arg0.get(i);
			String json = Util.getJsonFromSample(sample, arg1.getParameter(JMETER_RESPONSE));
			String url = arg1.getParameter(ES_PROTOCOL)+"://"+arg1.getParameter(ES_HOST)+":"+arg1.getParameter(ES_PORT);
			try
			{
				Util.sendJSON(url, arg1.getParameter(ES_INDEX), arg1.getParameter(ES_DOC), json, LOGGER);
			}
			catch(Exception ex)
			{
				LOGGER.error(ex.getMessage());
			}
		}
	}

}