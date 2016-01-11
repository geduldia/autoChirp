package autoChirp;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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

import com.fasterxml.jackson.databind.deser.std.DateDeserializers.SqlDateDeserializer;

/**
 * 
 * @author geduldia
 * 
 *         the database interface
 *
 */
public class DBConnector {

	static Connection connection;

	/**
	 * connects to a database
	 * 
	 * @param dbFilePath
	 * @return connection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * 
	 */
	public static Connection connect(String dbFilePath) throws SQLException, ClassNotFoundException {
		// register the driver
		Class.forName("org.sqlite.JDBC");

		connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
		System.out.println("Database " + dbFilePath + " successfully opened");

		return connection;
	}

	/**
	 * creates (and overrides) output-tables defined in createDatabaseFile.sql
	 *
	 */
	public static void createOutputTables(String dbCreationFile) {
		StringBuffer sql = new StringBuffer();
		;
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(dbCreationFile));
			String line = in.readLine();
			while (line != null) {
				sql.append(line + "\n");
				line = in.readLine();
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		try {
			connection.setAutoCommit(false);
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql.toString());

			stmt.close();
			connection.commit();
			System.out.println("Initialized new output-database.");
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * inserts a url and user_id into wikipedia-table
	 * 
	 * @param url
	 * @param user_id
	 * 
	 */
	public static void insertURL(String url, int user_id) {
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
				return;
			}
			e.printStackTrace();
		}
	}

	/**
	 * selects all urls to parse with the corresponding user-ids
	 * 
	 * @return urls and user_ids from wikipedia-table
	 */
	public static Map<String, List<Integer>> getURLs() {
		Map<String, List<Integer>> urls = new HashMap<String, List<Integer>>();
		try {
			connection.setAutoCommit(false);
			Statement stmt = connection.createStatement();
			String sql = "SELECT url, user_id FROM wikipedia";
			ResultSet result = stmt.executeQuery(sql);
			while (result.next()) {
				String url = result.getString(1);
				List<Integer> ids = urls.get(url);
				if (ids == null)
					ids = new ArrayList<Integer>();
				ids.add(result.getInt(2));
				urls.put(url, ids);
			}
			stmt.close();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return urls;
	}

	/**
	 * Writes the extracted tweets from a single document in the database (Fills
	 * the tables groups & tweets)
	 * 
	 * @param url
	 * @param tweetsByDate
	 *            - A map of Dates (as Strings) and the corresponding list of
	 *            tweets
	 * @param user_ids
	 *            - The list of users for the current url
	 * @param title
	 *            - The title of the current document/url
	 */
	public static void insertTweets(String url, Map<String, List<String>> tweetsByDate, List<Integer> user_ids,
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
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static void updateGroupStatus(int group_id, boolean enabled){
		try{
			connection.setAutoCommit(false);
			Statement stmt = connection.createStatement();
			String sql = "UPDATE groups SET enabled = '"+enabled+"' WHERE (group_id = '"+group_id+"')";
			stmt.executeUpdate(sql);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
	}

	public static void insertTwitterConfiguration(String twitter_callback_url, String twitter_consumer_key,
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
			e.printStackTrace();
		}

	}

	public static String[] getTwitterConfiguration() {
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
			e.printStackTrace();
			return null;
		}
	}

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
			sql = "SELECT user_id FROM users WHERE (twitter_handle = '"+twitter_handle+"' AND oauth_token = '"+oauthToken+"' AND oauth_token_secret ='"+oauthTokenSecret+"')";
			ResultSet result = stmt.executeQuery(sql);
			return result.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}

	}

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
			return toReturn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Map<Integer,Map<String,List<String>>> getAllNewTweets() {
		Map<Integer,Map<String, List<String>>> toReturn = new HashMap<Integer,Map<String, List<String>>>();
		try{
			connection.setAutoCommit(true);
			//Select all enabled group_ids with user
			String sql = "SELECT group_id, user_id FROM groups WHERE (enabled = 'true')";
			Statement stmt = connection.createStatement();
			ResultSet group_ids = stmt.executeQuery(sql);
			
			while(group_ids.next()){
				int group_id = group_ids.getInt(1);
				int user_id = group_ids.getInt(2);
				Statement stmt2 = connection.createStatement();
				//Select all tweets for the current group_id
				String sql2 = "SELECT * FROM tweets WHERE (group_id = '"+group_id+"' AND user_id='"+user_id+"')";
				ResultSet tweets = stmt2.executeQuery(sql2);
				while(tweets.next()){
					String date = tweets.getString(4);
					String tweet = tweets.getString(5);
					Map<String,List<String>> tweetsForUser = toReturn.get(user_id);
					if(tweetsForUser == null){
						tweetsForUser = new HashMap<String, List<String>>();
					}
					List<String> tweetsForDate = tweetsForUser.get(date);
					if(tweetsForDate == null){
						tweetsForDate = new ArrayList<String>();
					}
					tweetsForDate.add(tweet);
					tweetsForUser.put(date, tweetsForDate);
					toReturn.put(user_id, tweetsForUser);
				}
			}
			return toReturn;
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}
	
}
