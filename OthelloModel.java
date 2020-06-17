import javax.swing.*;
import java.awt.*;

//Joshua Horowitz Data Structures II
public class OthelloModel extends JFrame implements OthelloModelInterface{
    enum State  {EMPTY, WHITE, BLACK}
    private State player = State.BLACK;
    private boolean playerBlack = true;
    private JButton[][] board = new JButton[9][9];
    OthelloModel() {
        //add a buffer for borders and index checks
        JFrame othelloFrame = new JFrame("Othello");
        othelloFrame.setSize(600, 600);
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                int row = i;
                int col = j;
                board[i][j] = new JButton();
                board[i][j].setBackground(Color.GREEN);
                board[i][j].setText(State.EMPTY.toString());
                othelloFrame.add(board[i][j]);
                board[i][j].addActionListener(e -> {
                    if(playerBlack) {
                        makeMove(row, col, getPlayerState());
                        setPlayerState(State.WHITE);
                        playerBlack = false;

                    }
                    else {
                        makeMove(row, col, getPlayerState());
                        setPlayerState(State.BLACK);
                        playerBlack = true;

                    }
                });
            }
        }
        board[3][3].setText(State.WHITE.toString());
        board[4][4].setText(State.WHITE.toString());
        board[3][4].setText(State.BLACK.toString());
        board[4][3].setText(State.BLACK.toString());
        othelloFrame.setLayout(new GridLayout(9,9));
        othelloFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        othelloFrame.setVisible(true);
    }

    private void setPlayerState(State state) {
        this.player = state;
    }

    private State getPlayerState() {
        return this.player;
    }


    @Override
    public boolean makeMove(int row, int col, Object state) {
        if (row == 0)
            row++;
        if (col == 0)
            col++;
        if (row >= 0 && col >= 0 &&  row <= 8 && col <= 8) { //if inside of board
            if (board[row][col].getText().equals(State.EMPTY.toString())) { //if not on pre-existing piece
                if (hasANeighbor(row, col)) { //if has a neighbor
                    if (willFlip(row, col, state)) { //if it will flip pieces of opposite color
                        board[row][col].setText(state.toString());
                        if(boardFinished()) {
                            decideWinner();
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean willFlip(int row, int col, Object state) {
        return hasNeighboringOpponentToFlip(row, col, state);
    }

    @Override
    public boolean hasNeighboringOpponentToFlip(int row, int col, Object state) {
        boolean hasFlipped = false;
        String oppositeState;
        if(state.equals(State.BLACK)) {
            oppositeState = State.WHITE.toString();
        }
        else {
            oppositeState = State.BLACK.toString();
        }
        for (int a = (row - 1); a <= (row + 1); a++) {
            for (int b = (col - 1); b <= (col + 1); b++) {
                if(board[a][b].getText().equals(oppositeState)) { //if neighbor is of opposite color
                    if(checkForEnd(a, b, row, col, oppositeState, state)) { //if it has a piece of the same color on the other side of the row
                        flip(a, b, row, col, oppositeState, state); //flips the pieces
                        hasFlipped = true; //if i returned true here, it would stop after it found the first path
                    }                               //returning it below ensures that it will find every possible path to flip
                }
            }
        }
        return hasFlipped;
    }

    @Override
    public boolean checkForEnd(int destinationRow, int destinationCol, int sourceRow, int sourceCol, Object oppositeState, Object currentState) { //recursively goes down a path it found an opposite colored piece on
        int diffRow = destinationRow - sourceRow; //diffRow will be the destination (say, 5) - the source (say, 4) = 1
        int diffCol = destinationCol - sourceCol; //diffCol will be the destination (say, 4) - the source (say, 5) = -1
        int newRow = (diffRow) + (destinationRow); //newRow will be the row in the DIRECTION of the opposite colored piece. So it will be the opposite piece + diffRow
        int newCol = (diffCol) + (destinationCol);
        if(board[newRow][newCol].getText().equals(oppositeState)) {
            checkForEnd(newRow, newCol, destinationRow, destinationCol, oppositeState, currentState);
        }
        return !board[newRow][newCol].getText().equals(State.EMPTY.toString());
    }

    @Override
    public boolean hasANeighbor(int row, int col) { //This is my code from the Game Of Life assignment last semester, lightly edited
        int counter = 0;
        for (int a = (row - 1); a <= (row + 1); a++) {
            for (int b = (col - 1); b <= (col + 1); b++) {
                if (!(board[a][b].getText().equals(State.EMPTY.toString()))) {
                    counter++;
                }
            }
        }
        return counter > 0;
    }


    @Override
    public void flip(int destinationRow, int destinationCol, int sourceRow, int sourceCol, Object oppositeState, Object currentState) { //recursively goes down a path it found an opposite colored piece on, and flips them
        int diffRow = destinationRow - sourceRow;
        int diffCol = destinationCol - sourceCol;
        int newRow = diffRow + destinationRow;
        int newCol = diffCol + destinationCol;
        if(board[destinationRow][destinationCol].getText().equals(oppositeState)) {
            board[destinationRow][destinationCol].setText(currentState.toString());
            flip(newRow, newCol, destinationRow, destinationCol, oppositeState, currentState);
        }
    }

    @Override
    public boolean boardFinished() {
        for (JButton[] states : board) {
            for (int j = 0; j < states.length; j++) {
                if (states[j].getText().equals(State.EMPTY.toString())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void decideWinner() {
        int black = 0;
        int white = 0;
        for (int i = 0; i < board.length - 1; i++) {
            for (int j = 0; j < board[i].length - 1; j++) {
                if(board[i][j].getText().equals(State.BLACK.toString())) {
                    black++;
                }
                else {
                    white++;
                }
            }
        }
        if(black > white) {
            System.out.println("Black wins!");
        }
        else if(white > black){
            System.out.println("White wins!");
        }
        else {
            System.out.println("It's a tie!");
        }
    }

}
