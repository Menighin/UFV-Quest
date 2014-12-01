package com.onsoftwares.classes;

public class HistoryItem {
	private String date;
	private int questType;
	private String title;
	private String Description;
	private int points;
	private boolean solved;
	
	public HistoryItem(String date, int questType, String title,
			String description, int points, boolean solved) {
		super();
		this.date = date;
		this.questType = questType;
		this.title = title;
		Description = description;
		this.points = points;
		this.solved = solved;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getQuestType() {
		return questType;
	}

	public void setQuestType(int questType) {
		this.questType = questType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean isSolved() {
		return solved;
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	
}
