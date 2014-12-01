package com.onsoftwares.ufvquest;

import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import com.onsoftwares.classes.UserRankingArrayAdapter;
import com.onsoftwares.classes.UserRankingItem;
import com.onsoftwares.utils.GlobalSettings;
import com.onsoftwares.utils.WebserviceConnection;

public class UserRankingFragment extends Fragment {

	private TabHost tabHost;
	private TabHost.TabSpec w, t;
	private ListView listWeekly, listTotal;
	private UserRankingArrayAdapter adapterWeekly, adapterTotal;
	
	public UserRankingFragment() { }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_user_ranking, container, false);
		
		listWeekly = (ListView) rootView.findViewById(R.id.ranking_weekly_list);
		listTotal = (ListView) rootView.findViewById(R.id.ranking_total_list);
		
		adapterWeekly = new UserRankingArrayAdapter(getActivity());
		adapterTotal = new UserRankingArrayAdapter(getActivity());
		
		listWeekly.setAdapter(adapterWeekly);
		listTotal.setAdapter(adapterTotal);
		
		tabHost = (TabHost) rootView.findViewById(R.id.ranking_tabhost);
		tabHost.setup();
		
		w = tabHost.newTabSpec("WEEK");
		w.setContent(R.id.ranking_weekly_list);
	    w.setIndicator("Semanal");
		
	   	t = tabHost.newTabSpec("TOTAL");
	   	t.setContent(R.id.ranking_total_list);
	    t.setIndicator("Total");
	    
	    	    
	    tabHost.addTab(w);
	    tabHost.addTab(t);
		
	    new DownloadUserRankingAsync().execute("");
	    
		return rootView;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	private class DownloadUserRankingAsync extends AsyncTask<String, Void, Integer> {
		
		private ProgressDialog pdia;
		private JSONObject json;
		
		@Override
		protected void onPreExecute(){ 
		   super.onPreExecute();
		        pdia = new ProgressDialog(getActivity());
		        pdia.setMessage("Carregando...");
		        pdia.show();    
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		   
			if (networkInfo != null && networkInfo.isConnected()) {
		        try {
		        	URL url = new URL(GlobalSettings.API_URL + "/getUserRanking");
		    	    json = WebserviceConnection.getJson(params[0], url);
		    	    
		    	    Log.e("PARAMS", params[0]);
		    	    
		    	    return json.getInt("status");
		        } catch (Exception e) {
		        	Log.e("AttemptQuestAsync", e.toString());
		        	e.printStackTrace();
		        	return -1;
		        }
		    } else {
		    	return -3;
		    }
		}
		
		@Override
		protected void onPostExecute(Integer status) {
			pdia.dismiss();
			if (status == 1) {
				try {
					JSONArray array = json.getJSONArray("week");
					for (int i = 0; i < array.length(); i++) {
						JSONObject j = array.getJSONObject(i);
						adapterWeekly.addItem(new UserRankingItem(i + 1, j.getString("name"), j.getString("avatar"), j.getInt("points")));
					}
					array = json.getJSONArray("total");
					for (int i = 0; i < array.length(); i++) {
						JSONObject j = array.getJSONObject(i);
						adapterTotal.addItem(new UserRankingItem(i + 1, j.getString("name"), j.getString("avatar"), j.getInt("points")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(getActivity(), "Ocorreu um erro ao carregar o ranking", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
}
