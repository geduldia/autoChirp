package autoChirp;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class DBConnectorTest {
	
	private static String dbPath = "src/test/resources";
	private static String dbFileName = "autoChirp.db";
	private static String dbCreationFileName = "src/test/resources/createDatabaseFile.sql";
	
	@BeforeClass
	public static void dbConnection() {
		try {
			DBConnector.connect(dbPath + dbFileName);
			DBConnector.createOutputTables(dbCreationFileName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void insertAndGetUrlsTest(){
		DBConnector.insertURL("https://en.wikipedia.org/wiki/History_of_Denmark", 1);
		DBConnector.insertURL("https://en.wikipedia.org/wiki/History_of_Denmark",1);
		DBConnector.insertURL("https://en.wikipedia.org/wiki/History_of_Denmark", 2);
		DBConnector.insertURL("https://de.wikipedia.org/wiki/Britney_Spears", 2);
		Map<String, List<Integer>> urlsAnUserIDs = DBConnector.getURLs();
		Iterator<String> iterator = urlsAnUserIDs.keySet().iterator();
		Assert.assertEquals(2, urlsAnUserIDs.size());
		String url = iterator.next();
		Assert.assertEquals("https://en.wikipedia.org/wiki/History_of_Denmark", url);
		Assert.assertEquals(2,urlsAnUserIDs.get(url).size());
		url = iterator.next();
		Assert.assertEquals("https://de.wikipedia.org/wiki/Britney_Spears",url);
		Assert.assertEquals(1, urlsAnUserIDs.get(url).size());
	}
	
	@Test
	public void addTweetsTest() throws SQLException{
		Map<String, List<String>> testTweetsByDate = new TreeMap<String,List<String>>();
		List<String> tweets = Arrays.asList("tweet1", "tweet2", "tweet3");
		testTweetsByDate.put("2016-02-03 12:00:00", tweets);
		testTweetsByDate.put("2016-02-03 15:00:00", tweets);
		testTweetsByDate.put("1999-11-12 13:23:12", tweets);
		List<Integer> userIds = Arrays.asList(1,2,3);
		DBConnector.insertTweets("test_URL1", testTweetsByDate, userIds, "test_title1");
		DBConnector.insertTweets("test_URL2", testTweetsByDate, userIds, "test_title2");
		String sql = "SELECT user_id, group_name, url, enabled FROM groups";
		Statement stmt = DBConnector.connection.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		result.next();
		Assert.assertEquals(result.getInt(1),1);
		Assert.assertEquals(result.getString(2),"test_title1");
		Assert.assertEquals(result.getString(3), "test_URL1");
		Assert.assertEquals(result.getBoolean(4), false);
		result.next();
		Assert.assertEquals(result.getInt(1),2);
		result.next();
		Assert.assertEquals(result.getInt(1),3);
		result.next();
		Assert.assertEquals(result.getInt(1), 1);
		Assert.assertEquals(result.getString(2),"test_title2");
		Assert.assertEquals(result.getString(3),"test_URL2");
		Assert.assertEquals(result.getBoolean(4),false);		
	}
	
	@Test
	public void insertAndGetOAuthTokenTest(){
		DBConnector.insertNewUser("twitter_handle", "oauthToken", "oauthTokenSecret");
		DBConnector.insertNewUser("twitter_handle2", "oauthToken2", "oauthTokenSecret2");
		String[] userInfo = DBConnector.getOAuthTokenForUserID(1);
		Assert.assertEquals("twitter_handle", userInfo[0]);
		Assert.assertEquals("oauthToken", userInfo[1]);
		Assert.assertEquals("oauthTokenSecret", userInfo[2]);
		userInfo = DBConnector.getOAuthTokenForUserID(2);
		Assert.assertEquals("twitter_handle2", userInfo[0]);
		Assert.assertEquals("oauthToken2", userInfo[1]);
		Assert.assertEquals("oauthTokenSecret2", userInfo[2]);
	}
}
