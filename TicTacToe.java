import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends Applet implements ActionListener {
    int boardSize = 3;
    int cellSize = 100;
    int[][] board = new int[boardSize][boardSize];
    Button[][] buttons = new Button[boardSize][boardSize];
    int currentPlayer = 1;
    Label message = new Label("Player 1's turn");

    public void init() {
        setLayout(new BorderLayout());
        Panel boardPanel = new Panel(new GridLayout(boardSize, boardSize));
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                buttons[i][j] = new Button("");
                buttons[i][j].addActionListener(this);
                boardPanel.add(buttons[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
        add(message, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        Button button = (Button) e.getSource();
        int x = -1;
        int y = -1;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (buttons[i][j] == button) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        if (board[x][y] == 0) {
            board[x][y] = currentPlayer;
            if (currentPlayer == 1) {
                button.setLabel("X");
                currentPlayer = 2;
                message.setText("Player 2's turn");
            } else {
                button.setLabel("O");
                currentPlayer = 1;
                message.setText("Player 1's turn");
            }
            if (checkWin()) {
                if (currentPlayer == 1) {
                    message.setText("Player 2 wins!");
                } else {
                    message.setText("Player 1 wins!");
                }
                for (int i = 0; i < boardSize; i++) {
                    for (int j = 0; j < boardSize; j++) {
                        buttons[i][j].setEnabled(false);
                    }
                }
            }
        }
    }

    boolean checkWin() {
        for (int i = 0; i < boardSize; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != 0) {
                return true;
            }
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != 0) {
                return true;
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != 0) {
            return true;
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != 0) {
            return true;
        }
        return false;
    }
}