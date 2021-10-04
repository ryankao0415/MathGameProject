/*
 * Description: The Hiscore class includes all the methods needed to store the player's name in a TreeMap, display the top 5 scores,
 * and retrieve the scores from score.txt.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;

public class Hiscore {
	
	/* method to store a player's high score with their name.
	 * allows for duplicate names to have different scores. If a player with the same name and score is entered,
	 * deletes the name attributed with the score and adds it to the end of the list so that the player is displayed
	 * if they made it to the Top 5.
	*/
	public static TreeMap<Integer, List<String>> storeScore (String name, int score, TreeMap<Integer, List<String>> hiscores)
	{
		//Initialize list, add name to list in case key does not exist in hiscores
		List<String> names = new ArrayList<>();
		names.add(name);
		//For loop to check if key exists
		for(Entry<Integer, List<String>> entry: hiscores.entrySet())
		{
			if (entry.getKey().equals(score))
			{
				//Initialize a list values equal to the values of the map which is also a list
				List<String> values = entry.getValue();
				//for loop to check if the list has parameter name
				for (int i = 0; i < values.size(); i++)
				{
					//if list has name, remove it, then re-add it so the name
					//is at the end of the list
					//set names to values then ends the loop
					if (values.contains(name))
					{
						values.remove(i);
						values.add(name);
						names = values;
						break;
					}
					//If at the end of the the list, adds name to values
					//Set names to values
					else if (i == values.size() - 1)
					{
						values.add(name);
						names = values;
					}
				}
			}
		}
		//Put score and name to hiscore, and return it.
		hiscores.put(score, names); 
		return hiscores;
	}
	
	//method to return a string that displays the top 5 scores.
	public String display (TreeMap<Integer, List<String>> hiscores)
	{
		//Initialize needed variables
		int count = 0;
		String msg = "Name      | Score      <br/>";
		
		//For loop to read every key in a map
		for (Entry<Integer, List<String>> entry : hiscores.entrySet()) 
		{
			//Initialize a list equal to the values of the map which is also a list
			List<String> printable = entry.getValue();
			//for loop that starts at the end of the list that iterates until the beginning
			for (int i = printable.size() - 1; i >= 0; i--)
			{
				//Add name and score to string msg
				msg += printable.get(i) + "\t" + entry.getKey() + "<br/>";
				count++; //Increment count.
				if (count == 5) //If count == 5 i.e displayed 5 high scores, ends the loop
					break;
			}
			if (count == 5)
				break;
		}
		return msg; //Returns msg to display
	}
	
	//method to write a new score into score.txt so that it saves
	//a players score even after the program resets.
	public void writeScore(String name, int score) throws IOException 
	{
		//Write to file in format name:score with a newline after
		//each storage so that scores are saved.
		File log = new File("scores.txt");
		PrintWriter out = new PrintWriter(new FileWriter(log, true));
		out.write(name + ":" + score + "\n");
		out.flush();
		out.close();
	}
	
	//method that reads scores.txt to retrieve a name and their score
	//then stores all name and score in text file to a TreeMap variable
	//and returns the TreeMap with the text file data
	public static TreeMap<Integer, List<String>> readScores() throws IOException 
	{
		//Initialize variables to be able to read scores.txt and store the information to TreeMap readScore
		TreeMap<Integer, List<String>> readScore = new TreeMap<Integer, List<String>>(Collections.reverseOrder());
		String name;
		int score;
		String line;
		BufferedReader input = new BufferedReader(new FileReader("scores.txt"));
		
		line = input.readLine();//Read the first line of the file
		while (line != null) //While loop that reads until line is null, i.e until at the end of the scores.txt
		{
			name = line.substring(0, line.indexOf(":")); //name is from the first character to the character ':'
			score = Integer.parseInt(line.substring(line.indexOf(":") + 1)); //score is the rest of the line beginning after the character ':'
			readScore = storeScore(name, score, readScore); //store read name and score to readScore, calling the method storeScore to store it
			line = input.readLine(); //read the next line, store it in line
		}
		input.close(); //close input
		return readScore; //return the read scores.txt converted into a compatible TreeMap
	}
} //end of Hiscore
