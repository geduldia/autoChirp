package autoChirp.applications;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import autoChirp.DBConnector;



public class createTestDBApp {
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		String outputPath = "C:/sqlite/";
		String dbFileName = "autoChirp.db";
				
		DBConnector.connect(outputPath + dbFileName);		// conntect to output database
		DBConnector.createOutputTables();	
		DBConnector.insertURL("https://de.wikipedia.org/wiki/Universität_zu_Köln", 1);
		DBConnector.insertURL("https://de.wikipedia.org/wiki/Universität_zu_Köln", 2);
		DBConnector.insertURL("https://de.wikipedia.org/wiki/Geschichte_der_Stadt_Köln", 1);
		DBConnector.insertURL("https://de.wikipedia.org/wiki/Woody_Allen", 3);	
		DBConnector.insertURL("https://en.wikipedia.org/wiki/University_of_Cologne", 1);
		DBConnector.insertURL("https://en.wikipedia.org/wiki/Woody_Allen", 3);	
	}
}
