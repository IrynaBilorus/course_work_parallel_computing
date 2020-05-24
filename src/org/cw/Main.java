package org.cw;

import java.io.File;
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static Set<String> listFiles(String dir, int depth) throws IOException {
	    try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
	        return stream
	          .filter(file -> !Files.isDirectory(file))
		      .filter(file -> isValidFileName(file.getFileName()))
	          .map(Path::getFileName)
	          .map(Path::toString)
	          .collect(Collectors.toSet());
	    }
	}	
	
	private static boolean isValidFileName(Path path) {
		String fileName = path.toString();
		boolean isValid = fileName.endsWith(".txt");
		if(isValid) {
			fileName = fileName.substring(0, fileName.indexOf("_"));				
			Integer fileNameInt = Integer.valueOf(fileName);
			isValid = fileNameInt >= 6000 && fileNameInt <= 6250;
		}
		return isValid;
	}
}
