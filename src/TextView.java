/**
 * This class deals with user interactions.
 * @author Enes IA
 */

public final class TextView
{
	public TextView() {

	}

	public final void displayNewGameMessage() {
		System.out.println("---- NEW GAME STARTED ----");
		System.out.println("You cannot select full columns");
		System.out.println("Type -1 to give up, -2 to save the game.");
		System.out.println("Have fun! \n");
	}

	public int gameType () {
		System.out.println("Select game type:\n-- Player vs Player: 1\n-- Player vs AI: 2\n-- AI vs AI: 3");
		int selection = InputUtil.readIntFromUser();
		while (!(selection == 1 || selection == 2 || selection == 3)){
			invalidInput();
			selection = InputUtil.readIntFromUser();
		}
		return selection;
	}

	public int AILevel () {
		System.out.println("Select AI Level:\n-- Easy: 1\n-- Hard: 2");
		int selection = InputUtil.readIntFromUser();
		while (!(selection == 1 || selection == 2)){
			invalidInput();
			selection = InputUtil.readIntFromUser();
		}
		return selection;
	}

	public int[] variables () {
		System.out.println("Rows and columns should be greater than 3.\nX should be less than both of them.\n-- Default dimensions: 0\n-- Load game: 1");
		System.out.print("Rows: ");
		int rows = InputUtil.readIntFromUser();
		if (rows == 0) {
			return new int[] {6, 7, 4};
		} else if (rows == 1) {
			return new int[] {-1,-1,-1};
		}
		System.out.print("Cols: ");
		int cols = InputUtil.readIntFromUser();
		System.out.print("X: ");
		int x = InputUtil.readIntFromUser();

		// Checks if the inputs are valid
		while (rows <= 3 || cols <= 3 || x >= cols || x >= rows){
			invalidInput();
			System.out.print("\nRows: ");
			rows = InputUtil.readIntFromUser();
			if (rows == 0) {
				return new int[] {6, 7, 4};
			} else if (rows == 1) {
				return new int[] {-1,-1,-1};
			}
			System.out.print("Cols: ");
			cols = InputUtil.readIntFromUser();
			System.out.print("X: ");
			x = InputUtil.readIntFromUser();
		}

		return new int[] {rows, cols, x};
	}

	public final void invalidInput () {
		System.out.print("Invalid input: ");
	}

	public final void gameSaved () { System.out.println("Game saved"); 	}

	public final void playerMsg (String player) {
		System.out.print(player + ": ");
	}

	public final void AIMove (String player, int move) {
		System.out.println(player + ": " + (move + 1));
	}

	public final void restartMsg () {
		System.out.print("\nDo you want to restart the game? (Y | N): ");
	}

	public final void displayBoard(Model model) {
		int nrCols = model.getNrCols();

		String bottomLine = "-".repeat(4 * nrCols + 1);
		
		// A StringBuilder is used to assemble longer Strings more efficiently.
		StringBuilder sb = new StringBuilder();

		// Builds the top line showing columns.
		for (int i = 0 ; i < nrCols ; i++){
			sb.append("__" + (i+1) + "_");
		}
		sb.append("_\n");

		// For representing the whole board with pieces in it.
		for (String[] row : model.board){
			for (String elem : row){
				if (elem.equals(Model.PLAYER_1_VAL)) {
					// sb.append("|\u001B[32m " + elem + "\u001B[0m ");    \\ For the colour-coded board.
					sb.append("| " + elem + " ");
				} else if (elem.equals(Model.PLAYER_2_VAL)){
					// sb.append("|\u001B[31m " + elem + "\u001B[0m ");    \\ For the colour-coded board.
					sb.append("| " + elem + " ");
				} else {
					sb.append("| " + elem + " ");
				}
			}
			sb.append("|\n");
		}
		sb.append(bottomLine);
		sb.append("\n");

		System.out.println(sb);
	}

	public final void outcome(String input) {
		if (input.equals("draw")) {
			System.out.print("Draw, the board is full.\n");
		} else {
			System.out.print("The game has ended. " + input + " has won.\n");
		}
	}

	public final void endGame () {
		System.out.print("Thanks for playing! Hope you had fun.\n");
	}
}
