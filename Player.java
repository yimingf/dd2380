import java.lang.Thread.State;
import java.util.*;

public class Player {
    private int OTHER_PLAYER_MARK, CURRENT_PLAYER_MARK, MAX_DEPTH = 3, INIT_ALPHA = Integer.MIN_VALUE, INIT_BETA = Integer.MAX_VALUE;
    
    private static int[][] perm = {{0, 1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10, 11}, {12, 13, 14, 15}, {16, 17, 18, 19}, {20, 21, 22, 23}, {24, 25, 26, 27}, {28, 29, 30, 31}, {32, 33, 34, 35}, {36, 37, 38, 39}, {40, 41, 42, 43}, {44, 45, 46, 47}, {48, 49, 50, 51}, {52, 53, 54, 55}, {56, 57, 58, 59}, {60, 61, 62, 63}, {0, 4, 8, 12}, {1, 5, 9, 13}, {2, 6, 10, 14}, {3, 7, 11, 15}, {16, 20, 24, 28}, {17, 21, 25, 29}, {18, 22, 26, 30}, {19, 23, 27, 31}, {32, 36, 40, 44}, {33, 37, 41, 45}, {34, 38, 42, 46}, {35, 39, 43, 47}, {48, 52, 56, 60}, {49, 53, 57, 61}, {50, 54, 58, 62}, {51, 55, 59, 63}, {0, 16, 32, 48}, {1, 17, 33, 49}, {2, 18, 34, 50}, {3, 19, 35, 51}, {4, 20, 36, 52}, {5, 21, 37, 53}, {6, 22, 38, 54}, {7, 23, 39, 55}, {8, 24, 40, 56}, {9, 25, 41, 57}, {10, 26, 42, 58}, {11, 27, 43, 59}, {12, 48, 44, 60}, {13, 49, 45, 61}, {14, 50, 46, 62}, {15, 51, 47, 63}, {0, 5, 10, 15}, {16, 21, 26, 31}, {32, 37, 42, 47}, {48, 53, 58, 63}, {3, 6, 9, 12}, {19, 22, 25, 28}, {35, 38, 41, 44}, {51, 54, 57, 60}, {0, 17, 34, 51}, {4, 21, 38, 55}, {8, 25, 42, 59}, {12, 29, 46, 63}, {3, 18, 33, 48}, {7, 22, 37, 52}, {11, 26, 41, 56}, {15, 30, 45, 60}, {0, 20, 40, 60}, {1, 21, 41, 61}, {2, 22, 42, 62}, {3, 23, 43, 63}, {12, 24, 36, 48}, {13, 25, 37, 49}, {14, 26, 38, 50}, {15, 27, 39, 51}, {0, 21, 42, 63}, {3, 22, 41, 60}, {12, 25, 38, 51}, {15, 26, 37, 48}};

    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);
        // Initialization
        OTHER_PLAYER_MARK = gameState.getNextPlayer();
        CURRENT_PLAYER_MARK = OTHER_PLAYER_MARK == Constants.CELL_O ? Constants.CELL_X : Constants.CELL_O;
        // find best state
        int counter = 0, alpha = Integer.MIN_VALUE, tmp, best_state = 0;
        for (GameState  child : nextStates) {
            tmp = alphabeta(child, deadline, MAX_DEPTH, alpha, INIT_BETA, OTHER_PLAYER_MARK);
            if (tmp > alpha) {
                alpha = tmp;
                best_state = counter;
            }
            counter++;
        }     
        return nextStates.elementAt(best_state);
    }    
    
    // as given in the assignment description
    private int alphabeta(final GameState state, final Deadline finish, int depthLeft, int alpha, int beta, int player) {
        // Initialization
        int tmp,  v;
        depthLeft--;
        Vector<GameState> nextStates = new Vector<GameState>();
        state.findPossibleMoves(nextStates);
        
        // alpha beta stuff 
        if(finish.timeUntil() < 0)
            return 0;
        else if (state.isEOG()){
            if (player == CURRENT_PLAYER_MARK) 
                return state.isOWin() == true && player == Constants.CELL_O ? Integer.MAX_VALUE : state.isXWin() && player == Constants.CELL_O ? Integer.MIN_VALUE: 0;
            else
                return state.isOWin() == true && player == Constants.CELL_O ? Integer.MIN_VALUE : state.isXWin() && player == Constants.CELL_O ? Integer.MAX_VALUE: 0;
        } else if (depthLeft == 0 || nextStates.size() == 0)
            return evaluate(state, player); // Else we can search deeper
        else if (player == CURRENT_PLAYER_MARK) { // if players turn
            v = Integer.MIN_VALUE;
            for(GameState gs : nextStates){
                tmp = alphabeta(gs, finish, depthLeft, alpha, beta, OTHER_PLAYER_MARK);
                v = tmp > v ? tmp : v;
                alpha = alpha > v ? alpha : v;
                if(beta <= alpha)
                    break; // beta prune
            }
        } else { // if opponents turn
            v = Integer.MAX_VALUE;
            for(GameState gs : nextStates){
                tmp = alphabeta(gs, finish, depthLeft, alpha, beta, CURRENT_PLAYER_MARK);
                v = tmp < v ? tmp : v;
                beta = beta < v ? beta : v;
                if (beta <= alpha) 
                    break; // alpha prune
            }
        }
        return v;
    }

    private int evaluate(final GameState s, int p) {
        int Marks = 0;
        for (int i = 0; i < perm.length; i++) { // diagonal lines
            int score = 0;
            for (int j = 0; j < 4; j++) {
                if (s.at(perm[i][j]) == p) {
                    if (score < 0) {
                        score = 0;
                        break;
                    } else {
                        score++;
                    }
                } else if (s.at(perm[i][j]) != Constants.CELL_EMPTY) {
                    if (score > 0) {
                        score = 0;
                        break;
                    } else {
                        score--;
                    }
                } // if
            } // for 2
            Marks += score == 0 ? score : (score > 0 ? 1 : -2) * 76 ^ score;
        } // for 1

        return Marks;
    }
}