package org.cw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

	public static void main(String[] args) {

		try {
			System.out.println("Search files for indexing.");
			Set<String> files = listFiles("C:/Files", 3);
			if (files.size() > 0) {
				InvertedIndex index = new InvertedIndex(files);

				System.out.println("Found files:" + files.size());
				
				System.out.println("Enter number of threads: ");
				Scanner scan = new Scanner(System.in);
				int numThreads = scan.nextInt();
				
				long time0 = System.currentTimeMillis();
				System.out.println("Start of index creation");
				System.out.println("Number of threads is " + numThreads);

				IndexCreator[] creators = new IndexCreator[numThreads];
				for (int i = 0; i < numThreads; i++) {
					creators[i] = new IndexCreator(index);
					creators[i].start();
				}

				for (int i = 0; i < numThreads; i++) {
					try {
						creators[i].join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				long time1 = System.currentTimeMillis();
				System.out.println("Process is complete!\nTime of processing: " + (time1 - time0) + 
						"\nSize of index: " + index.size());

				System.out.println("\nEnter word for search: ");

				String wordForSearch = scan.next();
				
				List<String> foundFiles = index.searchFilesByWord(wordForSearch);
				if (!foundFiles.isEmpty()) {
					System.out.println("\nThe word \"" + wordForSearch + "\" found in the " + foundFiles.size() + " files:");
					for (String file : foundFiles) {
						System.out.println(file);
					}

				}
				
			} else {
				System.out.println("Files not found!!!");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Set<String> listFiles(String dir, int depth) throws IOException {
		try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
			return stream.filter(file -> !Files.isDirectory(file)).filter(file -> isValidFileName(file))
					.map(Path::toString).collect(Collectors.toSet());
		}
	}

	private static boolean isValidFileName(Path path) {
		String fileName = path.getFileName().toString();
		boolean isValid = fileName.endsWith(".txt") && fileName.indexOf("_") > 0;
		if (isValid) {	
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
