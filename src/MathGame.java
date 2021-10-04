/*
 * Description: The MathGame class is an operation class that includes all the methods needed to create a GUI application,
 * including components and event handlers, and generate a random question for the math game.
 */

import java.util.*;

import javax.swing.*;
import javax.swing.ImageIcon;

import java.awt.event.*;
import java.awt.Color;
import java.awt.Font;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MathGame 
{
	//variable initialization
	final int min = 1;
	int max = 50, counter = 0, choice = 0;
	int number1, number2, answer, operation, score;
	char operator = '?';
	String name;
	boolean decision = false;
	
	Random rand = new Random();
	Hiscore hiscore = new Hiscore();
	Clip clip = null;
	AudioInputStream audioStream = null;
	
	//adds images
	ImageIcon welcome = new ImageIcon("welcome.png");
	ImageIcon askName = new ImageIcon("name.png");
	ImageIcon board = new ImageIcon("board.jpg");
	ImageIcon scoreboard = new ImageIcon("score.png");
	ImageIcon topFive = new ImageIcon("Top5.png");
	
	//sets fonts
	Font light = new Font("Helvetica Light", Font.PLAIN, 18);
	Font bold1 = new Font("Helvetica Bold", Font.BOLD, 18);
	Font bold2 = new Font("Helvetica Bold", Font.PLAIN, 30);
	
	//creates instance of JFrame
	JFrame frame = new JFrame("Math Game"); 
	
	//start menu
	JButton startButton = new JButton("START"); //start button to start game
	JButton exitButton = new JButton("EXIT"); //exits game
	JLabel pic1 = new JLabel(welcome); //welcome image
		
	//asks user to enter name & play music
	JTextField textbox = new JTextField(null, 20);
	JButton enterButton = new JButton("ENTER");
	JButton musicyes = new JButton("Yes");
	JButton musicno = new JButton("No");
	JLabel label1 = new JLabel("Would you like music?");
	JLabel pic2 = new JLabel(askName);
	
	//asks math question
	JButton mathEnterButton = new JButton("ENTER");
	JLabel label2 = new JLabel();
	JLabel label3 = new JLabel("SCORE: " + score);
	JLabel pic3 = new JLabel(board);
	
	//displays scores
	JButton scoreButton = new JButton("Display High Scores");
	JLabel pic4 = new JLabel(scoreboard);
	JLabel pic5 = new JLabel(topFive);
	
	//TreeMap stores scores
	public static TreeMap<Integer, List<String>> readLeaders() throws IOException
    {
		TreeMap<Integer, List<String>> defaultScores = new TreeMap<Integer, List<String>>(Collections.reverseOrder());
        defaultScores = Hiscore.readScores();

        return defaultScores;
    } //end of readLeaders

    TreeMap<Integer, List<String>> leaderboard = new TreeMap<Integer, List<String>>();
	
	//starts game
	public void startGame()
	{
		try 
		{
			leaderboard = readLeaders();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		//sets frame size & centers window
		frame.setSize(800,600); //sets size of frame
		frame.setLocationRelativeTo(null); //centers window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
		//sets background color
		Color color = new Color(160,210,230);
		frame.getContentPane().setBackground(color);
		frame.setLayout(null); //using no layout managers  
		frame.setVisible(true); //making the frame visible
		
		//puts image in start menu
		frame.add(pic1);
		
		//start menu components (first screen)
		frame.add(startButton);
		frame.add(exitButton);
		frame.add(scoreButton);
		
			//sets bounds for start menu components
			pic1.setBounds(185,100,400,140);
			startButton.setBounds(290,300,200,40); 
			exitButton.setBounds(290,350,200,40); 
			scoreButton.setBounds(290,400,200,40); 
		
		//asking name components (second screen)
		frame.add(label2);
		frame.add(textbox);
		frame.add(enterButton);
		frame.add(musicno);
		frame.add(musicyes);
		
			//sets bounds for music components
			musicno.setBounds(0,0,0,0);
			musicyes.setBounds(0,0,0,0);
		
		//answering question components (third screen)
		frame.add(label3);
		frame.add(mathEnterButton);
		
		//sets fonts
		startButton.setFont(bold1);
		exitButton.setFont(light);
		scoreButton.setFont(light);
		label2.setFont(bold1);
		textbox.setFont(light);
		enterButton.setFont(light);
		musicyes.setFont(light);
		musicno.setFont(light);
		label1.setFont(bold1);
		label3.setFont(light);
		mathEnterButton.setFont(light);
		
		//adds buttons to action listener for event handling
		startButton.addActionListener(new startActionListener());
		exitButton.addActionListener(new exitActionListener());
		enterButton.addActionListener(new nameActionListener());
		musicno.addActionListener(new musicActionListener());
		musicyes.addActionListener(new musicActionListener());
		mathEnterButton.addActionListener(new mathActionListener());
		scoreButton.addActionListener(new scoreActionListener());
	} //end of startGame

	//generates random questions
	public void questionGenerator()
	{
		//creates questions
		operation = rand.nextInt(3) + min;
		
		//makes max # 10 to make it easier for user if random generator picks multiplication problem
		if (operation == 3)
			max = 10; 
		
		//sets random for number1 & number2
		number1 = rand.nextInt(max) + min; //random number max and min
		number2 = rand.nextInt(max) + min; 
				
		//ensures final answer of problem is not negative by switching #'s if random generator picks subtraction problem
		if (operation == 2 && number1 < number2)
		{
			int tempNum = number1;
			number1 = number2;
			number2 = tempNum;
		}
		answer = 0;
		
		//randomizes operator to randomize question types
		if (operation == 1)
		{
			operator = '+';
			answer = number1 + number2;
		}
		if (operation == 2)
		{
			operator = '-';
			answer = number1 - number2;
		}
		if (operation == 3)
		{
			operator = '*';
			answer = number1 * number2;
		}
			
		//prints question
		label2.setText("Question:  " + number1 + " " + operator + " " + number2 + " = ?");
	} //end of questionGenerator
	
	//exits game
	class exitActionListener implements ActionListener
	{
	      public void actionPerformed(ActionEvent e) 
	      {
	    	  System.exit(0); //exiting the window
	      }
	} //end of exitActionListener
	
	//asks user to enter name
	class startActionListener implements ActionListener
	{
	      public void actionPerformed(ActionEvent e) 
	      {
	    	  	//if user plays again
	    	  	score = 0;
	    	  	label3.setText("SCORE: " + score);
	    	  	textbox.setText(null);
	    	  	pic4.setBounds(0,0,0,0);
	    	  	pic5.setBounds(0,0,0,0);
		    	label2.setBounds(0,0,0,0);
	    	  	
		    	//clears frame
		    	startButton.setBounds(0,0,0,0);
		    	exitButton.setBounds(0,0,0,0);
		    	scoreButton.setBounds(0,0,0,0);
		    	pic1.setBounds(0,0,0,0);
		    	
		    	//adds image
		    	frame.add(pic2);
		    	
		    	//sets bounds for components
		    	textbox.setBounds(190,300,400,30);
		    	enterButton.setBounds(290,350,200,40);
		    	pic2.setBounds(40,0,700,500);
	    	  
		  		//sets fonts
				textbox.setFont(light);
				enterButton.setFont(light);
	      }
	} //end of startActionListener
	
	//displays scores
	class scoreActionListener implements ActionListener
	{
	      public void actionPerformed(ActionEvent e) 
	      {
	    	//clears frame
		    startButton.setBounds(0,0,0,0);
		    exitButton.setBounds(0,0,0,0);
		    scoreButton.setBounds(0,0,0,0);
		    pic1.setBounds(0,0,0,0);
		    
		    //adds image
	    	frame.add(pic5);
		    frame.add(pic4);
	    	
	    	//sets bounds to components
	    	pic4.setBounds(90,20,600,450);
	    	pic5.setBounds(90,-130,600,450);
	  		label2.setBounds(330,65,300,300);
	  			label2.setText("<html><center>" + hiscore.display(leaderboard) + "</center><html>");
	  			label2.setFont(bold1);
	  		startButton.setBounds(290,375,200,40);
    	  		startButton.setText("START");
    	  		startButton.setFont(bold1);
	  		exitButton.setBounds(290,425,200,40);
    	  		exitButton.setText("EXIT");
    			exitButton.setFont(light);
	      }
	} //end of scoreActionListener
	
	//asks user to play music
	class nameActionListener implements ActionListener //this action listener for the clear Button
	{
	      public void actionPerformed(ActionEvent e) 
	      {
	    	  	counter = 0;
	    	  	name = textbox.getText();
	    	  	
	    	  	//adds label
	    	  	frame.add(label1);
	    	  	
	    	  	//sets bounds to components
	    	  	musicyes.setBounds(375,495,100,30);
	    	  	musicno.setBounds(495,495,100,30);
	    	  	label1.setBounds(150,495,300,30);
	      }
	} //end of nameActionListener
	
	//starts game
	class musicActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			//clears frame
			label1.setBounds(0,0,0,0);
    	  	label2.setBounds(0,0,0,0);
    	  	enterButton.setBounds(0,0,0,0);
    	  	musicyes.setBounds(0,0,0,0);
			musicno.setBounds(0,0,0,0);
			pic2.setBounds(0,0,0,0);
    	  	
    	  	textbox.setText(null);
    	  	
    	  	//adds image
	    	frame.add(pic3);
    	  	
	    	//sets bounds for components
	    	textbox.setBounds(190,235,400,30);
    	  	mathEnterButton.setBounds(290,285,200,40);
			label2.setBounds(250,120,400,100);
				label2.setFont(bold2);
				label2.setForeground(Color.WHITE); //changes font color to white to make question visible
			label3.setBounds(600,0,100,100);
			pic3.setBounds(60,70,650,406);
				
			questionGenerator();
					
			//imports music
			File file = new File("Loop.wav");
			
			try 
			{
				audioStream = AudioSystem.getAudioInputStream(file);
			} 
			catch (UnsupportedAudioFileException e1) 
			{
				e1.printStackTrace();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			Clip clip = null;
			
			try 
			{
				clip = AudioSystem.getClip();
			} 
			catch (LineUnavailableException e1) 
			{
				e1.printStackTrace();
			}
			
			try 
			{
				clip.open(audioStream);
			} 
			catch (LineUnavailableException e1) 
			{
				e1.printStackTrace();
			} 
			catch (IOException e1) {
				e1.printStackTrace();
			}
			
			//if user decides to play music or not
			if (e.getSource() == musicyes)
			{
				clip.start();
			}
			else if(e.getSource() == musicno)
			{
				clip.stop();
			}
		}
	}
	
	//game continues
	class mathActionListener implements ActionListener
	{
	      public void actionPerformed(ActionEvent e) 
	      {
	    	  	counter += 1;
	    	  	if (counter < 10)
	    	  	{
		    	  	//stores name value after textbox input
		  			choice = Integer.parseInt(textbox.getText());
		  			textbox.setText("");
		  			
		  			//increments score if answer is correct
		  			if (choice == answer)
						score += 1; 
		  			
					//updates score
					label3.setText("SCORE: " + score);
					textbox.setText(null);
						
					questionGenerator();
		    	}
	    	  	else
	    	  	{
	    	  		choice = Integer.parseInt(textbox.getText());
	    	  		if (choice == answer)
						score += 1;
	    	  		
	    	  		//clears frame
	    	  		textbox.setBounds(0,0,0,0);
	    	  		mathEnterButton.setBounds(0,0,0,0);
	    	  		label3.setBounds(0,0,0,0);
	    	  		pic3.setBounds(0,0,0,0);
	    	  		
	    	  		try 
	    	  		{
	    	  			hiscore.writeScore(name, score);
	    	  		}
	    	  		catch (IOException e1) 
	    	  		{
	    	  			e1.printStackTrace();
	    			}
	    	  		
	    	  		//calculates scores by calling storeScore() from Hiscore class
	    	  		leaderboard = hiscore.storeScore(name, score, leaderboard);
	    	  		
	    	  		//adds image
			    	frame.add(pic5);
	    	  		frame.add(pic4);
			    	
			    	//sets bounds to components
	    	  		pic4.setBounds(90,20,600,450);
	    	    	pic5.setBounds(90,-130,600,450);
	    	  		label2.setBounds(330,65,300,300);
	    	  			label2.setText("<html><center>" + hiscore.display(leaderboard) + "</center><html>");
	    	  			label2.setFont(bold1);
	    	  		startButton.setBounds(290,375,200,40);
		    	  		startButton.setText("PLAY AGAIN");
		    	  		startButton.setFont(bold1);
	    	  		exitButton.setBounds(290,425,200,40);
		    	  		exitButton.setText("EXIT");
		    			exitButton.setFont(light);
	    	  	}
	      }  	
	} //end of mathActionListener
} //end of MathGame
