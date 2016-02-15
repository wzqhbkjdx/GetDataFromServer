package com.cgtrc.bym.testapplication.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/** 接收path服务器端返回的 InputStream, 转换为 json字符串
 * @author ben 
 * 
 */  
public class HttpUtils {
	
	public static String getJsonContent(String url_path, String encode) {
		
		String jsonString = "";
		
		try {
			URL url = new URL(url_path);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setConnectTimeout(3000);
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			
			int responseCode = connection.getResponseCode();
			
			if(200 == responseCode) {
				jsonString = changeInputStream(connection.getInputStream(),encode);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonString;
	}

	private static String changeInputStream(InputStream inputStream, String encode) {
		
		String jsonString = null;
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] data = new byte[1024];
			int len = 0;
			while((len=inputStream.read(data))!=-1){
				outputStream.write(data, 0, len); 
			}
			
			jsonString = new String(outputStream.toByteArray(), encode);
			
			inputStream.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonString;
	}

}










