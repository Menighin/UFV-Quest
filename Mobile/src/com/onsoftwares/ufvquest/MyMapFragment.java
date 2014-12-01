package com.onsoftwares.ufvquest;

import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.onsoftwares.classes.CustomInfoWindowAdapter;
import com.onsoftwares.classes.GoToAndAnswerQuest;
import com.onsoftwares.classes.Quest;
import com.onsoftwares.classes.SeekAndAnswerQuest;
import com.onsoftwares.utils.GlobalSettings;
import com.onsoftwares.utils.UFVQuestUtils;
import com.onsoftwares.utils.WebserviceConnection;

public class MyMapFragment extends Fragment implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {
	private View rootView;
	private GoogleMap map;
	private Marker mPositionMarker;
	private LocationClient mLocationClient;
	private LocationRequest mLocationRequest;
	private Circle mCircle;
	private MapActivity parent;


	public MyMapFragment() {	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if (rootView == null) {
			
			try {
				parent = (MapActivity) getActivity();
			} catch (Exception e) {
				e.printStackTrace();
				parent = null;
			}
			
			rootView = inflater.inflate(R.layout.fragment_map, container, false);
	
	        map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			
	        new DownloadQuests().execute("facebook_id=" + UFVQuestUtils.user.getFacebookId() + "&api_key=" + UFVQuestUtils.user.getApiKey());
	        
			// Creating hardcoded quests
	
			CustomInfoWindowAdapter customInfoWindowAdapter = new CustomInfoWindowAdapter(getActivity());
			map.setInfoWindowAdapter(customInfoWindowAdapter);
			map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
				@Override
				public void onInfoWindowClick(final Marker marker) {
					
					AlertDialog alert = new AlertDialog.Builder(getActivity())
											.setTitle("Confirmação")
											.setMessage("Essa ação gastará 1 de energia")
											.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

												@Override
												public void onClick(DialogInterface dialog, int which) {
													UFVQuestUtils.user.setEnergyLeft(UFVQuestUtils.user.getEnergyLeft() - 1);
													parent.navigationDrawerAdapter.notifyDataSetChanged();
													Quest q = UFVQuestUtils.questMarkerMap.get(marker);
													
													if (q != null) {
														final Fragment fragment;
														if (q instanceof GoToAndAnswerQuest) {
															fragment = new GoToAndAnswerFragment();
														} else {
															fragment = new SeekAndAnswerFragment();
														}
															
														if (fragment != null) {
															UFVQuestUtils.currentQuest = q;
															UFVQuestUtils.currentMarker = marker;
															FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
															
															transaction.replace(R.id.content_frame, fragment);
															transaction.addToBackStack(null);
															transaction.commit();
															
														}
													}
												}
											})
											.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
												
												@Override
												public void onClick(DialogInterface dialog, int which) {
													// Do nothing
												}
											}).create();
					alert.show();
				}
			});
			
			map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker m) {
					Quest q = UFVQuestUtils.questMarkerMap.get(m);
					if (q != null && q.isActive()) {
						m.showInfoWindow();
						map.animateCamera(CameraUpdateFactory.newLatLng(m.getPosition()));
					}
					return true;
				}
			});
			
			
		} else {
			 ViewGroup parent = (ViewGroup) rootView.getParent();
		        if (parent != null)
		            parent.removeView(rootView);
		}
		
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
	public void onResume() {
		super.onResume();
		mLocationClient.connect();
		
		((MapActivity) getActivity()).navigationDrawerAdapter.selectMenu(MapActivity.ITEM_QUESTS);
	}
	
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
		if (UFVQuestUtils.debug)
			Log.d("Debug", "onConnectionFailed called");

	}

	@Override
	public void onConnected(Bundle arg0) {
		if (UFVQuestUtils.debug)
			Log.d("Debug", "onConnected called");
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
	}

	@Override
	public void onDisconnected() {
		if (UFVQuestUtils.debug)
			Log.d("Debug", "onDisconnected called");

	}

	@Override
	public void onLocationChanged(Location arg0) {
		// Get the current location
		Location currentLocation = mLocationClient.getLastLocation();
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
			
			//Check if quest is eligible
			for (Marker m : UFVQuestUtils.questMarkers) {
				Quest q = UFVQuestUtils.questMarkerMap.get(m);
				if (distFrom(currentLocation.getLatitude(), currentLocation.getLongitude(), m.getPosition().latitude, m.getPosition().longitude) <= UFVQuestUtils.radius) {
					Boolean b = m.isInfoWindowShown();
					if (q instanceof GoToAndAnswerQuest)
						m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_quest1));
					else if (q instanceof SeekAndAnswerQuest)
						m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_quest2));
					q.setActive(true); // TODO: CHECAR SESSAPORRA PODE SER NULL
					if (b) m.showInfoWindow();
				} else {
					m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_quest1_disabled));
					q.setActive(false);
					m.hideInfoWindow();
				}
			}

			if (UFVQuestUtils.debug)
				Toast.makeText(getActivity(), currentLatLng.toString(),Toast.LENGTH_SHORT).show();
		}

	}
	
	private class DownloadQuests extends AsyncTask<String, Void, Integer> {
		
		private JSONObject json;
		
		@Override
		protected Integer doInBackground(String... params) {
			ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		   
			if (networkInfo != null && networkInfo.isConnected()) {
		        try {
		        	URL url = new URL(GlobalSettings.API_URL + "/getQuestIStillCanComplete");
		    	    json = WebserviceConnection.getJson(params[0], url);
		    	    
		    	    Log.e("JSON", json.toString());
		    	    
		    	    return json.getInt("status");
		        } catch (Exception e) {
		        	Log.e("RegisterUserAsync", e.toString());
		        	e.printStackTrace();
		        	return -1;
		        }
		    } else {
		    	return -3;
		    }
		}
		
		@Override
		protected void onPostExecute(Integer status) {
			if (status != 1) {
				Toast.makeText(getActivity(), "Ocorreu um erro ao baixar as quests", Toast.LENGTH_SHORT).show();
			} else {
				try {
					JSONArray quests = json.getJSONArray("quests");
					JSONObject j;
					for (int i = 0; i < quests.length(); i++) {
						j = quests.getJSONObject(i);
						String type = j.getString("type");
						String expirate = j.getString("expiration_date");
						expirate = expirate.substring(8, 10) + "/" + expirate.substring(5, 7) + "/" + expirate.substring(0, 4);
						if (type.equals("gtaa")) {
							UFVQuestUtils.quests.add(new GoToAndAnswerQuest.Builder()
									.id(j.getInt("id"))
									.title(j.getString("title"))
									.description(j.getString("description"))
									.location(new LatLng(j.getDouble("latitude"), j.getDouble("longitude")))
									.placeName(j.getString("place_name"))
									.points(j.getInt("points"))
									.question(j.getString("question"))
									.addAnswer(j.getString("answer1"))
									.addAnswer(j.getString("answer2"))
									.addAnswer(j.getString("answer3"))
									.addAnswer(j.getString("answer4"))
									.theRightOne(j.getInt("correct_answer"))
									.successRate(j.getDouble("success_rate"))
									.expirateOn(expirate)
									.build());
							
							
						} else if (type.equals("saa")) {
							UFVQuestUtils.quests.add(new SeekAndAnswerQuest.Builder()
								.id(j.getInt("id"))
								.title(j.getString("title"))
								.description(j.getString("description"))
								.location(new LatLng(j.getDouble("latitude"), j.getDouble("longitude")))
								.placeName(j.getString("place_name"))
								.points(j.getInt("points"))
								.question(j.getString("question"))
								.answer(j.getString("answer"))
								.successRate(j.getDouble("success_rate"))
								.expirateOn(expirate)
								.build());
							
						}
						
						
						UFVQuestUtils.questMarkers.add(map.addMarker(new MarkerOptions()
							.position(UFVQuestUtils.quests.get(i).getLocation())
							.title(UFVQuestUtils.quests.get(i).getTitle())
							.snippet(UFVQuestUtils.quests.get(i).getDescription())
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_quest1_disabled))));
						
						UFVQuestUtils.questMarkerMap.put(UFVQuestUtils.questMarkers.get(i), UFVQuestUtils.quests.get(i));
						
					}
					
					if (parent != null)
						parent.navigationDrawerAdapter.editNotification(1, quests.length() + "");
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
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
