import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

/**
 * Simple game for playing Tic Tac Toe with two players
 * 
 * @author Joe Samuel (www.JoeFS.com) 
 * @version 1.0
 */

public class TTT 
{
    //Variable initializations
    private JFrame frame;
    private JPanel panel, panel2;
    private JTextArea status = new JTextArea("New Game! We'll start with X.");
    private int numFreeSquares; // number of squares still free
    private String player;   // current player (PLAYER_X or PLAYER_O)
    private static final String PLAYER_X = "X"; // player using "X"
    private static final String PLAYER_O = "O"; // player using "O"
    private static final String EMPTY = " ";  // empty cell
    private static final String TIE = "T"; // game ended in a tie
    private String winner;   // winner: PLAYER_X, PLAYER_O, TIE, EMPTY = in progress
    
    // Buttons for the Grid
    private JButton buttons[][] = new JButton[3][3];    
    
    //Menu Bar
    JMenuBar menuBar = new JMenuBar(); 
    JMenu Game = new JMenu("Menu");
    JMenuItem New = new JMenuItem("New");
    JMenuItem Quit = new JMenuItem("Quit");
    
    /**
     * 
     * Constructor for the class TTT (Tic Tac Toe)
     * 
     */
   public TTT()
   {
        makeFrame();
        winner = EMPTY;
        
        //Initialization for the buttons on the grid 
        for(int i = 0; i < 3; i++)
         { 
           for(int j = 0; j < 3; j++)
            {
               buttons[i][j].setText(""); 
               buttons[i][j].setEnabled(true);
               numFreeSquares = 9;
               player = PLAYER_X; 
               status.setText("New Game! We'll start with X.");     //Setting up the default status for the label
            }
         }
   }
   
    /**
     * Builds and integrates functionality for the Graphical User Interface(GUI) 
     * 
     * @param  none
     * @return none
     */ 
   public void makeFrame() 
   {
        //Initializing the frame that will contain the two panels
        status.setEditable(false);
        frame =  new JFrame("Tic Tac Toe");
        
        //Menu Bar
        menuBar.add(Game);
        menuActionListener menuButtons = new menuActionListener();
        New.addActionListener(menuButtons);
        New.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, java.awt.Event.CTRL_MASK));
        Quit.addActionListener(menuButtons);
        Quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, java.awt.Event.CTRL_MASK));
        Game.add(New);
        Game.add(Quit);
        frame.setJMenuBar(menuBar);
        
        //Panel 1
        panel = new JPanel(new GridLayout(3,3));
        panel.setSize(300,300);
        panel.setBorder(new TitledBorder("Game"));
        
        //Positioning the buttons on the grid
        for(int i = 0; i < 3 ; i++)
        {
            for(int j = 0 ; j < 3; j++)
            {
            buttons[i][j] = new JButton();
            buttons[i][j].setFocusPainted(false);
            buttons[i][j].setToolTipText("Click here to choose your piece");
            addActionListener myButton = new addActionListener();
            buttons[i][j].addActionListener(myButton);
            panel.add(buttons[i][j]);
           }
       }

       frame.add(panel);
       
       //Panel 2
       panel2 = new JPanel();
       panel2.setSize(20,20);
       panel2.setBorder(new TitledBorder("Options"));
       JButton resetButton = new JButton("Reset the Board");
       resetActionListener reset = new resetActionListener();
       resetButton.addActionListener(reset);
       panel2.add(resetButton);
       panel2.add(status);
       frame.add(panel2, BorderLayout.SOUTH);
        
       //Frame settings
        frame.setSize(500,500);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
    
   /**
     * Controls the changing of turns for the game between the players
     * 
     * @param  none
     * @return none
     */
   public void acessturnChanger()
   {    
        if (player==PLAYER_X) 
            player=PLAYER_O;
         else 
            player=PLAYER_X;
   }
   
    /**
     * Modifies the game status label based on the current status of the game
     * 
     * @param  none
     * @return none 
     */ 
   public void gameStatus()
   {   
        if(winner != EMPTY)
        {
            if( winner != TIE)
            {
                status.setText("The winner of the game was " + winner);
            }
            else if (winner == TIE)
            {
                status.setText("The game ended in a Tie! Good play!");
            }
            else 
            {
                status.setText(player + "'s turn! Good luck!");
            }
        }
        else
        {
            status.setText("Game in progress..." + player + "'s turn! Good luck!");
      }
   }

    /**
     * Analyses the game to determine if the move was a win or a tie 
     * 
     * @param  row   provides the current row
     *         col   provides the current column
     * @return     true if there is a match or false if there wasn't a match 
     */
   private boolean haveWinner(int row, int col) 
   {
       // unless at least 5 squares have been filled, we don't need to go any further
       // (the earliest we can have a winner is after player X's 3rd move).

       if (numFreeSquares>4) 
       {
           return false;
        }
       // Note: We don't need to check all rows, columns, and diagonals, only those
       // that contain the latest filled square.  We know that we have a winner 
       // if all 3 squares are the same, as they can't all be blank (as the latest
       // filled square is one of them).

       // check row "row"
       if ( buttons[row][0].getText().equals(buttons[row][1].getText()) &&
            buttons[row][0].getText().equals(buttons[row][2].getText()) ) 
            {
                return true;
            }
       
       // check column "col"
       if ( buttons[0][col].getText().equals(buttons[1][col].getText()) &&
            buttons[0][col].getText().equals(buttons[2][col].getText()) ){
                 return true;
                }
                
       // if row=col check one diagonal
       if (row==col)
          if ( buttons[0][0].getText().equals(buttons[1][1].getText()) &&
               buttons[0][0].getText().equals(buttons[2][2].getText()) ) 
               {
                   return true;
                }
       // if row=2-col check other diagonal
       if (row==2-col)
          if ( buttons[0][2].getText().equals(buttons[1][1].getText()) &&
               buttons[0][2].getText().equals(buttons[2][0].getText()) )
               {
                   return true;
                }

       // no winner yet
       return false;
   }
    
    /**
     * Checks for a winner and ends the game with the appropriate message if there was a tie
     * 
     * @param  row   provides the current row
     *         col   provides the current column    
     * @return     true if there was a winner or a tie for the game
     */
   public boolean accesscheckWinner(int row, int col)
   {
            if (haveWinner(row,col) == true) {
                    winner = player; // must be the player who just went
                    gameStatus();
                    for(int i = 0; i < 3 ; i++)
                        {
                            for(int j = 0 ; j < 3; j++)
                            {
                            buttons[i][j].setEnabled(false);
                        }
                       }
                    return true;
                }
                 else if (numFreeSquares==0){
                    winner = TIE; // board is full so it's a tie
                    gameStatus();
                    return true;
            }
            return false;
   }
   
    /**
     * Monitors the game for keeping the game status current by reducing the number of avaiable moves and changing turns
     * 
     * @param  row   provides the current row
     *         col   provides the current column
     * @return     none
     */
   public void PlayGame(int row, int col)
   {
        numFreeSquares--;
        if(!accesscheckWinner(row, col))
        {
             acessturnChanger();
             gameStatus();
        }
   }

    /**
     * Initializes the game 
     * 
     * @param  args[]   arguments array for the main function
     * @return     none 
     */
    public static void main(String args[])
    {
        TTT new_TTT = new TTT();
    }
    
    //For implementing the button labels
        
        public class addActionListener implements ActionListener 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {   
                if(winner == EMPTY)  
                {
                    if(e.getSource() == buttons[0][0])
                    {
                        JButton button = (JButton)e.getSource();
                        if (player==PLAYER_X) 
                           button.setText("X");
                         else 
                           button.setText("O");
                        
                        button.setEnabled(false);
                        PlayGame(0,0);
                        
                    }
                    else if(e.getSource() == buttons[0][1])
                    {
                        JButton button = (JButton)e.getSource();
                        if (player==PLAYER_X) 
                           button.setText("X");
                         else 
                           button.setText("O");
                        button.setEnabled(false);
                        PlayGame(0,1);
                       
                    }
                    else if(e.getSource() == buttons[0][2])
                    {
                        JButton button = (JButton)e.getSource();
                        if (player==PLAYER_X) 
                           button.setText("X");
                         else 
                           button.setText("O");
                        button.setEnabled(false);
                        PlayGame(0,2);
                        
                    }
                    else if(e.getSource() == buttons[1][0])
                    {
                        JButton button = (JButton)e.getSource();
                        if (player==PLAYER_X) 
                           button.setText("X");
                         else 
                           button.setText("O");
                        button.setEnabled(false);
                        PlayGame(1,0);
                      
                    }
                    else if(e.getSource() == buttons[1][1])
                    {
                        JButton button = (JButton)e.getSource();
                        if (player==PLAYER_X) 
                           button.setText("X");
                         else 
                           button.setText("O");
                        button.setEnabled(false);
                        PlayGame(1,1);
                        
                    }
                    else if(e.getSource() == buttons[1][2])
                    {
                        JButton button = (JButton)e.getSource();
                        if (player==PLAYER_X) 
                           button.setText("X");
                         else 
                           button.setText("O");
                        button.setEnabled(false);
                        PlayGame(1,2);
                    }
                    else if(e.getSource() == buttons[2][0])
                    {
                        JButton button = (JButton)e.getSource();
                        if (player==PLAYER_X) 
                           button.setText("X");
                         else 
                           button.setText("O");
                        button.setEnabled(false);
                        PlayGame(2,0);
                    }
                    else if(e.getSource() == buttons[2][1])
                    {
                        JButton button = (JButton)e.getSource();
                        if (player==PLAYER_X) 
                           button.setText("X");
                         else 
                           button.setText("O");
                        button.setEnabled(false);
                        PlayGame(2,1);
                    }
                    else if(e.getSource() == buttons[2][2])
                    {
                        JButton button = (JButton)e.getSource();
                        if (player==PLAYER_X) 
                           button.setText("X");
                         else 
                           button.setText("O");
                        button.setEnabled(false);
                        PlayGame(2,2);
                    }
                }
            }
        }
        
            //For Resetting the board
        
        public class resetActionListener implements ActionListener 
        {
            public void actionPerformed(ActionEvent e)
            {   
                winner = EMPTY;
                for(int i = 0; i < 3; i++)
                 { 
                   for(int j = 0; j < 3; j++)
                    {
                       buttons[i][j].setText(""); 
                       buttons[i][j].setEnabled(true);
                       numFreeSquares = 9;
                       player = PLAYER_X; 
                       status.setText("New Game! We'll start with X.");
                    }
                 }
            }
        }
        
        //For the menu's
        public class menuActionListener implements ActionListener
        {
            public void actionPerformed(ActionEvent e)
            {
                if(e.getSource() == New)
                {
                    winner = EMPTY;
                    for(int i = 0; i < 3; i++)
                     { 
                       for(int j = 0; j < 3; j++)
                        {
                           buttons[i][j].setText(""); 
                           buttons[i][j].setEnabled(true);
                           numFreeSquares = 9;
                           player = PLAYER_X; 
                           status.setText("New Game! We'll start with X.");
                        }
                     }
                }
                else if(e.getSource() == Quit)
                {
                    System.exit(0);
                }
            }
        }
        
}