package com.example.foodexpress;

public class Train {
	private String name;
	private int id;
	
	public Train(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}
}
