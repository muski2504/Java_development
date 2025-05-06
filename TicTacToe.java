import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends JFrame {
    private JButton[][] board = new JButton[3][3];
    private boolean xTurn = true; // true = X, false = O

    public TicTacToe() {
        setTitle("Tic Tac Toe Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 3));

        Font btnFont = new Font("SansSerif", Font.BOLD, 55);

        // buttons for grid
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton b = new JButton();
                b.setFont(btnFont);
                board[i][j] = b;
                add(b);

                final int r = i, c = j;
                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        makeMove(b, r, c);
                    }
                });
            }
        }

        setVisible(true);
    }

    private void makeMove(JButton btn, int row, int col) {
        if (!btn.getText().equals("")) return; // already used

        btn.setText(xTurn ? "X" : "O");

        if (checkWinner()) {
            JOptionPane.showMessageDialog(this, "Player " + (xTurn ? "X" : "O") + " wins!");
            resetGame();
        } else if (isBoardFull()) {
            JOptionPane.showMessageDialog(this, "Match Draw!");
            resetGame();
        } else {
            xTurn = !xTurn; // switch turn
        }
    }

    private boolean checkWinner() {
        String curr = xTurn ? "X" : "O";

        // rows and cols
        for (int i = 0; i < 3; i++) {
            if (curr.equals(board[i][0].getText()) &&
                    curr.equals(board[i][1].getText()) &&
                    curr.equals(board[i][2].getText())) return true;

            if (curr.equals(board[0][i].getText()) &&
                    curr.equals(board[1][i].getText()) &&
                    curr.equals(board[2][i].getText())) return true;
        }

        // diagonals
        if (curr.equals(board[0][0].getText()) &&
                curr.equals(board[1][1].getText()) &&
                curr.equals(board[2][2].getText())) return true;

        if (curr.equals(board[0][2].getText()) &&
                curr.equals(board[1][1].getText()) &&
                curr.equals(board[2][0].getText())) return true;

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j].getText().equals("")) return false;
        return true;
    }

    private void resetGame() {
        // clears everything
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j].setText("");
        xTurn = true;
    }

    public static void main(String[] args) {
        new TicTacToe(); // call main window
    }
}
