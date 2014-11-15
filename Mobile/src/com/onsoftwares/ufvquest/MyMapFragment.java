package com.onsoftwares.ufvquest;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.onsoftwares.classes.CustomInfoWindowAdapter;
import com.onsoftwares.classes.MultipleChoiceQuest;
import com.onsoftwares.classes.UFVQuestUtils;

public class MyMapFragment extends Fragment implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
	private GoogleMap map;
	private Marker mPositionMarker;
	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	private Circle mCircle;
	private boolean quest1Active, quest2Active;

	public MyMapFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		
		// Creating hardcoded quests
		UFVQuestUtils.quests[0] = new MultipleChoiceQuest.Builder().id(0)
				.title("Aula de seminários")
				.description("Pergunta sobre a aula de seminários")
				.location(new LatLng(-20.765185, -42.868873)).placeName("CCE")
				.points(100).question("Quem é o professor da matéria?")
				.addAnswer("Levy Fidelix").addAnswer("Levi Lelis")
				.addAnswer("Levi Strauss").addAnswer("Levi Ackerman")
				.theRightOne(1).build();

		UFVQuestUtils.quests[1] = new MultipleChoiceQuest.Builder().id(0)
				.title("4 Pilastras")
				.description("Pergunta sobre as quatro pilastras")
				.location(new LatLng(-20.757251, -42.875297)).placeName("CCE")
				.points(100).question("Quantas são as pilastras?")
				.addAnswer("1").addAnswer("3").addAnswer("4").addAnswer("2")
				.theRightOne(2).build();

		UFVQuestUtils.questMarkers[0] = map.addMarker(new MarkerOptions()
				.position(UFVQuestUtils.quests[0].getLocation())
				.title(UFVQuestUtils.quests[0].getTitle())
				.snippet(UFVQuestUtils.quests[0].getDescription())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.marker_quest1)));

		UFVQuestUtils.questMarkers[1] = map.addMarker(new MarkerOptions()
				.position(UFVQuestUtils.quests[1].getLocation())
				.title(UFVQuestUtils.quests[1].getTitle())
				.snippet(UFVQuestUtils.quests[1].getDescription())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.marker_quest1)));

		quest1Active = quest2Active = true;
		CustomInfoWindowAdapter customInfoWindowAdapter = new CustomInfoWindowAdapter(
				getActivity());
		map.setInfoWindowAdapter(customInfoWindowAdapter);
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {

				
			}
		});

		return rootView;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		mLocationRequest = LocationRequest.create();

		mLocationRequest.setInterval(5000);

		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		// Set the interval ceiling to one minute
		mLocationRequest.setFastestInterval(1000);
		
		mLocationClient = new LocationClient(getActivity().getApplicationContext(), this, this);
    }
	
	@Override
	public void onStart() {
		super.onStart();
		mLocationClient.connect();
	};
	
	@Override
	public void onStop() {
		if (mLocationClient.isConnected()) {
			mLocationClient.removeLocationUpdates(this);
			mLocationClient.disconnect();
        }
		super.onStop();
	}; 

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.d("DEY MERDA", "DEU MERDA");

	}

	@Override
	public void onConnected(Bundle arg0) {
		Log.d("N DEY MERDA", "N DEU MERDA");
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	@Override
	public void onDisconnected() {
		Log.d("DEY MERDA", "DISCONECTOU");

	}

	@Override
	public void onLocationChanged(Location arg0) {
		// Get the current location
		Log.d("HUEHUE", "OIOIOOI");
		Location currentLocation = mLocationClient.getLastLocation();
		Log.d("HUEHUE", "OIOIOOI");
		// Display the current location in the UI
		if (currentLocation != null) {
			LatLng currentLatLng = new LatLng(currentLocation.getLatitude(),
					currentLocation.getLongitude());
			if (mPositionMarker == null) {
				mPositionMarker = map.addMarker(new MarkerOptions()
						.position(currentLatLng)
						.title("Eu")
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.male_user_marker)));
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
						15));
				mCircle = map.addCircle(new CircleOptions()
						.center(currentLatLng).radius(UFVQuestUtils.radius)
						.strokeColor(0xff7cc0df).fillColor(0x227cc0df)
						.strokeWidth(3f));

			} else {
				mPositionMarker.setPosition(currentLatLng);
				mCircle.setCenter(currentLatLng);
			}

			// Check if quest is eligible
			if (distFrom(currentLocation.getLatitude(),
					currentLocation.getLongitude(),
					UFVQuestUtils.questMarkers[0].getPosition().latitude,
					UFVQuestUtils.questMarkers[0].getPosition().longitude) <= UFVQuestUtils.radius) {

				if (!quest1Active) {
					UFVQuestUtils.questMarkers[0]
							.setIcon(BitmapDescriptorFactory
									.fromResource(R.drawable.marker_quest1));
					quest1Active = true;
				}

			} else {
				if (quest1Active) {
					UFVQuestUtils.questMarkers[0]
							.setIcon(BitmapDescriptorFactory
									.fromResource(R.drawable.marker_quest1_disabled));
					quest1Active = false;
				}
			}

			if (distFrom(currentLocation.getLatitude(),
					currentLocation.getLongitude(),
					UFVQuestUtils.questMarkers[1].getPosition().latitude,
					UFVQuestUtils.questMarkers[1].getPosition().longitude) <= UFVQuestUtils.radius) {

				if (!quest2Active) {
					UFVQuestUtils.questMarkers[1]
							.setIcon(BitmapDescriptorFactory
									.fromResource(R.drawable.marker_quest1));
					quest2Active = true;
				}
			} else {
				if (quest2Active) {
					UFVQuestUtils.questMarkers[1]
							.setIcon(BitmapDescriptorFactory
									.fromResource(R.drawable.marker_quest1_disabled));
					quest2Active = false;
				}
			}

			if (UFVQuestUtils.debug)
				Toast.makeText(getActivity(), currentLatLng.toString(),
						Toast.LENGTH_SHORT).show();
		}

	}

	private double distFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6371; // kilometers
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = (earthRadius * c);

		return Math.abs(dist * 1000);
	}
}
