package com.onsoftwares.classes;

public class User {
	private String name;
	private String facebookId;
	private String apiKey;
	private String avatar;
	private String gender;
	private int energyLeft;
	private int points;
	
	public User(String name, String facebookId, String apiKey, String avatar,
			String gender, int energyLeft, int points) {
		super();
		this.name = name;
		this.facebookId = facebookId;
		this.apiKey = apiKey;
		this.avatar = avatar;
		this.gender = gender;
		this.energyLeft = energyLeft;
		this.points = points;
	}

	public User() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getEnergyLeft() {
		return energyLeft;
	}

	public void setEnergyLeft(int energyLeft) {
		this.energyLeft = energyLeft;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public void addPoints(int points) {
		this.points += points;
	}
	
}
