package com.onsoftwares.classes;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onsoftwares.ufvquest.R;
import com.onsoftwares.utils.UFVQuestUtils;

public class HistoryArrayAdapter extends BaseAdapter {
	
	private ArrayList<HistoryItem> mData = new ArrayList<HistoryItem>();
	private LayoutInflater mInflater;
	private Context c;
	
	public HistoryArrayAdapter(Context c) {
		this.c = c;
		mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void addItem (HistoryItem u) {
		mData.add(u);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public HistoryItem getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HistoryItem h = getItem(position);
		
		convertView = mInflater.inflate(R.layout.history_item, null);
		TextView date = (TextView) convertView.findViewById(R.id.history_data);
		TextView title = (TextView) convertView.findViewById(R.id.history_title);
		TextView description = (TextView) convertView.findViewById(R.id.history_description);
		TextView points = (TextView) convertView.findViewById(R.id.history_points);
		ImageView icon = (ImageView) convertView.findViewById(R.id.history_image);
		
		date.setText(h.getDate());
		title.setText(h.getTitle());
		description.setText(h.getDescription());
		points.setText((h.isSolved() ? h.getPoints() : "0") + "pts");
		
		switch(h.getQuestType()) {
			case Quest.TYPE_GTAA:
				icon.setImageResource(R.drawable.quest1_ico);
				break;
			case Quest.TYPE_SAA:
				icon.setImageResource(R.drawable.quest2_ico);
				break;
		}
		
		if (h.isSolved()) {
			points.setTextColor(Color.parseColor("#FF55be34"));
		} else {
			points.setTextColor(Color.parseColor("#FFdb4e4e"));
		}
		
		return convertView;
	}
	
}
