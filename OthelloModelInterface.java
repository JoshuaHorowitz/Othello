public interface OthelloModelInterface {

    boolean makeMove(int row, int col, Object state);

    boolean willFlip(int row, int col, Object state);

    boolean hasNeighboringOpponentToFlip(int row, int col, Object state);

    boolean checkForEnd(int destinationRow, int destinationCol, int sourceRow, int sourceCol, Object oppositeState, Object currentState);

    boolean hasANeighbor(int row, int col);

    void flip(int destinationRow, int destinationCol, int sourceRow, int sourceCol, Object oppositeState, Object currentState);

    boolean boardFinished();

    void decideWinner();

}
