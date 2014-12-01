package com.onsoftwares.ufvquest;

import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.onsoftwares.classes.GoToAndAnswerQuest;
import com.onsoftwares.classes.NavigationDrawerArrayAdapter;
import com.onsoftwares.classes.NavigationDrawerItem;
import com.onsoftwares.utils.GlobalSettings;
import com.onsoftwares.utils.UFVQuestUtils;
import com.onsoftwares.utils.WebserviceConnection;

public class MapActivity extends FragmentActivity {

	public static final int ITEM_QUESTS = 1;
	public static final int ITEM_RANKING = 2;
	public static final int ITEM_HISTORY = 3;
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	public NavigationDrawerArrayAdapter navigationDrawerAdapter;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		
		navigationDrawerAdapter = new NavigationDrawerArrayAdapter(this);
		navigationDrawerAdapter.initialize();
		
		mDrawerList.setAdapter(navigationDrawerAdapter);


		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
			mDrawerLayout, /* DrawerLayout object */
			R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
			R.string.drawer_open, /* "open drawer" description */
			R.string.drawer_close /* "close drawer" description */
			) {
	
				/** Called when a drawer has settled in a completely closed state. */
				public void onDrawerClosed(View view) {
					super.onDrawerClosed(view);
					getActionBar().setTitle("Quest");
				}
	
				/** Called when a drawer has settled in a completely open state. */
				public void onDrawerOpened(View drawerView) {
					super.onDrawerOpened(drawerView);
					getActionBar().setTitle("Menu");
				}
		};
		
		mDrawerList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				Fragment fragment = null;
				switch(position) {
					case ITEM_QUESTS:
						navigationDrawerAdapter.selectMenu(position);
						fragment = new MyMapFragment();
						
						transaction.replace(R.id.content_frame, fragment);
						transaction.commit();
						
						break;
					case ITEM_RANKING:
						navigationDrawerAdapter.selectMenu(position);
						fragment = new UserRankingFragment();
						
						transaction.replace(R.id.content_frame, fragment);
						transaction.addToBackStack(null);
						transaction.commit();
						break;
					case ITEM_HISTORY:
						navigationDrawerAdapter.selectMenu(position);
						fragment = new HistoryFragment();
						
						transaction.replace(R.id.content_frame, fragment);
						transaction.addToBackStack(null);
						transaction.commit();
						break;
				}
				
				if(mDrawerLayout.isDrawerOpen(Gravity.LEFT))
					mDrawerLayout.closeDrawer(Gravity.LEFT);
			}
			
		});
		

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		Fragment fragment = new MyMapFragment();
		fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

	}
	
	@Override
	public void onBackPressed() {
		if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
			mDrawerLayout.closeDrawer(Gravity.LEFT);
	    }else{
	        super.onBackPressed();
	    }
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	 @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
	

}
