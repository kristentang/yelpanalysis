package hw6;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.List; 

public class YelpAnalysis {

	public static void main (String[] args) throws IOException{
		double start = System.currentTimeMillis();
		// Stores businesses as objects, add to list and queue, and calculate corpusDF Count		
		Map<String,Integer> corpusDFCount = new HashMap <> (); // string:words & integer:number of businesses word appears in 
		ArrayList<Business> businessList = new ArrayList<>(); // stores list of businesses 
		PriorityQueue <Business> businessQueue = new PriorityQueue <>(new charCountComparator()); // sorts businesses by character count 

		Reader r = new FileReader("yelpDatasetParsed_full.txt"); // reads in text file of yelp reviews
		BufferedReader br = new BufferedReader(r);
		try {
			while (true) {
				// for every business: 
				Business b = readBusiness(br); // create a business object 
				if (b==null) // end of file and processed all businesses
					break; 
				businessList.add(b); // add business to list 
				businessQueue.add(b); // add business to queue
				addDocumentCount(corpusDFCount,b); // add words in business to corpusDFCount
			} 
		} catch (FileNotFoundException e) { // if file cannot be found 
			System.out.println("File Not Found");
			return; 
		} finally {
			r.close(); // close the stream 
			br.close(); // close the stream 
		} // end try/catch block

		// Sort businesses by character count (to match queue) 
		Collections.sort(businessList, new charCountComparator());

		// For the top 10 businesses with most review characters, output top 30 tf-idf words and scores 
		for (int i=0; i<10; i++) { 
			Map<String,Double> tfidfScoreMap = getTfidfScore(corpusDFCount, businessQueue.remove(), 5); // calculates tfidf scores for all words in the business 
			List<Map.Entry<String,Double>> tfidfScoreList = new ArrayList<>(tfidfScoreMap.entrySet()); // string: word & double: tfidf score 
			sortByTfidf(tfidfScoreList); // arrange in order of tfidf scores 
			System.out.println(businessList.get(i)); // print the business info 
			printTopWords(tfidfScoreList, 30); // prints top 30 words with highest tfidf scores and their score 
			System.out.println(); 
		}
		double end = System.currentTimeMillis(); 
		System.out.println((end-start)*.001); 
	} // end main 

	
	private static Business readBusiness(BufferedReader br) throws IOException { 
		
		String business = br.readLine();
		// returns null if has already read all the businesses 
		if (business == null) {
			return null; 
		}
		// isolate elements of business 
		String splitBusiness = business.substring(1, (business.length()-2));  
		String[] elements = splitBusiness.split(", ");
        
		String businessID = elements[0]; 
		String businessName = elements[1]; 
		String businessAddress = elements[2]; 
		String reviews = elements[3]; 
		int reviewCharCount = business.length();

        Business b = new Business(businessID, businessName, businessAddress, reviews, reviewCharCount);
		return b; 	
	}
	
	// updates corpusDFCount 
	private static void addDocumentCount(Map<String, Integer> corpusDFCount, Business b) { 
		HashSet <String> seenWords = new HashSet <String>(); 
		String[] businessWords = b.reviews.split(" "); 
		
		for (String word : businessWords) {
			seenWords.add(word); // add novel words to seenWords hash set 
		}
		
		for (String word: seenWords) {
			if (corpusDFCount.get(word) == null) { // doesn't exist in corpusDFCount
				corpusDFCount.put(word, 1); // put word in corpusDFCount 
			} else {
				corpusDFCount.replace(word, corpusDFCount.get(word)+1); // update word's value in corpusDFCO
			}
		}	
	}
	
	// calculates tfidf scores for all words in a business 
	private static Map<String, Double> getTfidfScore (Map<String, Integer> map, Business b, int n) {
		Map<String,Double> tfidfScores = new HashMap <> ();
		Map <String, Integer> seenWords = new HashMap <String, Integer>(); // string: word & integer: number of occurrences in business b 
		String[] businessWords = b.reviews.split(" "); 
		
		// calculate how many times word is in business b 
		for (String word : businessWords) { // if already in seenWords, increase occurrence count
			if (seenWords.containsKey(word)) {
				seenWords.replace(word, seenWords.get(word)+1); 
			} else { // if novel word, add to seenWords 
				seenWords.put(word, 1);
			}	
		}
		
		// calculate tfidf score for each word 
		for (Map.Entry<String,Integer> wordEntry : seenWords.entrySet() ) {
			if (map.get(wordEntry.getKey()) < n) { // if it occurs less than n times in business, tfidf score is 0 
				tfidfScores.put(wordEntry.getKey(), 0.0);
			} else {	 // tfidf = number of times word appears in business/number of documents that contain word 
				tfidfScores.put(wordEntry.getKey(), (double)(wordEntry.getValue())/map.get(wordEntry.getKey())); 
			}
		}
		return tfidfScores; 
	}
	
	// sorts map entries by tfidf scores  using comparator
	private static void sortByTfidf(List<Map.Entry<String,Double>> mapList) {
		Collections.sort(mapList, new tfidfComparator());
	}
	
	// prints out the top n words with highest tfidf scores in (word, score) format, to 2 decimal places 
	private static void printTopWords(List<Map.Entry<String,Double>> mapList, int n) {
		for (int i = 0; i < n ; i++) {
			Map.Entry<String,Double> thisEntry = mapList.get(i); 
			String k = thisEntry.getKey(); 
			double v = thisEntry.getValue(); 
			PrintStream o = System.out; 
			o.format("(%s,%.2f) ", k, v);
		}
	}
	
} // end class YelpAnalysis 

//sorts businesses by their character count 
class charCountComparator implements Comparator<Business> {

	@Override
	public int compare(Business business1, Business business2) {
		if (business1.getReviewCharCount()<business2.getReviewCharCount()) {
			return 1; 
		} else if (business1.getReviewCharCount()>business2.getReviewCharCount()) {
			return -1;
		} else {
			return 0; 
		}		
	}
}

//sorts map entries by their tfidf score 
class tfidfComparator implements Comparator<Map.Entry<String,Double>> {

	@Override
	public int compare(Map.Entry<String,Double> map1, Map.Entry<String,Double> map2) {
		if (map1.getValue() < map2.getValue()) {
			return 1; 
		} else if (map1.getValue() > map2.getValue()) {
			return -1;
		} else {
			return 0; 
		}		
	}	
}
