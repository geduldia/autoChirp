package autoChirp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.format.datetime.joda.LocalDateTimeParser;

public class TwitterTest {
	
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
	public void startApplicationTwitterTest() throws IOException {

		//insert a new user
		int user_id = DBConnector.insertNewUser("tw_handle", "token", "tokensecret");
		//generate test-tweets
		Map<String, List<String>> testTweetsByDate = new HashMap<String, List<String>>();
		List<String> testTweets = Arrays.asList("tweet1", "tweet2", "tweet3");
		testTweetsByDate.put("2016-01-14 12:33:00", testTweets);
		testTweets = Arrays.asList("next_tweet1", "next_tweet2");
		testTweetsByDate.put("2016-01-14 12:33:05", testTweets);
			
		List<Integer> user_ids = new ArrayList<Integer>();
		user_ids.add(user_id);
		
		DBConnector.insertTweets("test_url", testTweetsByDate, user_ids, "test_title");
		// update group-status to enabled = true
		DBConnector.updateGroupStatus(1, true);
		// get all new enabled tweets
		Map<Integer, Map<String, List<String>>> allNewTweets = DBConnector.getAllNewTweets();
		System.out.println(allNewTweets);
		
		//schedule tweets
		TweetScheduler.scheduleInitialTweets(allNewTweets);
	}

}
