package com.temp.tool;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.temp.util.StringTemplateUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DomainNameCrawler {
	
	public static void main(String[] args) {
		for(int i = 0; i < 4; i++) {
			
		}
	}
	
	private static void crawl(String prefix) {
		for(String suffix : suffixes) {
			new Thread(() -> {
				Map<String, String> map = Maps.newHashMap();
				String domain = prefix + "." + suffix;
				map.put("domain", domain);
				String url = StringTemplateUtils.render(checkApiUrl, map);
				Request request = new Request.Builder().get().url(url).build();
				try {
					Response response = client.newCall(request).execute();
					String json = response.body().string();
					JSONObject obj = JSON.parseObject(json);
					Boolean result = "true".equals(obj.getString("success"));
					if (result) {
						JSONArray module = obj.getJSONArray("module");
						boolean isAvail = module.getJSONObject(0).getInteger("avail") != 0;
						if (isAvail) System.out.println(domain + "可用");
					} else {
						System.out.println("获取失败");
					}
				} catch (IOException e) {
					System.out.println("获取失败");
				}
				map.clear();
			}).start();
		}
	}
	
	private static final String checkApiUrl = "https://checkapi.aliyun.com/check/checkdomain?domain={domain}&token=check-web-hichina-com%3Agyr2vbtjx5fettr852b6b3joxfi7xl18&_=1498788314106";
	
	private static OkHttpClient client = new OkHttpClient().newBuilder()
			.connectTimeout(60, TimeUnit.SECONDS)
			.readTimeout(60, TimeUnit.SECONDS)
			.build();
	
	private static List<String> suffixes = Lists.newArrayList();
	static {
		suffixes.add("gift");
		suffixes.add("date");
		suffixes.add("com");
		suffixes.add("studio");
		suffixes.add("tv");
		suffixes.add("hk");
		suffixes.add("software");
		suffixes.add("wiki");
		suffixes.add("mom");
		suffixes.add("space");
		suffixes.add("red");
		suffixes.add("biz");
		suffixes.add("me");
		suffixes.add("games");
		suffixes.add("club");
		suffixes.add("xyz");
		suffixes.add("tel");
		suffixes.add("ren");
		suffixes.add("band");
		suffixes.add("press");
		suffixes.add("net");
		suffixes.add("xin");
		suffixes.add("vip");
		suffixes.add("pics");
		suffixes.add("group");
		suffixes.add("info");
		suffixes.add("tech");
		suffixes.add("gov.cn");
		suffixes.add("work");
		suffixes.add("pro");
		suffixes.add("click");
		suffixes.add("market");
		suffixes.add("wang");
		suffixes.add("science");
		suffixes.add("name");
		suffixes.add("party");
		suffixes.add("loan");
		suffixes.add("game");
		suffixes.add("shop");
		suffixes.add("com.net");
		suffixes.add("link");
		suffixes.add("lol");
		suffixes.add("video");
		suffixes.add("kim");
		suffixes.add("top");
		suffixes.add("rocks");
		suffixes.add("design");
		suffixes.add("mobi");
		suffixes.add("social");
		suffixes.add("win");
		suffixes.add("ltd");
		suffixes.add("lawyer");
		suffixes.add("live");
		suffixes.add("cc");
		suffixes.add("news");
		suffixes.add("website");
		suffixes.add("social");
		suffixes.add("org");
		suffixes.add("asia");
		suffixes.add("photo");
		suffixes.add("store");
		suffixes.add("cn");
		suffixes.add("co");
		suffixes.add("engineer");
		suffixes.add("help");
		suffixes.add("site");
		suffixes.add("trade");
		suffixes.add("online");
		suffixes.add("bid");
		suffixes.add("pub");
	}
}
