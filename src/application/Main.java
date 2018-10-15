package application;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Sample");
        stage.setWidth(300);
        stage.setHeight(190);

        VBox vbox = new VBox();
        vbox.setLayoutX(20);
        vbox.setLayoutY(20);


        final String content = "uerhuiv erfbuebrfiu erf uehnru erufnduiven erfn";
        String parsedStr = content.replaceAll("(.{10})", "$1\n");
        System.out.println(parsedStr);

        final Text text = new Text(10, 20, "");

        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(2000));
            }

            protected void interpolate(double frac) {
                final int length = parsedStr.length();
                final int n = Math.round(length * (float) frac);
                int count = 0;
                if(count == 14) {
                    text.setText("\n");
                    count = 0;
                }
               else {
                    text.setText(parsedStr.substring(0, n));
                    count ++;
                }
            }

        };

        animation.play();



        vbox.getChildren().add(text);
        vbox.setSpacing(10);
        ((Group) scene.getRoot()).getChildren().add(vbox);

        stage.setScene(scene);
        stage.show();
    }
}

   
  