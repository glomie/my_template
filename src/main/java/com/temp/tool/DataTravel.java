package com.temp.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DataTravel {

	private static List<String> inputs = new ArrayList<>();
	private static Stack<String> travelStack = new Stack<>();
	
	public static void main(String[] args) {
		JSONArray array = JSON.parseArray(data);
		for(int i = 0; i < array.size(); i++) travel(array.getJSONObject(i));
	}
	
	private static void travel(JSONObject obj) {
		String permission = obj.getString("permission");
		String title = obj.getString("title");
		JSONArray subMenus = obj.getJSONArray("subMenus");
		if(inputs.contains(permission)) {
			System.out.println(title + "的父辈们是" + travelStack.toString());
		}
		travelStack.push(title);
		for(int i = 0; i < subMenus.size(); i++) {
			travel(subMenus.getJSONObject(i));
		}
		travelStack.pop();
	}
	
	
	
	static {
		inputs.add("userinformation");
	}
	private static final String data = "[{\"action\":\"#\",\"title\":\"通用设置\",\"icon\":\"fa fa-cog\",\"permission\":\"Genger\",\"isLeaf\":false,\"isTab\":false,\"subMenus\":[{\"action\":\"usermanager\",\"title\":\"用户管理\",\"icon\":\"fa fa-users\",\"permission\":\"usermanager\",\"isLeaf\":false,\"isTab\":false,\"subMenus\":[{\"id\":\"tab1\",\"action\":\"userinformation\",\"title\":\"用户信息\",\"permission\":\"userinformation\",\"isLeaf\":true,\"isTab\":true,\"subMenus\":[]},{\"id\":\"tab2\",\"action\":\"authoritymanager\",\"title\":\"功能权限\",\"permission\":\"authoritymanager\",\"isLeaf\":true,\"isTab\":true,\"subMenus\":[{\"action\":\"usercenter\",\"title\":\"个人中心2\",\"icon\":\"fa fa-user\",\"permission\":\"usercenter2\",\"isLeaf\":true,\"isTab\":false,\"subMenus\":[]}]}]},{\"action\":\"usercenter\",\"title\":\"个人中心\",\"icon\":\"fa fa-user\",\"permission\":\"usercenter\",\"isLeaf\":true,\"isTab\":false,\"subMenus\":[]}]},{\"action\":\"systemsetting\",\"title\":\"系统设置\",\"icon\":\"fa fa-cog\",\"permission\":\"systemsetting\",\"isLeaf\":false,\"isTab\":false,\"subMenus\":[{\"action\":\"userlogininfo\",\"title\":\"用户登录信息\",\"icon\":\"fa fa-navicon\",\"permission\":\"userlogininfo\",\"isLeaf\":true,\"isTab\":false,\"subMenus\":[]},{\"action\":\"weatherinfo\",\"title\":\"气象信息\",\"icon\":\"fa fa-cloud\",\"permission\":\"weatherinfo\",\"isLeaf\":true,\"isTab\":false,\"subMenus\":[]},{\"action\":\"citysetting\",\"title\":\"城市设置\",\"icon\":\"fa fa-cloud\",\"permission\":\"citysetting\",\"isLeaf\":true,\"isTab\":false,\"subMenus\":[]}]},{\"action\":\"#\",\"title\":\"热网监控\",\"icon\":\"fa fa-cog\",\"permission\":\"heatingnetwork\",\"isLeaf\":false,\"isTab\":false,\"subMenus\":[{\"action\":\"navigationmap\",\"title\":\"导航地图\",\"icon\":\"fa fa-map\",\"permission\":\"navigationmap\",\"isLeaf\":true,\"isTab\":false,\"subMenus\":[]},{\"action\":\"monitorcontroll\",\"title\":\"热网监控1\",\"icon\":\"fa fa-cog\",\"permission\":\"monitorcontroll\",\"isLeaf\":true,\"isTab\":false,\"subMenus\":[]},{\"action\":\"reproduction\",\"title\":\"底图设置\",\"icon\":\"fa fa-cog\",\"permission\":\"reproduction\",\"isLeaf\":true,\"isTab\":false,\"subMenus\":[]}]},{\"action\":\"devicemanagement\",\"title\":\"设备管理\",\"icon\":\"fa fa-cog\",\"permission\":\"devicemanagement\",\"isLeaf\":false,\"isTab\":false,\"subMenus\":[{\"action\":\"collectorsettings\",\"title\":\"采集器设置\",\"icon\":\"fa fa-navicon\",\"permission\":\"collectorsettings\",\"isLeaf\":true,\"isTab\":false,\"subMenus\":[]},{\"action\":\"infrastructure\",\"title\":\"基础设施\",\"icon\":\"fa fa-navicon\",\"permission\":\"infrastructure\",\"isLeaf\":false,\"isTab\":false,\"subMenus\":[{\"id\":\"tab1\",\"action\":\"company\",\"title\":\"公司信息\",\"permission\":\"company\",\"isLeaf\":true,\"isTab\":true,\"subMenus\":[]},{\"id\":\"tab2\",\"action\":\"heatcompany\",\"title\":\"热力厂\",\"permission\":\"heatcompany\",\"isLeaf\":true,\"isTab\":true,\"subMenus\":[]},{\"id\":\"tab3\",\"action\":\"heatexchangestation\",\"title\":\"换热站\",\"permission\":\"heatexchangestation\",\"isLeaf\":true,\"isTab\":true,\"subMenus\":[]}]}]},{\"action\":\"alarmcenter\",\"title\":\"报警中心\",\"icon\":\"fa fa-cog\",\"permission\":\"alarmcenter\",\"isLeaf\":false,\"isTab\":false,\"subMenus\":[{\"action\":\"equipmentalarminfo\",\"title\":\"设备报警信息\",\"icon\":\"fa fa-navicon\",\"permission\":\"equipmentalarminfo\",\"isLeaf\":true,\"isTab\":false,\"subMenus\":[]}]}]";
}