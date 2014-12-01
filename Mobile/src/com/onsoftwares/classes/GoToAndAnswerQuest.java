package com.onsoftwares.classes;

import java.sql.Date;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

public class GoToAndAnswerQuest extends Quest {
	
	private String question;
	private ArrayList<String> answers;
	private int theRightOne;
	
	public GoToAndAnswerQuest() {
		super();
	}

	public GoToAndAnswerQuest(int id, String title, String description, String placeName, LatLng location, int points, Date createdOn,	String expirateOn, boolean diary, boolean solved,
			String question, ArrayList<String> answers, int theRightOne, double rate) {
		
		super(id, title, description, placeName, location, points, createdOn, expirateOn, diary, solved, rate);
		this.question = question;
		this.answers = answers;
		this.theRightOne = theRightOne;
		
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public ArrayList<String> getAnswers() {
		return answers;
	}

	public void setAnswers(ArrayList<String> answers) {
		this.answers = answers;
	}

	public int getTheRightOne() {
		return theRightOne;
	}

	public void setTheRightOne(int theRightOne) {
		this.theRightOne = theRightOne;
	}
	
	public void addAnswer (String answer) {
		answers.add(answer);
	}
	
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
		public String question;
		public ArrayList<String> answers;
		public int theRightOne;
		public double successRate;
		
		public Builder() {
			title = description = placeName = "";
			points = id = 0;
			location = new LatLng(0, 0);
			createdOn = new Date(0);
			expirateOn = "01/01/01";
			diary = solved = false;
			this.question = "";
			this.answers = new ArrayList<String>();
			this.theRightOne = 0;
			this.successRate = 0;
		}
		
		public Builder question(String question) {
			this.question = question;
			return this;
		}
		
		public Builder addAnswer(String answer) {
			this.answers.add(answer);
			return this;
		}
		
		public Builder theRightOne(int theRightOne) {
			this.theRightOne = theRightOne;
			return this;
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
		
		public GoToAndAnswerQuest build() {
			return new GoToAndAnswerQuest(id, title, description, placeName, location,points,
					createdOn, expirateOn, diary, solved, question, answers, theRightOne, successRate);
		}
	}
	
	
	
}
