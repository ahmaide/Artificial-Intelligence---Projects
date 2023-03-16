package Classes;

public class CourseTableEntry {
	public int section;
	public String instructor;
	public String time;
	public String day;
	public String courseSymbol;
	public String courseName;
	
	public CourseTableEntry(int section, String instructor, String day, String time) {
		super();
		this.section = section;
		this.instructor = instructor;
		this.time = time;
		this.day = day;
	}
	
	public CourseTableEntry(int section, String day, String time, String courseSymbol, String courseName) {
		super();
		this.section = section;
		this.time = time;
		this.day = day;
		this.courseSymbol = courseSymbol;
		this.courseName = courseName;
	}
	
	public int getSection() {
		return section;
	}
	public void setSection(int section) {
		this.section = section;
	}
	public String getInstructor() {
		return instructor;
	}
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}

	public String getCourseSymbol() {
		return courseSymbol;
	}

	public void setCourseSymbol(String courseSymbol) {
		this.courseSymbol = courseSymbol;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	
}
