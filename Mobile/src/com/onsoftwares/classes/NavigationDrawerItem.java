package com.onsoftwares.classes;

public class NavigationDrawerItem {
	private int iconIdActive;
	private int iconIdDisabled;
	private String label;
	private String notification;
	private int type;
	private boolean active;
	
	public NavigationDrawerItem() {
		super();
	}

	public NavigationDrawerItem(int iconIdActive, int iconIdDisabled,
			String label, String notification, int type, boolean active) {
		super();
		this.iconIdActive = iconIdActive;
		this.iconIdDisabled = iconIdDisabled;
		this.label = label;
		this.notification = notification;
		this.type = type;
		this.active = active;
	}

	public int getIconIdActive() {
		return iconIdActive;
	}

	public void setIconIdActive(int iconIdActive) {
		this.iconIdActive = iconIdActive;
	}

	public int getIconIdDisabled() {
		return iconIdDisabled;
	}

	public void setIconIdDisabled(int iconIdDisabled) {
		this.iconIdDisabled = iconIdDisabled;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getNotification() {
		return notification;
	}

	public void setNotification(String notification) {
		this.notification = notification;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	
}
