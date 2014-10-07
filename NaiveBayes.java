//http://nlp.stanford.edu/IR-book/html/htmledition/naive-bayes-text-classification-1.html
	//http://blog.datumbox.com/machine-learning-tutorial-the-naive-bayes-text-classifier/
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.*;

//import java.sql.*;
//import javax.sql.*;

public class NaiveBayes {
	
	// TEST - You want a variable to store the word, the total_pos, total_neg and total_occurances - This function contains a list of the words in a 1D array
		// It breaks up the tweet element of the corpus and removes the 
		public static Object[][] dbCreator(String[][] corpus) throws IOException {
			
			String[][] holder = word_and_sentiment(corpus);		//Stores each word and its frequency
			Object[][] holder_once = getWordFrequency(corpus);	//Used to get single word & total occurances
			int total_words = holder_once.length;					//This is an int with the total number of words
			int total_neg = 0;									//Counts the total neg of each word
			int total_pos = 0;									//Counts the total neg of each word
			
			String sentiment_holder="";							//Will hold the sentiment of each word appearance
			
			Object[][] count_all = new Object[total_words][4];	//4 columns holding(word,pos_count,neg_count,total)
	
			//Loops through the single words
			for(int x=0; x<total_words; x++){
				//Loops through multiple versions of word with sentiment
				for(int y=0; y<holder.length; y++){
					if(holder_once[x][0].equals(holder[y][0])){
						sentiment_holder = holder[y][1];		//Pass the sentiment to the sentiment holder
						if(sentiment_holder.equals("Positive")){
							total_pos++;
						}else if(sentiment_holder.equals("Negative")){
							total_neg++;
						}
					}

				}
				
				count_all[x][0] = holder_once[x][0];		//Pass the actual word to the DB
				count_all[x][1] = total_pos;
				count_all[x][2] = total_neg;
				count_all[x][3] = holder_once[x][1];		//Pass the total_count to the DB
				total_pos=0; 								//reset to 0
				total_neg=0;								//reset to 0
			}
			
			FileWriter out = null;
			out = new FileWriter("final_corpus.txt");
			//BufferedWriter output = new BufferedWriter(new FileWriter(file));

			for(int y=0; y<count_all.length; y++){
				String temp = count_all[y][0] + "\t\t" + count_all[y][1] + "\t" + count_all[y][2] + "\t" + count_all[y][3] + "\t";
				out.write(temp);
				out.append('\n');
			}
			out.close();
			
			return count_all;
		}
	
	
	//Gets each instance of a word and its sentiment
	public static String[][] word_and_sentiment(String[][] corpus) {
		List<String[]> word_list = new ArrayList<String[]>();
		
		// For loop that runs over each word passed in from the corpus and adds it to the list with its sentiment
		for (int x = 0; x < corpus.length; x++) {
			String[] holder = corpus[x][0].split(" ");		// Only looks at the first part of array that contains words
			for (int y=0; y<holder.length; y++){
				String[] holder2 = new String[2];
				holder2[0] = holder[y];
				holder2[1] = corpus[x][1];
				word_list.add(holder2);
			}
		}
		
		Iterator<String[]> hold_word = word_list.iterator();
		String[][] final_list = new String[word_list.size()][2];
		

		for(int x=0; x<word_list.size(); x++){
			String[] holder2 = hold_word.next();
			final_list[x][0] = holder2[0];	// Pass the individual word
			final_list[x][1] = holder2[1];	// Pass the sentiment
		}
		
		return final_list;
	}
	

	// This function contains a list of the words in a 1D array
	// It breaks up the tweet element of the corpus and removes the 
	public static String[] wordlist(String[][] corpus) {
		List<String> word_list = new ArrayList<String>();
		
		// For loop that runs over each word passed in from the corpus and adds it to the list
		for (int x = 0; x < corpus.length; x++) {
			String[] holder = corpus[x][0].split(" ");		// Only looks at the first part of array that contains words
			for (int y=0; y<holder.length; y++){
				word_list.add(holder[y]);
			}
		}
		
		Iterator<String> hold_word = word_list.iterator();
		String[] final_list = new String[word_list.size()];
		for(int x=0; x<final_list.length; x++){
			final_list[x] = hold_word.next();
			hold_word.remove();
		}
		
		return final_list;
	}

	
	// Counts the occurrences of each word in the test arrays
	public static Object[][] getWordFrequency(String[][] corpus) {
		String[] list = wordlist(corpus);
		Map<String, Integer> occurences = new HashMap<String, Integer>();
		
		for(String single_word : list){
				Integer old_value = occurences.get(single_word);
				if(old_value==null){
					occurences.put(single_word, 1);
				}else{
					occurences.put(single_word, old_value+1);
			}
		}
		
		//This is used to delete blanks from the count
		if(occurences.containsKey("")){
			occurences.remove("");
		}
		
		Object[][] word_count_array = new Object[occurences.size()][2]; //Will hold string for word and Integer for counter
		
		//For loop runs over the x marker in list and y which marks creating position in the new object array
		for(int x=0, y=0; x<list.length && y<word_count_array.length; x++){
			// If it contains the key it will add to the new array, this stops words already gone from being counted
			if(occurences.containsKey(list[x])){
				word_count_array[y][0] = list[x];
				word_count_array[y][1] = occurences.remove(list[x]);
				y++;
			}
		}
		/*demo for the counter and now held in word_count_array	
		for(int x=0; x<word_count_array.length; x++){
		System.out.println(word_count_array[x][0] + " : " + word_count_array[x][1]);
		}
		 */		
		
		return word_count_array;
	}

	
	public static double NaiveBayesCalculator(String tweet, Object[][] corpus){
		// 0.5(Prior)*(the amount of times the token appears in the class you're calculating for + 1) 
		//   		   / the amount of tokens in that class + the total amount of tokens in both classes
		
		//Used to find the total positive, negative and overall word counts
		double total_pos = 0;
		double total_neg = 0;
		double total_words = 0;
		
		
		//Counting totals for each Positive & Negative
		for(int x=0; x<corpus.length; x++){
			total_pos += (int) corpus[x][1];
			total_neg += (int) corpus[x][2];			
		}
		
		total_words = total_neg+total_pos;		
		double naive_bayes_pos = Math.log(total_pos/total_words);		// This is the prior for the positive
		double naive_bayes_neg = Math.log(total_neg/total_words);		// This is the prior for the negative
		double single_pos_word = 0; // This will hold the total positive for a single word	
		double single_neg_word = 0;
		
		String[] tweet_holder = tweet.split(" ");
		
		//Loop through tweet
		for(int x=0; x<tweet_holder.length; x++){
			//Loops through corpus
			for(int y=0; y<corpus.length; y++){
				//checks if the tweet matches a word in the corpus
				if(tweet_holder[x].equals(corpus[y][0])){
					single_pos_word = (double)(int) corpus[y][1];		//Pass the positive total into it
					single_neg_word = (double)(int) corpus[y][2];
					//total_word_appearances = (int) single_pos_word + (int) corpus[y][2];  //Gets total occurances of that word
					//System.out.println("Single Word : " + single_pos_word);
					break;
				}
			}
			//need to calculate for that single word first here
			naive_bayes_pos += Math.log((single_pos_word+1)/(total_pos + 1));
			naive_bayes_neg += Math.log((single_neg_word+1)/(total_neg + 1));
			System.out.println(naive_bayes_pos);
			System.out.println(naive_bayes_neg);
			//System.out.println("Total pos : " + total_pos + " Total Words: " + total_words);
		}
		
		//naive_bayes = prior*(naive_bayes);
		
		if(naive_bayes_pos>naive_bayes_neg){
			return 1;
		}else if(naive_bayes_pos<naive_bayes_neg){
			return -1;
		}else{
			return 0;
		}
			

		
	}
}
