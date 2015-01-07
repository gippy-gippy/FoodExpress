package com.example.foodexpress;

public class Shop {
	String name;
	int offset;
	double rating;
	
	public Shop(String name, int offset, int ratingsum, int count, int bo){
		this.name = name;
		this.offset = offset - bo;
		this.rating = ratingsum/(double) count;
	}
	
	public Shop(String name){
		this.name = name;
		offset = 0;
		rating = 0;
	}
	
	public String getName(){
		return name;
	}
	
	public int getOffset(){
		return offset;
	}
	
	public double getRating(){
		return (double) Math.round(rating * 10) / 10;
	}
}
