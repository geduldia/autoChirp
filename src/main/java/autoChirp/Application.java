package autoChirp;

import autoChirp.tweetCreation.TweetGroup;
import autoChirp.tweeting.TweetScheduler;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

/**
 * Default (SpringBoot-)Application class with main() method and minor
 * extensions: On start a persistent connection to the SQLite database is opened
 * and all relevant Tweets from that database are scheduled.
 *
 * @author Philip Schildkamp
 * @author Alena Geduldig
 */
@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

  private static Class<Application> applicationClass = Application.class;

	@Value("${autochirp.database.dbfile}")
	private String dbfile;

	@Value("${autochirp.database.schema}")
	private String schema;

	/**
	 * @param args
	 *            Command line arguments
	 * @throws IOException
	 *             IOException
	 */
	public static void main(String[] args) throws IOException {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
	}

  /**
	 * @param application
	 *            SpringApplicationBuilder object
	 * @return SpringApplicationBuilder
	 *             SpringApplicationBuilder object
	 */
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(applicationClass);
  }


	/**
	 * Open database connection and schedule relevant Tweets.
	 */
	@PostConstruct
	private void initializeApplication() {
		File file = new File(dbfile);

		if (!file.exists()) {
			DBConnector.connect(dbfile);
			DBConnector.createOutputTables(schema);
		} else {
			DBConnector.connect(dbfile);
		}

		Map<Integer, List<TweetGroup>> toSchedule = DBConnector.getAllEnabledGroups();
		for (int userID : toSchedule.keySet()) {
			for (TweetGroup group : toSchedule.get(userID)) {
				TweetScheduler.scheduleTweetsForUser(group.tweets, userID);
			}
		}
	}

}
