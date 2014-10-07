import java.io.*;
import java.util.Scanner;

public class PrepareCorpus{
	public static void main(String[] args) throws IOException{
		int training_number = 28000; // Enter the amount of lines in the training set
		String regex = ":::";		 // I've use a delimiter that doesn't appear anywhere in the trainginset
		
		// Create a two dimenstional array, it will hold the clean tweet and the sentiment
		String[][] hold_cleaned_tweets = new String[training_number][2];
		File training_set = new File("training.txt");
		Scanner read_training = new Scanner(training_set);		
		int x = 0;		
		while(read_training.hasNext()){
			String current_line = read_training.nextLine();
			String[] holder = current_line.split(regex);
			holder[0] = CleanTweet.cleanTweet(holder[0]);
			hold_cleaned_tweets[x][0] = holder[0];
			hold_cleaned_tweets[x][1] = holder[1];
			x++;	
		}		
		read_training.close();
		
		//I will now write to a new file with the 2D string 
		FileWriter out_clean_training = null;
		out_clean_training = new FileWriter("clean_training.txt");
		for(int y=0; y<hold_cleaned_tweets.length; y++){
			out_clean_training.write(hold_cleaned_tweets[y][0] + regex + hold_cleaned_tweets[y][1]);
			out_clean_training.append('\n');
		}
		out_clean_training.close();			
		
		//Now I will create another 2D String array to store the clean tweet and sentiment
		String[][] clean_and_sentiment = new String[training_number][2];
		File create_string_array = new File("clean_training.txt");
		Scanner read_clean = new Scanner(create_string_array);
		int z = 0;
		while(read_clean.hasNext()){
			String current_line = read_clean.nextLine();
			String[] holder = current_line.split(regex);
			clean_and_sentiment[z][0] = holder[0];
			clean_and_sentiment[z][1] = holder[1];
			z++;	
		}
		read_clean.close();
		
		//This will create the final_corpus text file with Word /Total_pos/ Total_neg /total_count
		Object[][] db = NaiveBayes.dbCreator(clean_and_sentiment);
		}
		
		
}
		/*		
		//If you want to run the program
		String tweet = JOptionPane.showInputDialog("Enter a Tweet");
			
		double result = NaiveBayes.NaiveBayesCalculator(tweet, db);
		
		System.out.println(result);*/
	
		


	

