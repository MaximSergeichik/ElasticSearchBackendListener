package com;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

public class SendData implements Runnable{

	private Logger LOGGER;
	private String uri;
	private String index;
	private String doc;
	private String body;
	
	public SendData(String uri, String index, String doc, String body, Logger LOGGER)
	{
		this.LOGGER = LOGGER;
		this.uri = uri;
		this.index = index;
		this.doc = doc;
		this.body = body;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
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
			
			//String response = "";
			//BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			//while (responseReader.readLine() != null)
			//{
//				response += responseReader.readLine();
			//}
			//responseReader.close();			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			LOGGER.info("request body: "+body);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			LOGGER.info("request body: "+body);
		}
	}
}
