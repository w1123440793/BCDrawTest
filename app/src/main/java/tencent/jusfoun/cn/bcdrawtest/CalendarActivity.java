package tencent.jusfoun.cn.bcdrawtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author  wangchenchen
 * CreateDate 2015/12/21.
 * Email wcc@jusfoun.com
 * Description
 */
public class CalendarActivity extends Activity {

    private CalendarView cv;
    private TextView tv;
    private ImageView btnBack;

    private long MaxDateLong;
    private long MinDateLong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        cv=(CalendarView)findViewById(R.id.calendarView1);
        tv=(TextView)findViewById(R.id.current_time_text);
        btnBack=(ImageView)findViewById(R.id.btnBack);
        SetData();
        //为calendarview组件的日期改变事件添加事件监听器

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub
                tv.setText("您选择的日期是："+String.valueOf(year)+"-"+
                        String.valueOf(month+1)+"-" +String.valueOf(dayOfMonth));
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }
    private void SetData() {

//        SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy");
//        String MinDateString =sdf.format(MyDate.nextDate());
//        Log.e("tomorrowString", MinDateString);
//        String MaxDateString =sdf.format(MyDate.nextDate(new MyDate(),30));
//        Log.e("NextString",MaxDateString );
//        try {
//            Date MinDate = sdf.parse(MinDateString);
//            MinDateLong= MinDate.getDate();
//            //继续转换得到秒数的long型
//            //MinDateLong = MinDate.getTime() / 1000;
//            Date  MaxDate = sdf.parse(MaxDateString);
//            //继续转换得到秒数的long型
//            MaxDateLong=MaxDate.getDate();
//            //MaxDateLong = MaxDate.getTime() / 1000;
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }catch (ParseException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        //设置该日历组件支持的最大值。
        long min=System.currentTimeMillis() - 30*24*60 * 60 * 1000;
        long max=System.currentTimeMillis() +  30*24*60 * 60 * 1000;
        cv.setMaxDate(min);
        cv.setMinDate(max);
        SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String MaxDateString = sdf.format(cv.getMaxDate());
        String MinDateString = sdf.format(cv.getMinDate());
        String maxString=sdf.format(max);
        String minString=sdf.format(min);
        Log.e("time","MaxDateString=="+MaxDateString
        +"\nMinDateString=="+MinDateString
        +"\nmax=="+max
        +"\nmin=="+min
        +"\nmaxString=="+maxString
        +"\nminString=="+minString);
        //设置该日历组件支持的最小值
    }
}
