package com.example.alarm;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    AlarmDB pdb;
    Context mContext = this.getApplicationContext();

    Intent intent = getIntent();
    //int pid= intent.getIntExtra("pid",-1);
    //String strTag = intent.getStringExtra("contents");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // 알람음 재생
        this.mediaPlayer = MediaPlayer.create(this, R.raw.alarm2);
        this.mediaPlayer.start();

        findViewById(R.id.btnClose).setOnClickListener(mClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // MediaPlayer release
        if (this.mediaPlayer != null) {
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
    }

    /* 알람 종료 */
    private void close() {
        if (this.mediaPlayer.isPlaying()) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }

        finish();
    }

    /*알람 종료 시 기록*/
    private void logAlarm() {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("MM-dd hh:mm");
        String getTime = simpleDate.format(mDate);

        int pid=intent.getIntExtra("pid",-1);

        mContext = getApplicationContext();
        pdb = new AlarmDB(mContext);

        pdb.logging(pid, getTime);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnClose:
                    // 알람 종료
                    close();
                    logAlarm();

                    break;
            }
        }
    };
}
