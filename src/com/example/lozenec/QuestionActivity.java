package com.example.lozenec;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class QuestionActivity extends Activity implements OnClickListener {

	Question q;
	RadioButton r1;
	RadioButton r2;
	RadioButton r3;
	RadioButton r4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);

		int quesNum = getIntent().getIntExtra("questionNumber", -1);
		q = QuestionBank.questions.get(quesNum);
		TextView text = (TextView) findViewById(R.id.question);
		text.setText(q.qText);
		r1 = (RadioButton) findViewById(R.id.rb1);
		r2 = (RadioButton) findViewById(R.id.rb2);
		r3 = (RadioButton) findViewById(R.id.rb3);
		r4 = (RadioButton) findViewById(R.id.rb4);
		Button btn = (Button) findViewById(R.id.btn_send);
		r1.setText(q.answers[0]);
		r2.setText(q.answers[1]);
		r3.setText(q.answers[2]);
		r4.setText(q.answers[3]);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (isCorrect()) {
			Intent i = new Intent();
			i.putExtra("correct", true);
			setResult(RESULT_OK, i);
			finish();
		} else {
			Intent i = new Intent();
			i.putExtra("correct", false);
			setResult(RESULT_OK, i);
			finish();
		}
	}

	Boolean isCorrect() {
		int marked = -1;
		if (r1.isChecked()) {
			marked = 1;
		}
		if (r2.isChecked()) {
			marked = 2;
		}
		if (r3.isChecked()) {
			marked = 3;
		}
		if (r4.isChecked()) {
			marked = 4;
		}
		if (marked == q.correctAns) {
			return true;
		}
		return false;
	}

}
