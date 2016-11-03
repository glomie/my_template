package com.example.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * I just want to test it!
 * @fileName Test.java
 * @author junyan
 * @date 2016年7月28日 下午2:22:40
 */
public class Test {

	public static void main(String[] args) {
		String json = "{\"verify_result\":{\"time_used\":1086,\"id_exceptions\":{\"id_photo_monochrome\":0,\"id_attacked\":0},\"request_id\":\"1467967809,237a5dc3-710d-46dc-a51e-93208e28a794\",\"face_genuineness\":{\"synthetic_face_threshold\":0.5,\"synthetic_face_confidence\":0.0,\"mask_confidence\":0.0,\"mask_threshold\":0.5},\"result_faceid\":{\"confidence\":87.489,\"thresholds\":{\"1e-3\":65.3,\"1e-5\":76.5,\"1e-4\":71.8,\"1e-6\":79.9}}},\"biz_no\":\"33333\",\"biz_extra_data\":\"\",\"liveness_result\":{\"result\":\"success\",\"version\":\"MegLive 2.4.0L\",\"log\":\"1467967799\n0:A\n864:Y\n4356:P\n6653:M\n8961:O\n\",\"failure_reason\":null},\"biz_id\":\"1467967672,a9e208ad-16c7-4f69-b47d-420f8d3209bb\"}";
		JSONObject obj = JSON.parseObject(json);
		String livenessResultJson = obj.getString("liveness_result");
		JSONObject livenessResult = JSON.parseObject(livenessResultJson);
		String result = livenessResult.getString("result");
		System.out.println(result);
		System.out.println(obj.get("biz_id"));
		BigDecimal bigDecimal = new BigDecimal("35434.55");
		System.out.println((int)Math.rint((bigDecimal.doubleValue() / 1000)) * 1000);
		List<Model1> list = new ArrayList<>();
		Model1 m1 = new Model1();
		m1.setId("xxxx");
		m1.setName("qqqq");
		list.add(m1);
		System.out.println(list.get(0).getId());
		System.out.println(0<<16);
		Random random = new Random();
		random.setSeed((new Date()).getTime());
		int res = random.nextInt(30) + 68;
		System.out.println(res);
	}
}

class Model1 {
	private String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	private String name;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
