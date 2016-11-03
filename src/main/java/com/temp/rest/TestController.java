package com.temp.rest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/test")
public class TestController {

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> get() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("test", "OK!");
		return map;
	}

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public String post(Model model, String data, String sign) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", "Hello World" + data);
		File file = new File("/root/Downloads/logs/mine.log");

		String fileData = data + "------------------------------" + sign;
		FileWriter fileWritter = new FileWriter(file, true);
		BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		bufferWritter.write(fileData);
		bufferWritter.newLine();
		bufferWritter.newLine();
		bufferWritter.close();
		// data =
		// "{\"verify_result\":null,\"biz_no\":\"33333\",\"biz_extra_data\":\"\",\"liveness_result\":{\"version\":\"MegLive
		// 2.4.0L\",\"failure_reason\":\"not_video\",\"log\":\"1467967275\n0:A\n2028:Y\n6048:c\n\",\"result\":\"failed\"},\"biz_id\":\"1467967229,94f383b5-af3a-4499-82dc-47e25cf5374a\"}";
		JSONObject obj = JSON.parseObject(data);
		String livenessResultJson = obj.getString("liveness_result");
		JSONObject livenessResult = JSON.parseObject(livenessResultJson);
		String result = livenessResult.getString("result");
		System.out.println("result=" + result);
		if (ResultType.success.name().equals(result)) {
			model.addAttribute("code", "1");
			model.addAttribute("result", "采集成功");
			model.addAttribute("bizId", obj.get("biz_id"));
		} else if (ResultType.failed.name().equals(result)) {
			model.addAttribute("code", "0");
			model.addAttribute("result", "采集失败");
			model.addAttribute("failReason", livenessResult.get("failure_reason"));
		}
		System.out.println("Done");
		return "index";
	}

	enum ResultType {
		success, failed
	}

}
