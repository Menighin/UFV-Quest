package com.onsoftwares.classes;

public class UserRankingItem {
	
	private int position;
	private String name;
	private String avatar;
	private int points;
	
	public UserRankingItem() {
		super();
	}
	
	public UserRankingItem(int position, String name, String avatar, int points) {
		super();
		this.position = position;
		this.name = name;
		this.avatar = avatar;
		this.points = points;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAvatar() {
		return avatar;
	}
	
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public int getPoints() {
		return points;
	}
	
	public void setPoints(int points) {
		this.points = points;
	}
	
}
