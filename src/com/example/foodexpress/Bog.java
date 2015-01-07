package com.example.foodexpress;

public class Bog {
	private String name;
	private int id;
	
	public Bog(int id, String name){
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
