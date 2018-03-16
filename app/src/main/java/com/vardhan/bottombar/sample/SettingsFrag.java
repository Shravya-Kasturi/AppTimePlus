package com.vardhan.bottombar.sample;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class SettingsFrag extends Fragment {
    Toolbar tb;
    AppListAdapter appListAdapter;
    ArrayList<EachAppItem> arrayList;
    ArrayList<Integer> piclogo;
    ArrayList<String> br;
    ListView listView;
    ArrayList<String> autofrnds;
    static String qw;
    int l;
    DatabaseReference databaseReference;
    ArrayList<ImageView> imk;
    FirebaseStorage storage;
    ArrayList<String> ka;
    StorageReference storageReference;
    TextView chooseapp;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.actionmenu, menu);
        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.actionbar, null);
        tb.addView(v);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, autofrnds);
        AutoCompleteTextView textView = (AutoCompleteTextView) v
                .findViewById(R.id.editText1);
        textView.setAdapter(adapter);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {

            final String s=((TextView)getActivity().findViewById(R.id.editText1)).getText().toString();

            databaseReference= FirebaseDatabase.getInstance().getReference("users");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    databaseReference.removeEventListener(this);
                    for(DataSnapshot d:dataSnapshot.getChildren()){
                        if(d.child("username").getValue(String.class).equals(s)){
                            databaseReference.child(d.getKey()).child("requests").child(FirebaseAuth.getInstance().getUid()).setValue(FirebaseAuth.getInstance().getUid());
                            ((TextView)getActivity().findViewById(R.id.editText1)).setText("");
                            Toast.makeText(getActivity(),"Sent Successfully!!!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else if (id == R.id.notify) {
            startActivity(new Intent(getActivity(), Friends.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.settingsxml, container, false);
        arrayList = new ArrayList<EachAppItem>();
        autofrnds=new ArrayList<String>();
        ka=new ArrayList<String>();
        imk=new ArrayList<ImageView>();
        chooseapp=(TextView)view.findViewById(R.id.chooseapp);
        br=new ArrayList<String>();
        piclogo=new ArrayList<Integer>();
        piclogo.add(R.mipmap.fbbadge);
        piclogo.add(R.mipmap.whatsappbadge);
        piclogo.add(R.mipmap.instabadge);
        piclogo.add(R.mipmap.twitterbadge);
        piclogo.add(R.mipmap.chromebadge);
        piclogo.add(R.mipmap.hikebadge);
        piclogo.add(R.mipmap.utubebadge);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseReference.removeEventListener(this);
                for(DataSnapshot d: dataSnapshot.getChildren())
                {
                    autofrnds.add(d.child("username").getValue(String.class));
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setHasOptionsMenu(true);
        tb = (Toolbar) view.findViewById(R.id.actiontool);
        listView = (ListView) view.findViewById(R.id.fr);
        tb.setTitle("Friends");
        tb.setLogo(R.mipmap.friends);
        ((AppCompatActivity) getActivity()).setSupportActionBar(tb);
        ImageView fbi=(ImageView)view.findViewById(R.id.fbi);
        ImageView whatsappi=(ImageView)view.findViewById(R.id.whatsappi);
        ImageView instai=(ImageView)view.findViewById(R.id.instai);
        ImageView hikei=(ImageView)view.findViewById(R.id.hikei);
        ImageView chromei=(ImageView)view.findViewById(R.id.chromei);
        ImageView twitteri=(ImageView)view.findViewById(R.id.twitteri);
        ImageView youtubei=(ImageView)view.findViewById(R.id.youtubei);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (qw){
                    case "fbt":
                        Toast.makeText(getActivity(),"Fb",Toast.LENGTH_SHORT).show();
                        break;

                    case "whatsappt":
                        Toast.makeText(getActivity(),"Whatsapp",Toast.LENGTH_SHORT).show();
                        break;

                    case "hiket":
                        Toast.makeText(getActivity(),"Hike",Toast.LENGTH_SHORT).show();
                        break;

                    case "twittert":
                        Toast.makeText(getActivity(),"Twitter",Toast.LENGTH_SHORT).show();

                        break;

                    case "instat":
                        Toast.makeText(getActivity(),"Insta",Toast.LENGTH_SHORT).show();

                        break;

                    case "utubet":
                        Toast.makeText(getActivity(),"Utube",Toast.LENGTH_SHORT).show();

                        break;



                    case "chromet":
                        Toast.makeText(getActivity(),"Chrome",Toast.LENGTH_SHORT).show();

                        break;

                }
            }
        });

        fbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qw=v.getTag().toString();
                chooseapp.setText("Facebook");
                databaseReference=FirebaseDatabase.getInstance().getReference("users");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        databaseReference.removeEventListener(this);
                        ArrayList<String> fr=new ArrayList<String>();
                        ArrayList<String> ur=new ArrayList<String>();
                        for(DataSnapshot d:dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("friends").getChildren())
                        {
                            fr.add(d.getValue(String.class));
                        }
                        ArrayList<String> tr=new ArrayList<String>();
                        br.clear();
                        for(String s:fr){
                            ur.add(dataSnapshot.child(s).child("username").getValue(String.class));
                            tr.add(dataSnapshot.child(s).child("times").child("0").getValue(String.class));
                            br.add(dataSnapshot.child(s).child("badges").child("0").getValue(String.class));
                        }
                        arrayList.clear();
                        for(int z=0;z<tr.size();z++){
                            EachAppItem k = new EachAppItem(R.mipmap.aboy, ur.get(z), tr.get(z),br.get(z));
                            arrayList.add(k);
                        }
                        if (arrayList.size() > 0) {
                            appListAdapter = new AppListAdapter(getActivity(), R.layout.app_list_item, arrayList,fr,piclogo.get(0));
                            listView.setAdapter(appListAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        whatsappi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qw=v.getTag().toString();
                chooseapp.setText("Whatsapp");
                databaseReference=FirebaseDatabase.getInstance().getReference("users");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        databaseReference.removeEventListener(this);
                        ArrayList<String> fr=new ArrayList<String>();
                        ArrayList<String> ur=new ArrayList<String>();
                        for(DataSnapshot d:dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("friends").getChildren())
                        {
                            fr.add(d.getValue(String.class));
                        }
                        ArrayList<String> tr=new ArrayList<String>();
                        br.clear();
                        for(String s:fr){
                            ur.add(dataSnapshot.child(s).child("username").getValue(String.class));
                            tr.add(dataSnapshot.child(s).child("times").child("1").getValue(String.class));
                            br.add(dataSnapshot.child(s).child("badges").child("1").getValue(String.class));
                        }
                        arrayList.clear();
                        for(int z=0;z<tr.size();z++){
                            EachAppItem k = new EachAppItem(R.mipmap.aboy, ur.get(z), tr.get(z),br.get(z));
                            arrayList.add(k);
                        }
                        if (arrayList.size() > 0) {
                            appListAdapter = new AppListAdapter(getActivity(), R.layout.app_list_item, arrayList,fr,piclogo.get(1));
                            listView.setAdapter(appListAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        instai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qw=v.getTag().toString();
                chooseapp.setText("Instagram");
                databaseReference=FirebaseDatabase.getInstance().getReference("users");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        databaseReference.removeEventListener(this);
                        ArrayList<String> fr=new ArrayList<String>();
                        ArrayList<String> ur=new ArrayList<String>();
                        for(DataSnapshot d:dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("friends").getChildren())
                        {
                            fr.add(d.getValue(String.class));
                        }
                        ArrayList<String> tr=new ArrayList<String>();
                        br.clear();
                        for(String s:fr){
                            ur.add(dataSnapshot.child(s).child("username").getValue(String.class));
                            tr.add(dataSnapshot.child(s).child("times").child("2").getValue(String.class));
                            br.add(dataSnapshot.child(s).child("badges").child("2").getValue(String.class));
                        }
                        arrayList.clear();
                        for(int z=0;z<tr.size();z++){
                            EachAppItem k = new EachAppItem(R.mipmap.aboy, ur.get(z), tr.get(z),br.get(z));
                            arrayList.add(k);
                        }
                        if (arrayList.size() > 0) {
                            appListAdapter = new AppListAdapter(getActivity(), R.layout.app_list_item, arrayList,fr,piclogo.get(2));
                            listView.setAdapter(appListAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        hikei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qw=v.getTag().toString();
                chooseapp.setText("Hike");
                databaseReference=FirebaseDatabase.getInstance().getReference("users");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        databaseReference.removeEventListener(this);
                        ArrayList<String> fr=new ArrayList<String>();
                        ArrayList<String> ur=new ArrayList<String>();
                        for(DataSnapshot d:dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("friends").getChildren())
                        {
                            fr.add(d.getValue(String.class));
                        }
                        ArrayList<String> tr=new ArrayList<String>();
                        br.clear();
                        for(String s:fr){
                            ur.add(dataSnapshot.child(s).child("username").getValue(String.class));
                            tr.add(dataSnapshot.child(s).child("times").child("5").getValue(String.class));
                            br.add(dataSnapshot.child(s).child("badges").child("5").getValue(String.class));
                        }
                        arrayList.clear();
                        for(int z=0;z<tr.size();z++){
                            EachAppItem k = new EachAppItem(R.mipmap.aboy, ur.get(z), tr.get(z),br.get(z));
                            arrayList.add(k);
                        }
                        if (arrayList.size() > 0) {
                            appListAdapter = new AppListAdapter(getActivity(), R.layout.app_list_item, arrayList,fr,piclogo.get(5));
                            listView.setAdapter(appListAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });



        chromei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qw=v.getTag().toString();
                chooseapp.setText("Chrome");
                databaseReference=FirebaseDatabase.getInstance().getReference("users");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        databaseReference.removeEventListener(this);
                        ArrayList<String> fr = new ArrayList<String>();
                        ArrayList<String> ur = new ArrayList<String>();
                        for (DataSnapshot d : dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("friends").getChildren()) {
                            fr.add(d.getValue(String.class));
                        }
                        ArrayList<String> tr = new ArrayList<String>();
                        br.clear();
                        for(String s:fr){
                            ur.add(dataSnapshot.child(s).child("username").getValue(String.class));
                            tr.add(dataSnapshot.child(s).child("times").child("4").getValue(String.class));
                            br.add(dataSnapshot.child(s).child("badges").child("4").getValue(String.class));
                        }
                        arrayList.clear();


                        for(int z=0;z<tr.size();z++){
                            EachAppItem k = new EachAppItem(R.mipmap.aboy, ur.get(z), tr.get(z),br.get(z));
                            arrayList.add(k);
                        }
                        if (arrayList.size() > 0) {
                            appListAdapter = new AppListAdapter(getActivity(), R.layout.app_list_item, arrayList,fr,piclogo.get(4));
                            listView.setAdapter(appListAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        twitteri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qw=v.getTag().toString();
                chooseapp.setText("Twitter");
                databaseReference=FirebaseDatabase.getInstance().getReference("users");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        databaseReference.removeEventListener(this);
                        ArrayList<String> fr=new ArrayList<String>();
                        ArrayList<String> ur=new ArrayList<String>();
                        for(DataSnapshot d:dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("friends").getChildren())
                        {
                            fr.add(d.getValue(String.class));
                        }
                        ArrayList<String> tr=new ArrayList<String>();
                        br.clear();
                        for(String s:fr){
                            ur.add(dataSnapshot.child(s).child("username").getValue(String.class));
                            tr.add(dataSnapshot.child(s).child("times").child("3").getValue(String.class));
                            br.add(dataSnapshot.child(s).child("badges").child("3").getValue(String.class));
                        }
                        arrayList.clear();
                        for(int z=0;z<tr.size();z++){
                            EachAppItem k = new EachAppItem(R.mipmap.aboy, ur.get(z), tr.get(z),br.get(z));
                            arrayList.add(k);
                        }
                        if (arrayList.size() > 0) {
                            appListAdapter = new AppListAdapter(getActivity(), R.layout.app_list_item, arrayList,fr,piclogo.get(3));
                            listView.setAdapter(appListAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        youtubei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qw=v.getTag().toString();
                chooseapp.setText("You Tube");
                databaseReference=FirebaseDatabase.getInstance().getReference("users");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        databaseReference.removeEventListener(this);
                        ArrayList<String> fr=new ArrayList<String>();
                        ArrayList<String> ur=new ArrayList<String>();
                        for(DataSnapshot d:dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("friends").getChildren())
                        {
                            fr.add(d.getValue(String.class));
                        }
                        ArrayList<String> tr=new ArrayList<String>();
                        br.clear();
                        for(String s:fr){
                            ur.add(dataSnapshot.child(s).child("username").getValue(String.class));
                            tr.add(dataSnapshot.child(s).child("times").child("6").getValue(String.class));
                            br.add(dataSnapshot.child(s).child("badges").child("6").getValue(String.class));
                        }
                        arrayList.clear();
                        for(int z=0;z<tr.size();z++){
                            EachAppItem k = new EachAppItem(R.mipmap.aboy, ur.get(z), tr.get(z),br.get(z));
                            arrayList.add(k);
                        }

                        if (arrayList.size() > 0) {
                            appListAdapter = new AppListAdapter(getActivity(), R.layout.app_list_item, arrayList,fr,piclogo.get(6));
                            listView.setAdapter(appListAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        return view;
    }


}