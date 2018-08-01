package com.wise.bottombar.sample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class StatFrag extends android.support.v4.app.Fragment {
    GraphView gv;
    Button monthbt;
    ImageButton backbtn;
    SharedPreferences shared,shared1;
    TextView DayL;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_stat,container,false);
        shared = getActivity().getSharedPreferences("Weekly", MODE_PRIVATE);
        shared1 = getActivity().getSharedPreferences("Monthly", MODE_PRIVATE);
        DayL=(TextView)view.findViewById(R.id.DayL);
        DayL.setText("Days in a Week");
        TextView datev=(TextView)view.findViewById(R.id.datev);
        Calendar calendar;
        gv=(GraphView)view.findViewById(R.id.graph);
        gv.getViewport().setMaxY(12);
        gv.getViewport().setMinY(0);
        monthbt=(Button)view.findViewById(R.id.monthbtn);
        backbtn=(ImageButton)view.findViewById(R.id.backbtn);
        backbtn.setVisibility(View.GONE);
        calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); //Date and time
        String currentDate = sdf.format(calendar.getTime());


//Day of Name in full form like,"Saturday", or if you need the first three characters you have to put "EEE" in the date format and your result will be "Sat".
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String dayName = sdf_.format(date);
        datev.setText("" + dayName + "," + currentDate + "");



        displayweekdata();
        String mostday=datetoday("12/03/2018");
        String lstday=datetoday("13/03/2018");
        displayweekdata();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayL.setText("Days in a Week");
                displayweekdata();
                backbtn.setVisibility(View.GONE);

            }
        });
        monthbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayL.setText("Days in a Month");
                backbtn.setVisibility(View.VISIBLE);
                displaymonthdata();
            }
        });

        return view;

    }



    public BarGraphSeries<DataPoint> drawgraph(String s[]){
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
        for(int i=0;i<s.length;i++)
        {
            String m=""+s[i].charAt(3)+s[i].charAt(4);
            int min=Integer.parseInt(m);
            String h=""+s[i].charAt(0)+s[i].charAt(1);
            int hmin=(Integer.parseInt(h))*60;
            hmin=hmin+min;
            series.appendData(new DataPoint(i,hmin/60),true,28);
        }
        return series;
    }
    public String mostused(String s[])
    {
        int max=0;
        String h="";
        String temp=new String();
        for(int i=0;i<s.length;i++)
        {

            String m=""+s[i].charAt(3)+s[i].charAt(4);
            int min=Integer.parseInt(m);
            h=""+s[i].charAt(0)+s[i].charAt(1);
            int hmin=(Integer.parseInt(h))*60;
            hmin=hmin+min;
            if(hmin>max){
                max=hmin;
                temp=s[i];
            }

        }
        return h;
    }
    public String leastused(String s[])
    {
        int mins=999999;
        String h="";
        String temp=new String();
        for(int i=0;i<s.length;i++)
        {

            String m=""+s[i].charAt(3)+s[i].charAt(4);
            int min=Integer.parseInt(m);
            h=""+s[i].charAt(0)+s[i].charAt(1);
            int hmin=(Integer.parseInt(h))*60;
            hmin=hmin+min;
            if(hmin<mins){
                mins=hmin;
                temp=s[i];
            }

        }
        return h;
    }

    public void displaymonthdata()
    {

        gv.removeAllSeries();
        gv.getViewport().setMaxX(30);
        gv.getViewport().setMinX(0);
        gv.getViewport().setMaxY(12);
        gv.getViewport().setMinY(0);
        gv.getViewport().setXAxisBoundsManual(true);
        String s[]=new String[28];
        for(int i=1;i<=28;i++){
            int totalSecs=Integer.parseInt(shared1.getString(String.valueOf(i),String.valueOf(0)));
            int hours = totalSecs / 3600;
            int minutes = (totalSecs % 3600) / 60;
            int seconds = totalSecs % 60;

            String timeString = String.format("%02d:%02d:%02d", hours*3, minutes, seconds);
            s[i-1]=timeString;
        }
        final BarGraphSeries<DataPoint> series=drawgraph(s);
        gv.addSeries(series);
        series.setDataWidth(0.25);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(),"You have used your phone for " +dataPoint.toString().substring(6,9)+" hrs on "+dataPoint.toString().substring(1,3)+" of this month", Toast.LENGTH_SHORT).show();
            }
        });
    }

    String datetoday(String s)
    {
        SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
        Date dt1= null;
        String finalDay="";
        try {
            dt1 = format1.parse(s);
            DateFormat format2=new SimpleDateFormat("EE");
            finalDay=format2.format(dt1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDay;
    }
    void displayweekdata()
    {
        gv.getViewport().setMaxX(7);
        gv.getViewport().setMinX(0);
        gv.getViewport().setMaxY(12);
        gv.getViewport().setMinY(0);
        gv.getViewport().setXAxisBoundsManual(true);
        String st[]=new String[7];
        for(int i=1;i<=7;i++){
            int totalSecs=Integer.parseInt(shared.getString(String.valueOf(i),String.valueOf(0)));
            int hours = totalSecs / 3600;
            int minutes = (totalSecs % 3600) / 60;
            int seconds = totalSecs % 60;

            String timeString = String.format("%02d:%02d:%02d", hours*2, minutes, seconds);
            st[i-1]=timeString;
        }
        BarGraphSeries<DataPoint> series =drawgraph(st);
        gv.addSeries(series);
//        series.setSpacing(7);
        series.setDataWidth(0.25);

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                String w="sunday";
                switch(dataPoint.toString().substring(1,2))
                {
                    case "0":w="Sunday";
                    break;

                    case "1":w="Monday";
                    break;

                    case "2":w="Tuesday";
                    break;

                    case "3":w="Wednesday";
                    break;

                    case "4":w="Thursday";
                    break;

                    case "5":w="Friday";
                    break;

                    case "6":w="Saturday";
                    break;

                    case "7":w="Sunday";
                }
                Toast.makeText(getActivity(),"You have used your phone for " +dataPoint.toString().substring(5,8)+" hrs on "+w, Toast.LENGTH_SHORT).show();
            }
        });
    }
}