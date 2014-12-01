package com.onsoftwares.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.onsoftwares.ufvquest.R;
import com.onsoftwares.utils.UFVQuestUtils;

public class CustomInfoWindowAdapter implements InfoWindowAdapter {
	
	private View view;
	private Context context;
	private LayoutInflater inflater;
	 
    public CustomInfoWindowAdapter(Context c) {
    	this.context = c;
    	inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.custom_infowindow, null);
    }
	
	@Override
	public View getInfoContents(Marker marker) {
		
		TextView title = (TextView) view.findViewById(R.id.custom_infowindow_title);
		TextView snippet = (TextView) view.findViewById(R.id.custom_infowindow_snippet);
		ImageView ico = (ImageView) view.findViewById(R.id.custom_infowindow_quest_ico);
		TextView points = (TextView) view.findViewById(R.id.custom_infowindow_points);
		TextView expireOn = (TextView) view.findViewById(R.id.custom_infowindow_expirate);
		TextView clickToSolve = (TextView) view.findViewById(R.id.custom_infowindow_clickToSolve);
		TextView successRate = (TextView) view.findViewById(R.id.custom_infowindow_rate);
		
		Quest q = UFVQuestUtils.questMarkerMap.get(marker);
		
		
		title.setText(marker.getTitle());
		
		if (q != null) {
			
			ico.setVisibility(View.VISIBLE);
			snippet.setVisibility(View.VISIBLE);
			expireOn.setVisibility(View.VISIBLE);
			points.setVisibility(View.VISIBLE);
			clickToSolve.setVisibility(View.VISIBLE);
			
			snippet.setText(marker.getSnippet());
			points.setText(q.getPoints() + "pts");
			expireOn.setText(q.getExpirateOn().substring(0, 5));
			successRate.setText(q.getSuccessRate() + "%");
			
			if (q instanceof SeekAndAnswerQuest)
				ico.setImageResource(R.drawable.quest2_ico);
			else if (q instanceof GoToAndAnswerQuest)
				ico.setImageResource(R.drawable.quest1_ico);
			
		} else {
			ico.setVisibility(View.GONE);
			snippet.setVisibility(View.GONE);
			expireOn.setVisibility(View.GONE);
			points.setVisibility(View.GONE);
			clickToSolve.setVisibility(View.GONE);
		}
		
		return view;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

}
