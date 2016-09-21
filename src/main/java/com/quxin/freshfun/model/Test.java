package com.quxin.freshfun.model;

public class Test {
	private String id;
	private String name;
	private double age;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	@Override
	public String toString() {
		return "Test [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
	public Test(String id, String name, double age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}
	public Test() {
		super();
	}
	
}
