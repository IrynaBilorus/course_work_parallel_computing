package org.cw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexCreator extends Thread {

	String fileName;
	InvertedIndex index;
	List<String> excludedWords = Arrays.asList("a", "the", "on", "in", "at", "by", "do", "will", "is", "are", "from", "of", "to", "and", "or");

	protected IndexCreator(String fileName, InvertedIndex index) {
		super();
		this.fileName = fileName;
		this.index = index;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void setIndex(InvertedIndex index) {
		this.index = index;
	}

	public void run() {
		try {
			long time0 = System.currentTimeMillis();
			List<String> uniqueWords = new ArrayList<>();
			
			String fileContent = new String(Files.readAllBytes(Paths.get(fileName)));
			fileContent = fileContent
					.replaceAll("-", " ")
					.replaceAll(".", "")
					.replaceAll("?", "")
					.replaceAll("!", "")
					.replaceAll(",", "")
					.replaceAll(":", "")
					.replaceAll("(", "")
					.replaceAll(")", "")
					.toLowerCase();
			
			String[] words = fileContent.split("\\s");
			
			for(String word : words) {
				if(!excludedWords.contains(word) && !uniqueWords.contains(word)) {
					uniqueWords.add(word);
				}
			}
			long time1 = System.currentTimeMillis();
			System.out.println("Time for parsing: " + (time1-time0));
			
			index.addToIndex(fileName, uniqueWords);	
			
			long time2 = System.currentTimeMillis();
			
			System.out.println("Time for adding to index: " + (time2-time1));
			System.out.println("Time creating index: " + (time2-time0));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
