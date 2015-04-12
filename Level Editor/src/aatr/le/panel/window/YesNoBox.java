package aatr.le.panel.window;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public final class YesNoBox {

	public static final int ANSWER_NO = 0;
	public static final int ANSWER_YES = 1;
	public static final int ANSWER_NONE = -1;

	private static int answer;

	public static final int ask(String title, String question, String tAns, String fAns) {

		Stage stage = new Stage();
		
		stage.setTitle(title);
		
		stage.initModality(Modality.APPLICATION_MODAL);

		GridPane gp = new GridPane();

		Text txtQuestion = new Text(question);

		Button btnYes = new Button(tAns);

		btnYes.setOnAction(e -> {
			answer = ANSWER_YES;
			stage.close();
		});

		Button btnNo = new Button(fAns);

		btnNo.setOnAction(e -> {
			answer = ANSWER_NO;
			stage.close();
		});
		
		gp.setVgap(25);
		gp.setHgap(50);

		gp.setPadding(new Insets(20));

		btnYes.setPrefHeight(20);

		GridPane.setHalignment(btnYes, HPos.CENTER);
		GridPane.setHalignment(btnNo, HPos.CENTER);
		GridPane.setConstraints(txtQuestion, 0, 1);
		GridPane.setColumnSpan(txtQuestion, 3);
		GridPane.setConstraints(btnYes, 1, 2);
		GridPane.setConstraints(btnNo, 2, 2);

		gp.getChildren().addAll(txtQuestion, btnYes, btnNo);

		stage.setScene(new Scene(gp,
				txtQuestion.getLayoutBounds().getWidth() + 20 * 2, txtQuestion
						.getLayoutBounds().getHeight() + 20 * 2 + 25 * 2 + 20));

		stage.setOnCloseRequest(e -> {
			answer = ANSWER_NONE;
		});
		
		stage.setResizable(false);
		
		stage.showAndWait();
		
		return answer;
	}
}