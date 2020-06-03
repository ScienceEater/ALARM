package com.example.alarm;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class sa_TabFragment_Alarm extends Fragment {
    int cnt=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sa_tab_fragment_alarm, null);

        Button button1 = (Button)view.findViewById(R.id.newalarm_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), AlarmSetActivity.class);
                startActivity(intent1);
            }
        });

        LinearLayout layout1 = view.findViewById(R.id.alarms);
        layout1.setBackgroundColor(Color.argb(255, 255, 255, 255));

        TextView tv_id;
        TextView tv_etc;

        tv_id = new TextView(this.getContext());
        tv_id.append("test");
        tv_id.setTextSize(20);

        layout1.addView(tv_id);
        tv_etc = new TextView(this.getContext());
        tv_etc.append("king\n");
        layout1.addView(tv_etc);



        return view;
    }
}
