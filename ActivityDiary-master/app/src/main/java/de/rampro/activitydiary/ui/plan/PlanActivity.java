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
    /*button原本做成了点击展开DatePickerDialog，然后将RecyclerView中的内容更新为所选日期的plans，并且将button的text设为所选日期
    button现在功能为展示从calendarView页面选中的日期，点击可返回calendarView页面
    add_button实现点击跳转add_plan页面*/
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

        //toolbar爆改actionbar，具体原理
        setSupportActionBar(findViewById(R.id.plan_overview));
        ActionBar bar = getSupportActionBar();
        if(bar != null) bar.setDisplayHomeAsUpEnabled(true);


        button.setOnClickListener(new View.OnClickListener() {
            //点击button跳转到CalendarActivity
            @Override
            public void onClick(View v) {
                /*
                //button原本可实现的通过DatePickerDialog更新RecyclerView

                Calendar calendar = Calendar.getInstance();

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


        //点击add_button跳转到add_plan页面
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentplan = new Intent(PlanActivity.this, AddPlan.class);
                startActivity(intentplan);
            }
        });
    }

    //更新recyclerview的方法
    private void updatePlanItems(int year, int month, int dayOfMonth,RecyclerView recyclerView) {
        //这里要注意数据转换为字符串的格式与数据库中数据存储的格式是否一致
        String date = year + "-" + month + "-" + dayOfMonth;
        List<Plan> plans = queryPlansOfDate(date);//new ArrayList<>();
        /*//本段代码仅作测试用（由于之前数据库查询日期格式不匹配所以用这个样本来检验方法是否能正确把数据放到recyclerview中）
        for (int i = 0; i < 20; i++) {
            Plan plan = new Plan();
            plan.setTitle("title1");
            plan.setTime("13:00");
            plans.add(plan);
        }*/
        //通过查看log确认查询函数是否正确输出
        Log.i("test", plans.toString());
        PlanAdapter planAdapter = new PlanAdapter(plans);
        recyclerView.setAdapter(planAdapter);

    }

    //数据查询
    public List<Plan> queryPlansOfDate(String date) {
        List<Plan> plans = new ArrayList<>();
        //uri与相应contentProvider绑定
        Uri uri = EventContentProvider.CONTENT_URI;
        //要查询的列名
        String[] projection ={"activity_name", "start_time"};
        //查询条件，`?`是占位符
        String selection = "start_date=?";
        //查询条件中有几个`?`就有几个实际值
        String[] selectionArgs = {date};
        //查看字符串是否以正确格式转换
        Log.i("date", date);
        //输出结果顺序
        String sortOrder = "start_time ASC";

        //查询
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
                //检查属性是否被正确获取
                Log.i("test2", "title:"+name+"time:"+time);
                plans.add(plan);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return plans;
    }

    //让actionbar起到navigation作用的方法
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


