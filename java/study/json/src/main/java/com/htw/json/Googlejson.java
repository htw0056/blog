package com.htw.json;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.htw.bean.Person;

public class Googlejson {
	public static void main(String[] args) throws IOException {
		createJsonByBean();
		readJsonFromFile();
	}

	public static void createJsonByBean() {
		Person jo = new Person();
		jo.setName("test");
		jo.setAge(21.2);
		jo.setBirthday("1995-03-12");
		jo.setCar(null);
		jo.setGirlfriend(false);
		jo.setMajor(new String[] { "计算机", "a" });
		Gson gson = new Gson();
		System.out.println(gson.toJson(jo));
	}

	public static void readJsonFromFile() throws IOException {
		File file = new File("test.json");
		String content = FileUtils.readFileToString(file);
		JsonParser jsonParser = new JsonParser();
		JsonObject parse = (JsonObject) jsonParser.parse(content);
		System.out.println(parse.get("name").getAsString());
		JsonElement jsonElement = parse.get("major");
		if (jsonElement.isJsonArray())
		{
			JsonArray asJsonArray = jsonElement.getAsJsonArray();
			for (int i = 0; i < asJsonArray.size(); i++)
			{
				System.out.println(asJsonArray.get(i).getAsString());
			}
		}
	}
}
