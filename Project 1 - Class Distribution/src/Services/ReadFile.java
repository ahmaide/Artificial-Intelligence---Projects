package Services;

import Classes.Course;
import Classes.Randomization;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ReadFile {
    public static ArrayList<Course> courses = new ArrayList<>();
    public static ArrayList<String> doctors = new ArrayList<>();
    public static ArrayList<String> courseIDs = new ArrayList<>();
    public static Map<String, String> coursesNames = new HashMap<>();
    public static Map<String, Integer> advisoryPlanENCS = new HashMap<>();
    public static Map<String, Integer> advisoryPlanENEE = new HashMap<>();
    public static int[] coursesHeader;

    public static void readCourses() throws IOException, CsvException {
        storeCoursesNames();
        storeCoursesYears();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader("src/Data/Data.csv"))
                .withSkipLines(1).build()) {
            List<String[]> r = reader.readAll();
            r.forEach(x -> {
                Course course = new Course();
                String line = Arrays.toString(x);
                int lineLen = line.length();
                String[] attributes = (line.substring(1, lineLen - 1)).split("\\s*,\\s*");

                course.setCourseID(attributes[0]);
                course.setCourseSectionNum(Integer.parseInt(attributes[1]));
                course.setCourseInstructor(attributes[2]);
                course.setCourseCapacity(Integer.parseInt(attributes[3]));
                int plannedYear = advisoryPlanENCS.get(course.getCourseID()) != null ? advisoryPlanENCS.get(course.getCourseID())
                        :advisoryPlanENEE.get(course.getCourseID());
                course.setCoursePlannedYear(plannedYear);
                course.calCourseType();
                if (!doctors.contains(course.getCourseInstructor())) {
                    doctors.add(course.getCourseInstructor());
                }
                if (!courseIDs.contains(course.getCourseID())) {
                    courseIDs.add(course.getCourseID());
                }
                courses.add(course);
            });
        }
        coursesHeader = new int[courses.size()];
        for (int i = 0; i < courses.size(); i++) {
            coursesHeader[i] = courses.get(i).getID();
        }
        coursesHeader = Randomization.randomizeArray(coursesHeader);
    }

    public static void storeCoursesNames() {
        coursesNames.put("ENCS2110", "DIGITAL ELECTRONICS AND COMPUTER ORGANIZATION LABORATORY");
        coursesNames.put("ENCS2340", "DIGITAL SYSTEMS");
        coursesNames.put("ENCS2380", "COMPUTER ORGANIZATION AND MICROPROCESSOR");
        coursesNames.put("ENCS311", "ASSEMBLY LANGUAGE LABORATORY");
        coursesNames.put("ENCS3130", "LINUX LABORATORY");
        coursesNames.put("ENCS3310", "ADVANCED DIGITAL SYSTEMS DESIGN");
        coursesNames.put("ENCS3320", "COMPUTER NETWORKS");
        coursesNames.put("ENCS3330", "DIGITAL INTEGRATED CIRCUITS");
        coursesNames.put("ENCS3340", "ARTIFICIAL INTELLIGENCE");
        coursesNames.put("ENCS3341", "EMBEDDED SYSTEMS");
        coursesNames.put("ENCS336", "COMPUTER ORGANIZATION AND ASSEMBLY LANGUAGE");
        coursesNames.put("ENCS3390", "OPERATING SYSTEMS");
        coursesNames.put("ENCS411", "COMPUTER DESIGN LAB");
        coursesNames.put("ENCS4110", "COMPUTER DESIGN LABORATORY");
        coursesNames.put("ENCS412", "INTERFACING LABORATORY");
        coursesNames.put("ENCS4130", "COMPUTER NETWORK LABORATORY");
        coursesNames.put("ENCS4310", "DIGITAL SIGNAL PROCESSING (DSP)");
        coursesNames.put("ENCS4320", "APPLIED CRYPTOGRAPHY");
        coursesNames.put("ENCS4330", "REAL-TIME APPLICATIONS AND EMBEDDED SYSTEMS");
        coursesNames.put("ENCS4370", "COMPUTER ARCHITECTURE");
        coursesNames.put("ENCS4380", "INTERFACING TECHNIQUES");
        coursesNames.put("ENCS514", "REAL-TIME SYSTEMS LAB");
        coursesNames.put("ENCS5150", "ADVANCED COMPUTER SYSTEMS ENGINEERING LABORATORY");
        coursesNames.put("ENCS5322", "NETWORK SECURITY PROTOCOLS");
        coursesNames.put("ENCS5332", "VLSI DESIGN");
        coursesNames.put("ENCS5341", "MACHINE LEARNING AND DATA SCIENCE");
        coursesNames.put("ENCS5399", "SP.TOP: VERIFICATION AND VALIDATION OF HARDWARE");
        coursesNames.put("ENEE2101", "BASIC ELECTRICAL ENGINEERING LAB");
        coursesNames.put("ENEE2102", "CIRCUITS LAB");
        coursesNames.put("ENEE2103", "CIRCUITS AND ELECTRONICS LABORATORY");
        coursesNames.put("ENEE2304", "CIRCUIT ANALYSIS");
        coursesNames.put("ENEE2307", "PROBABILITY AND ENGINEERING STATISTICS");
        coursesNames.put("ENEE2311", "NETWORK ANALYSIS 1");
        coursesNames.put("ENEE2312", "SIGNALS AND SYSTEMS");
        coursesNames.put("ENEE2360", "ANALOG ELECTRONICS");
        coursesNames.put("ENEE3101", "ELECTRICAL MACHINES LAB");
        coursesNames.put("ENEE3112", "ELECTRONICS LAB");
        coursesNames.put("ENEE3302", "CONTROL SYSTEMS");
        coursesNames.put("ENEE3304", "ELECTRONICS 2");
        coursesNames.put("ENEE3305", "POWER ELECTRONICS");
        coursesNames.put("ENEE3307", "LIGHTING AND ACOUSTICS ENGINEERING");
        coursesNames.put("ENEE3308", "ELECTROMECHANICAL PRINCIPLES AND APPLICATIONS");
        coursesNames.put("ENEE3309", "COMMUNICATION SYSTEMS");
        coursesNames.put("ENEE3318", "ELECTROMAGNETICS");
        coursesNames.put("ENEE4102", "FUNDAMENTALS OF ELECTRICAL MACHINES LAB");
        coursesNames.put("ENEE4104", "ENGINEERING SIMULATION LAB");
        coursesNames.put("ENEE4105", "CONTROL AND POWER ELECTRONICS LAB");
        coursesNames.put("ENEE4113", "COMMUNICATIONS LAB");
        coursesNames.put("ENEE4302", "CONTROL SYSTEMS 1");
        coursesNames.put("ENEE4303", "ELECTRICAL MACHINES FUNDAMENTALS");
        coursesNames.put("ENEE5102", "POWER LAB");
        coursesNames.put("ENEE5303", "ELECTRICAL MACHINE DRIVES AND SPECIAL MACHINES");
        coursesNames.put("ENEE5304", "INFORMATION AND CODING THEORY");
        coursesNames.put("ENEE5307", "RENEWABLE ENERGY AND PHOTOVOLTAIC POWER SYSTEMS");
    }

    public static void storeCoursesYears() {
        advisoryPlanENCS.put("ENCS2110", 2);
        advisoryPlanENCS.put("ENCS2340", 2);
        advisoryPlanENCS.put("ENCS2380", 2);
        advisoryPlanENCS.put("ENCS311", 3);
        advisoryPlanENCS.put("ENCS3130", 3);
        advisoryPlanENCS.put("ENCS3310", 3);
        advisoryPlanENCS.put("ENCS3320", 3);
        advisoryPlanENCS.put("ENCS3330", 3);
        advisoryPlanENCS.put("ENCS3340", 3);
        advisoryPlanENCS.put("ENCS3390", 3);
        advisoryPlanENCS.put("ENCS411", 4);
        advisoryPlanENCS.put("ENCS4110", 3);
        advisoryPlanENCS.put("ENCS412", 4);
        advisoryPlanENCS.put("ENCS4130", 4);
        advisoryPlanENCS.put("ENCS4320", 4);
        advisoryPlanENCS.put("ENCS4330", 4);
        advisoryPlanENCS.put("ENCS4370", 4);
        advisoryPlanENCS.put("ENCS4380", 4);
        advisoryPlanENCS.put("ENCS514", 5);
        advisoryPlanENCS.put("ENCS5150", 5);
        advisoryPlanENCS.put("ENCS5322", 5);
        advisoryPlanENCS.put("ENCS5332", 5);
        advisoryPlanENCS.put("ENCS5341", 4);
        advisoryPlanENCS.put("ENCS5399", 5);
        advisoryPlanENCS.put("ENEE2103", 3);
        advisoryPlanENCS.put("ENEE2304", 2);
        advisoryPlanENCS.put("ENEE2307", 2);
        advisoryPlanENCS.put("ENEE2312", 2);
        advisoryPlanENCS.put("ENEE2360", 3);
        advisoryPlanENCS.put("ENEE3309", 3);
        advisoryPlanENCS.put("ENEE4113", 3);
        advisoryPlanENCS.put("ENCS4310", 4);

        advisoryPlanENEE.put("ENEE3302", 4);////check
        advisoryPlanENEE.put("ENCS2110", 3);
        advisoryPlanENEE.put("ENCS2340", 2);
        advisoryPlanENEE.put("ENCS3310", 4);
        advisoryPlanENEE.put("ENCS3320", 4);
        advisoryPlanENEE.put("ENCS3330", 5);
        advisoryPlanENEE.put("ENCS3390", 5);
        advisoryPlanENEE.put("ENCS4130", 5);
        advisoryPlanENEE.put("ENCS4370", 5);
        advisoryPlanENEE.put("ENCS5332", 5);
        advisoryPlanENEE.put("ENEE2102", 2);
        advisoryPlanENEE.put("ENEE2307", 2);
        advisoryPlanENEE.put("ENEE2311", 2);
        advisoryPlanENEE.put("ENEE2312", 2);
        advisoryPlanENEE.put("ENEE3101", 3);
        advisoryPlanENEE.put("ENEE3112", 3);
        advisoryPlanENEE.put("ENEE3304", 3);
        advisoryPlanENEE.put("ENEE3305", 3);
        advisoryPlanENEE.put("ENEE3308", 4);
        advisoryPlanENEE.put("ENEE3309", 3);
        advisoryPlanENEE.put("ENEE3318", 3);
        advisoryPlanENEE.put("ENEE4104", 4);
        advisoryPlanENEE.put("ENEE4105", 4);
        advisoryPlanENEE.put("ENEE4113", 4);
        advisoryPlanENEE.put("ENEE4302", 4);
        advisoryPlanENEE.put("ENEE5102", 5);
        advisoryPlanENEE.put("ENEE5303", 4);
        advisoryPlanENEE.put("ENEE5304", 4);
        advisoryPlanENEE.put("ENEE5307", 4);
    }

}
