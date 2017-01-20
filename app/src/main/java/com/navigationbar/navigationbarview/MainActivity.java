package com.navigationbar.navigationbarview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.navigationbar.navigationbarview.view.DataBean;
import com.navigationbar.navigationbarview.view.MyAdapter;
import com.navigationbar.navigationbarview.view.NavigationBar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String[] characters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N"
            , "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "|"};

    private List<String> dataList = new ArrayList<>();
    private ListView listView;
    private MyAdapter myAdapter;
    private String[] dataArray;
    private CustomSectionIndexer customSectionIndexer;
    private NavigationBar navigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        navigationBar = (NavigationBar) findViewById(R.id.nv);
        final TextView tvChar = (TextView) findViewById(R.id.tv_char);
        initData();
        customSectionIndexer = new CustomSectionIndexer(dataArray, characters);
        listView = (ListView) findViewById(R.id.lv);
        myAdapter = new MyAdapter(this, dataList);
        myAdapter.setSectionIndexer(customSectionIndexer);
        listView.setAdapter(myAdapter);
        navigationBar.setOnGetCharacterCallBack(new NavigationBar.OnGetCharacterCallBack() {
            @Override
            public void onGetCharacter(String character, final int position) {
                tvChar.setText(character);
                int positionForSection = customSectionIndexer.getPositionForSection(position);
//                listView.smoothScrollToPosition(positionForSection);
                listView.smoothScrollToPositionFromTop(positionForSection, 0, 2000);
           /*     listView.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.smoothScrollToPosition(position);

                    }
                });*/
            }
        });
        navigationBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    tvChar.setVisibility(View.VISIBLE);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    tvChar.setVisibility(View.GONE);
                }
                return false;   //just get touch status;
            }
        });
    }

    private void initData() {
        int i=0;
        while(i < 100) {
            dataList.add(characters[i % characters.length] + i);
            i++;
        }
        Collections.sort(dataList, comparator);

        dataArray = new String[dataList.size()];
        for (int j=0; j< dataList.size(); j++) {
            String s = dataList.get(j);
            dataArray[j] = s.substring(0, 1);
        }
       /* dataArray = dataList.toArray(new String[dataList.size()]);*/
    }

    Comparator<String> comparator = new Comparator<String>() {
        @Override
        public int compare(String s, String t1) {
            String sFirstChar;
            String t1FirstChar;
            if (s.length() == 0) {
                sFirstChar = " ";
            } else {
                sFirstChar = s.substring(0, 1);
            }
            if (t1.length() == 0) {
                t1FirstChar = " ";
            } else {
                t1FirstChar = t1.substring(0, 1);
            }
            Log.i("chen","s: " + s + "  t1: "+ t1 + "--->sFirstChar: " + sFirstChar + "  t1FirstChar: "+ t1FirstChar);


            return sFirstChar.compareTo(t1FirstChar);
        }
    };
}
