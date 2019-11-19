package com.yilnz.macdirstat;

import lombok.Data;

import java.io.File;

@Data
public class DeletingFile {
	private File file;

	public DeletingFile(String path) {
		this.file = new File(path);
	}

	@Override
	public String toString() {
		if(file.getPath().equals("/")){
			return "/";
		}
		return file.getName();
	}
}
