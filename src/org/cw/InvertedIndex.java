package org.cw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvertedIndex {

	Map<String, List<Integer>> index = new HashMap<>();
	
	Map<Integer, String> files = new HashMap<>();
	
	public synchronized void addToIndex(String file, List<String> words) {
		int fileID = files.size();
		files.put(fileID, file);
		for(String word : words) {
			if(index.containsKey(word)) {
				index.get(word).add(fileID);
			} else {
				index.put(word, Arrays.asList(fileID));
			}
		}
	}
	
	public List<String> searchFilesByWord(String word){
		List<String> fileNames = new ArrayList<>();
		if (index.containsKey(word)) {
			List<Integer> fileIDs = index.get(word);
			for(Integer fileID : fileIDs) {
				fileNames.add(files.get(fileID));
			}
		}
		
		return fileNames;
	}
	
}
