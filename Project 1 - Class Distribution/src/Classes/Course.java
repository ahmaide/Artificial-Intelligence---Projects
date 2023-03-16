package Classes;

import java.util.Objects;

public class Course {
    private static int count = 0;;
    private int ID;
    private String courseID;
    private int courseSectionNum;
    private String courseInstructor;
    private int courseCapacity;
    private char courseType;
    private int coursePlannedYear;

    public Course(){
        this.ID = count++;
    }

    public Course(String courseID, int courseSectionNum, String courseInstructor, int courseCapacity, int coursePlannedYear) {
        this.courseID = courseID;
        this.courseSectionNum = courseSectionNum;
        this.courseInstructor = courseInstructor;
        this.courseCapacity = courseCapacity;
        this.coursePlannedYear = coursePlannedYear;
        calCourseType();
        this.ID = count++;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
        calCourseType();
    }

    public int getCourseSectionNum() {
        return courseSectionNum;
    }

    public void setCourseSectionNum(int courseSectionNum) {
        this.courseSectionNum = courseSectionNum;
    }

    public String getCourseInstructor() {
        return courseInstructor;
    }

    public void setCourseInstructor(String courseInstructor) {
        this.courseInstructor = courseInstructor;
    }

    public int getCourseCapacity() {
        return courseCapacity;
    }

    public void setCourseCapacity(int courseCapacity) {
        this.courseCapacity = courseCapacity;
    }

    public int getCoursePlannedYear() {
        return coursePlannedYear;
    }

    public void setCoursePlannedYear(int coursePlannedYear) {
        this.coursePlannedYear = coursePlannedYear;
    }

    public int getCourseCreditHours(){
        return Character.getNumericValue(this.courseID.indexOf(5));
    }

    public char getCourseType() {
        return courseType;
    }

    public void setCourseType(char courseType) {
        this.courseType = courseType;
    }

    public void calCourseType(){
        int num = this.courseID.charAt(5)-'0';
        if (num==1) {
            this.courseType = 'l';
        }else {
            this.courseType = 'c';
        }
    }

    public static int getCount() {
        return count;
    }

    public int getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseID='" + courseID + '\'' +
                ", courseSectionNum=" + courseSectionNum +
                ", courseInstructor='" + courseInstructor + '\'' +
                ", courseCapacity=" + courseCapacity +
                '}';
    }
}
