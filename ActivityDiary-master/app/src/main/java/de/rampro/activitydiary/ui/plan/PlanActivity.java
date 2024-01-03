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

package de.rampro.activitydiary.ui.plan;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.rampro.activitydiary.R;
import de.rampro.activitydiary.db.EventContentProvider;
import de.rampro.activitydiary.ui.generic.AddPlan;
import de.rampro.activitydiary.ui.generic.BaseActivity;
import de.rampro.activitydiary.db.LocalDBHelper;
import de.rampro.activitydiary.ui.generic.CalendarActivity;

public class PlanActivity extends BaseActivity {
    Button button,add_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_overview);
        //calendarView = findViewById(R.id.calendarView);
        button = findViewById(R.id.calendarView);
        add_button = findViewById(R.id.add_plan);
        RecyclerView recyclerView = findViewById(R.id.daily_plan);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 获取Intent以及附加的日期信息
        Intent intent = getIntent();
        int year = intent.getIntExtra("YEAR", -1); // 默认值为-1
        int month = intent.getIntExtra("MONTH", -1);
        int day = intent.getIntExtra("DAY", -1);
        String selectedDate = year + "-" + month + "-" + day;
        button.setText(selectedDate);

        updatePlanItems(year,month,day,recyclerView);
        /*//calendarView点击事件
        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date =new Date(calendarView.getDate());
//                String dateString = dateFormat.format(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String dateString = calendar.get(calendar.YEAR)+"年"+calendar.get(calendar.MONTH)+"月"+ calendar.get(calendar.DAY_OF_MONTH)+"日";
                //System.out.println();
                Toast.makeText(PlanActivity.this,"clicked"+dateString,Toast.LENGTH_LONG).show();
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String dateString = year+"年"+(month+1)+"月"+ dayOfMonth+"日";
                //System.out.println();
                Toast.makeText(PlanActivity.this,"clicked"+dateString,Toast.LENGTH_LONG).show();
                updatePlanItems(year, month, dayOfMonth);
            }
        });*/
        setSupportActionBar(findViewById(R.id.plan_overview));
        ActionBar bar = getSupportActionBar();
        if(bar != null) bar.setDisplayHomeAsUpEnabled(true);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Calendar calendar = Calendar.getInstance();

                // 初始化DatePickerDialog，并设置当前选中的年月
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PlanActivity.this,
                        null,
                        //dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

                // 确认按钮
                datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(view -> {
                    int year = datePickerDialog.getDatePicker().getYear();
                    int monthOfYear = datePickerDialog.getDatePicker().getMonth() + 1;
                    int dayOfMonth = datePickerDialog.getDatePicker().getDayOfMonth();
                    String selectedDate = year + "-" + monthOfYear + "-" + dayOfMonth;
                    button.setText(selectedDate);
                    //更新recyclerView
                    updatePlanItems(year,monthOfYear,dayOfMonth,recyclerView);
                    datePickerDialog.dismiss();
                });*/
                Intent intentplan = new Intent(PlanActivity.this, CalendarActivity.class);
                startActivity(intentplan);
            }
        });



        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentplan = new Intent(PlanActivity.this, AddPlan.class);
                startActivity(intentplan);
            }
        });
    }


    private void updatePlanItems(int year, int month, int dayOfMonth,RecyclerView recyclerView) {
        String date = year + "-" + month + "-" + dayOfMonth;
        List<Plan> plans = queryPlansOfDate(date);//new ArrayList<>();
        /*for (int i = 0; i < 20; i++) {
            Plan plan = new Plan();
            plan.setTitle("title1");
            plan.setTime("13:00");
            plans.add(plan);
        }*/
        Log.i("test", plans.toString());
        PlanAdapter planAdapter = new PlanAdapter(plans);
        recyclerView.setAdapter(planAdapter);

    }

    public List<Plan> queryPlansOfDate(String date) {
        List<Plan> plans = new ArrayList<>();
        /*LocalDBHelper dbHelper = new LocalDBHelper(this);
        // 这里使用 SQLiteDatabase 对象来执行查询
        SQLiteDatabase db = dbHelper.getReadableDatabase();*/
        Uri uri = EventContentProvider.CONTENT_URI;
        String[] projection ={"activity_name", "start_time"};
        String selection = "start_date=?";
        String[] selectionArgs = {date};
        Log.i("date", date);
        String sortOrder = "start_time ASC";
        Cursor cursor = getContentResolver().query(uri,projection,selection,selectionArgs,sortOrder);
        if(cursor == null){
            Log.i("test cursor", "cursor null");
        }
        else{
            Log.i("test cursor", "cursor not null:"+cursor.getCount());
        }
        if (cursor.moveToFirst()) {
            do {
                // 从 cursor 中获取数据并添加到 plans 列表中
                Plan plan = new Plan();
                String name = cursor.getString(cursor.getColumnIndex("activity_name"));
                String time = cursor.getString(cursor.getColumnIndex("start_time"));
                plan.setTitle(name);
                plan.setTime(time);
                Log.i("test2", name+time);
                // ... 其他属性 ...
                plans.add(plan);
            }while (cursor.moveToNext());
        }
        Log.i("test1", plans.toString());
        cursor.close();
        return plans;
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


