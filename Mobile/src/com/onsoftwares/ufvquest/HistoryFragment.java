package com.onsoftwares.ufvquest;

import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.onsoftwares.classes.HistoryArrayAdapter;
import com.onsoftwares.classes.HistoryItem;
import com.onsoftwares.classes.Quest;
import com.onsoftwares.classes.UserRankingArrayAdapter;
import com.onsoftwares.classes.UserRankingItem;
import com.onsoftwares.utils.GlobalSettings;
import com.onsoftwares.utils.UFVQuestUtils;
import com.onsoftwares.utils.WebserviceConnection;

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

public class HistoryFragment extends Fragment {

	private ListView historyList;
	private HistoryArrayAdapter historyAdapter;
	
	public HistoryFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_history, container, false);
		
		historyList = (ListView) rootView.findViewById(R.id.history_list);
		
		historyAdapter = new HistoryArrayAdapter(getActivity());
		
		historyList.setAdapter(historyAdapter);
		
	    new DownloadHistoryAsync().execute("facebook_id=" + UFVQuestUtils.user.getFacebookId() + "&api_key=" + UFVQuestUtils.user.getApiKey());
	    
		return rootView;
	}
	
private class DownloadHistoryAsync extends AsyncTask<String, Void, Integer> {
		
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
		        	URL url = new URL(GlobalSettings.API_URL + "/getQuestHistory");
		    	    json = WebserviceConnection.getJson(params[0], url);
		    	    
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
					JSONArray array = json.getJSONArray("history");
					
					for (int i = 0; i < array.length(); i++) {
						JSONObject j = array.getJSONObject(i);
						String date = j.getString("timestamp");
						date = date.substring(8, 10) + "/" + date.substring(5, 7) + "/" + date.substring(0, 4);
						int questType = j.getString("quest_type").equals("gtaa") ? Quest.TYPE_GTAA : Quest.TYPE_SAA;
						historyAdapter.addItem(new HistoryItem(date, questType, j.getString("quest_title"), j.getString("quest_description"), j.getInt("points"), j.getBoolean("solved")));
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
