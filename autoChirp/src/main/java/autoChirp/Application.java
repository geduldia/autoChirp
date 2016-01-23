package autoChirp;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

	@Value("${dbFilePath}")
	private String dbFilePath;

	@Value("${createDatabaseFile}")
	private String createDatabaseFile;

	public static void main(String[] args) throws IOException {

		ApplicationContext ctx = SpringApplication.run(Application.class, args);

		System.out.println("Let's inspect the beans provided by Spring Boot:");

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
		

	}
	@PostConstruct
	private void connectDB(){
		System.out.println(dbFilePath);
		File file = new File(dbFilePath);

		if (!file.exists()) {
			DBConnector.connect(dbFilePath);
			DBConnector.createOutputTables(createDatabaseFile);
		} else {
			DBConnector.connect(dbFilePath);
		}
	}

}
