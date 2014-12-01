package com.onsoftwares.ufvquest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onsoftwares.utils.UFVQuestUtils;

public class MultipleChoiceQuestActivity extends ActionBarActivity {
	
	private LinearLayout root;
	private TextView questionTextView;
	private int questId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multiple_choice_quest);
		
		questionTextView = (TextView) findViewById(R.id.multiple_choice_question);
		root = (LinearLayout) findViewById(R.id.multiple_choice_root);
		
		questId = getIntent().getIntExtra("quest", 0);
		
		/*setTitle(UFVQuestUtils.quests[questId].getTitle());
		
		questionTextView.setText(UFVQuestUtils.quests[questId].getQuestion());
		
		for (String answer : UFVQuestUtils.quests[questId].getAnswers()) {
			Button btn = new Button(this);
			LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, pxToDp(50));
			btn.setLayoutParams(btnParams);
			btn.setText(answer);
			btn.setOnClickListener(checkAnswer);
			root.addView(btn);
		}*/
	}
	
	// Check answer if button is clicked
	View.OnClickListener checkAnswer = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Button btn = (Button) v;
			/*if (btn.getText().equals(UFVQuestUtils.quests[questId].getAnswers().get(UFVQuestUtils.quests[questId].getTheRightOne()))) {
				Toast.makeText(MultipleChoiceQuestActivity.this, "Acertou, parabéns fera.", Toast.LENGTH_LONG).show();
				finish();
			} else {
				Toast.makeText(MultipleChoiceQuestActivity.this, "Errou heim", Toast.LENGTH_SHORT).show();
			}*/
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.multiple_choice_quest, menu);
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
	
	public int pxToDp(int px) {
	    return (int)(px * getResources().getDisplayMetrics().density);
	}
}
