package com.onsoftwares.ufvquest;

import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.onsoftwares.utils.GlobalSettings;
import com.onsoftwares.utils.WebserviceConnection;

public class SplashScreen extends Activity {
	
	private ImageView pin;
	private ImageView innerCircle1;
	private ImageView innerCircle2;
	private ImageView innerCircle3;
	
	private boolean shouldIRegisterUserNow;
	
	private UiLifecycleHelper uiHelper;
	
	private com.facebook.widget.LoginButton fbButton;
	
	private SharedPreferences sp;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_splash_screen);
		
		sp = getSharedPreferences(GlobalSettings.MY_PREFERENCES, Context.MODE_PRIVATE);
		
		shouldIRegisterUserNow = true;
		
		pin = (ImageView) findViewById(R.id.splash_screen_pin);
		innerCircle1 = (ImageView) findViewById(R.id.splash_screen_circle1);
		innerCircle2 = (ImageView) findViewById(R.id.splash_screen_circle2);
		innerCircle3 = (ImageView) findViewById(R.id.splash_screen_circle3);
		fbButton = (com.facebook.widget.LoginButton) findViewById(R.id.authButton);
		
		Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_from_top);
		in.setDuration(300);
		in.setStartOffset(300);
		
		Animation inflate1 = AnimationUtils.loadAnimation(this, R.anim.scale);
		inflate1.setDuration(200);
		inflate1.setRepeatCount(Animation.INFINITE);
		inflate1.setStartOffset(700);
		
		Animation inflate2 = AnimationUtils.loadAnimation(this, R.anim.scale);
		inflate2.setDuration(200);
		inflate2.setRepeatCount(Animation.INFINITE);
		inflate2.setStartOffset(600);
		
		Animation inflate3 = AnimationUtils.loadAnimation(this, R.anim.scale);
		inflate3.setDuration(200);
		inflate3.setRepeatCount(10);
		inflate3.setRepeatMode(Animation.RESTART);
		inflate3.setStartOffset(500);
		
		
		pin.setAnimation(in);
		innerCircle1.startAnimation(inflate1);
		innerCircle2.startAnimation(inflate2);
		innerCircle3.startAnimation(inflate3);
		
		if (sp.getBoolean("logged", false))
			new Handler().postDelayed(new Runnable () {
	        	@Override
	        	public void run() {
	        		Intent i; 
	    			i = new Intent(SplashScreen.this, MapActivity.class);
	        		startActivity(i);
	        		finish();
	        	}
	        }, 3000);
		
		 if (sp.getBoolean("logged", false))
	        fbButton.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (state.isOpened()) {
	        if (!sp.getBoolean("logged", false)) {
	        	
	        	fbButton.setVisibility(View.INVISIBLE);
	        	
	        	Request.newMeRequest(session, new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							try {
								String urlParams = "facebook_id=" + user.getId() + 
													"&name=" + URLEncoder.encode(user.getName(), "UTF-8") +
													"&gender=" + user.getInnerJSONObject().getString("gender") + 
													"&avatar=" + "http://graph.facebook.com/" + user.getId() + "/picture?type=large";
								new RegisterUser().execute(urlParams);
								
								fbButton.setVisibility(View.INVISIBLE);
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
					}
				}).executeAsync();
	        } 
	    } else if (state.isClosed()) {
	        Log.i("Chiodi viado", "Logged out...");
	    }
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }

	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	private class RegisterUser extends AsyncTask<String, Void, Integer> {
		
		private JSONObject json;
		
		@Override
		protected Integer doInBackground(String... params) {
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		   
			if (networkInfo != null && networkInfo.isConnected()) {
		        try {
		        	URL url = new URL(GlobalSettings.API_URL + "/createUser");
		    	    json = WebserviceConnection.getJson(params[0], url);
		    	    
		    	   
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
			if (status != 1 && status != -7) {
				Toast.makeText(SplashScreen.this, "Ocorreu um erro no registro de usuário", Toast.LENGTH_SHORT).show();
			} else {
				SharedPreferences.Editor e = sp.edit();
				e.putBoolean("logged", true);
				e.commit();
				Intent i; 
    			i = new Intent(SplashScreen.this, MapActivity.class);
        		startActivity(i);
        		finish();
			}
		}
		
	}
}

//ImageView user_picture;
//userpicture=(ImageView)findViewById(R.id.userpicture);
//URL img_value = null;
//img_value = new URL("http://graph.facebook.com/"+id+"/picture?type=large");
//Bitmap mIcon1 = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
//userpicture.setImageBitmap(mIcon1);