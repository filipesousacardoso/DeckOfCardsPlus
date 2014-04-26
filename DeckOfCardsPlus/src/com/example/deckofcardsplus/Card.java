package com.example.deckofcardsplus;

public class Card {

	String number;
	String naipe;

	public Card(String number, String naipe){
		this.number=number;
		this.naipe=naipe;

	}

	public void setNumber(String number){
		this.number=number;
	}

	public void setNaipe(String naipe){
		this.naipe=naipe;
	}

	public String getNumber(){
		return number;
	}

	public String getNaipe(){
		return naipe;
	}

}


