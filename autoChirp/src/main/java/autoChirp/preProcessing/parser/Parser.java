package autoChirp.preProcessing.parser;

import autoChirp.preProcessing.Document;

/**
 * @author Alena Geduldig
 * 
 * A simple Interface for a Parser (e.g. WikipediaParser)
 * Ensures that each Parser has to return a Document-Object consisting of text, title, url and language.
 *
 */

public interface Parser {
	
	
	
	/**
	 * creates a Document-Object from an url including text, title, url and langauge
	 * @param url 
	 * @return a document object
	 */
	public Document parse(String url);
	
	
	

}
