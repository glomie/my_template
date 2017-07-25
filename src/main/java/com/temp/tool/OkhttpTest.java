package com.temp.tool;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkhttpTest {

	public static final MediaType APPLICATION_JSON = MediaType.parse("application/json; charset=utf-8");
	
	public static void main(String[] args) {
		OkHttpClient client = new OkHttpClient().newBuilder()
				.connectTimeout(60, TimeUnit.SECONDS)
				.readTimeout(60, TimeUnit.SECONDS)
				.build();
		RequestBody requestBody = RequestBody.create(APPLICATION_JSON, "dasdasdas");
        Request request = new Request.Builder().url("http://www.youtube.com").post(requestBody).build();
        long time1 = System.currentTimeMillis();
		try {
			System.out.println("连接中...");
			client.newCall(request).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("超时，执行时间" + (System.currentTimeMillis() - time1) + "ms");
			e.printStackTrace();
		}
		
	}
}
