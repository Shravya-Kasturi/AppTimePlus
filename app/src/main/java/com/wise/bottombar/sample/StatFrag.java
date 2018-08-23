package com.wise.bottombar.sample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
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
    SharedPreferences shared,shared1;
    Button todaygb,weekgb,monthgb;
    StaticLabelsFormatter staticLabelsFormatter;

    TextView t;
    ConstraintLayout c;


    LayerDrawable getBorders(int bgColor, int borderColor,
                             int left, int top, int right, int bottom){
        // Initialize new color drawables
        ColorDrawable borderColorDrawable = new ColorDrawable(borderColor);
        ColorDrawable backgroundColorDrawable = new ColorDrawable(bgColor);

        // Initialize a new array of drawable objects
        Drawable[] drawables = new Drawable[]{
                borderColorDrawable,
                backgroundColorDrawable
        };

        // Initialize a new layer drawable instance from drawables array
        LayerDrawable layerDrawable = new LayerDrawable(drawables);

        // Set padding for background color layer
        layerDrawable.setLayerInset(
                1, // Index of the drawable to adjust [background color layer]
                left, // Number of pixels to add to the left bound [left border]
                top, // Number of pixels to add to the top bound [top border]
                right, // Number of pixels to add to the right bound [right border]
                bottom // Number of pixels to add to the bottom bound [bottom border]
        );

        // Finally, return the one or more sided bordered background drawable
        return layerDrawable;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_stat,container,false);
        if(FiveColorChangingTabsActivity.phoneCheckFlag==0) {
            LockScreenStateReceiver mLockScreenStateReceiver = new LockScreenStateReceiver();
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
            filter.addAction(Intent.ACTION_USER_PRESENT);

            getActivity().registerReceiver(mLockScreenStateReceiver, filter);

            FiveColorChangingTabsActivity.phoneCheckFlag = 1;
        }

        t=(TextView)view.findViewById(R.id.count);
        shared = getActivity().getSharedPreferences("Weekly", MODE_PRIVATE);
        shared1 = getActivity().getSharedPreferences("Monthly", MODE_PRIVATE);

        final LayerDrawable bottomBorder = getBorders(
                Color.parseColor("#edf4fb"), // Background color
                Color.parseColor("#FF8c00"), // Border color
                0, // Left border in pixels
                0, // Top border in pixels
                0, // Right border in pixels
                12 // Bottom border in pixels
        );

        todaygb=(Button)view.findViewById(R.id.todaygb);
        weekgb=(Button)view.findViewById(R.id.weekgb);
        monthgb=(Button)view.findViewById(R.id.monthgb);
        c=(ConstraintLayout)view.findViewById(R.id.constvis);
        TextView datev=(TextView)view.findViewById(R.id.datev);
        Calendar calendar;
        gv=(GraphView)view.findViewById(R.id.graph);
        gv.getViewport().setMaxY(12);
        gv.getViewport().setMinY(0);

        calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy"); //Date and time
        String currentDate = sdf.format(calendar.getTime());


//Day of Name in full form like,"Saturday", or if you need the first three characters you have to put "EEE" in the date format and your result will be "Sat".
        SimpleDateFormat sdf_ = new SimpleDateFormat("EEEE");
        Date date = new Date();
        String dayName = sdf_.format(date);
        datev.setText("" + dayName + "," + currentDate + "");

        staticLabelsFormatter= new StaticLabelsFormatter(gv);

        displayweekdata();
        String mostday=datetoday("12/03/2018");
        String lstday=datetoday("13/03/2018");


        todaygb.setBackground(bottomBorder);
        todaygb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekgb.setTextColor(Color.parseColor("#666666"));
                weekgb.setBackgroundColor(Color.parseColor("#edf4fb"));
                monthgb.setTextColor(Color.parseColor("#666666"));
                monthgb.setBackgroundColor(Color.parseColor("#edf4fb"));
                todaygb.setTextColor(Color.parseColor("#FF8c00"));
                todaygb.setBackgroundColor(Color.parseColor("#edf4fb"));
                todaygb.setBackground(bottomBorder);
                weekgb.setBackgroundColor(Color.parseColor("#d5dbe1"));
                monthgb.setBackgroundColor(Color.parseColor("#d5dbe1"));
                displayTodaydata();
            }
        });

        weekgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todaygb.setTextColor(Color.parseColor("#666666"));
                todaygb.setBackgroundColor(Color.parseColor("#edf4fb"));
                monthgb.setTextColor(Color.parseColor("#666666"));
                monthgb.setBackgroundColor(Color.parseColor("#edf4fb"));
                weekgb.setTextColor(Color.parseColor("#FF8c00"));
                weekgb.setBackgroundColor(Color.parseColor("#edf4fb"));
                weekgb.setBackground(bottomBorder);
                todaygb.setBackgroundColor(Color.parseColor("#d5dbe1"));
                monthgb.setBackgroundColor(Color.parseColor("#d5dbe1"));
                displayweekdata();
            }
        });

        monthgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weekgb.setTextColor(Color.parseColor("#666666"));
                weekgb.setBackgroundColor(Color.parseColor("#edf4fb"));
                todaygb.setTextColor(Color.parseColor("#666666"));
                todaygb.setBackgroundColor(Color.parseColor("#edf4fb"));
                monthgb.setTextColor(Color.parseColor("#FF8c00"));
                monthgb.setBackgroundColor(Color.parseColor("#edf4fb"));
                monthgb.setBackground(bottomBorder);
                weekgb.setBackgroundColor(Color.parseColor("#d5dbe1"));
                todaygb.setBackgroundColor(Color.parseColor("#d5dbe1"));
                displaymonthdata();
            }
        });

        displayTodaydata();
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
        gv.setVisibility(View.VISIBLE);
        c.setVisibility(View.INVISIBLE);
        Calendar cal = Calendar.getInstance();
        String m=String.valueOf(cal.get(Calendar.MONTH)+1);



        staticLabelsFormatter.setHorizontalLabels(new String[] {"1", "2", "3","4","5","6","7","8","9","10","11","12","13","14","15","16"
            ,"17","18","19","20","21","22","23","24","25","26","27","28"});

        gv.getViewport().setMaxX(15);
        gv.getViewport().setMinX(1);
        gv.getViewport().setMaxY(12);
        gv.getViewport().setMinY(0);
        gv.getViewport().setXAxisBoundsManual(false);

        gv.getViewport().setScrollable(true);
        gv.getViewport().setScrollableY(true);


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

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });
        gv.getGridLabelRenderer().setTextSize(28);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.parseColor("#FF8C00"));
        gv.addSeries(series);
        series.setDataWidth(0.5);

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


    void displayTodaydata() {

        c.setVisibility(View.VISIBLE);
        gv.setVisibility(View.INVISIBLE);
        SharedPreferences s=getActivity().getSharedPreferences("unlock",MODE_PRIVATE);
        t.setText(String.valueOf(s.getInt("count",0)));
    }
    void displayweekdata()
    {

        gv.removeAllSeries();
        gv.setVisibility(View.VISIBLE);
        c.setVisibility(View.INVISIBLE);
        gv.getViewport().setMaxX(7);
        gv.getViewport().setMinX(0);
        gv.getViewport().setMaxY(12);
        gv.getViewport().setMinY(0);
        gv.getViewport().setXAxisBoundsManual(true);

        gv.getViewport().setScrollable(true);
        gv.getViewport().setScrollableY(true);
        //StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(gv);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Sun", "Mon", "Tue","Wed","Thu","Fri","Sat","Sun"});

        gv.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
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
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        gv.getGridLabelRenderer().setTextSize(45);
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.parseColor("#FF8C00"));
        gv.addSeries(series);

        series.setDataWidth(0.5);

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

class LockScreenStateReceiver extends BroadcastReceiver {
    private LockScreenStateReceiver mLockScreenStateReceiver;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // screen is turn off
            //("Screen locked");
        } else {
            //Handle resuming events if user is present/screen is unlocked
            SharedPreferences ret=context.getSharedPreferences("unlock", MODE_PRIVATE);
            int count=ret.getInt("count",0);
            count++;
            SharedPreferences.Editor editor = context.getSharedPreferences("unlock", MODE_PRIVATE).edit();
            editor.putInt("count",count);
editor.apply();
editor.commit();

        }
    }
}