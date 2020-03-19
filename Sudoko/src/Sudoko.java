import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*** COPYRIGHT(C) DIGITAL WARRIORS 2020
 * REFER TO THE END OF THE FILE TO FIND WHAT WORK IS REMAINING TO BE DONE AND CURRENT BUGS
 */


public class Sudoko extends JFrame {
    //setting some board display stuff
    public static final int GRID_SIZE = 9; //board size
    public static final int SUBGRID_SIZE = 3; //each grid on the board;
    public static final int CELL_SIZE = 60; //for size of a single cell
    public static final int CANVAS_WIDTH = GRID_SIZE * SUBGRID_SIZE; //canvas width
    public static final int CANVAS_HEIGHT = GRID_SIZE * SUBGRID_SIZE;//canvas height

    //colouring stuff
    public static final Color EMPTY_BGCOLOR = Color.YELLOW; //for empty cell background
    public static final Color NONEMPTY_BGCOLOR = Color.WHITE; //for non empty cell background
    public static final Color NONEMPTY_TEXTCOLOR = Color.BLACK; //for non empty cell text
    public static final Font TEXT_FONT = new Font("Monospaced",Font.BOLD, 22);

    //for accepting inputs
    //cells will either contain number 1-9 or will remain blank
    private JTextField[][] Cells= new JTextField[GRID_SIZE][GRID_SIZE];

    //placing the puzzle grid numbers
    //this is the actual puzzle which will be answer to the sudoku
    private int[][] puzzle = {      {6, 4, 8, 2, 7, 1, 5, 9, 3},
                                    {9, 2, 1, 6, 5, 7, 4, 8, 3},
                                    {6, 3, 5, 2, 4, 7, 9, 1, 8},
                                    {8, 5, 6, 4, 2, 1, 9, 3, 7},
                                    {3, 2, 1, 5, 8, 7, 6, 4, 9},
                                    {5, 7, 4, 1, 6, 2, 8, 9, 3},
                                    {4, 3, 7, 8, 9, 6, 2, 5, 1},
                                    {1, 5, 3, 9, 2, 7, 6, 4, 8},
                                    {2, 6, 8, 1, 3, 5, 9, 4, 7}     };

    //open and closing masks cells
    // false = cell is closed and already has numbers
    // true = cell is open and accepts input
    // for testing purpose we have opened only 3 cells
    private boolean[][] masks =
            {       {false, false, false, false, false, true, false, false, false},
                    {false, false, false, false, false, false, false, false, true},
                    {false, false, false, false, false, false, false, false, false},
                    {false, false, false, true, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false}     };

    //inner class of for input listener
    private class InputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //this are selected rows and columns
            //we have assigned them -1 because 0 means an element in array
            int row_selected = -1;
            int col_selected = -1;
            boolean found = false; //used as a base case for further loop,
            // if found = true then we found the cell
            // from where the event was triggered so we stop the loop

            //getting the source from which cell the input came
            JTextField source = (JTextField)e.getSource();

            //travesing the entire board
            //to find the cell from where the event was triggered
            for(int row = 0; row < GRID_SIZE && !found; ++row ) {
                for(int col = 0; col < GRID_SIZE && !found; ++col) {
                    if(Cells[row][col] == source ){
                            row_selected = row;
                            col_selected = col;
                            found = true;
                            //converting the received cell input to text form
                            String raw_received = Cells[row_selected][col_selected].getText();
                            //converting the above text input to integer form
                            int received = Integer.parseInt(raw_received);
                            //if the received input matches the actual number in the puzzle array
                            //remember puzzle array is our answer to the entire sudoku
                            if(puzzle[row][col] == received ) {
                                Cells[row][col].setText(raw_received);
                                Cells[row][col].setBackground(Color.GREEN);
                                masks[row][col] = false;

                            }
                            //if not matches then we will turn the color to red
                            //red indicates that the placed number is wrong
                            else {
                                Cells[row][col].setBackground(Color.RED);
                            }

                    }
                }
            }
            //we will check if the puzzle is solved fully or not
            //if all the masks become false, which means if all cells are closed
            //then the puzzle is solved
            boolean solved = false;
            for(int row = 0; row < GRID_SIZE; ++row) {
                for(int col = 0; col < GRID_SIZE; ++col) {
                    //if each and every element has false mask value then the puzzle is solved
                    if(masks[row][col] == false) {
                        solved = true;
                    }
                    else {
                        solved = false;
                    }
                }
            }
            //if solved is true them the user solved it fully
            if(solved == true) {
                //giving congrats to user if the puzzle is solved fully
                JOptionPane.showMessageDialog(null, "Congratulation!");
            }

        }
    }

    //constructor for the puzzle board
    Sudoko() {
        Container container = getContentPane();
        container.setLayout(new GridLayout(GRID_SIZE,GRID_SIZE)); //9x9 grid layout for board

        InputListener listener = new InputListener();


        for(int row = 0; row < GRID_SIZE; ++row) {
            for(int col = 0; col < GRID_SIZE; ++col) {
                Cells[row][col] = new JTextField();
                container.add(Cells[row][col]);
                //if the mask is true
                //which means if the cell is empty
                //then we will set the below setting
                if(masks[row][col]) {
                    Cells[row][col].setText(""); //set the text to empty
                    Cells[row][col].setEditable(true); //the cell must be set to editible
                    Cells[row][col].setBackground(EMPTY_BGCOLOR); //the cell should have the bgcolor mentioned
                    Cells[row][col].addActionListener(listener); //adding action listener to each empty cell

                }
                //if the mask is false
                //which means if the cell is not empty
                //then we will set the below setting
                else {
                    Cells[row][col].setText(String.valueOf(puzzle[row][col]));
                    Cells[row][col].setEditable(false);
                    Cells[row][col].setBackground(NONEMPTY_BGCOLOR);
                    Cells[row][col].setForeground(NONEMPTY_TEXTCOLOR);

                }
                //beautifying stuff
                Cells[row][col].setHorizontalAlignment(JTextField.CENTER);
                Cells[row][col].setFont(TEXT_FONT);
            }
        }


        container.setPreferredSize(new Dimension(CANVAS_WIDTH,CANVAS_HEIGHT));
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sudoko");
        setVisible(true);

    }

    public static void main(String args[]) {
        //initiating the object of the sudoku
        Sudoko obj = new Sudoko();
    }

}

/*** TODO LIST
 * There is some problem with the size of the board when the game starts
 * There is some bug with the board numbers as same cell has one number multiple time, so need to fix it
 * randomize the puzzle number so everytime same number do not get repeated in all the games
 * create difficulty level and open random cells according to difficulty level
 * create start, pause, resume button
 * if u have any work left or any idea then mention them here and if you solved any of above implementation or bugs
 * then erase them and edit this section accordingly
 */


