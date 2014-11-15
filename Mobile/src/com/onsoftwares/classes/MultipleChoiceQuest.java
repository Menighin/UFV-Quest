package com.onsoftwares.classes;

import java.util.ArrayList;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;
import com.onsoftwares.classes.Quest.Builder;

public class MultipleChoiceQuest extends Quest {
	
	private String question;
	private ArrayList<String> answers;
	private int theRightOne;
	
	public MultipleChoiceQuest() {
		super();
	}

	public MultipleChoiceQuest(int id, String title, String description, String placeName, LatLng location, int points, Date createdOn,	Date expirateOn, boolean diary, boolean solved,
			String question, ArrayList<String> answers, int theRightOne) {
		
		super(id, title, description, placeName, location, points, createdOn, expirateOn, diary, solved);
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
		public Date expirateOn;
		public boolean diary;
		public boolean solved;
		public String question;
		public ArrayList<String> answers;
		public int theRightOne;
		
		public Builder() {
			title = description = placeName = "";
			points = id = 0;
			location = new LatLng(0, 0);
			createdOn = new Date();
			expirateOn = new Date();
			diary = solved = false;
			this.question = "";
			this.answers = new ArrayList<String>();
			this.theRightOne = 0;
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
		
		public MultipleChoiceQuest build() {
			return new MultipleChoiceQuest(id, title, description, placeName, location,points,
					createdOn, expirateOn, diary, solved, question, answers, theRightOne);
		}
	}
	
	
	
}
