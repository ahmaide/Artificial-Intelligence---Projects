import Classes.*;
import Services.*;
import com.opencsv.exceptions.CsvException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage primaryStage;
    private Parent root;
    @Override
    public void start(Stage primaryStage){
        try {
            this.primaryStage= primaryStage;
            this.primaryStage.setTitle("TimeTabling");
            root = FXMLLoader.load(getClass().getResource("Interface.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.getScene().getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, CsvException {
        ReadFile.readCourses();
        Info.generateTimeSlots();
        GeneticAlgorithm.initializePopulation();
        GeneticAlgorithm.solve(GeneticAlgorithm.population);
        Chromosome best = GeneticAlgorithm.bestSolution;
        System.out.println(Info.getYearCoursesConflictss(best));
        System.out.println(Info.getYearCoursesSharedDayss(best));
        launch(args);
    }
}