package com.cbase.game;

import java.util.Scanner;

public class Player implements Play {

	int data = 0;
	boolean playing = false;
	String name="";
	
	public int play(Scanner scanner) {
		System.out.print("Select 1, 2, or 3 for Rock, Paper, Scissors for player " + this.name + " > ");
        this.data  = scanner.nextInt();
        return this.data;
	}
	

    public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public boolean isPlaying() {
		return playing;
	}


	public void setPlaying(boolean playing) {
		this.playing = playing;
	}


	public int getData() {
		return data;
	}


	public void setData(int data) {
		this.data = data;
	}
}
