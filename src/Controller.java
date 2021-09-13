/**
 * This class facilitates communication between model and view.
 * @author Enes IA
 */

// Imports for testing.
import java.io.FileWriter;
import java.io.IOException;

public final class Controller
{
	private Model model;
	private TextView view;
	private AI agentAI;
	private boolean isGameOn = true;

	public Controller(Model model, TextView view, AI agentAI) {
		this.model = model;
		this.view = view;
		this.agentAI = agentAI;
	}

	// Asks user whether if they wish to restart the game.
	public void restart () {
		isGameOn = true;
		view.restartMsg();
		char restart = InputUtil.readCharFromUser();

		while (restart != 'Y' && restart != 'N'){
			view.invalidInput();
			restart = InputUtil.readCharFromUser();
		}

		if (restart == 'Y') {
			model.resetModel(false);
			startSession();
		} else {
			view.endGame();
		}
	}

	// Asks user for an input and evaluates the game every time called.
	public void playerVSplayer () {
		String player = model.whichPlayer();

		view.playerMsg(player);
		int playerMove = InputUtil.readIntFromUser() - 1; // -1 to make it more user friendly.
		while (!model.isMoveValid(playerMove)  && playerMove != -2 && playerMove != -3) {
			view.invalidInput();
			playerMove = InputUtil.readIntFromUser() - 1;
		}

		// Did player concede or save the game?
		if (playerMove == -2) {
			// Get's the other player since the current player has conceded the game.
			model.setCounter(model.getCounter() + 1);
			player = model.whichPlayer();

			view.outcome(player);
			isGameOn = false;
			return;
		} else if (playerMove == -3) {
			model.saveGame();
			view.gameSaved();
			isGameOn = false;
			return;
		} else {
			model.makeMove(playerMove);
		}
		view.displayBoard(model);

		if (model.isWinner()){
			view.outcome(player);
			isGameOn = false;
			return;
		} else if (model.isBoardFull()) {
			view.outcome("draw");
			isGameOn = false;
			return;
		}
	}

	// Asks user for an input or gets an input from the AI and evaluates the game every time called.
	public void playerVSAI (String AILevel) {
		String player = model.whichPlayer();

		if (player.equals("Player 2")){player = "AI";}

		int playerMove = 0;

		if (player.equals("Player 1")) {
			view.playerMsg(player);
			playerMove = InputUtil.readIntFromUser() - 1; // -1 to make it more user friendly.
		} else {
			switch (AILevel) {
				case "easy":
					playerMove = agentAI.easyAI(model);
					view.AIMove(player, playerMove);
					break;
				case "hard":
					playerMove = agentAI.hardAI(model);
					view.AIMove(player, playerMove);
					break;
			}
		}

		while (!model.isMoveValid(playerMove)  && playerMove != -2 && playerMove != -3) {
			view.invalidInput();
			playerMove = InputUtil.readIntFromUser() - 1;
		}

		if ( playerMove == -2 ) {
			view.outcome("AI");
			isGameOn = false;
			return;
		} else if (playerMove == -3) {
			model.saveGame();
			view.gameSaved();
			isGameOn = false;
			return;
		} else {
			model.makeMove(playerMove);
		}
		view.displayBoard(model);

		if (model.isWinner()){
			view.outcome(player);
			isGameOn = false;
			return;
		} else if (model.isBoardFull()) {
			view.outcome("draw");
			isGameOn = false;
			return;
		}
	}

	// AI plays against the AI mostly for testing purposes.
	public void AIvsAI () {
		String player = model.whichPlayer();

		int playerMove;

		if (player.equals("Player 1")) {
			playerMove = agentAI.hardAI(model);
		} else {
			playerMove = agentAI.hardAI(model);
		}
		view.AIMove(player, playerMove);

		model.makeMove(playerMove);
		view.displayBoard(model);

		if (model.isWinner()){
			view.outcome(player);
			isGameOn = false;
			return;
		} else if (model.isBoardFull()) {
			view.outcome("draw");
			isGameOn = false;
			return;
		}
	}

	public void startSession () {
		// Board dimensions set up.
		int[] vars =  view.variables();
		if (vars[0] == -1 || vars[1] == -1 || vars[2] == -1) {
			model.loadGame();
		} else {
			model.varsSetUp(vars[0], vars[1], vars[2]);
			model.resetModel(false);
		}

		// Game loop.
		int type = view.gameType();
		switch (type) {
			case 1:
				view.displayNewGameMessage();
				view.displayBoard(model);
				while (isGameOn) {
					playerVSplayer();
				}
				break;
			case 2:
				int AILevel = view.AILevel();
				view.displayNewGameMessage();
				view.displayBoard(model);
				switch (AILevel) {
					case 1:
						while (isGameOn) {
							playerVSAI("easy");
						}
						break;
					case 2:
						while (isGameOn) {
							playerVSAI("hard");
						}
						break;
				}
				break;
			case 3:
				view.displayNewGameMessage();
				view.displayBoard(model);
				while (isGameOn) {
					AIvsAI();
				}
				break;
		}

		restart();
	}

	// Random variables for board dimensions and ConnectX value.
	// Used during testing.
	private int[] randomVars () {
		int[] varsRandom = {0, 1, 2};
		for (int i : varsRandom) {
			varsRandom[i] = Math.toIntExact(Math.round(Math.random() * 20 + 4));
		}
		varsRandom[2] = Math.toIntExact(Math.round(Math.random() * ((Math.min(varsRandom[0], varsRandom[1])/2) + 4)));
		return varsRandom;
	}

	// This method plays n number of games between two AIs.
	// I used this to test if the AI and win checks worked properly.
	public void testing () {
		try {
			FileWriter results = new FileWriter("results.txt");
			FileWriter resultsGames = new FileWriter("resultsGames.txt");
			int n = 1000; // How many games to be played.

			// Data about the results.
			int player1Count = 0;
			int player2Count = 0;
			int drawCount = 0;

			int[] vars = {6, 7, 4};
			model.varsSetUp(vars[0], vars[1], vars[2]);

			// Test loop.
			for (int i = 0; i < n; i++) {
				model.resetModel(false);

				while (isGameOn) {
					AIvsAI();
				}
				isGameOn = true;

				// Win counts for players and draws.
				String player;
				if (model.isBoardFull()){
					player = "Draw";
				} else {
					model.setCounter(model.getCounter() + 1);
					player = model.whichPlayer();
				}

				if (player.equals("Player 1")){
					player1Count++;
				} else if (player.equals("Player 2")) {
					player2Count++;
				} else {
					drawCount++;
				}

				// Writes the game result to resultsGames.txt.
				for (String[] row : model.board) {
					for (String chr : row) {
						if (chr.equals(" ")) {
							resultsGames.write(".");
						} else if (chr.equals(model.getPlayer1())){
							resultsGames.write(model.getPlayer1());
						} else if (chr.equals(model.getPlayer2())){
							resultsGames.write(model.getPlayer2());
						} else {
							resultsGames.write("E");    //    for potential errors.
						}
					}
					resultsGames.write("\n");
				}
				resultsGames.write("\n");
			}
			results.write("Player 1: " + player1Count + "\nPlayer 2: " + player2Count + "\nDraw: " + drawCount + "\n");
			resultsGames.close();
			results.close();
		} catch (IOException ignore) {}

	}
}
