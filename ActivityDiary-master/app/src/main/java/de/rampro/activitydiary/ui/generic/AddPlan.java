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
import de.rampro.activitydiary.db.ActivityDiaryContentProvider;
import de.rampro.activitydiary.db.ActivityDiaryContract;
import de.rampro.activitydiary.db.EventContentProvider;
import de.rampro.activitydiary.model.Event;

import de.rampro.activitydiary.R;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddPlan extends AppCompatActivity {

    private Spinner spinnerActivities;
    private TextInputEditText editNote;
    private Button buttonAddToCalendar, pdate_start, ptime_start, pdate_end, ptime_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);

        spinnerActivities = findViewById(R.id.spinnerActivities);
        buttonAddToCalendar = findViewById(R.id.buttonAddToCalendar);
        editNote = findViewById(R.id.edit_plan_note);
        pdate_start = findViewById(R.id.pdate_start);
        ptime_start = findViewById(R.id.ptime_start);
        pdate_end = findViewById(R.id.pdate_end);
        ptime_end = findViewById(R.id.ptime_end);

        // 假设这个方法设置了Spinner的适配器并从数据库中填充数据
        setupSpinnerWithData();

        // 添加到日历的逻辑
        buttonAddToCalendar.setOnClickListener(v -> addToCalendar());

        Button button = findViewById(R.id.add_activity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPlan.this, EditActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupSpinnerWithData() {
        List<String> activities = new ArrayList<>();

        // 获取ContentResolver
        ContentResolver resolver = getContentResolver();


        // 定义要查询的URI和列
        Uri activitiesUri = ActivityDiaryContract.DiaryActivity.CONTENT_URI;
        String[] projection = new String[] {"name"};

        // 执行查询
        Cursor cursor = resolver.query(activitiesUri, projection, null, null, null);

        // 检查并处理查询结果
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String activity = cursor.getString(cursor.getColumnIndex("name"));  // 获取列数据
                activities.add(activity);
            }
            cursor.close();
        }

        // 设置Spinner适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActivities.setAdapter(adapter);
    }


    public void StartDatePickerDialog(View view) {
        // 获取当前日期
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 创建并显示日期选择器对话框
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year1, monthOfYear, dayOfMonth) -> {
                    // 这里处理日期选择结果
                    String selectedDate = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    Button button = (Button) view;
                    button.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    public void EndDatePickerDialog(View view) {
        // 获取当前日期
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 创建并显示日期选择器对话框
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view1, year1, monthOfYear, dayOfMonth) -> {
                    // 这里处理日期选择结果
                    String selectedDate = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    Button button = (Button) view;
                    button.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    public void StartTimePickerDialog(View view) {
        // 获取当前时间
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // 创建并显示时间选择器对话框
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view12, hourOfDay, minute1) -> {
                    // 这里处理时间选择结果
                    String selectedTime = hourOfDay + ":" + minute1;
                    Button button = (Button) view;
                    button.setText(selectedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    public void EndTimePickerDialog(View view) {
        // 获取当前时间
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // 创建并显示时间选择器对话框
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view12, hourOfDay, minute1) -> {
                    // 这里处理时间选择结果
                    String selectedTime = hourOfDay + ":" + minute1;
                    Button button = (Button) view;
                    button.setText(selectedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }



    private void addToCalendar() {
        String selectedActivity = spinnerActivities.getSelectedItem().toString();
        String startDate = pdate_start.getText().toString();
        String startTime = ptime_start.getText().toString();
        String endDate = pdate_end.getText().toString();
        String endTime = ptime_end.getText().toString();
        String note = editNote.getText().toString();

        // 检查输入有效性
        if (selectedActivity.isEmpty() || startDate.isEmpty()) {
            // 显示错误或进行相应处理
            return;
        }

        // 创建一个对象来表示活动
        Event event = new Event(selectedActivity, startDate, endDate, startTime, endTime, note);

        // 调用数据库操作方法添加到数据库
        addToDatabase(event);
    }

    private void addToDatabase(Event event) {
        ContentValues values = new ContentValues();
        values.put("activity_name", event.getActivityName());
        values.put("start_date", event.getStartDate());
        values.put("end_date", event.getEndDate());
        values.put("start_time", event.getStartTime());
        values.put("end_time", event.getEndTime());
        values.put("note", event.getNote());

        try {
            Uri uri = getContentResolver().insert(EventContentProvider.CONTENT_URI, values);
            if (uri != null) {
                Toast.makeText(this, "事件添加成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "添加事件失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("AddEventActivity", "数据库操作异常", e);
            Toast.makeText(this, "添加事件时发生错误", Toast.LENGTH_SHORT).show();
        }
    }

}
