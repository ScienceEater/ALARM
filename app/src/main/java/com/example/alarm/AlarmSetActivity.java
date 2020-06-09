package com.example.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AlarmSetActivity extends AppCompatActivity {

    private View view;

    AlarmDB pdb;
    Context mContext;

    private Calendar calendar;
    static int cnt = 3;
    int isSuper=0;
    private TimePicker timePicker;
    //public static TabFragment_Alarm

    private ToggleButton _toggleSun, _toggleMon, _toggleTue, _toggleWed, _toggleThu, _toggleFri, _toggleSat,_toggleSuper;
    String strTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_set);

        this.calendar = Calendar.getInstance();
        // 현재 날짜 표시
        displayDate();

        this.timePicker = findViewById(R.id.timePicker);
        //Calender, 알람 버튼에 리스너 연결
        findViewById(R.id.btnAlarm).setOnClickListener(mClickListener);

        //태그 추가
        EditText editText1 = (EditText) findViewById(R.id.Tag) ;
        strTag = editText1.getText().toString() ;


        _toggleSun = (ToggleButton) findViewById(R.id.btnWeek1);
        _toggleMon = (ToggleButton) findViewById(R.id.btnWeek2);
        _toggleTue = (ToggleButton) findViewById(R.id.btnWeek3);
        _toggleWed = (ToggleButton) findViewById(R.id.btnWeek4);
        _toggleThu = (ToggleButton) findViewById(R.id.btnWeek5);
        _toggleFri = (ToggleButton) findViewById(R.id.btnWeek6);
        _toggleSat = (ToggleButton) findViewById(R.id.btnWeek7);
        _toggleSuper=(ToggleButton) findViewById(R.id.isSuper);
    }

    /* 날짜 표시 */
    private void displayDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        //((TextView) findViewById(R.id.txtDate)).setText(format.format(this.calendar.getTime()));
    }

    /* 알람 등록 */
    private void setAlarm() {
        //DB실험
        mContext = getApplicationContext();
        pdb = new AlarmDB(mContext); // SizerDB 연동 클래스 인스턴스


        // 알람 시간 설정
        this.calendar.set(Calendar.HOUR_OF_DAY, this.timePicker.getHour());
        this.calendar.set(Calendar.MINUTE, this.timePicker.getMinute());
        this.calendar.set(Calendar.SECOND, 0);

        // 현재일보다 이전이면 등록 실패
        if (this.calendar.before(Calendar.getInstance())) {
            Toast.makeText(getApplicationContext(), "알람시간이 현재시간보다 이전일 수 없습니다.", Toast.LENGTH_LONG).show();
            return;
        }

        /**
         요일 추가하는거(레이아웃 완성되면 추가 할 부분), 비트 연산으로 해당 요일값 추가
         **/
        int week = 0;
        if(_toggleSun.getText().toString().contains("on")) week+=1<<1; //일요일
        if(_toggleMon.getText().toString().contains("on")) week+=1<<2; //월요일
        if(_toggleTue.getText().toString().contains("on")) week+=1<<3; //화요일
        if(_toggleWed.getText().toString().contains("on")) week+=1<<4; //수요일
        if(_toggleThu.getText().toString().contains("on")) week+=1<<5; //목요일
        if(_toggleFri.getText().toString().contains("on")) week+=1<<6; //금요일
        if(_toggleSat.getText().toString().contains("on")) week+=1<<7; //요일
        if(_toggleSuper.getText().toString().contains("on")) isSuper=1; //요일



        /**
         * 비트연산말고 배열로 해보는 중, 데이터베이스와 연관되어, 다시 비트 연산으로 개발하려는 중.
         * weekday
         * boolean[] weekday = { false, _toggleSun.isChecked(), _toggleMon.isChecked(), _toggleTue.isChecked(), _toggleWed.isChecked(),
         *                 _toggleThu.isChecked(), _toggleFri.isChecked(), _toggleSat.isChecked() }; // sunday=1 이라서 0의 자리에는 아무 값이나 넣었음
         */

        /**
         * 태그 값을 전달할 부분 구현 필요.
         * EditText id: memoedit
         */

        /**
         Receiver 설정
         Receiver에 알람이 울릴 요일을 전달. 1이 일요일, 7이 토요일 의미!
         Receiver에 PID 전달

         **/

        // https://wonjerry.tistory.com/19 앱 꺼져도 다시 알람 만들 수 있는.

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);

        intent.putExtra("week",week);
        intent.putExtra("pid",pdb.SelectPid()+1);
        //Fragment fragment =new TabFragment_Info();
        Bundle bundle =new Bundle();
        bundle.putInt("cnt",cnt);
        //fragment.setArguments(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), cnt++, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //알람 DB에 저장하기
        int pid=pdb.SelectPid()+1;
        pdb.insert(pid,week,1,isSuper,1,strTag,1,this.timePicker.getHour(),this.timePicker.getMinute());
        int[][] t=pdb.SelectTime(pdb.SelectPid());
        Log.d("AlarmDB",Integer.toString(pdb.SelectPid())+Integer.toString(t[0][0])+"요일"+Integer.toString(t[0][1])+"시"+Integer.toString(t[0][2])+"분");
        // 알은 24시간만다 반복되도록 설정
        AlarmManager alarmManager = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
        long oneday = 24 * 60 * 60 * 1000;// 24시간 마다 반복
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, this.calendar.getTimeInMillis(),oneday, pendingIntent);
        //슈퍼 알람이면 자식 알람도 추가해준다.
        if(isSuper==1)
        {
            Intent super_intent = new Intent(getApplicationContext(), AlarmReceiver.class);

            super_intent.putExtra("week",week);
            super_intent.putExtra("cid",pdb.SelectCid()+1);
            int cid=pdb.SelectCid();
            pdb.super_insert(pid,cid,this.timePicker.getHour(),1,this.timePicker.getMinute()+1,week);
            PendingIntent super_pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), cnt++, super_intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager super_alarmManager = (AlarmManager) getSystemService(getApplicationContext().ALARM_SERVICE);
            this.calendar.add( this.calendar.MINUTE, 1 );
            super_alarmManager.set(AlarmManager.RTC_WAKEUP, this.calendar.getTimeInMillis(),super_pendingIntent);
        }




        // Toast 보여주기 (알람 시간 표시)
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Toast.makeText(getApplicationContext(), "Alarm : " + format.format(calendar.getTime()), Toast.LENGTH_LONG).show();
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnAlarm:
                    // 알람 등록
                    setAlarm();

                    break;
            }
        }
    };

}
