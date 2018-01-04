package com.htw.json;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.htw.bean.Person;

public class OrgJson {

	public static void main(String[] args) throws IOException {
		createJsonSimple();
		createJsonByMap();
		createJsonByBean();
		readJsonFromFile();
	}

	public static void createJsonSimple() {
		JSONObject jo = new JSONObject();
		Object ObjectNull = null;
		jo.put("name", "test");
		jo.put("age", 21.2);
		jo.put("birthday", "1995-03-12");
		jo.put("car", ObjectNull);
		jo.put("girlfriend", false);
		jo.put("major", new String[] { "计算机", "a" });
		System.out.println(jo.toString());

		// 复合格式
		JSONObject test = new JSONObject();
		test.put("hello", jo);
		System.out.println(test.toString());
	}

	public static void createJsonByMap() {
		Map<String, Object> mp = new HashMap<String, Object>();
		mp.put("name", "test");
		mp.put("age", 21.2);
		mp.put("birthday", "1995-03-12");
		mp.put("car", null);
		mp.put("girlfriend", false);
		mp.put("major", new String[] { "计算机", "a" });

		System.out.println(new JSONObject(mp).toString());

		// 复合格式
		Map<String, Object> mp2 = new HashMap<String, Object>();
		mp2.put("hello", mp);
		System.out.println(new JSONObject(mp2).toString());
	}

	public static void createJsonByBean() {
		Person jo = new Person();
		jo.setName("test");
		jo.setAge(21.2);
		jo.setBirthday("1995-03-12");
		jo.setCar(null);
		jo.setGirlfriend(false);
		jo.setMajor(new String[] { "计算机", "a" });
		System.out.println(new JSONObject(jo).toString());
	}

	public static void readJsonFromFile() throws IOException {
		File file = new File("test.json");
		String content = FileUtils.readFileToString(file);
		JSONObject jsonObejct = new JSONObject(content);
		if (!jsonObejct.isNull("name")) {

			System.out.println(jsonObejct.getString("name"));
		}
		System.out.println(jsonObejct.getBoolean("girlfriend"));
		JSONArray jsonArray = jsonObejct.getJSONArray("major");
		for (int i = 0; i < jsonArray.length(); i++) {
			System.out.println(jsonArray.getString(i));
		}
	}
}
