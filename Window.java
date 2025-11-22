import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Window extends JFrame {

    private JLabel[][] cells = new JLabel[9][9];
    private char[] chars = new char[9];

    public Window() {
        setLayout(new BorderLayout());
        setSize(800, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setResizable(false);
        setLocationRelativeTo(rootPane);
        add(addButtons(), BorderLayout.CENTER);
        add(drawGrid(), BorderLayout.NORTH);

        setVisible(true);
    }

    public JPanel drawGrid() {

        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new FlowLayout());

        JPanel holder = new JPanel();
        holder.setLayout(new GridLayout(9, 9));

        outerPanel.add(holder);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                JLabel label = new JLabel("", SwingConstants.CENTER);
                cells[i][j] = label;
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setVerticalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("SansSerif", Font.BOLD, 20));

                panel.setPreferredSize(new Dimension(60, 60));
                if ((i / 3 + j / 3) % 2 == 0) {
                    panel.setBackground(new Color(200, 200, 200));
                } else {
                    panel.setBackground(new Color(150, 150, 150));
                }
                panel.add(label, BorderLayout.CENTER);
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                holder.add(panel);
            }
        }

        outerPanel.setVisible(true);
        return outerPanel;
    }

    public JPanel addButtons() {
        JPanel holder = new JPanel();
        holder.setLayout(new FlowLayout());

        JButton generateSudokuBtn = new JButton();
        generateSudokuBtn.setText("Generate Random");
        generateSudokuBtn.addActionListener(e -> {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    cells[i][j].setText("");
                }
            }
            generateRandomSudoku();
            for (int i=0; i<60; i++) {
                int row = (int)(Math.random() * 9);
                int col = (int)(Math.random() * 9);
                cells[row][col].setText("");
            }
        });

        JButton solveBtn = new JButton();
        solveBtn.setText("Solve Sudoku");
        solveBtn.addActionListener(e -> {
            solveSudoku();
        });

        holder.add(generateSudokuBtn);
        holder.add(solveBtn);

        holder.setVisible(true);

        return holder;
    }

    private boolean solveSudoku() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (cells[i][j].getText() == "") {
                    for (char ch = '1'; ch<='9'; ch++) {
                        if (isValid(i, j, ch)) {
                            cells[i][j].setText("" + ch);
                            cells[i][j].setForeground(new Color(113, 55, 230));
                            if (solveSudoku())
                                return true;
                            cells[i][j].setText("");
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private void generateRandomCharArray() {
        Set<Character> usedChars = new HashSet<>();
        int randomChar = 0;

        for (int i = 0; i < 9; i++) {
            randomChar = (int) (Math.random() * 9);
            char ch = (char) (randomChar + '1');

            if (!usedChars.contains(ch)) {
                chars[i] = ch;
            }

            usedChars.add(ch);
        }
    }

    public boolean generateRandomSudoku() {
        generateRandomCharArray();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (cells[i][j].getText() == "") {
                    for (char ch : chars) {
                        if (isValid(i, j, ch)) {
                            cells[i][j].setText("" + ch);
                            cells[i][j].setForeground(new Color(0, 0, 0));
                            if (generateRandomSudoku())
                                return true;
                            cells[i][j].setText("");
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int row, int col, char ch) {
        for (int i = 0; i < 9; i++) {
            String rowString = cells[row][i].getText();
            String colString = cells[i][col].getText();
            String gridString = cells[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3].getText();

            if (!rowString.isEmpty() && rowString.charAt(0) == ch)
                return false;
            if (!colString.isEmpty() && colString.charAt(0) == ch)
                return false;
            if (!gridString.isEmpty() && gridString.charAt(0) == ch)
                return false;
        }
        return true;
    }
}
