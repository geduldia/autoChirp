
package autoChirp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import autoChirp.tweetCreation.Tweet;
import autoChirp.tweetCreation.TweetGroup;
import autoChirp.tweeting.TweetScheduler;

public class TweetWorkflow {
	
	/**
	 * @author Alena
	 * 
	 * junit test-class for the twitter-workflow
	 * 
	 * 
	 */

	private static String dbPath = "src/test/resources/";
	private static String dbFileName = "autoChirp.db";
	private static String dbCreationFileName = "src/test/resources/createDatabaseFile.sql";

	@BeforeClass
	public static void dbConnection() {
		DBConnector.connect(dbPath + dbFileName);
		DBConnector.createOutputTables(dbCreationFileName);
	}

	@Test
	public void scheduleAllNewTweets() throws IOException {
		//read twitter configs from file
		String[] secrets = new String[5];
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader("src/test/resources/twitter_secrets.txt"));
			String line = in.readLine();
			int i = 0;
			while(line != null){
				secrets[i] = line.split(":")[1].trim();
				i++;
				line= in.readLine();
			}
			in.close();
		} catch (IOException e) {
			System.out.println("twitter_secretes.txt is missing");
			return;
		}
		//insert App-Configs in DB
		DBConnector.insertTwitterConfiguration("", secrets[3], secrets[4]);
		
		//insert new user
		int user_id = DBConnector.insertNewUser(secrets[0], secrets[1], secrets[2]);
		
		//generate test-tweets
		LocalDateTime ldt = LocalDateTime.now();
		ldt = ldt.plusSeconds(10);
		String tweetDate =  ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		List<Tweet> tweets = new ArrayList<Tweet>();
		tweets.add(new Tweet(tweetDate, "Grundstücksverkehrsgenehmigungszuständigkeitsüber-tragungsverordnung"));
		tweets.add(new Tweet(tweetDate, "Ich geh nur mal kurz raus und bleibe vielleicht eine Weile"));
		TweetGroup group = new TweetGroup("test_title");	
		group.setTweets(tweets);
		List<Integer> user_ids = new ArrayList<Integer>();
		user_ids.add(user_id);
		
		//insert test-tweets
		DBConnector.insertTweets("test_url", group, user_ids);
		
		// update group-status to enabled = true
		DBConnector.updateGroupStatus(1, true);

		// get all new enabled tweets
		Map<Integer, List<Tweet>> allNewTweets = DBConnector.getAllNewTweets();
		
		//schedule tweets
		//TweetScheduler.scheduleInitialTweets(allNewTweets);
        
		
		//program has to run until all tweets are tweeted
		LocalDateTime date = LocalDateTime.parse(tweetDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		date = date.plusSeconds(10);
		boolean stop = false;
		while(!stop){
			LocalDateTime now = LocalDateTime.now();
			if(now.isAfter(date)){
				stop = true;
			}
		}
	}

	@Test
	public void scheduleTweetsForUser(){
		//read twitter configs from file
				String[] secrets = new String[5];
				BufferedReader in;
				try {
					in = new BufferedReader(new FileReader("src/test/resources/twitter_secrets.txt"));
					String line = in.readLine();
					int i = 0;
					while(line != null){
						secrets[i] = line.split(":")[1].trim();
						i++;
						line= in.readLine();
					}
					in.close();
				} catch (IOException e) {
					System.out.println("twitter_secretes.txt is missing");
					return;
				}
				//insert App-Configs in DB
				DBConnector.insertTwitterConfiguration("", secrets[3], secrets[4]);
				//insert new user
				int user_id = DBConnector.insertNewUser(secrets[0], secrets[1], secrets[2]);
				//generate test-tweets
				LocalDateTime ldt = LocalDateTime.now();
				ldt = ldt.plusSeconds(10);
				String tweetDate =  ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				List<Tweet> tweets = new ArrayList<Tweet>();
				tweets.add(new Tweet(tweetDate, "Test-Tweet3"));
				TweetGroup group = new TweetGroup("test_title");	
				group.setTweets(tweets);
				List<Integer> user_ids = new ArrayList<Integer>();
				user_ids.add(user_id);
				
				//insert test-tweets
				DBConnector.insertTweets("test_url", group, user_ids);
				
				// update group-status to enabled = true
				DBConnector.updateGroupStatus(1, true);

				// get all new enabled tweets
				List<Tweet> newTweets = DBConnector.getAllNewTweetsForUser(user_id, 1);
				
				//schedule tweets
				//TweetScheduler.scheduleGroup(newTweets, user_id);
		        
				
				//program has to run until all tweets are tweeted
				LocalDateTime date = LocalDateTime.parse(tweetDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				date = date.plusSeconds(10);
				boolean stop = false;
				while(!stop){
					LocalDateTime now = LocalDateTime.now();
					if(now.isAfter(date)){
						stop = true;
					}
				}
	}
}
