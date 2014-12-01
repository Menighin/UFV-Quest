package com.onsoftwares.ufvquest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.onsoftwares.classes.GoToAndAnswerQuest;
import com.onsoftwares.classes.Quest;
import com.onsoftwares.utils.UFVQuestUtils;

public class GoToAndAnswerFragment extends Fragment {
	
	private GoToAndAnswerQuest quest;
	private TextView question;
	private TextView title;
	private TextView feedback;
	private Button answer1;
	private Button answer2;
	private Button answer3;
	private Button answer4;
	private Button clickedAnswer;
	
	private final View.OnClickListener buttonClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			clickedAnswer = (Button) v;
			
			answer1.setBackgroundResource(R.drawable.gtaa_button_disabled);
			answer2.setBackgroundResource(R.drawable.gtaa_button_disabled);
			answer3.setBackgroundResource(R.drawable.gtaa_button_disabled);
			answer4.setBackgroundResource(R.drawable.gtaa_button_disabled);
			answer1.setEnabled(false);
			answer2.setEnabled(false);
			answer3.setEnabled(false);
			answer4.setEnabled(false);
			
			Button b = (Button) v;
			
			new UFVQuestUtils.AttemptQuestAsync(getActivity(), GoToAndAnswerFragment.this).execute("facebook_id=" + UFVQuestUtils.user.getFacebookId() + 
					"&api_key=" + UFVQuestUtils.user.getApiKey() + "&quest_id=" + UFVQuestUtils.currentQuest.getId() + "&answer=" + b.getTag() + "&quest_type=gtaa");
				
			
		}
	};
	
	public final Handler handleAttempt = new Handler() {
		public void handleMessage(Message msg) {
	        final int what = msg.what;
	        switch(what) {
	        	case Quest.GOT_IT_RIGHT:
	        		feedback.setText("Parabéns fera, você acertou!\nGanhou " + msg.arg1 + " pontos!");
					feedback.setTextColor(Color.parseColor("#00AA00"));
					UFVQuestUtils.user.addPoints(msg.arg1);
					
					((MapActivity)getActivity()).navigationDrawerAdapter.notifyDataSetChanged();
					
					if (clickedAnswer != null)
						clickedAnswer.setBackgroundResource(R.drawable.gtaa_button_right);
					
	        		break;
	        	case Quest.GOT_IT_WRONG:
	        		feedback.setText("Errou :(");
					feedback.setTextColor(Color.parseColor("#AA0000"));
	        		break;
	        	default:
	        		break;
	        }
	        
	        UFVQuestUtils.removeQuest(getActivity());
	    }
	};
	
	public GoToAndAnswerFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_go_to_and_answer, container, false);
		
		quest = (GoToAndAnswerQuest) UFVQuestUtils.currentQuest;
		
		title = (TextView) rootView.findViewById(R.id.gtaa_title);
		question = (TextView) rootView.findViewById(R.id.gtaa_question);
		answer1 = (Button) rootView.findViewById(R.id.gtaa_answer1);
		answer2 = (Button) rootView.findViewById(R.id.gtaa_answer2);
		answer3 = (Button) rootView.findViewById(R.id.gtaa_answer3);
		answer4 = (Button) rootView.findViewById(R.id.gtaa_answer4);
		feedback = (TextView) rootView.findViewById(R.id.gtaa_feedback);
		
		title.setText(quest.getTitle());
		question.setText(quest.getQuestion());
		answer1.setText(quest.getAnswers().get(0));
		answer2.setText(quest.getAnswers().get(1));
		answer3.setText(quest.getAnswers().get(2));
		answer4.setText(quest.getAnswers().get(3));
		answer1.setOnClickListener(buttonClick);
		answer2.setOnClickListener(buttonClick);
		answer3.setOnClickListener(buttonClick);
		answer4.setOnClickListener(buttonClick);
		
		return rootView;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
}
