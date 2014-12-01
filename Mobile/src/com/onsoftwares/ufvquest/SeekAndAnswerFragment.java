package com.onsoftwares.ufvquest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.onsoftwares.classes.Quest;
import com.onsoftwares.classes.SeekAndAnswerQuest;
import com.onsoftwares.utils.UFVQuestUtils;

public class SeekAndAnswerFragment extends Fragment {
	
	private SeekAndAnswerQuest quest;
	private TextView question;
	private TextView title;
	private TextView feedback;
	private EditText answer;
	private Button reply;
	
	private final View.OnClickListener buttonClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			answer.setEnabled(false);
			reply.setEnabled(false);
			
			new UFVQuestUtils.AttemptQuestAsync(getActivity(), SeekAndAnswerFragment.this).execute("facebook_id=" + UFVQuestUtils.user.getFacebookId() + 
					"&api_key=" + UFVQuestUtils.user.getApiKey() + "&quest_id=" + UFVQuestUtils.currentQuest.getId() + "&answer=" + answer.getText().toString() + "&quest_type=saa");
				
			
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
					
					answer.setTextColor(Color.parseColor("#00AA00"));
					
	        		break;
	        	case Quest.GOT_IT_WRONG:
	        		feedback.setText("Errou :(");
					feedback.setTextColor(Color.parseColor("#AA0000"));
					answer.setTextColor(Color.parseColor("#AA0000"));
	        		break;
	        	default:
	        		break;
	        }
	        
	       UFVQuestUtils.removeQuest(getActivity());
	    }
	};
	
	public SeekAndAnswerFragment() {	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_seek_and_answer, container, false);
		
		quest = (SeekAndAnswerQuest) UFVQuestUtils.currentQuest;
		
		title = (TextView) rootView.findViewById(R.id.saa_title);
		question = (TextView) rootView.findViewById(R.id.saa_question);
		answer = (EditText) rootView.findViewById(R.id.saa_answer);
		feedback = (TextView) rootView.findViewById(R.id.saa_feedback);
		reply = (Button) rootView.findViewById(R.id.saa_answer_button);
		
		title.setText(quest.getTitle());
		question.setText(quest.getQuestion());
		reply.setOnClickListener(buttonClick);
		
		return rootView;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
}
