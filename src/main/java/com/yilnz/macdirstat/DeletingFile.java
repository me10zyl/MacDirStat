package com.yilnz.macdirstat;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import lombok.Data;

import java.io.File;
import java.util.List;

@Data
public class DeletingFile extends HBox {
	private File file;
	private Long fileSize;
	private boolean isFavorite;
	private Label labelFileSize;
	private Label labelFileName;

	private final long RED_THRESHOLD = 50 * 1024 * 1024; //50MB

	public DeletingFile(String path) {
		this.file = new File(path);
		calcFileSize();
		this.labelFileName = new Label(fileNameString());
		this.labelFileSize = new Label(fileSizeString());
		final Paint color = getFileNameColor();
		if (color != null) {
			labelFileName.setTextFill(color);
		}
		final Paint color2 = getFileSizeColor();
		if (color2 != null) {
			labelFileSize.setTextFill(color2);
		}
		this.getChildren().add(labelFileSize);
		this.getChildren().add(labelFileName);
		this.setAlignment(Pos.CENTER_LEFT);
	}

	public void update(){
		labelFileSize.setText(fileSizeString());
	}

	public boolean isFavoriteDir(List<String> paths){
		final boolean anyMatch = paths.stream().anyMatch(e -> e.toLowerCase().contains(file.getPath().toLowerCase()));
		isFavorite = anyMatch;
		return anyMatch;
	}

	private Paint getFileNameColor(){
		if(this.file.isDirectory()){
			 return Paint.valueOf("#0000FF");
		}
		return null;
	}

	private Paint getFileSizeColor(){
		if(this.getFileSize() >= RED_THRESHOLD){
			return Paint.valueOf("#FF0000");
		}
		return null;
	}

	private void calcFileSize() {
		if (this.fileSize != null) {
			return;
		}
		if (file != null) {
			this.fileSize = FileDirUtil.sizeOfFile(file, 1, 1);
		}
	}

	public void calcDirSize(){
		if (file != null) {
			this.fileSize = FileDirUtil.sizeOfDir(file);
		}
	}

	public String fileSizeString() {
		if (file.getPath().equals("/")) {
			return "";
		}
		return FileDirUtil.getSizeString(this.fileSize);
	}

	public String fileNameString(){
		if (file.getPath().equals("/")) {
			return "/";
		}
		return file.getName();
	}
}
