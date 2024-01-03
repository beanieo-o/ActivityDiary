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
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;

import de.rampro.activitydiary.R;
import de.rampro.activitydiary.ui.plan.PlanActivity;

public class CalendarActivity extends BaseActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CalendarView calendarView = findViewById(R.id.calendarView);

        setSupportActionBar(findViewById(R.id.calendar_view_toolbar));
        ActionBar bar = getSupportActionBar();
        if(bar != null) bar.setDisplayHomeAsUpEnabled(true);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //显示用户选择的日期
                month=month+1; //*
                Toast.makeText(CalendarActivity.this, year + "年" + month + "月" + dayOfMonth + "日", Toast.LENGTH_SHORT).show();
                Intent intentplan = new Intent(CalendarActivity.this, PlanActivity.class);
                // 将选中的日期作为额外信息放入Intent
                intentplan.putExtra("YEAR", year);
                intentplan.putExtra("MONTH", month);
                intentplan.putExtra("DAY", dayOfMonth);
                startActivity(intentplan);
            }


        });

/*        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentplan = new Intent(CalendarActivity.this, AddPlan.class);
                startActivity(intentplan);
            }
        });*/
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. For
                // more details, see the Navigation pattern on Android Design:
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
