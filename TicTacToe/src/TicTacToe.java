import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
@SuppressWarnings("serial")
public class TicTacToe extends JFrame implements ActionListener 
{
	
	private final boolean Is_JAR = false;
	
	private byte openSpacesLeft;
	private space board[][] = new space[3][3];
	
	public TicTacToe () 
	{
    	setWindowProperties ();
    	setComponentProperties ();
    }
	
    private void setWindowProperties () 
    {
    	setIconImage(Toolkit.getDefaultToolkit().getImage("TTTicon.attribute"));
    	this.getContentPane().setBackground(Color.lightGray);
		setVisible(true);
		setResizable(false);
    	setTitle("TicTacToe");
    	setLayout(new GridLayout(3,3));
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	setSize(100, 190);
		if (Is_JAR) 
			setSize(getWidth() + 10, getHeight() +10);
    }
    
    private void setComponentProperties () 
    {
		JMenuBar bar = new JMenuBar();
		JMenu downMenu = new JMenu("Options");
		bar.add(downMenu);
		String downMenuItems[] = {"Reset", "About", "Exit"};
		for (String name : downMenuItems) {
			JMenuItem it = new JMenuItem(name);
			it.addActionListener(this);
			if (name.equals("Exit")) downMenu.addSeparator();
			downMenu.add(it);
		}
		boardSetup();
		bar.setVisible(true);
		setJMenuBar(bar);
	}
    
    private void resetBoard () 
    {
    	for (space row[] : board)
    		for (space tile : row)
    			tile.reset();
    }
	
	private void boardSetup () 
	{
		openSpacesLeft = 9;
		for (byte pos = 0; pos < 3; pos++)
			for (byte loc = 0; loc < 3; loc++) 
    		{
    			board[pos][loc] = new space();
				board[pos][loc].addActionListener(this);
				add(board[pos][loc]);
    		}
	}
	
	public static void main (String... cheese) 
	{
    	try 
    	{
    		UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} 
    	catch (Exception any) 
    	{ }
        EventQueue.invokeLater(() -> 
        {
            new TicTacToe();
        });
    }
    
    private void scanBoard () 
    {
    	int results[] = scanDiagonals(0);
		for (byte operation = 0; operation < 2; operation++) 
			for (byte pos = 0; pos < 3; pos++) 
			{
				byte playerMoves = 0;
				byte computerMoves = 0;
				for (byte loc = 0; loc < 3 && results[2] == 0; loc++) 
				{
					if (board[loc][pos].isComputer()) 
						computerMoves++;
					else if (board[loc][pos].isPlayer()) 
						playerMoves++;
				}
				if (playerMoves == 3 || computerMoves == 3 || (openSpacesLeft == 0 && operation == 1) || results[2] == 1) 
				{
					System.out.println(playerMoves);
					int choice = -1;
					while (true) 
					{
						if (computerMoves == 3 || results[1] == 3)
							choice = JOptionPane.showConfirmDialog( this, "The Computer won!\nWould you like to play again?", "Game End", JOptionPane.YES_NO_OPTION);
						else if (playerMoves == 3 || results[0] == 3)
							choice = JOptionPane.showConfirmDialog( this, "You won!\nWould you like to play again?", "Game End", JOptionPane.YES_NO_OPTION);
						else
							choice = JOptionPane.showConfirmDialog( this, "Tied Game!\nWould you like to play again?", "Game End", JOptionPane.YES_NO_OPTION);
						if (choice != -1 || choice != JOptionPane.CLOSED_OPTION)
							break;
						else 
							choice = -1;
					}
					if (choice == JOptionPane.NO_OPTION) 
						System.exit(0);
					else 
						resetBoard();
					return;
				}
			}
    }

    private int[] scanDiagonals (int operation) 
    {
    	int playerMoves = 0, computerMoves = 0, openSpaces = 0, returnTrigger = 0;
    	int openSpace[] = {0,0};
    	for (int pos[] = {0,0}; pos[0] < 3; pos[0]++, pos[1]++) 
    	{
    		if (board[pos[0]][pos[1]].isComputer()) 
    			computerMoves++;
    		else if (board[pos[0]][pos[1]].isPlayer()) 
				playerMoves++;
    		else 
    		{
				openSpace[0] = pos[0];
				openSpace[1] = pos[1];
				openSpaces++;
			}
    	}
    	if ((operation == 0 && (playerMoves == 3 || computerMoves == 3)) || (operation == 1 && (playerMoves == 2 || computerMoves == 2) && openSpaces == 1)) 
    		returnTrigger = 1;
    	else
    		playerMoves = computerMoves = openSpaces = 0;
    	for (int pos[] = {2,0}; pos[1] < 3 && returnTrigger == 0; pos[0]--, pos[1]++) 
    	{
    		if (board[pos[0]][pos[1]].isComputer()) 
    			computerMoves++;
    		else if (board[pos[0]][pos[1]].isPlayer()) 
				playerMoves++;
    		else
			{
				openSpace[0] = pos[0];
				openSpace[1] = pos[1];
				openSpaces++;
			}
    	}
    	if ((operation == 0 && (playerMoves == 3 || computerMoves == 3)) || (operation == 1 && (playerMoves == 2 || computerMoves == 2) && openSpaces == 1)) 
    		returnTrigger = 1;
    	if (operation == 0) 
    	{
    		int toRet[] = {playerMoves, computerMoves, returnTrigger};
    		return toRet;
    	} 
    	else 
    	{
    		int toRet[] = {openSpace[0], openSpace[1], returnTrigger};
    		return toRet;
    	}
    }
	
    public void actionPerformed (ActionEvent evt) 
    {
		String btnPress = evt.getActionCommand();
		if (evt.getSource().getClass().equals(space.class) && openSpacesLeft == 0) 
			return;
		switch (btnPress) 
		{
		case "Reset":
			resetBoard();
			break;
		case "About":
			JOptionPane.showMessageDialog( this, "Written and Tested by Auden Childress", "About", JOptionPane.NO_OPTION);
			break;
		case "Exit":
			System.exit(0);
		}
		
		if (evt.getSource().getClass().equals(space.class))
			if (((space) evt.getSource()).markPlayer()) 
			{
				int results[] = scanDiagonals(1);
				openSpacesLeft--;
				scanBoard();
				if (openSpacesLeft == 9) return;
				if (openSpacesLeft != 0) 
				{
					boolean choiceMade = false;
					boolean proAvailable = false;
					byte open[] = {0,0};
					for (byte operation = 0; operation < 2; operation++) 
					{
						for (byte pos = 0; pos < 3; pos++) {
							byte off = 0, def = 0, pro = 0;
							for (byte loc = 0; loc < 3 && results[2] == 0; loc++) 
							{
								if (operation == 0) 
								{
									if (board[pos][loc].isComputer()) off++;
									if (board[pos][loc].isPlayer()) def++;
									if (!board[pos][loc].isPlayer() && !board[pos][loc].isComputer()) 
									{
										pro++;
										open[0] = pos;
										open[1] = loc;
									}
								}
								if (operation == 1) 
								{
									if (board[loc][pos].isComputer()) off++;
									if (board[loc][pos].isPlayer()) def++;
									if (!board[loc][pos].isPlayer() && !board[loc][pos].isComputer()) 
									{
										pro++;
										open[0] = loc;
										open[1] = pos;
									}
								 }
							}
							if (((off == 2 || def == 2) && pro == 1) || results[2] == 1) 
							{
								if (results[2] == 1) 
								{
									open[0] = (byte) results[0];
									open[1] = (byte) results[1];
								}
								board[open[0]][open[1]].markComputer();
								choiceMade = true;
								openSpacesLeft--;
								break;
							} 
							else if (pro == 3) 
								proAvailable = true;
						}
						if (choiceMade) 
							break;
					}
					if (board[open[0]][open[1]].isComputer() && !board[open[0]][open[1]].isPlayer() && !choiceMade && proAvailable) 
					{
						board[open[0]][open[1]].markComputer();
						openSpacesLeft--;
						choiceMade = true;
					}
					while (!choiceMade) 
					{
						if (board[new Random().nextInt(3)][new Random().nextInt(3)].markComputer()) 
						{
							openSpacesLeft--;
							break;
						}
					}
				}
			}
		scanBoard();
	}
}


class space extends JButton {
	private boolean playerOwned = false;
	private boolean computerOwned = false;
	
	space () 
	{
		super();
	}
	
	public boolean isPlayer () 
	{
		return this.playerOwned;
	}
	
	public boolean isComputer () 
	{
		return this.computerOwned;
	}
	
	public boolean markPlayer () 
	{
		if (this.playerOwned || this.computerOwned) 
			return false;
		this.playerOwned = true;
		this.setBackground(Color.blue);
		this.setForeground(Color.white);
		this.setText("O");
		return true;
	}
	
	public boolean markComputer () 
	{
		if (this.playerOwned || this.computerOwned) 
			return false;
		this.computerOwned = true;
		this.setBackground(Color.yellow);
		this.setForeground(Color.black);
		this.setText("X");
		return true;
	}
	
	public void reset () 
	{
		this.playerOwned = this.computerOwned = false;
		this.setBackground(Color.white);
		this.setText("");
	}
}