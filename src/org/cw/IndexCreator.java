package org.cw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IndexCreator extends Thread {

	InvertedIndex index;

	protected IndexCreator(InvertedIndex index) {
		super();
		this.index = index;
	}

	public void run() {
		while (true) {
			String fileName = index.getFileForIndex();
			if (fileName != null) {
				List<String> uniqueWords = new ArrayList<>();

				try {
					String fileContent = new String(Files.readAllBytes(Paths.get(fileName)));
					
 					fileContent = fileContent.toLowerCase().replaceAll("[^a-z0-9'\\-\\s]","");

					String[] words = fileContent.split("\\s");

					for (String word : words) {
						if (!word.trim().isEmpty() && !index.getExcludedWords().contains(word) && !uniqueWords.contains(word)) {
							uniqueWords.add(word);
						}
					}

					index.addToIndex(fileName, uniqueWords);

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				break;
			}
		}
	}
}
