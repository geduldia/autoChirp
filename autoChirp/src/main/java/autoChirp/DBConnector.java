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

/**
 * 
 * @author geduldia
 * 
 * the database interface DB-i/o)
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
	public static Connection connect(String dbFilePath) throws SQLException,
			ClassNotFoundException {

		// register the driver
		Class.forName("org.sqlite.JDBC");

		connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
		System.out.println("Database " + dbFilePath + " successfully opened");

		return connection;
	}


	/**
	 * creates (and overrides) output-tables defined in createDatabaseFile.sql
	 */
	public static void createOutputTables() {
		StringBuffer sql = new StringBuffer();;
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader("src/test/resources/createDatabaseFile.sql"));
			String line = in.readLine();
			while(line != null){
				sql.append(line+"\n");
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
	 * @param url
	 * @param user_id
	 * 
	 * inserts url ans user_id in wikipedia-table
	 */
	public static void insertURL(String url, int user_id) {
		try {
			connection.setAutoCommit(false);
			Statement stmt = connection.createStatement();
			String sql = "INSERT INTO wikipedia (url, user_id) VALUES ('"+url+"', "+"'"+user_id+"'"+")";
			stmt.executeUpdate(sql);
			stmt.close();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @return all urls and user_ids from wikipedia-table
	 */
	public static Map<String,List<Integer>>  getURLs() {
		Map<String, List<Integer>> urls = new HashMap<String, List<Integer>>();
		try {
			connection.setAutoCommit(false);
			Statement stmt = connection.createStatement();
			String sql = "SELECT url, user_id FROM wikipedia";
			ResultSet result = stmt.executeQuery(sql);
			while(result.next()){
				//urls.put(result.getString(1));
				String url = result.getString(1);
				List<Integer> ids = urls.get(url);
				if(ids == null) ids = new ArrayList<Integer>();
				ids.add(result.getInt(2));
				urls.put(url, ids);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return urls;
	}

	public static void addTweets(Map<String, List<String>> tweetsByDate,List<Integer> user_ids, String title) {
		try {
			connection.setAutoCommit(false);
			PreparedStatement prep = connection.prepareStatement("INSERT INTO groups(user_id, group_name, enabled) VALUES(?,?,?)");
			for (int user : user_ids) {
				prep.setInt(1, user);
				prep.setString(2, title);
				prep.setBoolean(3, false);
				prep.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
