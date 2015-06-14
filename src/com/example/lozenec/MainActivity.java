package com.example.lozenec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnMarkerClickListener,
		LocationListener {

	private static final double MINIMUN_DISTANCE = 40;
	int score = 0;
	GoogleMap map;
	Marker[] markers;
	int visible = 3;
	int numOfQuestions;
	LocationManager lm;
	Location currentLocation;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rezult);

		tv = (TextView) findViewById(R.id.textView1);
		tv.setText("точки: " + String.valueOf(0));
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		loadQuestions();
		numOfQuestions = QuestionBank.questions.size();
		markers = new Marker[numOfQuestions];
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		for (int i = 0; i < numOfQuestions; i++) {
			Marker m = map.addMarker(new MarkerOptions()
					.position(QuestionBank.questions.get(i).position)
					.snippet(i + "").visible(false));
			markers[i] = m;
			if (i < visible) {
				m.setVisible(true);
			}
		}
		map.setOnMarkerClickListener(this);
		CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(lm
				.getLastKnownLocation(lm.NETWORK_PROVIDER).getLatitude(), lm
				.getLastKnownLocation(lm.NETWORK_PROVIDER).getLongitude()), 12);
		map.animateCamera(cu);
		currentLocation = lm.getLastKnownLocation(lm.NETWORK_PROVIDER);
	}

	private void loadQuestions() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(this
					.getAssets().open("questions.txt")));
			String line;

			while ((line = br.readLine()) != null) {
				String[] var = line.split(", ");
				QuestionBank.questions.add(new Question(var[0], var[1], var[2],
						var[3], var[4], Integer.parseInt(var[5]), Double
								.parseDouble(var[6]), Double
								.parseDouble(var[7])));

			}

		} catch (IOException e) {
			// You'll need to add proper error handling here
		}

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		Intent i = new Intent(this, QuestionActivity.class);
		String quesNum = marker.getSnippet();
		int num = Integer.parseInt(quesNum);

		Location questionLocation = new Location("name");
		questionLocation
				.setLatitude(QuestionBank.questions.get(num).position.latitude);
		questionLocation
				.setLongitude(QuestionBank.questions.get(num).position.longitude);
		double distance = currentLocation.distanceTo(questionLocation);

		if (distance < MINIMUN_DISTANCE) {
			i.putExtra("questionNumber", num);
			startActivityForResult(i, num);
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data.getBooleanExtra("correct", false)) {
				score++;

				tv.setText("точки: " + String.valueOf(score));
				markers[requestCode].setVisible(false);
				if (visible < numOfQuestions) {
					markers[visible].setVisible(true);
					visible++;
					Toast t1 = Toast.makeText(this, "Верен отговор",
							Toast.LENGTH_LONG);
					t1.show();
				}
			}

			else {
				Toast t2 = Toast.makeText(this, "Грешен отговор",
						Toast.LENGTH_LONG);
				t2.show();
				markers[requestCode].setVisible(false);
			}
		}

	}

	@Override
	public void onLocationChanged(Location location) {
		currentLocation = location;

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
