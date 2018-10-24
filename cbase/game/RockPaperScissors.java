package com.cbase.game;

import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;

public class RockPaperScissors implements Runnable {
	Map<String, Player> playerMap = new HashMap<String, Player>();
	
	private static final String tie = "Game's TIE";
	private static final String p1win = "Player 1 wins the game";
	private static final String p2win = "Player 2 wins the game";

	public void run() {
		try {
			String name = Thread.currentThread().getName();
			Player player1 = playerMap.get("P1");
			Player player2 = playerMap.get("P2");
			
			//check if the thread's judge thread so it might need to wait if the 2 players not entered the input
			if (name.equalsIgnoreCase("judge")) {
				if (((player1.data == 0) || (player2.data == 0))) {	//make sure the input data available from 2 players
					synchronized(this) {
					try {
						wait();	//judge thread is going to wait until notify it.
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					}
				}
			}

			//invoke play to get the input from the palyer
			play();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * 
	 * This method to start the play and gets the input from the player
	 * @param  none
	 * 
	 */

	public void play() {
		
		synchronized (playerMap) {	//lock the playerMap object
			Player player1 = playerMap.get("P1");
			Player player2 = playerMap.get("P2");
			
			if (playerMap.size() > 0) {
				Scanner scanner = new Scanner(System.in);
				if (player1 != null && player1.data == 0) {
					int data = player1.play(scanner);
					player1.setData(data);
				} else if (player2 != null && player2.data == 0) {
					int data = player2.play(scanner);
					player2.setData(data);
				}
			

				if ((player1.data != 0) && (player2.data != 0)) {
					if (Thread.currentThread().getName().equalsIgnoreCase("judge")) {
						whoWins(player1, player2);
					}
					synchronized(this) {
						notifyAll();
					}
				}
			
			}
		}
	}

	/**
	 * Check who wins the game, this is dedicated for the judge thread
	 * 
	 * protected for supporting writing unit test case
	 * 
	 * @param player1
	 * @param player2
	 */
	protected void whoWins(Player player1, Player player2) {
		int player1Data = player1.getData();
		int player2Data = player2.getData();

		switch (player1Data) {
		case 1:
			if (player2Data == 1) {
				printResult(tie);
			}
			if (player2Data == 2) {
				printResult(p2win);
			}
			if (player2Data == 3) {
				printResult(p1win);
			}
			break;
		case 2:
			if (player2Data == 2) {
				printResult(tie);
			}
			if (player2Data == 1) {
				printResult(p1win);
			}
			if (player2Data == 3) {
				printResult(p2win);
			}
			break;

		case 3:
			if (player2Data == 3) {
				printResult(tie);
			}
			if (player2Data == 1) {
				printResult(p2win);
			}
			if (player2Data == 2) {
				printResult(p1win);
			}
			break;

		}
	}
	
	/**
	 * just for printing the result
	 * protected for supporting writing unit test case
	 * 
	 * @param result
	 */
	protected void printResult(String result) {
		System.out.println("");
		System.out.println("Declaring Result **** : " + result);
	}

}

// Main Class
class Game {
	
	/**
	 * main method to kick of the game with threads
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		RockPaperScissors thread = new RockPaperScissors();
		Player player = new Player();
		
		int n = 2; // Number of threads
		for (int i = 0; i < 2; i++) {
			
			//creating player instance to set values and get into the game
			player = new Player();
			player.setPlaying(true);
			
			//player 1
			if (i == 1) {
				player.setName("Player1");
				thread.playerMap.put("P1", player);
			}
			else { //player 2
				player.setName("Player2");
				thread.playerMap.put("P2", player);
			}
			
			//start the thread to run
			Thread object = new Thread(thread);
			object.start();
		}
		
		//judge thread
		Thread object = new Thread(thread, "judge");
		object.start();
	}
}
