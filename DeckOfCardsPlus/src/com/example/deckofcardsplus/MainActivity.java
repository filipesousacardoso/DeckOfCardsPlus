package com.example.deckofcardsplus;

import java.util.ArrayList;
import java.util.Collections;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ArrayList<Card> deckofcards=new ArrayList<Card>();
	private String[] number=new String[] {"2","3","4","5","6","7","8","9","10","Q","J","K","A","Joker"};
	private String[] naipe=new String[] {"spade","heart","diamond","clove","joker"};
	private GestureDetector mGestureDetector;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;	

	LinearLayout menu;

	ImageView shadow;
	RelativeLayout card;

	ImageView cardnaiper;
	ImageView cardnaipel;
	ImageView cardnaipec;

	TextView cardnumberl;
	TextView cardnumberr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		setupGestureDetector();

		menu= (LinearLayout) findViewById(R.id.menu);
		TextView newdeck= (TextView) findViewById(R.id.new_deck);
		TextView shuffle= (TextView) findViewById(R.id.shuffle);
		TextView settings= (TextView) findViewById(R.id.settings);
		shadow= (ImageView) findViewById(R.id.shadow);
		card= (RelativeLayout) findViewById(R.id.card);
		cardnaiper= (ImageView) findViewById(R.id.cardnaiper);
		cardnaipel= (ImageView) findViewById(R.id.cardnaipel);
		cardnaipec= (ImageView) findViewById(R.id.cardnaipec);
		cardnumberl= (TextView) findViewById(R.id.cardnumberl);
		cardnumberr= (TextView) findViewById(R.id.cardnumberr);

		loadSettings();

		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				menu.setVisibility(View.INVISIBLE);
			}
		});

		newdeck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resetDeck();
			}
		});

		shuffle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				shuffleDeck();
			}
		});

		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, SettingsActivity.class));
			}
		});
	}
	protected void shuffleDeck() {
		Collections.shuffle(this.deckofcards);
		showCardView();
	}
	protected void resetDeck() {
		deckofcards.clear();
		for(String n : naipe){
			for(String c : number){
				deckofcards.add(new Card(c,n));
			}
		}
		showCardView();
		shadow.setVisibility(View.VISIBLE);
		card.setVisibility(View.VISIBLE);
	}
	private void showCardView() {
		Card card=deckofcards.get(0);
		cardnumberl.setText(card.getNumber());
		cardnumberr.setText(card.getNumber());
		if(card.getNaipe().equals("heart")){
			cardnaiper.setImageResource(R.drawable.heart);
			cardnaipel.setImageResource(R.drawable.heart);
			cardnaipec.setImageResource(R.drawable.heartb);
		}else if(card.getNaipe().equals("diamond")){
			cardnaiper.setImageResource(R.drawable.diamond);
			cardnaipel.setImageResource(R.drawable.diamond);
			cardnaipec.setImageResource(R.drawable.diamondb);
		}else if(card.getNaipe().equals("spade")){
			cardnaiper.setImageResource(R.drawable.spade);
			cardnaipel.setImageResource(R.drawable.spade);
			cardnaipec.setImageResource(R.drawable.spadeb);
		}else if(card.getNaipe().equals("joker")){
			cardnaiper.setImageResource(R.drawable.joker);
			cardnaipel.setImageResource(R.drawable.joker);
			cardnaipec.setImageResource(R.drawable.jokerb);
		}
	}

	private void loadSettings(){
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		int size = prefs.getInt("deckofcards_size", 0);

		if(size==0){
			resetDeck();
		}else{
			deckofcards.clear();
			for(int i=0;i<size;i=i+2)
				deckofcards.add(new Card(
						prefs.getString("deckofcards_" + i, null),
						prefs.getString("deckofcards_" + i+1, null)));
		}
		showCardView();
	}

	private void saveSettings(){
		editor = prefs.edit();

		cleanSavedDeck();
		editor.putInt("deckofcards_size", deckofcards.size()*2);

		int count=0;
		for(int i=0;i<deckofcards.size()*2;i=i+2){
			editor.putString("deckofcards_" + i, deckofcards.get(count).getNumber());
			editor.putString("deckofcards_" + i+1, deckofcards.get(count).getNaipe());
			count++;
		}
		editor.commit(); 
	}

	private void cleanSavedDeck() {

		for(int i=0;i<prefs.getInt("deckofcards_size", 0);i++){
			editor.remove("deckofcards_" + i);
		}
		editor.remove("deckofcards_size");
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void setupGestureDetector() {
		mGestureDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {

			@Override
			public boolean onFling(MotionEvent event1, MotionEvent event2,
					float velocityX, float velocityY) {
				if(deckofcards.size()==1){
					shadow.setVisibility(View.INVISIBLE);
					card.setVisibility(View.INVISIBLE);
					menu.setVisibility(View.VISIBLE);
				}else{
					deckofcards.remove(0);
					showCardView();}
				return false;

			}

			@Override
			public boolean onSingleTapConfirmed(MotionEvent event) {
				if(deckofcards.size()==1){
					shadow.setVisibility(View.INVISIBLE);
					card.setVisibility(View.INVISIBLE);
					menu.setVisibility(View.VISIBLE);
				}else{
					deckofcards.remove(0);
					showCardView();}
				return false;
			}

			@Override
			public void onLongPress(MotionEvent event) {
				menu.setVisibility(View.VISIBLE);
			}
		});
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return false;

	}

	@Override
	protected void onDestroy(){
		saveSettings();
		super.onDestroy();
	}
	@Override
	protected void onPause(){
		saveSettings();
		super.onPause();
	}
}
