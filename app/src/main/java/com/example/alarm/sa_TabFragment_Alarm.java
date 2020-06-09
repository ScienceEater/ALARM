package com.example.alarm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;



public class sa_TabFragment_Alarm extends Fragment {
    AlarmDB pdb;
//    Context mContext;
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mContext = context;
//    }

//    mContext = getActivity().getApplicationContext();
    //pdb = new AlarmDB;
    //int cnt = pdb.CountPid();
    int cnt = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sa_tab_fragment_alarm, null);
        //

        HorizontalScrollView scrollView = (HorizontalScrollView) view.findViewById(R.id.hoview);

        LinearLayout TopLayout = view.findViewById(R.id.alarms);
        //TopLayout.setLayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        //TopLayout.setOrientation(LinearLayout.HORIZONTAL);
        //TopLayout.setBackgroundColor(Color.argb(255, 255, 255, 255))

//        Map<String, LinearLayout> n = new HashMap<String, LinearLayout>();
//        for (int i = 0 ; i<cnt; i++){
//            n.put("linearLayout" + i, linearLayout1);
//        }

        for (int i = 0; i < cnt; i++){
            View _view = inflater.inflate(R.layout.alarm_item, null);
            LinearLayout _linearLayout = _view.findViewById(R.id.object1);
            TopLayout.addView(_linearLayout);
        }
        scrollView.removeAllViews();
        scrollView.addView(TopLayout);

        //
        Button button1 = (Button)view.findViewById(R.id.newalarm_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), AlarmSetActivity.class);
                startActivity(intent1);
            }
        });


//        TextView tv_id;
//        TextView tv_etc;
//
//        tv_id = new TextView(this.getContext());
//        tv_id.append("test");
//        tv_id.setTextSize(20);
//
//        layout1.addView(tv_id);
//        tv_etc = new TextView(this.getContext());
//        tv_etc.append("king\n");
//        layout1.addView(tv_etc);


        return view;


    }
}
