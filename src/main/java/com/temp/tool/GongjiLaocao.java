package com.temp.tool;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GongjiLaocao {

	private static OkHttpClient client = new OkHttpClient();
	
	public static void main(String[] args) throws IOException {
		delete();
	}
	
	private static void insert() throws IOException {
		String url = "http://www.1huiwang.com/article/addArticle";
		for(int i = 0; i < 10; i++) {
			RequestBody requestBody = new FormBody.Builder().add("topicId", "1").add("title", "sdasd")
					.add("content",
							"dsaddsdsddsadasdsadasdsafsdfdsgfdgfdgfdgfdhgfhgfhgfjgfjfjfjfjgfhgfhgfhgfhfhfdgdsfgdgdssadasadsadsadsadsadasdsadsadasdasdsad")
					.add("userId", "31").build();
			Request request = new Request.Builder().addHeader("Cookie", "JSESSIONID=1n7l4lhfbnxhq1dr0fib5wo0f7")
					.url(url).post(requestBody).build();

			Response response = client.newCall(request).execute();
			if (!response.isSuccessful()) {
				break;
			}
			
			
			System.out.println("执行第" + (i + 1) + "次");
		}
	}
	
	private static void delete() throws IOException {
		for(int i = 165; i < 200; i++) {
			Request request = new Request.Builder()
					.url("http://www.1huiwang.com/article/delete?id=" + i).addHeader("Cookie", "JSESSIONID=1n7l4lhfbnxhq1dr0fib5wo0f7").get().build();
			Response response = client.newCall(request).execute();
			if (!response.isSuccessful()) {
				break;
			}
			System.out.println("删除第" + i + "篇文章");
		}
	}
}
