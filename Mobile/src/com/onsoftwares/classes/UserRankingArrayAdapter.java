package com.onsoftwares.classes;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onsoftwares.ufvquest.R;
import com.onsoftwares.utils.UFVQuestUtils;

public class UserRankingArrayAdapter extends BaseAdapter {
	
	private ArrayList<UserRankingItem> mData = new ArrayList<UserRankingItem>();
	private LayoutInflater mInflater;
	private Context c;
	
	public UserRankingArrayAdapter(Context c) {
		this.c = c;
		mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void addItem (UserRankingItem u) {
		mData.add(u);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public UserRankingItem getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		UserRankingItem u = getItem(position);
		
		convertView = mInflater.inflate(R.layout.user_ranking_item, null);
		TextView pos = (TextView) convertView.findViewById(R.id.ranking_position);
		ImageView avatar = (ImageView) convertView.findViewById(R.id.ranking_avatar);
		TextView name = (TextView) convertView.findViewById(R.id.ranking_name);
		TextView points = (TextView) convertView.findViewById(R.id.ranking_points);
		
		pos.setText(u.getPosition() + "º");
		name.setText(u.getName());
		points.setText(u.getPoints() + "pts");
		avatar.setImageDrawable(new RoundImage(BitmapFactory.decodeResource(c.getResources(), R.drawable.avatar)));
		avatar.setTag(u.getAvatar());
		new UFVQuestUtils.DownloadPictureAsync().execute(avatar);
		
		
		return convertView;
	}
	
}
