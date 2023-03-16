import Classes.*;
import Services.GeneticAlgorithm;
import Services.Info;
import Services.ReadFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private GridPane doctorTable;

    @FXML
    private TableView<CourseTableEntry> doctorBrowser;

    @FXML
    private TableColumn<CourseTableEntry, String> courseLabel;

    @FXML
    private TableColumn<CourseTableEntry, String> section;

    @FXML
    private TableColumn<CourseTableEntry, String> courseTitle;

    @FXML
    private TableColumn<CourseTableEntry, String> days;

    @FXML
    private TableColumn<CourseTableEntry, String> time;

    @FXML
    private ComboBox<String> doctorsCombo;

    @FXML
    void showTable(ActionEvent event) {
        fillTable();
    }

    @FXML
    private AnchorPane container;

    @FXML
    private VBox vboxContainer;

    @FXML
    private LineChart<?, ?> ch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeDoctorsCombo();
        bindDoctorBrowserTable();
        fillCourseBrowser();
        fillGraph();
    }

    public void bindDoctorBrowserTable() {
        courseLabel.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("courseSymbol"));
        courseLabel.setStyle("-fx-alignment: CENTER;");
        section.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("section"));
        section.setStyle("-fx-alignment: CENTER;");
        courseTitle.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("courseName"));
        days.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("day"));
        days.setStyle("-fx-alignment: CENTER;");
        time.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("time"));
        time.setStyle("-fx-alignment: CENTER;");

    }

    public void fillCourseBrowser() {
        int height = 0;
        ArrayList<String> addedCourses = new ArrayList<>();
        for (int i = 0; i < ReadFile.courses.size(); i++) {
            Course course = ReadFile.courses.get(i);
            if (addedCourses.contains(course.getCourseID()))
                continue;
            height++;
            Label label = new Label(course.getCourseID() + ":\t " + ReadFile.coursesNames.get(course.getCourseID()));
            label.setStyle("-fx-font-size: 16;");
            vboxContainer.getChildren().add(label);

            TableView<CourseTableEntry> table = new TableView<CourseTableEntry>();
            table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            table.getItems().clear();


            table.setEditable(true);
            ArrayList<CourseTableEntry> sectionsList = new ArrayList<>();
            sectionsList.clear();
            ArrayList<int[]> bestGenes = GeneticAlgorithm.bestSolution.getGenes();
            int count = 0;
            for (int j = 0; j < bestGenes.size(); j++) {
                Course c = Info.getCourseFromID(bestGenes.get(j)[0]);
                if (c != null && c.getCourseID().equals(course.getCourseID())) {
                    height++;
                    TimeSlot t = Info.getTimeFromID(bestGenes.get(j)[1]);
                    double startTime = t.getStartTime();
                    double endTime = t.getEndTime();

                    String sTimeStr = String.valueOf((int) startTime) + ":" + (Math.round((startTime - (int) startTime) * 60) < 5.1 ? "0" : "") + String.valueOf(
                            Math.round((startTime - (int) startTime) * 60)
                    );
                    String eTimeStr = String.valueOf((int) endTime) + ":" + (Math.round((endTime - (int) endTime) * 60) < 5.1 ? "0" : "") + String.valueOf(
                            Math.round((endTime - (int) endTime) * 60)
                    );
                    String cTime = sTimeStr + "-" + eTimeStr;

                    String days = "";
                    if (t.getDays()[0])
                        days += "S";
                    if (t.getDays()[1])
                        days += "M";
                    if (t.getDays()[2])
                        days += "T";
                    if (t.getDays()[3])
                        days += "W";
                    if (t.getDays()[4])
                        days += "R";

                    sectionsList.add(
                            new CourseTableEntry(c.getCourseSectionNum(), c.getCourseInstructor(), days, cTime)
                    );
                }
            }
            height++;
            sectionsList.sort(Comparator.comparing(CourseTableEntry::getSection));
            TableColumn<CourseTableEntry, Integer> section = new TableColumn<CourseTableEntry, Integer>("Section");
            section.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, Integer>("section"));

            TableColumn<CourseTableEntry, String> instructor = new TableColumn<CourseTableEntry, String>("Instructor");
            instructor.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("instructor"));
            instructor.setPrefWidth(130);

            TableColumn<CourseTableEntry, String> day = new TableColumn<CourseTableEntry, String>("Day");
            day.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("day"));

            TableColumn<CourseTableEntry, String> time = new TableColumn<CourseTableEntry, String>("Time");
            time.setCellValueFactory(new PropertyValueFactory<CourseTableEntry, String>("time"));

            table.getColumns().addAll(section, instructor, day, time);
            table.setMaxHeight((sectionsList.size() + 5) * 35);


            table.setId("my-table");
            table.getItems().setAll(sectionsList);

            vboxContainer.getChildren().add(table);
            addedCourses.add(course.getCourseID());
        }
        vboxContainer.setPrefHeight((height + 50) * 25);
        container.setPrefHeight((height + 20) * 40);
        vboxContainer.setPadding(new Insets(40));
        vboxContainer.setSpacing(30);

    }

    public void initializeDoctorsCombo() {
        for (int i = 0; i < ReadFile.doctors.size(); i++) {
            doctorsCombo.getItems().add(ReadFile.doctors.get(i));
        }
    }

    public void fillTable() {
        doctorTable.getChildren().clear();
        doctorBrowser.getItems().clear();

        int selectedIndex = doctorsCombo.getSelectionModel().getSelectedIndex();
        Color sectionsColors[] = new Color[ReadFile.coursesHeader.length];
        for (int i = 0; i < sectionsColors.length; i++) {
            sectionsColors[i] = Color.rgb(Randomization.getRandomNumber(0, 255), Randomization.getRandomNumber(0, 255), Randomization.getRandomNumber(0, 255), 0.5);
        }

        ArrayList<int[]> c = GeneticAlgorithm.bestSolution.getGenes();
        for (int i = 0; i < c.size(); i++) {
            if (ReadFile.doctors.get(selectedIndex).equals(Info.getCourseFromID(c.get(i)[0]).getCourseInstructor())) {
                Course course = Info.getCourseFromID(c.get(i)[0]);
                TimeSlot t = Info.getTimeFromID(c.get(i)[1]);
                double startTime = t.getStartTime();
                double endTime = t.getEndTime();

                String sTimeStr = String.valueOf((int) startTime) + ":" + (Math.round((startTime - (int) startTime) * 60) < 5.1 ? "0" : "") + String.valueOf(
                        Math.round((startTime - (int) startTime) * 60)
                );
                String eTimeStr = String.valueOf((int) endTime) + ":" + (Math.round((endTime - (int) endTime) * 60) < 5.1 ? "0" : "") + String.valueOf(
                        Math.round((endTime - (int) endTime) * 60)
                );
                String cTime = sTimeStr + "-" + eTimeStr;

                String days = "";
                if (t.getDays()[0])
                    days += "S";
                if (t.getDays()[1])
                    days += "M";
                if (t.getDays()[2])
                    days += "T";
                if (t.getDays()[3])
                    days += "W";
                if (t.getDays()[4])
                    days += "R";

                doctorBrowser.getItems().add(new CourseTableEntry(course.getCourseSectionNum(), days, cTime, course.getCourseID(), ReadFile.coursesNames.get(course.getCourseID())));
                int column = (int) ((t.getStartTime() - 8) * 2);
                int width = (int) ((t.getEndTime() - t.getStartTime()) * 2);
                ArrayList<Integer> rows = new ArrayList<Integer>();
                for (int j = 0; j < t.getDays().length; j++) {
                    if (t.getDays()[j]) {
                        if (j >= 1) {
                            rows.add(j + 1);
                        } else {
                            rows.add(j);
                        }

                    }
                }
                for (int j = 0; j < rows.size(); j++) {
                    Rectangle label = new Rectangle(38.51 * width, 30);
                    label.setFill(sectionsColors[i]);
                    Text text = new Text(course.getCourseID());
                    StackPane pane = new StackPane();
                    pane.getChildren().addAll(label, text);
                    doctorTable.add(pane, column, rows.get(j));
                }

            }
        }
    }

    public void fillGraph(){
        XYChart.Series series = new XYChart.Series();
        series.setName("Score of the chromosome");
        ArrayList<XYChartClass>c = GeneticAlgorithm.charts;

        for (int i = 0 ; i < c.size() ; i++)
            series.getData().add(new XYChart.Data(Integer.toString(c.get(i).getX()) ,c.get(i).getY())) ;


        ch.getData().add(series) ;
    }
}
