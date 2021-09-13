/**
 * This class handles the NPC moves for ConnectX.
 * @author Enes IA
 */

public final class AI {

    public AI () {

    }

    // Returns an array of valid moves for AI to play.
    private boolean[] validMoves (Model model){
        int nrCols = model.getNrCols();
        boolean[] validMoves = new boolean[nrCols];
        for (int move = 0; move < model.board[0].length; move++) {
            if (model.board[0][move].equals(" ")){
                validMoves[move] = true;
            }
        }
        return validMoves;
    }

    // This AI tries to be more strategic by trying to make the winning move,
    // blocking the player's winning move, or make a move that might make a
    // winning move possible next round.
    public int hardAI (Model model) {

        // Picks the most beneficial move.
        int finalMovePlayer = finalMove(model);
        model.setCounter(model.getCounter() + 1);
        int finalMoveOpponent = finalMove(model);
        model.setCounter(model.getCounter() - 1);
        int optimalMove = optimalMove(model);
        if (finalMovePlayer >= 0) {
            return finalMovePlayer;
        } else if (finalMoveOpponent >= 0) {
            return finalMoveOpponent;
        } else if (optimalMove >= 0) {
            return optimalMove;
        }

        // Random value if no strategic move is found.
        int move = Math.toIntExact(Math.round((model.getNrCols() - 1) * Math.random()));
        while (!model.isMoveValid(move)) {
            move = Math.toIntExact(Math.round((model.getNrCols() - 1) * Math.random()));
        }

        return move;
    }

    // This AI only blocks opponent's moves.
    public int easyAI (Model model){

        model.setCounter(model.getCounter() + 1);
        int finalMoveOpponent = finalMove(model);
        model.setCounter(model.getCounter() - 1);
        if (finalMoveOpponent >= 0) {
            return finalMoveOpponent;
        }

        int move = Math.toIntExact(Math.round((model.getNrCols() - 1) * Math.random()));

        while (!validMoves(model)[move]){
            move = Math.toIntExact(Math.round((model.getNrCols() - 1) * Math.random()));
        }

        return move;
    }

    // Checks if there is a winning move in the game for the selected player.
    private int finalMove(Model model){
        for (int x = 0; x < model.getNrCols(); x++) {
            if (model.isMoveValid(x)) {
                    model.testMove(x);
                    if(model.isWinner()){
                        model.undoMove(x);
                        return x;
                    }
                    model.undoMove(x);
                }
            }
        return -1;
    }

    // Checks if there is a move that can guarantee a win next round.
    // This doesn't always work for horizontal and diagonal double win moves.
    public int optimalMove (Model model) {
        int move = -1;
        for (int i = 1; i < model.getNrCols() - 1; i++) {
            boolean up = false;
            boolean left = false;
            boolean right = false;
            if (model.isMoveValid(i)) {
                model.testMove(i);
                if (model.isMoveValid(i)) {
                    model.testMove(i);
                    if(model.isWinner()){
                        up = true;
                    }
                    model.undoMove(i);
                }
                if (model.isMoveValid(i-1)) {
                    model.testMove(i-1);
                    if(model.isWinner()){
                        left = true;
                    }
                    model.undoMove(i-1);
                }
                if (model.isMoveValid(i+1)) {
                    model.testMove(i+1);
                    if(model.isWinner()){
                        right = true;
                    }
                    model.undoMove(i+1);
                }
                model.undoMove(i);
            }

            // Looks for the optimal move.
            if ((up && right) || (up && left) || (left && right)) {
                return i;
            } else if (up || right || left) {
                if (left) {
                    move = i - 1;
                } else if (right) {
                    move = i + 1;
                } else {
                    move = i;
                }
            }

        }
        return move;
    }

}
