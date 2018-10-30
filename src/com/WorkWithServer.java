package com;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.exception.ExceptionUtils;


public class WorkWithServer{

	
	public static boolean doPost(String uri, String body)
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
				Util.writeLog("Request successfully completed", Util.LogLevel.info);
				return true;
			}
			else
			{
				Util.writeLog("Request returned "+connection.getResponseCode()+" response code!", Util.LogLevel.error);
				Util.writeLog("Requested url: "+uri, Util.LogLevel.info);
				Util.writeLog("Request body: "+body, Util.LogLevel.info);
				return false;
			}		
			
		} catch (MalformedURLException e) {
			Util.writeLog(ExceptionUtils.getStackTrace(e), Util.LogLevel.error);
			Util.writeLog("Requested url: "+uri, Util.LogLevel.info);
			Util.writeLog("Request body: "+body, Util.LogLevel.info);
		}
		catch (IOException e) {
			Util.writeLog(ExceptionUtils.getStackTrace(e), Util.LogLevel.error);
			Util.writeLog("Requested url: "+uri, Util.LogLevel.info);
			Util.writeLog("Request body: "+body, Util.LogLevel.info);
		}
		return false;
	}
	
	public static boolean doGet(String uri)
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
			Util.writeLog(ExceptionUtils.getStackTrace(e), Util.LogLevel.error);
			return false;
		}
	}
	
	public static boolean doHead(String uri)
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
			Util.writeLog(ExceptionUtils.getStackTrace(e), Util.LogLevel.error);
		}
		return false;
	}
}
