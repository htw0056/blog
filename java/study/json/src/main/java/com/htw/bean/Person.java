package com.htw.bean;

import com.google.gson.annotations.SerializedName;

public class Person {
	@SerializedName("NAME")
	private String name;
	private double age;
	private String birthday;
	private Object car;
	private boolean girlfriend;
	private String[] major;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAge() {
		return age;
	}

	public void setAge(double age) {
		this.age = age;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public Object getCar() {
		return car;
	}

	public void setCar(Object car) {
		this.car = car;
	}

	public boolean isGirlfriend() {
		return girlfriend;
	}

	public void setGirlfriend(boolean girlfriend) {
		this.girlfriend = girlfriend;
	}

	public String[] getMajor() {
		return major;
	}

	public void setMajor(String[] major) {
		this.major = major;
	}

}
