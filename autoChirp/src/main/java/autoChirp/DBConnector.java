package autoChirp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBConnector {

	static Connection connection;

	public static Connection connect(String dbFilePath) throws SQLException,
			ClassNotFoundException {
		//Connection connection;

		// register the driver
		Class.forName("org.sqlite.JDBC");

		connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
		System.out.println("Database " + dbFilePath + " successfully opened");

		return connection;
	}

	public static void createOutputTables()
			throws SQLException, IOException {
		StringBuffer sql = new StringBuffer();;
		BufferedReader in = new BufferedReader(new FileReader("src/test/resources/createDatabaseFile.sql"));
		String line = in.readLine();
		while(line != null){
			sql.append(line+"\n");
			line = in.readLine();
		}
		in.close();
		connection.setAutoCommit(false);
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(sql.toString());

		stmt.close();
		connection.commit();
		System.out.println("Initialized new output-database.");
	}

	public static void insertURL(String url, int user_id) throws SQLException{
		connection.setAutoCommit(false);
		Statement stmt = connection.createStatement();
		String sql = "INSERT INTO wikipedia (url, user_id) VALUES ('"+url+"', "+"'"+user_id+"'"+")";
		stmt.executeUpdate(sql);
		stmt.close();
		connection.commit();
	}

	public static Map<String,List<Integer>>  getURLs() throws SQLException{
		Map<String, List<Integer>> urls = new HashMap<String, List<Integer>>();
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
		return urls;
	}

	public static void addTweets(Map<String, List<String>> tweetsByDate, int user_id) {
		// TODO Auto-generated method stub

	}
}
