package com.yilnz.macdirstat;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class Controller implements Initializable {
	@FXML
	private TreeView<DeletingFile> tree;
	private ConfigHolder configHolder;
	private final Logger logger = LoggerFactory.getLogger(Controller.class);

	class SelectEventHandler implements EventHandler<ActionEvent>{
		private Consumer<TreeItem<DeletingFile>> consumer;

		public SelectEventHandler(Consumer<TreeItem<DeletingFile>> consumer) {
			this.consumer = consumer;
		}

		@Override
		public void handle(ActionEvent event) {
			final ObservableList<TreeItem<DeletingFile>> selectedItems = tree.getSelectionModel().getSelectedItems();
			if (selectedItems.size() > 0) {
				selectedItems.forEach(consumer);
			}
		}
	}

	private void expandAll(TreeItem<DeletingFile> root){
		traverse(root, e->{
			if (e.getValue() != null && !e.getValue().getFile().isHidden()) {
				e.setExpanded(true);
			}
		});
	}

	private void traverse(TreeItem<DeletingFile> root, Consumer<TreeItem<DeletingFile>> consumer){
		consumer.accept(root);

		final ObservableList<TreeItem<DeletingFile>> children = root.getChildren();
		if(children.size() > 50){
			return;
		}
		for (TreeItem<DeletingFile> child : children) {
			traverse(child, consumer);
		}
	}


	public void initialize(URL location, ResourceBundle resources) {
		configHolder = new ConfigHolder();
		logger.info("initialize tree view");
		TreeItem<DeletingFile> rootItem = new TreeItem<> (new DeletingFile("/"));
		rootItem.setExpanded(true);
		tree.setRoot(rootItem);
		addItem(rootItem);
		logger.info("initialize tree view ok.");

		final MenuItem deepCalcItem = new MenuItem("自动展开");
		final MenuItem deleteMenuItem = new MenuItem("删除");
		final MenuItem[] items = new MenuItem[]{deepCalcItem, deleteMenuItem};
		deepCalcItem.setOnAction(new SelectEventHandler(e->{
			//final ArrayList<TreeItem<DeletingFile>> all = new ArrayList<>();
			expandAll(e);
			/*traverse(e, a -> {
				a.getValue().calcDirSize();
				a.getValue().update();
			});*/
		}));
		deleteMenuItem.setOnAction(e->{
			final ObservableList<TreeItem<DeletingFile>> selectedItems = tree.getSelectionModel().getSelectedItems();
			if (selectedItems.size() > 0) {
				StringBuilder selection = new StringBuilder();
				for (TreeItem<DeletingFile> selectedItem : selectedItems) {
					selection.append(selectedItem.getValue().getFile().getName()).append("\n");
				}
				selection = new StringBuilder(selection.substring(0, selection.length() - 1));
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "删除 " + selection + " ?", ButtonType.YES, ButtonType.NO);
				alert.showAndWait();

				if (alert.getResult() == ButtonType.YES) {
					for (TreeItem<DeletingFile> selectedItem : selectedItems) {
						try {
							FileDirUtil.deleteFileAndDir(selectedItem.getValue().getFile());
							selectedItem.getParent().getChildren().remove(selectedItem);
						} catch (IOException e1) {
							new Alert(Alert.AlertType.ERROR, "删除失败:" + e1.toString()).showAndWait();
						}

					}
				}
			}
		});
		tree.setContextMenu(new ContextMenu(items));
	}

	private void handleItem(TreeItem<DeletingFile> item){
		if(item.getValue().isFavoriteDir(configHolder.getFavoriteDirs())){
			item.setExpanded(true);
		}
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
				//System.out.println("changed " + newValue + observable);
				if (newValue && item.getChildren().size() > 0 && item.getChildren().get(0).getValue() == null) {
					item.getChildren().clear();
					addItem(item);
				}
			};
			booleanProperty.addListener(changeListener);
			handleItem(item);
			children.add(item);
		}

		if (children.size() > 1) {
			Collections.sort(children, (o1, o2) -> {
				int o1Int = 0;
				int o2Int = 0;
				if(o1.getValue().isFavorite()){
					o1Int = -1;
				}
				if(o2.getValue().isFavorite()){
					o2Int = -1;
				}
				int favorite = o1Int - o2Int;
				if(favorite != 0) {
					return favorite;
				}
				final int r = -o1.getValue().getFileSize().compareTo(o2.getValue().getFileSize());
				if(r == 0){
					return o1.getValue().getFile().getName().compareTo(o2.getValue().getFile().getName());
				}
				return r;
			});
		}
	}
}
