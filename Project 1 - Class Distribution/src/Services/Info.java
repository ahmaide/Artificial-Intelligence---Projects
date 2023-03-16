package Services;

import Classes.Chromosome;
import Classes.Course;
import Classes.Randomization;
import Classes.TimeSlot;

import java.util.ArrayList;
import java.util.Arrays;

public class Info {
    private static ArrayList<TimeSlot> coursesSlots = null;
    private static ArrayList<TimeSlot> labsSlots = null;

    public static void generateTimeSlots() {
        boolean[] SM = {true, true, false, false, false};
        boolean[] SW = {true, false, false, true, false};
        boolean[] MW = {false, true, false, true, false};
        boolean[] TR = {false, false, true, false, true};

        boolean[] S = {true, false, false, false, false};
        boolean[] W = {false, false, false, true, false};
        boolean[] M = {false, true, false, false, false};
        boolean[] R = {false, false, false, false, true};
        boolean[] T = {false, false, true, false, false};

        coursesSlots = new ArrayList<>();
        labsSlots = new ArrayList<>();
        TimeSlot slot;
        for (double startTime = 8.5; startTime < 16.75; startTime += 1.25) {
            slot = new TimeSlot(startTime, (startTime + 1.25), TR, "TR");
            coursesSlots.add(slot);
            slot = new TimeSlot(startTime, (startTime + 1.25), SM, "SM");
            coursesSlots.add(slot);
            slot = new TimeSlot(startTime, (startTime + 1.25), SW, "SW");
            coursesSlots.add(slot);
            slot = new TimeSlot(startTime, (startTime + 1.25), MW, "MW");
            coursesSlots.add(slot);
            startTime += (startTime == 8.5) ? 0.25 : 0.16666666666666666666666666666667;
        }

        for (double startTime = 8.5; startTime < 16.75; startTime += 2.66666666666666666666666666666667) {
            slot = new TimeSlot(startTime, (startTime + 2.66666666666666666666666666666667), S, "S");
            labsSlots.add(slot);
            slot = new TimeSlot(startTime, (startTime + 2.66666666666666666666666666666667), R, "R");
            labsSlots.add(slot);
            slot = new TimeSlot(startTime, (startTime + 2.66666666666666666666666666666667), W, "W");
            labsSlots.add(slot);
            slot = new TimeSlot(startTime, (startTime + 2.66666666666666666666666666666667), T, "T");
            labsSlots.add(slot);
            slot = new TimeSlot(startTime, (startTime + 2.66666666666666666666666666666667), M, "M");
            labsSlots.add(slot);
            startTime += (startTime == 8.5) ? 0.25 : 0.16666666666666666666666666666667;
        }
    }

    public static ArrayList<TimeSlot> getCoursesSlots() {
        if (coursesSlots == null)
            generateTimeSlots();
        return coursesSlots;
    }

    public static ArrayList<TimeSlot> getLabsSlots() {
        if (labsSlots == null)
            generateTimeSlots();
        return labsSlots;
    }

    public static int getProfessorsConflicts(Chromosome chromosome) {
        ArrayList<int[]> courses = chromosome.getGenes();
        int conflicts = 0;
        for (int i = 0; i < courses.size(); i++) {
            for (int j = i + 1; j < courses.size(); j++) {
                TimeSlot timeSlot1 = getTimeFromID(courses.get(i)[1]);
                TimeSlot timeSlot2 = getTimeFromID(courses.get(j)[1]);

                Course course1 = getCourseFromID(courses.get(i)[0]);
                Course course2 = getCourseFromID(courses.get(j)[0]);

                if (timeSlot1 != null && timeSlot2 != null &&
                        course1 != null && course2 != null &&
                        timeSlot1.conflict(timeSlot2) &&
                        course1.getCourseInstructor().equals(course2.getCourseInstructor())) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    public static int getCourseSectionsConflicts(Chromosome chromosome) {
        ArrayList<int[]> courses = chromosome.getGenes();
        int conflicts = 0;
        for (int i = 0; i < courses.size(); i++) {
            for (int j = i + 1; j < courses.size(); j++) {
                TimeSlot timeSlot1 = getTimeFromID(courses.get(i)[1]);
                TimeSlot timeSlot2 = getTimeFromID(courses.get(j)[1]);

                Course course1 = getCourseFromID(courses.get(i)[0]);
                Course course2 = getCourseFromID(courses.get(j)[0]);

                if (timeSlot1 != null && timeSlot2 != null &&
                        timeSlot1.conflict(timeSlot2) &&
                        course1 != null && course2 != null &&
                        course1.getCourseID().equals(course2.getCourseID())) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    public static int getCourseSectionsDaysConflicts(Chromosome chromosome) {
        ArrayList<int[]> courses = chromosome.getGenes();
        int conflicts = 0;
        for (int i = 0; i < courses.size(); i++) {
            for (int j = i + 1; j < courses.size(); j++) {
                TimeSlot timeSlot1 = getTimeFromID(courses.get(i)[1]);
                TimeSlot timeSlot2 = getTimeFromID(courses.get(j)[1]);

                Course course1 = getCourseFromID(courses.get(i)[0]);
                Course course2 = getCourseFromID(courses.get(j)[0]);

                if (timeSlot1 != null && timeSlot2 != null &&
                        Arrays.equals(timeSlot1.getDays(), timeSlot2.getDays()) &&
                        course1 != null && course2 != null &&
                        course1.getCourseID().equals(course2.getCourseID())) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    public static double getYearCoursesSharedDays(Chromosome chromosome) {
        ArrayList<ArrayList<int[]>> encsLevels = new ArrayList<>();
        ArrayList<ArrayList<int[]>> eneeLevels = new ArrayList<>();
        prepareLevels(chromosome, encsLevels, eneeLevels);
        double shared = 0;
        double conflicts1 = 0;
        double conflicts2 = 0;
        conflicts1 = Math.abs(1-(getSharedDays(encsLevels, conflicts1)/encsLevels.size())) * 5;
        conflicts2 = Math.abs(1-(getSharedDays(eneeLevels, conflicts2)/eneeLevels.size())) * 5;
        shared = conflicts1 + conflicts2;
        return shared;
    }

    public static double getYearCoursesConflicts(Chromosome chromosome) {
        ArrayList<ArrayList<int[]>> encsLevels = new ArrayList<>();
        ArrayList<ArrayList<int[]>> eneeLevels = new ArrayList<>();
        prepareLevels(chromosome, encsLevels, eneeLevels);
        double conflicts = 0;
        double conflicts1 = 0;
        double conflicts2 = 0;

        conflicts1 = Math.abs(1-(getConflicts(encsLevels, conflicts1)/encsLevels.size())) * 5;
        conflicts2 = Math.abs(1-(getConflicts(eneeLevels, conflicts2)/eneeLevels.size())) * 5;

        conflicts = conflicts1 + conflicts2;
        return conflicts;
    }

    public static double getSharedDays(ArrayList<ArrayList<int[]>> eneeLevels, double conflicts) {
        for (int i = 0; i < eneeLevels.size(); i++) {
            double tempConflicts = 0.0;
            for (int j = 0; j < eneeLevels.get(i).size(); j++) {
                for (int c = j + 1; c < eneeLevels.get(i).size(); c++) {
                    TimeSlot timeSlot1 = getTimeFromID(eneeLevels.get(i).get(j)[1]);
                    TimeSlot timeSlot2 = getTimeFromID(eneeLevels.get(i).get(c)[1]);
                    Course course1 = getCourseFromID(eneeLevels.get(i).get(j)[0]);
                    Course course2 = getCourseFromID(eneeLevels.get(i).get(c)[0]);

                    if (timeSlot1 != null && timeSlot2 != null &&
                            course1 != null && course2 != null &&
                            timeSlot1.shareDays(timeSlot2) &&
                            !course1.getCourseID().equals(course2.getCourseID())
                    ) {
                        tempConflicts++;
                    }
                }
            }
            tempConflicts = tempConflicts >= eneeLevels.get(i).size() ? eneeLevels.get(i).size() : tempConflicts;
            conflicts += (tempConflicts) / eneeLevels.get(i).size();
        }
        return conflicts;
    }

    public static double getConflicts(ArrayList<ArrayList<int[]>> eneeLevels, double conflicts) {
        for (int i = 0; i < eneeLevels.size(); i++) {
            double tempConflicts = 0.0;
            for (int j = 0; j < eneeLevels.get(i).size(); j++) {
                for (int c = j + 1; c < eneeLevels.get(i).size(); c++) {
                    TimeSlot timeSlot1 = getTimeFromID(eneeLevels.get(i).get(j)[1]);
                    TimeSlot timeSlot2 = getTimeFromID(eneeLevels.get(i).get(c)[1]);
                    Course course1 = getCourseFromID(eneeLevels.get(i).get(j)[0]);
                    Course course2 = getCourseFromID(eneeLevels.get(i).get(c)[0]);

                    if (timeSlot1 != null && timeSlot2 != null &&
                            course1 != null && course2 != null &&
                            timeSlot1.conflict(timeSlot2) &&
                            !course1.getCourseID().equals(course2.getCourseID())) {
                        tempConflicts++;
                    }
                }
            }
            tempConflicts = tempConflicts >= eneeLevels.get(i).size() ? eneeLevels.get(i).size() : tempConflicts;
            conflicts += (tempConflicts) / eneeLevels.get(i).size();
        }
        return conflicts;
    }

    public static void prepareLevels(Chromosome chromosome, ArrayList<ArrayList<int[]>> encsLevels, ArrayList<ArrayList<int[]>> eneeLevels) {
        for (int i = 0; i < 4; i++) {
            encsLevels.add(new ArrayList<>());
            eneeLevels.add(new ArrayList<>());
        }
        ArrayList<int[]> courses = chromosome.getGenes();
        for (int i = 0; i < courses.size(); i++) {
            int year = -1;
            Course course = getCourseFromID(courses.get(i)[0]);
            if (course != null && ReadFile.advisoryPlanENEE.get(course.getCourseID()) != null
                    && ReadFile.advisoryPlanENCS.get(course.getCourseID()) != null){
                int year1 = ReadFile.advisoryPlanENEE.get(course.getCourseID()) - 2;
                int year2 = ReadFile.advisoryPlanENCS.get(course.getCourseID()) - 2;
                if (year1 < year2){
                    encsLevels.get(year1).add(courses.get(i));
                }else{
                    eneeLevels.get(year2).add(courses.get(i));
                }
            } else if (course != null && ReadFile.advisoryPlanENEE.get(course.getCourseID()) != null){
                year = ReadFile.advisoryPlanENEE.get(course.getCourseID()) - 2;
                eneeLevels.get(year).add(courses.get(i));
            } else if (course != null && ReadFile.advisoryPlanENCS.get(course.getCourseID()) != null) {
                year = ReadFile.advisoryPlanENCS.get(course.getCourseID()) - 2;
                encsLevels.get(year).add(courses.get(i));
            }
        }
    }

    public static Chromosome generateRandomSolution(){
        Chromosome solution = new Chromosome();
        for (int i = 0; i < ReadFile.coursesHeader.length; i++){
            int[] gene = new int[2];
            gene[0] = ReadFile.coursesHeader[i];

            if(Info.getCourseFromID(gene[0]).getCourseType() == 'l'){
                int randomTimeSlot = Randomization.getRandomNumber(0,(Info.getLabsSlots().size() - 1));
                gene[1] = labsSlots.get(randomTimeSlot).getId();
            }
            else{
                if(Math.random()<0.4){
                    int times = Randomization.getRandomNumber(0, 5);
                    gene[1] = coursesSlots.get(times*4).getId();
                }
                else{
                    int days = Randomization.getRandomNumber(1, 3);
                    int times = Randomization.getRandomNumber(0, 5);
                    gene[1] = coursesSlots.get((times*4) + days).getId();
                }
            }
            solution.addGene(gene);
        }
        Chromosome.calculateFitness(solution);
        return solution;
    }

    /*public static Chromosome generateRandomSolution(){
        Chromosome solution = new Chromosome();
        for (int i = 0; i < ReadFile.coursesHeader.length; i++){
            int[] gene = new int[2];
            gene[0] = ReadFile.coursesHeader[i];
            int randomTimeSlot = Randomization.getRandomNumber(0,
                    (Info.getCourseFromID(gene[0]).getCourseType() == 'c') ? (Info.getCoursesSlots().size() - 1) :
                            (Info.getLabsSlots().size() - 1)
            );
            gene[1] = (Info.getCourseFromID(gene[0]).getCourseType() == 'c') ? coursesSlots.get(randomTimeSlot).getId() :
                    labsSlots.get(randomTimeSlot).getId();
            solution.addGene(gene);
        }
        Chromosome.calculateFitness(solution);
        return solution;
    }*/

    public static Course getCourseFromID(int ID) {
        for (int i = 0; i < ReadFile.courses.size(); i++) {
            if (ReadFile.courses.get(i).getID() == ID) {
                return ReadFile.courses.get(i);
            }
        }
        return null;
    }

    public static TimeSlot getTimeFromID(int timeID) {
        for (int i = 0; i < Info.coursesSlots.size(); i++) {
            if (Info.coursesSlots.get(i).getId() == timeID) {
                return Info.coursesSlots.get(i);
            }
        }
        for (int i = 0; i < Info.labsSlots.size(); i++) {
            if (Info.labsSlots.get(i).getId() == timeID) {
                return Info.labsSlots.get(i);
            }
        }
        return null;
    }

    public static double getYearCoursesSharedDayss(Chromosome chromosome) {
        ArrayList<ArrayList<int[]>> encsLevels = new ArrayList<>();
        ArrayList<ArrayList<int[]>> eneeLevels = new ArrayList<>();
        prepareLevels(chromosome, encsLevels, eneeLevels);
        double shared = 0;
        double conflicts1 = 0;
        double conflicts2 = 0;
        System.out.println("ENCS\n");
        conflicts1 = Math.abs(1-(getSharedDayss(encsLevels, conflicts1)));

        System.out.println("ENEE\n");
        conflicts2 = Math.abs(1-(getSharedDayss(eneeLevels, conflicts2)));
        shared = conflicts1 + conflicts2;
        System.out.println(shared);
        return shared;
    }

    public static double getYearCoursesConflictss(Chromosome chromosome) {
        ArrayList<ArrayList<int[]>> encsLevels = new ArrayList<>();
        ArrayList<ArrayList<int[]>> eneeLevels = new ArrayList<>();
        prepareLevels(chromosome, encsLevels, eneeLevels);
        double conflicts = 0;
        double conflicts1 = 0;
        double conflicts2 = 0;
        System.out.println("ENCS:\n");
        conflicts1 = Math.abs(1-(getConflictss(encsLevels, conflicts1)/encsLevels.size()))*5;

        System.out.println("ENEE\n");
        conflicts2 = Math.abs(1-(getConflictss(eneeLevels, conflicts2)/eneeLevels.size()))*5;
        conflicts = conflicts1 + conflicts2;
        System.out.println(conflicts);
        //we may exclude the shared courses between encs and enee
        //so that each the fitness is not calculated twice
        return conflicts;
    }

    public static double getSharedDayss(ArrayList<ArrayList<int[]>> eneeLevels, double conflicts) {
        for (int i = 0; i < eneeLevels.size(); i++) {
            double tempConflicts = 0.0;
            int count = 0;
            for (int j = 0; j < eneeLevels.get(i).size(); j++) {
                for (int c = j + 1; c < eneeLevels.get(i).size(); c++) {
                    TimeSlot timeSlot1 = getTimeFromID(eneeLevels.get(i).get(j)[1]);
                    TimeSlot timeSlot2 = getTimeFromID(eneeLevels.get(i).get(c)[1]);
                    Course course1 = getCourseFromID(eneeLevels.get(i).get(j)[0]);
                    Course course2 = getCourseFromID(eneeLevels.get(i).get(c)[0]);

                    if (timeSlot1 != null && timeSlot2 != null &&
                            course1 != null && course2 != null &&
                            timeSlot1.shareDays(timeSlot2) &&
                            !course1.getCourseID().equals(course2.getCourseID())
                    ) {
                        tempConflicts++;
                    }
                    count++;
                }
            }
            tempConflicts = tempConflicts >= eneeLevels.get(i).size() ? eneeLevels.get(i).size() : tempConflicts;
            System.out.println("Level: " + i + 2 + ", Conflicts: " + tempConflicts + ", Size: " + count);
            //conflicts += (tempConflicts%eneeLevels.get(i).size()) / eneeLevels.get(i).size();
            conflicts += (tempConflicts) / eneeLevels.get(i).size();
        }
        return conflicts;
    }

    public static double getConflictss(ArrayList<ArrayList<int[]>> eneeLevels, double conflicts) {
        for (int i = 0; i < eneeLevels.size(); i++) {
            double tempConflicts = 0.0;
            int count = 0;
            for (int j = 0; j < eneeLevels.get(i).size(); j++) {
                for (int c = j + 1; c < eneeLevels.get(i).size(); c++) {
                    TimeSlot timeSlot1 = getTimeFromID(eneeLevels.get(i).get(j)[1]);
                    TimeSlot timeSlot2 = getTimeFromID(eneeLevels.get(i).get(c)[1]);
                    Course course1 = getCourseFromID(eneeLevels.get(i).get(j)[0]);
                    Course course2 = getCourseFromID(eneeLevels.get(i).get(c)[0]);

                    if (timeSlot1 != null && timeSlot2 != null &&
                            course1 != null && course2 != null &&
                            timeSlot1.conflict(timeSlot2) &&
                            !course1.getCourseID().equals(course2.getCourseID())) {
                        tempConflicts++;
                    }
                    count++;
                }
            }
            tempConflicts = tempConflicts >= eneeLevels.get(i).size() ? eneeLevels.get(i).size() : tempConflicts;
            System.out.println("Level: " + i + 2 + ", Conflicts: " + tempConflicts + ", Size: " + count);
            //conflicts += (tempConflicts%eneeLevels.get(i).size()) / eneeLevels.get(i).size();
            conflicts += (tempConflicts) / eneeLevels.get(i).size();
        }
        return conflicts;
    }

}
