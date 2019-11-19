package com.yilnz.macdirstat;

import java.io.File;
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
}
