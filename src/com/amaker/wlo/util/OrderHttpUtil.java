package com.amaker.wlo.util;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class OrderHttpUtil {
	// 基础URL
	public static final String BASE_URL="http://192.168.2.2:8080/WirelessOrder_Server/";
	// 获得Get请求对象request
	public static HttpGet getHttpGet(String url){
		HttpGet request = new HttpGet(url);
		 return request;
	}
	// 获得Post请求对象request
	public static HttpPost getHttpPost(String url){
		 HttpPost request = new HttpPost(url);
		 return request;
	}
	// 根据请求获得响应对象response
	public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	// 根据请求获得响应对象response
	public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	
	/**
	 * 将URL打包成HttpPost请求，发送，得到查询结果 网络异常 返回 "exception"
	 */
	public static String getHttpPostResultForUrl(String url){
		System.out.println("url==="+url);
		HttpPost httpPost = getHttpPost(url);
		String resultString = null;
		
		try {
			HttpResponse response = getHttpResponse(httpPost);
			
			if(response.getStatusLine().getStatusCode() == 200)
				resultString = EntityUtils.toString(response.getEntity());				
		} catch (ClientProtocolException e) {
			resultString = "exception";
			e.printStackTrace();
		} catch (IOException e) {
			resultString = "exception";
			e.printStackTrace();
		}
		
		return resultString;
	}
	
	/**
	 * 发送Post请求，得到查询结果 网络异常 返回 "exception"
	 */
	public static String getHttpPostResultForRequest(HttpPost httpPost){
		String resultString = null;
		
		try {
			HttpResponse response = getHttpResponse(httpPost);
			
			if(response.getStatusLine().getStatusCode() == 200)
				resultString = EntityUtils.toString(response.getEntity());				
			
		} catch (ClientProtocolException e) {
			resultString = "exception";
			e.printStackTrace();
		} catch (IOException e) {
			resultString = "exception";
			e.printStackTrace();
		}
		
		return resultString;
	}
	
	/**
	 * 将URL打包成HttpGet请求，发送，得到查询结果 网络异常 返回 "exception"
	 */
	public static String getHttpGetResultForUrl(String url){
		
		HttpGet httpGet = getHttpGet(url);
		String resultString = null;
		
		try {
			HttpResponse response = getHttpResponse(httpGet);
			if(response.getStatusLine().getStatusCode() == 200)
				resultString = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			resultString = "exception";
			e.printStackTrace();
		} catch (IOException e) {
			resultString = "exception";
			e.printStackTrace();
		}
		
		return resultString;
	}
	
	/**
	 * 发送Get请求，得到查询结果 网络异常 返回 "exception"
	 */
	public static String getHttpGetResultForRequest(HttpGet httpGet){
		String resultString = null;
		try {
			HttpResponse response = getHttpResponse(httpGet);
			if(response.getStatusLine().getStatusCode() == 200)
				resultString = EntityUtils.toString(response.getEntity());
		} catch (ClientProtocolException e) {
			resultString = "exception";
			e.printStackTrace();
		} catch (IOException e) {
			resultString = "exception";
			e.printStackTrace();
		}
		
		return resultString;
	}
}
