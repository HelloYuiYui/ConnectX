/**
 * This class deals with the game board and interactions with the board.
 * @author Enes IA
 */

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public final class Model
{
	// ===========================================================================
	// ================================ CONSTANTS ================================
	// ===========================================================================
	// The most common version of Connect Four has 7 rows and 6 columns.
	public static final int DEFAULT_NR_ROWS = 7;
	public static final int DEFAULT_NR_COLS = 6;
	public static final String PLAYER_1_VAL = "0";
	public static final String PLAYER_2_VAL = "X";

	// ========================================================================
	// ================================ FIELDS ================================
	// ========================================================================
	// The size of the board.

	private int nrRows;
	private int nrCols;
	private int connectX;    // Number of pieces needed consecutively to win the game.
	private int counter = 0;
	public String[][] board;

	// =============================================================================
	// ================================ CONSTRUCTOR ================================
	// =============================================================================

	public Model()	{
		// Initialise the board size to its default values.
		nrRows = DEFAULT_NR_ROWS;
		nrCols = DEFAULT_NR_COLS;
		board = new String[nrRows][nrCols];
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[i].length; j++){
				board[i][j] = " ";
			}
		}
	}
	
	// ====================================================================================
	// ================================ MODEL INTERACTIONS ================================
	// ====================================================================================

	public void varsSetUp (int rows, int cols, int X) {
		nrRows = rows;
		nrCols = cols;
		connectX = X;
		board = new String[rows][cols];
	}

	public void resetModel (boolean forLoad) {
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[i].length; j++){
				board[i][j] = " ";
			}
		}
		if (!forLoad) {
			counter = 0;
		}
	}

	public boolean isRowEmpty (String[] row) {
		for (String elem : row){
			if (!elem.equals(" ")){
				return false;
			}
		}
		return true;
	}

	// Checks all non-empty rows to see if there is a winner.
	public boolean checkHorizontal () {
		for (int i = board.length - 1; i >= 0; i--) {
			if (isRowEmpty(board[i])) {break;}
			for (int j = 0; j < board[i].length - connectX + 1; j++) {
				boolean streak = false;
				int k = 0;
				while (k < connectX - 1) {
					String elem = board[i][j+k];
					String nextElem = board[i][j+k+1];
					if (elem.equals(" ")) {
						streak = false;
						break;
					} else if (elem.equals(nextElem)){
						streak = true;
						k++;
					} else {
						streak = false;
						break;
					}
				}
				if (streak) {
					return true;
				}
			}
		}
		return false;
	}

	// Checks all non-empty columns to see if there is a winner.
	public boolean checkVertical () {
		for (int i = board.length - 1; i >= connectX - 1; i--) {
			for (int j = 0; j < board[i].length; j++) {
				boolean streak = false;
				int k = 0;
				while (k < connectX - 1) {
					String elem = board[i-k][j];
					String nextElem = board[i-k-1][j];
					if (elem.equals(" ")) {
						streak = false;
						break;
					} else if (elem.equals(nextElem)){
						streak = true;
						k++;
					} else {
						streak = false;
						break;
					}
				}
				if (streak) {
					return true;
				}
			}
		}
		return false;
	}

	// Diagonal wins, from left bottom right upwards.
	public boolean checkDiagonalUp () {
		for (int i = board.length - 1; i >= connectX - 1; i--) {
			for (int j = 0; j < board[i].length - connectX + 1; j++) {
				boolean streak = false;
				int k = 0;
				while (k < connectX - 1) {
					String elem = board[i-k][j+k];
					String nextElem = board[i-k-1][j+k+1];
					if (elem.equals(" ")) {
						streak = false;
						break;
					} else if (elem.equals(nextElem)){
						streak = true;
						k++;
					} else {
						streak = false;
						break;
					}
				}
				if (streak) {
					return true;
				}
			}
		}
		return false;
	}

	// Diagonal wins, from minimum right bottom left upwards.
	public boolean checkDiagonalDown () {
		for (int i = board.length - 1; i >= connectX - 1; i--) {
			for (int j = connectX - 1; j < board[i].length; j++) {
				boolean streak = false;
				int k = 0;
				while (k < connectX - 1) {
					String elem = board[i-k][j-k];
					String nextElem = board[i-k-1][j-k-1];
					if (elem.equals(" ")) {
						streak = false;
						break;
					} else if (elem.equals(nextElem)){
						streak = true;
						k++;
					} else {
						streak = false;
						break;
					}
				}
				if (streak) {
					return true;
				}
			}
		}
		return false;
	}

	// Is there a winner?
	public boolean isWinner () {
		return ( checkHorizontal() || checkVertical() || checkDiagonalUp() || checkDiagonalDown() );
	}

	public boolean isMoveValid (int move) {
		boolean isValid = false;
		if (move < nrCols && move >= 0) {
			if (board[0][move].equals(" ")){
				isValid = true;
			}
		}
		return isValid;
	}

	// Returns the selected player's tile string.
	public String player (int count) {
		if (count % 2 == 0) {
			return PLAYER_1_VAL;
		} else if (count % 2 == 1) {
			return PLAYER_2_VAL;
		}
		return "?";
	}

	// Return which player's turn it is.
	public String whichPlayer () {
		if (counter % 2 == 0) {
			return ("Player 1");
		} else {
			return ("Player 2");
		}
	}

	public boolean isBoardFull (){
		for (String[] row : board) {
			for (String elem : row) {
				if (elem.equals(" ")) {
					return false;
				}
			}
		}
		return true;
	}

	public void makeMove(int move) {
		for (int i = 0; i < board.length; i++) {
			if ( !(board[i][move]).equals(" ") ) {
				board[i - 1][move] = player(counter);
				counter++;
				break;
			} else if ( i == board.length - 1 ) {
				board[i][move] = player(counter);
				counter++;
				break;
			}
		}
	}

	// Exactly like makeMove but doesn't not affect the counter.
	public void testMove(int move) {
		for (int i = 0; i < board.length; i++) {
			if ( !(board[i][move]).equals(" ") ) {
				board[i - 1][move] = player(counter);
				break;
			} else if (i == board.length - 1) {
				board[i][move] = player(counter);
				break;
			}
		}
	}

	public void undoMove(int move) {
		for (String[] row : board) {
			if ( !(row[move]).equals(" ") ) {
				row[move] = " ";
				break;
			}
		}
	}

	public void saveGame () {
		try {
			FileWriter save = new FileWriter("save.txt");
			// Saves connectX and counter to properly load the game.
			save.write(connectX + "\n" + counter + "\n");

			for (int i = 0; i < board.length; i++) {
				for (String chr : board[i]) {
					if (chr.equals(" ")) {
						save.write(".");
					} else if (chr.equals(getPlayer1())) {
						save.write(getPlayer1());
					} else if (chr.equals(getPlayer2())) {
						save.write(getPlayer2());
					} else {
						save.write("E");
					}
				}

				if (i != board.length - 1) {
					save.write("\n");
				}
			}

			save.close();
		} catch (IOException ignored) { }
	}

	public void setDimensionsFromSave () {
		try {
			FileReader save = new FileReader("save.txt");
			Scanner getDimensions = new Scanner(save);
			int rowCounter = 0;
			int colCounter = 0;

			while (getDimensions.hasNextLine()) {
				String row = getDimensions.nextLine();
				if (rowCounter == 0) {
					connectX = Integer.parseInt(row);
				} else if (rowCounter == 1) {
					counter = Integer.parseInt(row);
				} else if (rowCounter == 2) {
					for (int i = 0; i < row.length(); i++) {
						colCounter++;
					}
				}
				rowCounter++;
			}

			getDimensions.close();

			varsSetUp(rowCounter-2, colCounter, connectX);
			resetModel(true);
		} catch (IOException ignored) { }
	}

	public void loadGame () {
		setDimensionsFromSave();

		try {
			FileReader save = new FileReader("save.txt");
			Scanner contents = new Scanner(save);
			int i = 0;
			contents.nextLine();
			contents.nextLine();
			while (contents.hasNextLine()) {
				String row = contents.nextLine();
				for (int j = 0; j < row.length(); j++) {
					char chr = row.charAt(j);
					if (chr == '.') {
						board[i][j] = " ";
					} else if (chr == getPlayer1().charAt(0)) {
						board[i][j] = getPlayer1();
					} else if (chr == getPlayer2().charAt(0)) {
						board[i][j] = getPlayer2();
					}  else {
						board[i][j] = "E";
					}
				}
				i++;
			}
			contents.close();
			save.close();
		} catch (IOException ignored) { }
	}

	public void setCounter(int c) {counter = c;}

	// =========================================================================
	// ================================ GETTERS ================================
	// =========================================================================

	public int getNrRows() {return nrRows;}

	public int getNrCols() {return nrCols;}

	public String getPlayer1() {return PLAYER_1_VAL;}

	public String getPlayer2() {return PLAYER_2_VAL;}

	public int getCounter() {return counter;}
}
