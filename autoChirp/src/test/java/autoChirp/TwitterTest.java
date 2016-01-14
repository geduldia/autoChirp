//package autoChirp;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import autoChirp.DBConnector;
//import ch.qos.logback.core.util.Duration;
//
//import org.apache.tomcat.jni.Local;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//public class TwitterTest {
//	
//	/**
//	 * @author Alena
//	 * 
//	 * junit test-class for the twitter-workflow
//	 * 
//	 * 
//	 */
//
//	private static String dbPath = "src/test/resources/";
//	private static String dbFileName = "autoChirp.db";
//	private static String dbCreationFileName = "src/test/resources/createDatabaseFile.sql";
//
//	@BeforeClass
//	public static void dbConnection() {
//		DBConnector.connect(dbPath + dbFileName);
//		DBConnector.createOutputTables(dbCreationFileName);
//	}
//
//	@Test
//	public void initialTweedSchedulingTest() throws IOException {
//		//read twitter configs from file
//		String[] secrets = new String[5];
//		BufferedReader in;
//		try {
//			in = new BufferedReader(new FileReader("src/test/resources/twitter_secrets.txt"));
//			String line = in.readLine();
//			int i = 0;
//			while(line != null){
//				secrets[i] = line.split(":")[1].trim();
//				i++;
//				line= in.readLine();
//			}
//			in.close();
//		} catch (IOException e) {
//			System.out.println("twitter_secretes are is missing");
//			return;
//		}
//		
//		//insert App-Configs in DB
//		DBConnector.insertTwitterConfiguration("", secrets[3], secrets[4]);
//		
//		//insert new user
//		int user_id = DBConnector.insertNewUser(secrets[0], secrets[1], secrets[2]);
//		//generate test-tweets
//		String date1 = "2016-01-14 16:02:00";
//		String date2 = "2016-01-14 16:02:30";
//		Map<String, List<String>> testTweetsByDate = new HashMap<String, List<String>>();
//		List<String> testTweets = Arrays.asList("Test: Hello World");
//		testTweetsByDate.put(date1, testTweets);
//		testTweets = Arrays.asList(date1, "Test2: Hello World");
//		testTweetsByDate.put(date2, testTweets);
//			
//		List<Integer> user_ids = new ArrayList<Integer>();
//		user_ids.add(user_id);
//		
//		//insert test-tweets
//		DBConnector.insertTweets("test_url", testTweetsByDate, user_ids, "test_title");
//		
//		// update group-status to enabled = true
//		DBConnector.updateGroupStatus(1, true);
//
//		// get all new enabled tweets
//		Map<Integer, Map<String, List<String>>> allNewTweets = DBConnector.getAllNewTweets();
//		
//		//schedule tweets
//		TweetScheduler.scheduleInitialTweets(allNewTweets);
//        
//		
//		//program has to run until all tweets are tweeted
////		LocalDateTime date = LocalDateTime.parse(date2, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
////		date = date.plusSeconds(10);
////		boolean stop = false;
////		while(!stop){
////			LocalDateTime now = LocalDateTime.now();
////			if(now.isAfter(date)){
////				stop = true;
////			}
////		}
////        in = new BufferedReader(new InputStreamReader(System.in));
////    	in.readLine();
//	}
//
//	@Test
//	public void tweetSchedulingTest(){
//		
//	}
//}
