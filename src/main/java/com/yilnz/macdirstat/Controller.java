package com.yilnz.macdirstat;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
	@FXML
	private TreeView<DeletingFile> tree;


	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("initialize tree view");
		TreeItem<DeletingFile> rootItem = new TreeItem<> (new DeletingFile("/"));
		tree.setRoot(rootItem);
		addItem(rootItem);
		System.out.println("initialize tree view ok.");
	}

	public void addItem(TreeItem<DeletingFile> parent){
		final List<File> list = FileDirUtil.list(parent.getValue().getFile().getPath());
		final ObservableList<TreeItem<DeletingFile>> children = parent.getChildren();
		for (int i = 0; i < list.size(); i++) {
			TreeItem<DeletingFile> item = new TreeItem<> (new DeletingFile(list.get(i).getPath()));

			if(item.getValue().getFile().isDirectory() && FileDirUtil.list(item.getValue().getFile().getPath()).size() > 0){
				item.getChildren().add(new TreeItem<>(null));
			}

			final BooleanProperty booleanProperty = item.expandedProperty();
			final ChangeListener<Boolean> changeListener = (observable, oldValue, newValue) -> {
				if (newValue) {
					item.getChildren().clear();
					addItem(item);
				}
			};
			booleanProperty.addListener(changeListener);

			children.add(item);
		}
	}
}
