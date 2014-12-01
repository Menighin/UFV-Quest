package com.onsoftwares.utils;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.Marker;
import com.onsoftwares.classes.Quest;
import com.onsoftwares.classes.RoundImage;
import com.onsoftwares.classes.User;
import com.onsoftwares.ufvquest.GoToAndAnswerFragment;
import com.onsoftwares.ufvquest.MapActivity;
import com.onsoftwares.ufvquest.SeekAndAnswerFragment;

public class UFVQuestUtils {
	
	public static final boolean debug = false;
	
	public static final float radius = 2000f;
	
	public static User user;
	
	public static ArrayList<Quest> quests = new ArrayList<Quest>();
	public static ArrayList<Marker> questMarkers = new ArrayList<Marker>();
	public static HashMap<Marker, Quest> questMarkerMap = new HashMap<Marker, Quest>();
	public static Quest currentQuest;
	public static Marker currentMarker;
	
	
	public static class DownloadPictureAsync extends AsyncTask<ImageView, Void, Bitmap> {
		
		ImageView view;
		
		@Override
		protected Bitmap doInBackground(ImageView... imageViews) {
			view = imageViews[0];
			try {
				HttpGet httpRequest = new HttpGet(URI.create((String) view.getTag()));
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
                HttpEntity entity = response.getEntity();
                BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
                Bitmap b = BitmapFactory.decodeStream(bufHttpEntity.getContent());
                httpRequest.abort();
                return b;
			} catch (Exception e) {
				Log.e("WAT", e.toString());
				e.printStackTrace();
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			if (result != null)
				view.setImageDrawable(new RoundImage(result));
		}
		
	}
	
	public static void removeQuest(Context c) {
		UFVQuestUtils.currentMarker.remove();
		UFVQuestUtils.questMarkers.remove(UFVQuestUtils.currentMarker);
		UFVQuestUtils.quests.remove(UFVQuestUtils.currentQuest);
		UFVQuestUtils.questMarkerMap.remove(UFVQuestUtils.currentMarker);
		((MapActivity)c).navigationDrawerAdapter.removeNumberQuests();
	}
	
	public static class AttemptQuestAsync extends AsyncTask<String, Void, Integer> {
		
		private JSONObject json;
		private ProgressDialog pdia;
		private Context c;
		private Fragment f;
		
		public AttemptQuestAsync(Context c, Fragment f) {
			this.c = c;
			this.f = f;
		}
		
		@Override
		protected void onPreExecute(){ 
		   super.onPreExecute();
		        pdia = new ProgressDialog(c);
		        pdia.setMessage("Enviando...");
		        pdia.show();    
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			
			ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		   
			if (networkInfo != null && networkInfo.isConnected()) {
		        try {
		        	URL url = new URL(GlobalSettings.API_URL + "/tryQuest");
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
			Message msg = new Message();
			if (status == 1) {
				try {
					msg.what = Quest.GOT_IT_RIGHT;
					msg.arg1 = json.getInt("points_won");
				} catch (JSONException e) {
					e.printStackTrace();
					msg.what = Quest.GOT_IT_WRONG;
				}
			}
			else if (status == 2) {
				msg.what = Quest.GOT_IT_WRONG;
			} else {
				Toast.makeText(c, "Ocorreu um erro ao tentar quest", Toast.LENGTH_SHORT).show();
				msg.what = Quest.GOT_IT_WRONG;
				Log.d("ERRO", json.toString());
			}
			
			if (f instanceof GoToAndAnswerFragment)
				((GoToAndAnswerFragment) f).handleAttempt.sendMessage(msg);
			else if (f instanceof SeekAndAnswerFragment)
				((SeekAndAnswerFragment) f).handleAttempt.sendMessage(msg);
		}
		
	}
}
