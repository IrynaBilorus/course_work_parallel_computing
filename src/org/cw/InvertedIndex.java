package org.cw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InvertedIndex {

	Set<String> filesForIndex;
			
	Map<String, List<Integer>> index = new HashMap<>();
	
	Map<Integer, String> files = new HashMap<>();
	
	List<String> excludedWords = Arrays.asList("a", "the", "on", "in", "at", "by", "do", "will", "is", "are", "from",
			"of", "to", "and", "or");
	
	public List<String> getExcludedWords() {
		return excludedWords;
	}

	protected InvertedIndex(Set<String> filesForIndex) {
		super();
		this.filesForIndex = filesForIndex;
	}
	
	public synchronized String getFileForIndex() {
		String fileName = null;
		if (filesForIndex != null && !filesForIndex.isEmpty()) {
			fileName = filesForIndex.iterator().next();
			filesForIndex.remove(fileName);
		}
		return fileName;
	}
	
	public synchronized void addToIndex(String file, List<String> words) {
		int fileID = files.size();
		files.put(fileID, file);
		for(String word : words) {
			if(index.containsKey(word)) {
				index.get(word).add(fileID);
			} else {
				List<Integer> fileIDs = new ArrayList<>();
				fileIDs.add(fileID);
				index.put(word, fileIDs);
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

	
	public int size() {
		return index.size();
	}

}
