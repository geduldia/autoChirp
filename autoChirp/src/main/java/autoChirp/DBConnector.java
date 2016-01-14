package autoChirp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Alena
 * 
 *         A class for database input/output. Includes methods to write and read
 *         the database
 * 
 */
public class DBConnector {

	private static Connection connection;

	/**
	 * connects to a database
	 * 
	 * @param dbFilePath
	 * @return connection
	 * 
	 */
	public static Connection connect(String dbFilePath) {
		// register the driver
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.out.print("DBConnector.connect: ");
			e.printStackTrace();
			System.exit(0);
		}

		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
		} catch (SQLException e) {
			System.out.print("DBConnector.connect: ");
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("Database '" + dbFilePath + "' successfully opened");
		return connection;
	}

	/**
	 * creates (and overrides) output-tables defined in dbCreationFileName
	 * 
	 * @param dbCreationFileName
	 *
	 */
	public static void createOutputTables(String dbCreationFileName) {
		//read creationFile
		StringBuffer sql = new StringBuffer();
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(dbCreationFileName));
			String line = in.readLine();
			while (line != null) {
				sql.append(line + "\n");
				line = in.readLine();
			}
			in.close();
		} catch (IOException e) {
			System.out.print("DBConnector.createOututTables: couldnt create database");
			e.printStackTrace();
			System.exit(0);
		}
		try {
			connection.setAutoCommit(false);
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql.toString());
			stmt.close();
			connection.commit();
			System.out.println("Initialized new output-database.");
		} catch (SQLException e) {
			System.out.print("DBConnector.createOututTables: couldnt create database");
			e.printStackTrace();
		}
		
	}

	/**
	 * inserts an url with user_id into wikipedia-table
	 * 
	 * @param url
	 * @param user_id
	 * @return returns true if insertion was successful
	 * 
	 */
	public static boolean isertUrl(String url, int user_id) {
		try {
			connection.setAutoCommit(false);
			Statement stmt = connection.createStatement();
			String sql = "INSERT INTO wikipedia (url, user_id) VALUES ('" + url + "', " + "'" + user_id + "'" + ")";
			stmt.executeUpdate(sql);
			stmt.close();
			connection.commit();
		} catch (SQLException e) {
			if (e.getMessage().equals("columns user_id, url are not unique")) {
				System.out.println("Already in DB: " + url + " with user UserID " + user_id);
				return true;
			} else {
				System.out.println("DBConnector.insertUrl:  couldnt insert urls");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * searches for new urls to parse in wikipedia-table
	 * 
	 * @return map of urls (key) and associated user_ids (value) from
	 *         wikipedia-table
	 */
	public static Map<String, List<Integer>> getUrls() {
		Map<String, List<Integer>> toReturn = new HashMap<String, List<Integer>>();
		ResultSet result = null;
		Statement stmt = null;
		try {
			connection.setAutoCommit(false);
			stmt = connection.createStatement();
			String sql = "SELECT url, user_id FROM wikipedia";
			result = stmt.executeQuery(sql);
		} catch (SQLException e) {
			System.out.print("DBconnector.getUrls: Couldnt read from wikipedia-table");
			e.printStackTrace();
		}
		try {
			while (result.next()) {
				String url = result.getString(1);
				List<Integer> ids = toReturn.get(url);
				if (ids == null)
					ids = new ArrayList<Integer>();
				ids.add(result.getInt(2));
				toReturn.put(url, ids);
			}
		} catch (SQLException e) {
			System.out.print("DBConnector.getUrls: no urls to parse. wikipedia is empty");
		}
		try {
			stmt.close();
			connection.commit();
		} catch (SQLException e) {
			System.out.print("DBConnector.getUrls: ");
			e.printStackTrace();
		}
		return toReturn;
		//TODO  delete parsed urls
	}

	/**
	 * Writes the extracted tweets from a single document into the database
	 * 
	 * @param url
	 *            - the url of the document
	 * @param tweetsByDate
	 *            - A map of dates (key) and the associated list of tweets
	 * @param user_ids
	 *            - the list of all users who imported the document
	 * @param title
	 *            - The title of the current document
	 * @return returns true if insertion was successful
	 * 
	 * 
	 */
	public static boolean insertTweets(String url, Map<String, List<String>> tweetsByDate, List<Integer> user_ids,
			String title) {
		try {
			connection.setAutoCommit(false);
			PreparedStatement prepUsers = connection
					.prepareStatement("INSERT INTO groups(user_id, group_name, url, enabled) VALUES(?,?,?,?)");
			PreparedStatement prepTweets = connection
					.prepareStatement("INSERT INTO tweets(user_id, group_id, scheduled_date, tweet) VALUES(?,?,?,?)");
			for (int user : user_ids) {
				prepUsers.setInt(1, user);
				prepUsers.setString(2, title);
				prepUsers.setString(3, url);
				prepUsers.setBoolean(4, false);
				prepUsers.executeUpdate();
				Statement stmt = connection.createStatement();
				String sql = "SELECT group_id FROM groups WHERE url = '" + url + "' AND user_id = '" + user + "';";
				ResultSet result = stmt.executeQuery(sql);
				int group_id = result.getInt(1);
				for (String date : tweetsByDate.keySet()) {
					for (String tweet : tweetsByDate.get(date)) {
						prepTweets.setInt(1, user);
						prepTweets.setInt(2, group_id);
						prepTweets.setString(3, date);
						prepTweets.setString(4, tweet);
						prepTweets.executeUpdate();
					}
				}
			}
			prepUsers.close();
			prepTweets.close();
			connection.commit();
		} catch (Exception e) {
			System.out.print("DBConnector.insertTweets: Couldnt insert tweets ");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * updates the field 'enabled' in groups-table
	 * 
	 * @param group_id
	 * @param enabled
	 * @return returns true if update was successful
	 */
	public static boolean updateGroupStatus(int group_id, boolean enabled) {
		try {
			connection.setAutoCommit(false);
			Statement stmt = connection.createStatement();
			String sql = "UPDATE groups SET enabled = '" + enabled + "' WHERE (group_id = '" + group_id + "')";
			stmt.executeUpdate(sql);
			stmt.close();
			connection.commit();
			// TODO schedule tweets
		} catch (SQLException e) {
			System.out.println("DBConnector.updateGroupStatus: couldnt update group-status");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * inserts the twitter-configuration parameters for this application
	 * 
	 * @param twitter_callback_url
	 * @param twitter_consumer_key
	 * @param twitter_consumer_secret
	 * @return returns true if insertion was successful
	 */
	public static boolean insertTwitterConfiguration(String twitter_callback_url, String twitter_consumer_key,
			String twitter_consumer_secret) {
		try {
			connection.setAutoCommit(false);
			Statement stmt = connection.createStatement();
			String sql = "INSERT INTO configuration (twitter_callback_url, twitter_consumer_key, twitter_consumer_secret) VALUES('"
					+ twitter_callback_url + "','" + twitter_consumer_key + "','" + twitter_consumer_secret + "')";
			stmt.execute(sql);
			stmt.close();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("DBConnector.insertTweitterConfiguration: couldnt insert tweitter-config");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * reads the twitter-configuration of this app
	 * 
	 * @return String-Array with twitter_callback_url (0), twitter_consumer_key
	 *         (1) twitter_consumer_secret (2)
	 */
	public static String[] getAppConfig() {
		try {
			connection.setAutoCommit(false);
			Statement stmt = connection.createStatement();
			String sql = "SELECT twitter_callback_url, twitter_consumer_key, twitter_consumer_secret FROM configuration";
			ResultSet result = stmt.executeQuery(sql);
			String[] toReturn = new String[3];
			toReturn[0] = result.getString(1);
			toReturn[1] = result.getString(2);
			toReturn[2] = result.getString(3);
			stmt.close();
			connection.commit();
			return toReturn;
		} catch (SQLException e) {
			System.out.print("DBConnctor.getTwitterConfiguration: ");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * creates a new user in the users-table
	 * 
	 * @param twitter_handle
	 * @param oauthToken
	 * @param oauthTokenSecret
	 * @return the user_id of the new user or -1 if insertion was not successful
	 */
	public static int insertNewUser(String twitter_handle, String oauthToken, String oauthTokenSecret) {
		try {
			connection.setAutoCommit(false);
			Statement stmt = connection.createStatement();
			String sql = "INSERT INTO users (twitter_handle, oauth_token, oauth_token_secret) VALUES ('"
					+ twitter_handle + "', " + "'" + oauthToken + "', " + "'" + oauthTokenSecret + "' )";
			stmt.executeUpdate(sql);
			stmt.close();
			connection.commit();
			stmt = connection.createStatement();
			sql = "SELECT user_id FROM users WHERE (twitter_handle = '" + twitter_handle + "' AND oauth_token = '"
					+ oauthToken + "' AND oauth_token_secret ='" + oauthTokenSecret + "')";
			ResultSet result = stmt.executeQuery(sql);
			int toReturn = result.getInt(1);
			stmt.close();
			connection.commit();
			return toReturn;
		} catch (SQLException e) {
			System.out.println("DBConnector.insertNewUser: couldnt insert the new user " + twitter_handle);
			e.printStackTrace();
			return -1;
		}

	}

	/**
	 * reads the user-config of a specific user
	 * 
	 * @param userID
	 * @return String-Array with twitter_handle (0), oauth_token (1) and
	 *         oauth_token_secret (2)
	 */
	public static String[] getUserConfig(int userID) {
		try {
			connection.setAutoCommit(false);
			String sql = "SELECT twitter_handle, oauth_token, oauth_token_secret FROM users WHERE user_id = '" + userID
					+ "';";
			Statement stmt = connection.createStatement();
			ResultSet result = stmt.executeQuery(sql);
			String[] toReturn = new String[3];
			toReturn[0] = result.getString(1);
			toReturn[1] = result.getString(2);
			toReturn[2] = result.getString(3);
			stmt.close();
			connection.commit();
			return toReturn;
		} catch (Exception e) {
			System.out.println("DBConnector: couldnt read config for user_id " + userID);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * selects all new (= not yet scheduled) tweets for a specific user and
	 * group
	 * 
	 * @param user_id
	 * @param group_id
	 * @return A map of dates (key) and the associated list of tweets (value)
	 */
	public static Map<String, List<String>> getAllNewTweetsForUser(int user_id, int group_id) {
		Map<String, List<String>> toReturn = new HashMap<String, List<String>>();
		try {
			connection.setAutoCommit(false);
			Statement stmt = connection.createStatement();
			String sql = "SELECT * FROM tweets WHERE (group_id = '" + group_id + "' AND user_id='" + user_id + "')";
			ResultSet tweets = stmt.executeQuery(sql);
			try {
				while (tweets.next()) {
					String date = tweets.getString(4);
					String tweet = tweets.getString(5);
					List<String> tweetsForDate = toReturn.get(date);
					if (tweetsForDate == null) {
						tweetsForDate = new ArrayList<String>();
					}
					tweetsForDate.add(tweet);
					toReturn.put(date, tweetsForDate);
				}
			} catch (SQLException e) {
				System.out.println("DBConnector.getAllNewTweets: no new tweets to schedule");
			}
			
			stmt.close();
			connection.commit();
		} catch (SQLException e) {
			System.out.println("DBConnector.getAllNewTweets: ");
			e.printStackTrace();
		}
		return toReturn;
	}

	/**
	 * selects all new (not yet scheduled) tweets. This method should be
	 * executed once after application-start
	 * 
	 * @return A Map of user_ids (key) and a map of tweets sorted by their
	 *         tweetdate (value)
	 */

	public static Map<Integer, Map<String, List<String>>> getAllNewTweets() {
		Map<Integer, Map<String, List<String>>> toReturn = new HashMap<Integer, Map<String, List<String>>>();
		ResultSet group_ids = null;
		try {
			connection.setAutoCommit(true);
			String sql = "SELECT group_id, user_id FROM groups WHERE (enabled = 'true')";
			Statement stmt = connection.createStatement();
			group_ids = stmt.executeQuery(sql);
		} catch (SQLException e) {
			System.out.println("DBConntor.getAllNewTweets: couldnt read from table groups");
			e.printStackTrace();
			return toReturn;
		}
		// Select all enabled group_ids with user
		try {
			while (group_ids.next()) {
				int group_id = group_ids.getInt(1);
				int user_id = group_ids.getInt(2);
				ResultSet tweets = null;
				try {
					Statement stmt2 = connection.createStatement();
					// Select all tweets for the current group_id
					String sql2 = "SELECT * FROM tweets WHERE (group_id = '" + group_id + "' AND user_id='" + user_id + "')";
					tweets = stmt2.executeQuery(sql2);
				} catch (Exception e) {
					System.out.println("DBConntor.getAllNewTweets: couldnt read from table tweets");
					e.printStackTrace();
					return toReturn;
				}
				while (tweets.next()) {
					String date = tweets.getString(4);
					String tweet = tweets.getString(5);
					Map<String, List<String>> tweetsForUser = toReturn.get(user_id);
					if (tweetsForUser == null) {
						tweetsForUser = new HashMap<String, List<String>>();
					}
					List<String> tweetsForDate = tweetsForUser.get(date);
					if (tweetsForDate == null) {
						tweetsForDate = new ArrayList<String>();
					}
					tweetsForDate.add(tweet);
					tweetsForUser.put(date, tweetsForDate);
					toReturn.put(user_id, tweetsForUser);
				}
			}
		} catch (SQLException e) {
			System.out.println("DBConnector.readAllNewTweets: no new Tweets to schedule");
			e.printStackTrace();
			return toReturn;
		}
		return toReturn;
	}
}
