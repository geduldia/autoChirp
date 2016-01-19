package autoChirp;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import autoChirp.tweetCreation.Tweet;
import autoChirp.tweetCreation.TweetGroup;

/**
 * 
 * @author Alena
 * 
 * Junit test-class for the DBConnector functionality
 *
 */

public class DBConnectorTest {

	private static String dbPath = "src/test/resources";
	private static String dbFileName = "autoChirp.db";
	private static String dbCreationFileName = "src/test/resources/createDatabaseFile.sql";

	@BeforeClass
	//connect and reset database
	public static void dbConnection() {
		DBConnector.connect(dbPath + dbFileName);
		DBConnector.createOutputTables(dbCreationFileName);
	}

	@Test
	public void insertAndGetUrlsTest() {
		DBConnector.isertUrl("https://en.wikipedia.org/wiki/History_of_Denmark", 1);
		DBConnector.isertUrl("https://en.wikipedia.org/wiki/History_of_Denmark", 1);
		DBConnector.isertUrl("https://en.wikipedia.org/wiki/History_of_Denmark", 2);
		DBConnector.isertUrl("https://de.wikipedia.org/wiki/Britney_Spears", 2);
		Map<String, List<Integer>> urlsAnUserIDs = DBConnector.getUrls();
		Iterator<String> iterator = urlsAnUserIDs.keySet().iterator();
		Assert.assertEquals(2, urlsAnUserIDs.size());
		String url = iterator.next();
		Assert.assertEquals("https://en.wikipedia.org/wiki/History_of_Denmark", url);
		Assert.assertEquals(2, urlsAnUserIDs.get(url).size());
		url = iterator.next();
		Assert.assertEquals("https://de.wikipedia.org/wiki/Britney_Spears", url);
		Assert.assertEquals(1, urlsAnUserIDs.get(url).size());
	}

	@Test
	public void insertAndGetTweetsTest() throws SQLException {
		List<Tweet> tweets = new ArrayList<Tweet>();
		Tweet tweet = new Tweet("1999-11-12 13:23:12", "tweet1");
		tweets.add(tweet);
		tweet = new Tweet("2013-11-12 23:23:12", "tweet2");
		tweets.add(tweet);
		TweetGroup group = new TweetGroup("test_title1");
		group.setTweets(tweets);
		List<Integer> userIds = Arrays.asList(1, 2, 3);
		DBConnector.insertTweets("test_URL1", group, userIds);
		DBConnector.insertTweets("test_URL2", group, userIds);
		//TODO  read tweets
	}

	@Test
	public void insertAndGetOAuthTokenTest() {
		int user_id = DBConnector.insertNewUser("twitter_handle", "oauthToken", "oauthTokenSecret");
		Assert.assertEquals(1, user_id);
		user_id = DBConnector.insertNewUser("twitter_handle2", "oauthToken2", "oauthTokenSecret2");
		Assert.assertEquals(2, user_id);
		String[] userInfo = DBConnector.getUserConfig(1);
		Assert.assertEquals("twitter_handle", userInfo[0]);
		Assert.assertEquals("oauthToken", userInfo[1]);
		Assert.assertEquals("oauthTokenSecret", userInfo[2]);
		userInfo = DBConnector.getUserConfig(2);
		Assert.assertEquals("twitter_handle2", userInfo[0]);
		Assert.assertEquals("oauthToken2", userInfo[1]);
		Assert.assertEquals("oauthTokenSecret2", userInfo[2]);
	}

	@Test
	public void insertAndGetTwitterConfigurationTest() {
		DBConnector.insertTwitterConfiguration("test_callback_url", "test_consumer_key", "test_consumer_secret");
		String[] twitterConfig = DBConnector.getAppConfig();
		Assert.assertEquals("test_callback_url", twitterConfig[0]);
		Assert.assertEquals("test_consumer_key", twitterConfig[1]);
		Assert.assertEquals("test_consumer_secret", twitterConfig[2]);
	}
	
	@Test 
	public void insertAndGetGroups(){
		List<Integer> userIds = Arrays.asList(1, 2, 3);
		List<Tweet> tweets = new ArrayList<Tweet>();
		Tweet tweet = new Tweet("1999-11-12 13:23:12", "tweet1");
		tweets.add(tweet);
		tweet = new Tweet("2013-11-12 23:23:12", "tweet2");
		tweets.add(tweet);
		TweetGroup group = new TweetGroup("test_title1");
		group.setTweets(tweets);
		DBConnector.insertTweets("url", group, userIds);
		group = new TweetGroup("test_title2");
		tweets.add(new Tweet("2015-11-12 14:23:12", "tweet3"));
		group.setTweets(tweets);
		DBConnector.insertTweets("test_url2", group,userIds );
		List<TweetGroup> groups = DBConnector.getActiveGroupsForUser(2);
		Assert.assertTrue(groups.size() == 0);
		DBConnector.updateGroupStatus(1, true);
		groups = DBConnector.getActiveGroupsForUser(1);
		Assert.assertTrue(groups.size() != 0);
		System.out.println(groups.get(0).tweets.size());
	}
	

	
}
