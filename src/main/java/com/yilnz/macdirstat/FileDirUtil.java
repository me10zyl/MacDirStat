package com.yilnz.macdirstat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileDirUtil {

	public static List<File> list(String path){
		final File dir = new File(path);
		if (dir.isDirectory()) {
			final File[] obj = dir.listFiles();
			if(obj == null){
				return new ArrayList<>();
			}
			return Arrays.stream(obj).collect(Collectors.toList());
		}else{
			return new ArrayList<>();
		}
	}

	public static String getSizeString(double size){
		if(size > 1024 * 1024 * 1024){
			return String.format("%.2f", size / (1024 * 1024 * 1024)) + "G";
		}else if(size > 1024 * 1024){
			return String.format("%.2f", size / (1024 * 1024)) + "M";
		}else if(size > 1024){
			return String.format("%.2f",size / 1024) + "K";
		}
		if(size == 0){
			return "0";
		}
		return size + "";
	}

	public static void deleteFileAndDir(File file) throws IOException {
		if(file.isDirectory()){
			final File[] files = file.listFiles();
			if (files != null) {
				for (File file1 : files) {
					deleteFileAndDir(file1);
				}
			}

		}
		Files.delete(file.toPath());
	}

	public static long sizeOfFile(File directory, int depth, int maxdepth) {
		if(directory.isFile()){
			return directory.length();
		}
		if(maxdepth < depth){
			return 0;
		}
		depth++;
		long size = 0L;
		File[] files = directory.listFiles();
		for (int i = 0; files != null && i < files.length; ++i) {
			File file = files[i];
			if (file.isDirectory()) {
				size += sizeOfFile(file, depth, maxdepth);
			} else {
				size += file.length();
			}
		}
		return size;
	}

	public static long sizeOfDir(File directory) {
		return sizeOfFile(directory, 0, Integer.MAX_VALUE);
	}
}
