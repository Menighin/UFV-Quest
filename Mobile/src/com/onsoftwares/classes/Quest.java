package com.onsoftwares.classes;

import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

public class Quest {
	
	private int id;
	private String title;
	private String description;
	private String placeName;
	private LatLng location;
	private int points;
	private Date createdOn;
	private Date expirateOn;
	private boolean diary;
	private boolean solved;
	
	public Quest() {
		super();
	}

	public Quest(int id, String title, String description, String placeName, LatLng location, int points, Date createdOn, Date expirateOn, boolean diary, boolean solved) {
		
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.placeName = placeName;
		this.location = location;
		this.points = points;
		this.createdOn = createdOn;
		this.expirateOn = expirateOn;
		this.diary = diary;
		this.solved = solved;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public LatLng getLocation() {
		return location;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getExpirateOn() {
		return expirateOn;
	}

	public void setExpirateOn(Date expirateOn) {
		this.expirateOn = expirateOn;
	}

	public boolean isDiary() {
		return diary;
	}

	public void setDiary(boolean diary) {
		this.diary = diary;
	}

	public boolean isSolved() {
		return solved;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	
	
	
	
	// Builder of Quest class
	static public class Builder {
		public int id;
		public String title;
		public String description;
		public String placeName;
		public LatLng location;
		public int points;
		public Date createdOn;
		public Date expirateOn;
		public boolean diary;
		public boolean solved;
		
		public Builder() {
			title = description = placeName = "";
			points = id = 0;
			location = new LatLng(0, 0);
			createdOn = new Date();
			expirateOn = new Date();
			diary = solved = false;
		}
		
		public Builder id(int id) {
			this.id = id;
			return this;
		}
		
		public Builder title(String title) {
			this.title = title;
			return this;
		}
		
		public Builder description(String description) {
			this.description = description;
			return this;
		}
		
		public Builder placeName(String placeName) {
			this.placeName = placeName;
			return this;
		}
		
		public Builder location(LatLng location) {
			this.location = location;
			return this;
		}
		
		public Builder points(int points) {
			this.points = points;
			return this;
		}
		
		public Builder createdOn(Date createdOn) {
			this.createdOn = createdOn;
			return this;
		}
		
		public Builder expirateOn(Date expirateOn) {
			this.expirateOn = expirateOn;
			return this;
		}
		
		public Builder diary(boolean diary) {
			this.diary = diary;
			return this;
		}
		
		public Builder solved(boolean solved) {
			this.solved = solved;
			return this;
		}
		
		public Quest build() {
			return new Quest(id, title, description, placeName, location, points, createdOn, expirateOn, diary, solved);
		}
	}
	
	
}
