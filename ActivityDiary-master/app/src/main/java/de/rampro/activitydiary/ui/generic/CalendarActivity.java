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

package de.rampro.activitydiary.ui.generic;

import static de.rampro.activitydiary.R.id.calendarView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import de.rampro.activitydiary.R;

public class CalendarActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //显示用户选择的日期
                    month=month+1; //*
                    Toast.makeText(CalendarActivity.this, year + "年" + month + "月" + dayOfMonth + "日", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.activity_plan_overview);//跳转到详细页面
            }




        });
    }
}
