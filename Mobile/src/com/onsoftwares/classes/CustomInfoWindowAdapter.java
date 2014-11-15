package com.onsoftwares.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.onsoftwares.ufvquest.R;

public class CustomInfoWindowAdapter implements InfoWindowAdapter {
	
	private View view;
	private Context context;
	private LayoutInflater inflater;
	 
    public CustomInfoWindowAdapter(Context c) {
    	this.context = c;
    	inflater = LayoutInflater.from(c);
        view = inflater.inflate(R.layout.custom_infowindow, null);
    }
	
	@Override
	public View getInfoContents(Marker marker) {
		
		TextView title = (TextView) view.findViewById(R.id.custom_infowindow_title);
		title.setText(marker.getTitle());
		
		TextView snippet = (TextView) view.findViewById(R.id.custom_infowindow_snippet);
		snippet.setText(marker.getSnippet());
		
		return view;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

}
