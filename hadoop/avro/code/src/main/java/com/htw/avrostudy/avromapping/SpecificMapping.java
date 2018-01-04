package com.htw.avrostudy.avromapping;

import example.avro.User;

/*
 * 特殊映射
 * 即先通过tools生成代码，然后调用新生成的类---mvn compile
 */
public class SpecificMapping {

	public static void main(String[] args) {
		User user1=new User();
		user1.setName("htw");
		user1.setFavoriteNumber(3);
		user1.setFavoriteColor("white");
		System.out.println(user1);
	}

}
