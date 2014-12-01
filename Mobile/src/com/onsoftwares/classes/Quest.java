package com.onsoftwares.classes;

import java.sql.Date;

import com.google.android.gms.maps.model.LatLng;

public class Quest {
	
	public static final int GOT_IT_RIGHT = 1;
	public static final int GOT_IT_WRONG = 2;
	public static final int TYPE_GTAA = 70;
	public static final int TYPE_SAA = 71;
	
	private int id;
	private String title;
	private String description;
	private String placeName;
	private LatLng location;
	private int points;
	private Date createdOn;
	private String expirateOn;
	private boolean diary;
	private boolean solved;
	private boolean active;
	private double successRate;
	
	public Quest() {
		super();
	}

	public Quest(int id, String title, String description, String placeName, LatLng location, int points, Date createdOn, String expirateOn, boolean diary, boolean solved, double successRate) {
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
		this.active = false;
		this.successRate = successRate;
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

	public String getExpirateOn() {
		return expirateOn;
	}

	public void setExpirateOn(String expirateOn) {
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
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public double getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(double successRate) {
		this.successRate = successRate;
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
		public String expirateOn;
		public boolean diary;
		public boolean solved;
		public double successRate;
		
		public Builder() {
			title = description = placeName = "";
			points = id = 0;
			location = new LatLng(0, 0);
			createdOn = new Date(0);
			expirateOn = "01/01/01";
			diary = solved = false;
			successRate = 0;
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
		
		public Builder expirateOn(String expirateOn) {
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
		
		public Builder successRate(double rate) {
			this.successRate = rate;
			return this;
		}
		
		
		public Quest build() {
			return new Quest(id, title, description, placeName, location, points, createdOn, expirateOn, diary, solved, successRate);
		}
	}
	
	
}
