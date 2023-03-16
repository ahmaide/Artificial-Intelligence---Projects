package Classes;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

public class TimeSlot {
    private static int count = 0;
    private int id;
    private double startTime;
    private double endTime;
    private String dayStr;
    private boolean[] days = new boolean[5];

    public TimeSlot() {
        this.id = count++;
    }

    public TimeSlot(double startTime, double endTime, boolean[] days, String daysString) {
        super();
        this.id = count++;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayStr = daysString;
        this.days = days;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public boolean[] getDays() {
        return days;
    }

    public void setDays(boolean[] days) {
        this.days = days;
    }

    public static int getCount() {
        return count;
    }

    public boolean conflict(TimeSlot timeSlot) {
        if (!shareDays(timeSlot))
            return false;
        else {
            if ((this.getStartTime() >= timeSlot.getStartTime() &&
                    this.getStartTime() <= timeSlot.getEndTime()) ||
                    (this.getEndTime() <= timeSlot.getEndTime() &&
                            this.getEndTime() >= timeSlot.getStartTime()) ||
                    (this.getStartTime() <= timeSlot.getStartTime() &&
                            this.getEndTime() >= timeSlot.getEndTime()) ||
                    (this.getStartTime() >= timeSlot.getStartTime() &&
                            this.getEndTime() <= timeSlot.getEndTime()))
                return true;
            else
                return false;
        }
    }

    public boolean shareDays(TimeSlot timeSlot) {
        for (int i = 0; i < 5; i++) {
            if (this.getDays()[i] && timeSlot.getDays()[i])
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "id=" + id +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", dayStr='" + dayStr + '\'' +
                ", days=" + Arrays.toString(days) +
                '}';
    }
}
