package com.example.lozenec;

import com.google.android.gms.maps.model.LatLng;

public class Question {
	
	String qText;
	String[] answers = new String[4];
	int correctAns;
	LatLng position;
	public Question(String questionText, String ans1,String ans2,String ans3,
			String ans4, int correct, double latitude, double longitude){
		this.qText = questionText;
		this.answers[0] = ans1;
		this.answers[1] = ans2;
		this.answers[2] = ans3;
		this.answers[3] = ans4;
		this.correctAns = correct;
		this.position = new LatLng(latitude, longitude);
	}
}
