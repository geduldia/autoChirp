package autoChirp.tweetCreation.Parser;

import autoChirp.tweetCreation.Document;

/**
 * @author geduldia
 * 
 * A simple Interface for a Parser (e.g. WikipediaParser)
 * It just ensures that each Parser has to return a Document-Object consisting of text, title, url and language.
 *
 */

public interface Parser {
	
	
	
	/**
	 * creates a Document-Object from an url including text title, url and langauge
	 * @param url - to parse
	 * @return
	 */
	public Document parse(String url);
	
	
	

}
