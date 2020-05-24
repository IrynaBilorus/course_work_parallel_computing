package org.cw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

	public static void main(String[] args) {

		try {
			Set<String> files = listFiles("C:/Files", 3);
			for(String fileName : files) {
				System.out.println(fileName);
			}
			
			System.out.println(files.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Set<String> listFiles(String dir, int depth) throws IOException {
	    try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
	        return stream
	          .filter(file -> !Files.isDirectory(file))
		      .filter(file -> isValidFileName(file))
	          .map(Path::toString)
	          .collect(Collectors.toSet());
	    }
	}	
	
	private static boolean isValidFileName(Path path) {
		String fileName = path.getFileName().toString();
		boolean isValid = fileName.endsWith(".txt") && fileName.indexOf("_") > 0;
		if(isValid) {
			fileName = fileName.substring(0, fileName.indexOf("_"));
			if (fileName.matches("\\d+")) {
	            Integer fileNameInt = Integer.valueOf(fileName);
	            if (path.getParent().getFileName().toString().equals("unsup")) {
	                isValid = fileNameInt >= 24000 && fileNameInt < 25000;
	            } else {
	                isValid = fileNameInt >= 6000 && fileNameInt < 6250;	                
	            }
			} else {
			    isValid = false;
			}
		}
		return isValid;
	}
}
