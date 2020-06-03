package com.example.alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AlarmDB {

    SQLiteDatabase db;
    ContentValues row;
    Context ctx;
    AlarmDBHelper mHelper;

    public AlarmDB(Context ctx){
        this.ctx = ctx;
        mHelper = new AlarmDBHelper(ctx);

    }






    /**
     * DB추가
     */
    public void insert(int pid,int week,int GameType,int isSuper,int ActivateNum,String content,int music,int Hour,int Minute) {
        db = mHelper.getWritableDatabase();


        db.execSQL("INSERT INTO Alarm_P VALUES ("+Integer.toString(pid)+","+Integer.toString(week)+","+Integer.toString(GameType)+
                ","+Integer.toString(isSuper)+","+Integer.toString(1)+","+Integer.toString(ActivateNum)+",'"+content+"',"+Integer.toString(music)+","+
                Integer.toString(3)+","+Integer.toString(Hour)+","+Integer.toString(Minute)+ ");");

        Toast.makeText(ctx.getApplicationContext(), "추가 성공", Toast.LENGTH_SHORT).show();
    }

    //super 알람 추가
    public void super_insert(int cid,int pid,int Hour,int IsActive,int Minute,int Week) {
        db = mHelper.getWritableDatabase();


        db.execSQL("INSERT INTO Alarm_C VALUES ("+Integer.toString(cid)+","+Integer.toString(pid)+","+Integer.toString(Hour)+
                ","+Integer.toString(IsActive)+","+Integer.toString(Minute)+","+Integer.toString(Week)+ ");");

        Toast.makeText(ctx.getApplicationContext(), "추가 성공", Toast.LENGTH_SHORT).show();
    }
    /**
     *

     * DB 조회
     *


     *
     */
    public int SelectActive(int pid){
        int ret=0;
        db=mHelper.getReadableDatabase();
        Cursor cursor;
        cursor=db.rawQuery("SELECT IsActive from Alarm_P WHERE pid="+pid,null);
        if(cursor.moveToFirst()){
            ret=cursor.getInt(0);
        }
        cursor.close();
        mHelper.close();
        Log.d("Alarm active db return",Integer.toString(ret));
        return ret;
    }
    public int SelectPid(){
        int ret=0;
        db=mHelper.getReadableDatabase();
        Cursor cursor;
        cursor=db.rawQuery("SELECT MAX(pid) from Alarm_P",null);
        if(cursor.moveToFirst()){
            ret=cursor.getInt(0);
        }
        cursor.close();
        mHelper.close();
        return ret;
    }

    public int SelectCid(){
        int ret=0;
        db=mHelper.getReadableDatabase();
        Cursor cursor;
        cursor=db.rawQuery("SELECT MAX(pid) from Alarm_C",null);
        if(cursor.moveToFirst()){
            ret=cursor.getInt(0);
        }
        cursor.close();
        mHelper.close();
        return ret;
    }
    public int[][] SelectTime(int pid){

        int i=0;
        int ret[][];
        String[] str;

        db = mHelper.getReadableDatabase();
        Cursor cursor;

        // SQL 명령으로 읽기
        cursor = db.rawQuery("SELECT Week,Hour,Minute  FROM Alarm_P WHERE PID="+Integer.toString(pid), null);

        if (cursor.moveToFirst()) {
            ret = new int[cursor.getCount()][3];
            do{
                ret[i][0]=cursor.getInt(0);
                ret[i][1]=cursor.getInt(1);
                ret[i][2]=cursor.getInt(2);
            }while (cursor.moveToNext());

        }else{
            ret=null;
        }

        cursor.close();
        mHelper.close();
        return ret;
    }


    /**
     *

     * DB 파일을 복사하자...  // 내장되어 있는 DB를 외장메모리로 복사하는 기능
     * 지금은 필요X


     *
     * @param toPath
     * @param toFilename
     * @param srcName
     * @see
     */
    private void TransferFile(String toPath, String toFilename, String srcName) {
        String	dstName = toPath + "/" + toFilename;
        try {
            File targetPath = new File(toPath);

            Log.d("Alarm", "Transfer Src = " + srcName);
            Log.d("Alarm", "Transfer Dst = " + dstName);

            targetPath.mkdirs();

            File		fi = new File(srcName);
            File		fo = new File(dstName);

            FileInputStream fis = new FileInputStream(fi);
            BufferedInputStream bis  = new BufferedInputStream(fis);

            FileOutputStream fos = null;
            BufferedOutputStream bos = null;

            if (fo.exists()) {
                fo.delete();
                fo.createNewFile();
            }

            fos = new FileOutputStream(fo);
            bos  = new BufferedOutputStream(fos);

            int read = -1;
            byte[] buffer = new byte[1024];

            while ((read = bis.read(buffer, 0, 1024)) != -1) {
                bos.write(buffer, 0, read);
            }

            bos.flush();
            fos.close();
            bos.close();
            fis.close();
            bis.close();
        } catch (FileNotFoundException e) {
            Log.w("Alarm", "FILE not found("+ srcName + " or " + dstName +")");
        } catch (IOException e) {
            Log.w("Alarm", "io exception("+ srcName + "to" + dstName + ")");
        }
    }


}