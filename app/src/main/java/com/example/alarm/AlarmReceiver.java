package com.example.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    AlarmDB  pdb;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent sIntent = new Intent(context, AlarmService.class);
        pdb = new AlarmDB(context); // SizerDB 연동 클래스 인스턴스

        /**
         * 만약 켜져 있는 알람이 아니라면 자동 종료 되도록 설정,
         */
        int pid=intent.getIntExtra("pid",-1);

        /*
            super alarm은 여기서 처리
         */
        if (pid==-1)
        {
            int cid=intent.getIntExtra("cid",-1);
            Log.e("Alarm Active cid", Integer.toString(cid));
            if (pdb.SelectActive(cid) == 0) {
                return;
            }

            Calendar cal = Calendar.getInstance();
            int weekday = intent.getIntExtra("week", -1);
            Log.e("alarm", Integer.toString(weekday) + "현재 값");
            //bit연산을 통해서 해당 요일이 존재하는지 검사합니다.
            if ((1 << (cal.get(Calendar.DAY_OF_WEEK)) & (weekday)) == 0) {
                Log.e("alarm", "오늘은 해당 날짜가 아닙니다");
                return;
            }
        }
        else {


            Log.e("Alarm Active pid", Integer.toString(pid));
            if (pdb.SelectActive(pid) == 0) {
                return;
            }

            Calendar cal = Calendar.getInstance();
            int weekday = intent.getIntExtra("week", -1);
            Log.e("alarm", Integer.toString(weekday) + "현재 값");
            //bit연산을 통해서 해당 요일이 존재하는지 검사합니다.
            if ((1 << (cal.get(Calendar.DAY_OF_WEEK)) & (weekday)) == 0) {
                Log.e("alarm", "오늘은 해당 날짜가 아닙니다");
                return;
            }

        }


        // Oreo(26) 버전 이후부터는 Background 에서 실행을 금지하기 때문에 Foreground 에서 실행해야 함
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(sIntent);
        } else {
            context.startService(sIntent);
        }

    }
}
