/*
 * ActivityDiary
 *
 * Copyright (C) 2023 Raphael Mack http://www.raphael-mack.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.rampro.activitydiary.model;

public class Event {
    private String activityName;
    private String startDate;
    private String startTime;
    private String endDate;

    private String endTime;
    private String note;

    public Event(String activityName, String startDate, String endDate, String startTime, String endTime, String note) {
        this.activityName = activityName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.note = note;
    }

    public String getActivityName(){
        return activityName;
    }
    public void setActivityName(String name){ activityName = name;}

    public String getStartDate(){ return startDate; }
    public void setStartDate(String sdate){ startDate = sdate;}

    public String getEndDate(){ return endDate; }
    public void setEndDate(String edate){ endDate = edate;}

    public String getStartTime(){ return startTime; }
    public void setStartTime(String stime){ startTime = stime;}

    public String getEndTime(){ return endTime; }
    public void setEndTime(String etime){ endTime = etime;}

    public String getNote(){ return  note;}
    public void setNote(String Note){ note = Note;}
}
