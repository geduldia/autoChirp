package autoChirp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.unihd.dbs.uima.annotator.heideltime.resources.Language;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceSplitter {
	private SentenceModel sentenceModel;
	private Language language;
	
	public SentenceSplitter(Language language){
		if(language == Language.GERMAN){
			setSentenceSplittingModel("data/OpenNLP_sentenceModels/de-sent.bin");
		}
		else{
			setSentenceSplittingModel("data/OpenNLP_sentenceModels/en-sent.bin");
		}
	
	}
	
	private void setSentenceSplittingModel(String model){
		InputStream modelIn = null;
		try {
			modelIn = new FileInputStream(model);
			sentenceModel = new SentenceModel(modelIn);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
				}
			}
		}
	}

	public List<String> splitIntoSentences(String text){
		String[] sentences = null;
		SentenceDetectorME detector = new SentenceDetectorME(sentenceModel);
		sentences = detector.sentDetect(text);	
		return Arrays.asList(sentences);
	}
}
