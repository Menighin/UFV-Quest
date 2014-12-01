package com.onsoftwares.classes;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onsoftwares.ufvquest.R;
import com.onsoftwares.utils.UFVQuestUtils;

public class NavigationDrawerArrayAdapter extends BaseAdapter {

	public static final int TYPE_ITEM = 0;
	public static final int TYPE_HEADER = 1;
	public static final int TYPE_SEPARATOR = 2;
	public static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
	
	private ArrayList<NavigationDrawerItem> mData = new ArrayList<NavigationDrawerItem>();	
	private LayoutInflater mInflater;
	private Context c;
	
	
	public NavigationDrawerArrayAdapter(Context c) {
		this.c = c;
		mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void initialize() {
		addItem(new NavigationDrawerItem(0, 0, "", "", NavigationDrawerArrayAdapter.TYPE_HEADER, true));
		addItem(new NavigationDrawerItem(R.drawable.navigation_item_quests, R.drawable.navigation_item_quests_disabled, "Quests", "", NavigationDrawerArrayAdapter.TYPE_ITEM, true));
		addItem(new NavigationDrawerItem(R.drawable.navigation_item_ranking, R.drawable.navigation_item_ranking_disabled, "Ranking", "", NavigationDrawerArrayAdapter.TYPE_ITEM, false));
		addItem(new NavigationDrawerItem(R.drawable.navigation_item_history, R.drawable.navigation_item_history_disabled, "Histórico", "", NavigationDrawerArrayAdapter.TYPE_ITEM, false));
	}
	
	public void addItem(NavigationDrawerItem n) {
		mData.add(n);
		notifyDataSetChanged();
	}
	
	 @Override
     public int getItemViewType(int position) {
         return mData.get(position).getType();
     }
	 
	 @Override
     public int getViewTypeCount() {
         return TYPE_MAX_COUNT;
     }
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public NavigationDrawerItem getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void editNotification(int pos, String notification) {
		mData.get(pos).setNotification(notification);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		int type = getItemViewType(position);
		NavigationDrawerItem n = getItem(position);
		
		switch(type) {
			case TYPE_ITEM:
				convertView = mInflater.inflate(R.layout.navigation_drawer_item, null);
				ImageView icon = (ImageView) convertView.findViewById(R.id.navigation_item_icon);
				TextView label = (TextView) convertView.findViewById(R.id.navigation_item_label);
				TextView notification = (TextView) convertView.findViewById(R.id.navigation_item_notification);
				RelativeLayout wrapper = (RelativeLayout) convertView.findViewById(R.id.navigation_item_wrapper);
				
				label.setText(n.getLabel());
				if (n.isActive()) {
					icon.setImageResource(n.getIconIdActive());
					label.setTextColor(Color.parseColor("#FFFFFF"));
					wrapper.setBackgroundColor(Color.parseColor("#FF222222"));
				} else {
					icon.setImageResource(n.getIconIdDisabled());
					label.setTextColor(Color.parseColor("#AAAAAA"));
					wrapper.setBackgroundColor(Color.parseColor("#00000000"));
				}
				
				notification.setText(n.getNotification());
				
				if (n.getNotification().length() == 0)
					notification.setVisibility(View.GONE);
				
				break;
			case TYPE_HEADER:
				convertView = mInflater.inflate(R.layout.navigation_drawer_header, null);
				ImageView avatar = (ImageView) convertView.findViewById(R.id.navigation_header_avatar);
				TextView name = (TextView) convertView.findViewById(R.id.navigation_header_name);
				TextView points = (TextView) convertView.findViewById(R.id.navigation_header_points);
				LinearLayout energyContainer = (LinearLayout) convertView.findViewById(R.id.navigation_header_energy);
				
				avatar.setImageDrawable(new RoundImage(BitmapFactory.decodeResource(c.getResources(), R.drawable.avatar)));
				avatar.setTag(UFVQuestUtils.user.getAvatar());
				
				new UFVQuestUtils.DownloadPictureAsync().execute(avatar);
				
				name.setText(UFVQuestUtils.user.getName());
				points.setText(UFVQuestUtils.user.getPoints() + "pts");
				
				ImageView imageView;
				for (int i = 0; i < 5; i++) {
					imageView = new ImageView(c);
					RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
					imageView.setLayoutParams(params);
					if (i < UFVQuestUtils.user.getEnergyLeft())
						imageView.setImageResource(R.drawable.energy_full);
					else
						imageView.setImageResource(R.drawable.energy_empty);
					
					energyContainer.addView(imageView);
				}
				
				break;
		}
		
		return convertView;
	}
	
	public void removeNumberQuests() {
		getItem(1).setNotification((Integer.parseInt(getItem(1).getNotification()) - 1) + ""); 
		notifyDataSetChanged();
	}
	
	public void selectMenu(int position) {
		for (NavigationDrawerItem item : mData) 
			item.setActive(false);
		
		mData.get(position).setActive(true);
		notifyDataSetChanged();
	}

}
