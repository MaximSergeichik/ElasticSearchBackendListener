package com;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

public class WorkWithServer{

	
	public static boolean doPost(String uri, String body, Logger LOGGER)
	{
		URL url;
		try {
			url = new URL(uri);
			HttpURLConnection connection;
			connection = (HttpURLConnection)url.openConnection();
			
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			
			BufferedWriter requestWriter = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			requestWriter.write(body);
			requestWriter.close();
			
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK || connection.getResponseCode() == HttpURLConnection.HTTP_CREATED)
			{
				LOGGER.info("Request successfully completed");
				return true;
			}
			else
			{
				LOGGER.error("Request returned "+connection.getResponseCode()+" response code!");
				LOGGER.info("Requested URL: " + uri);
				LOGGER.info("Request body: "+body);
				return false;
			}
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
			LOGGER.info("Requested url:" + uri);
			LOGGER.info("Request body: " + body);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			LOGGER.info("Requested url:" + uri);
			LOGGER.info("Request body: " + body);
		}
		return false;
	}
	
	public static boolean doGet(String uri, Logger LOGGER)
	{
		try
		{
			URL url = new URL(uri);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				return true;
			}
			else
			{
				return false;
			}
		} catch (Exception e)
		{
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			return false;
		}
	}
	
	public static boolean doHead(String uri, Logger LOGGER)
	{
		try
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
		} catch (Exception e)
		{
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}
}
