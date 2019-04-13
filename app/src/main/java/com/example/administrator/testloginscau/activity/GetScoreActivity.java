package com.example.administrator.testloginscau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.administrator.testloginscau.R;
import com.example.administrator.testloginscau.api.Api;
import com.example.administrator.testloginscau.Interface.ApiService;
import com.example.administrator.testloginscau.myview.TimeTableView;
import com.example.administrator.testloginscau.util.Utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetScoreActivity extends AppCompatActivity {

    private static final String TAG = "GetScoreActivity";

    private Button commit;
    private WebView scoreText;
    private TimeTableView timeTableView;
    private ApiService apiService = Api.getApi();
    private String userCredit;
    private String sessionId = "null";
    private String name;
    private String btn = null;
    private String baseUrl = "http://zfsoft.jlmu.cn/(zhux2wbi2vm5iv45g3v2x4bc)/";
    private String generator = "9727EB43";
    private String viewState = null;
    private String viewState2 = null;
    private String year;
    private String term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        initYearChoose();
        initTermChoose();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        userCredit = intent.getStringExtra("credit");
        sessionId = intent.getStringExtra("sessionId");

        try {
            name = URLEncoder.encode(name, "gb2312");
            btn = URLEncoder.encode("历年成绩","gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        commit = (Button) findViewById(R.id.commit);
        scoreText = (WebView) findViewById(R.id.webView);

        // timeTableView = (TimeTableView) findViewById(R.id.timetable_view);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewState();
            }
        });
    }
    public void initYearChoose(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String date = simpleDateFormat.format(new java.util.Date());
        //初始化学年选择的下拉spinner
        List<String> years = new ArrayList<>();
        int year_tmp = Integer.parseInt(date) - 5;
        for(int i = 0;i<11;i++,year_tmp++){
            years.add(year_tmp+"-"+(year_tmp+1));
        }
        final Spinner yearsSpinner = (Spinner)findViewById(R.id.spinnerfirst);
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsSpinner.setAdapter(adapter);
        yearsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                year = adapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void initTermChoose(){
        //初始化学期选择的spinner
        List<String> terms = new ArrayList<>();
        terms.add("1");
        terms.add("2");
        terms.add("3");
        Spinner termsSpinner = (Spinner) findViewById(R.id.spinnersecond);
        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,terms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        termsSpinner.setAdapter(adapter);
        termsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                term = adapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void getViewState(){
        apiService.toSearchScore(
                sessionId,
                "zfsoft.jlmu.cn",//这个不能加http!!!!!222.162.163.4
                //baseUrl + "xs_main.aspx?xh=" + userCredit,
                baseUrl + "xs_main.aspx?xh=" + userCredit,
                userCredit,
                name,
                "N121603"
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //scoreText.setText(response.body().string() + " ");
                    String info = response.body().string();


                    List<String> list = Utility.parseToSearchScore(info);
                    viewState = list.get(0);
                   // viewState2 = list.get(1);

                    getCourse();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void getCourse(){

        String referer = null;
        String text = null;

        //获取课表
        apiService.getCourse(
                sessionId,
                "zfsoft.jlmu.cn",//这个不能加http!!!!!
                baseUrl + "xskbcx.aspx?xh=" + userCredit + "&xm=" + name + "&gnmkdm=N121603",
                userCredit,
                name,
                "N121603",
                "",
                "",
                viewState,
                //viewState2,
                year,
                term
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //scoreText.setText(response.body().string());
                    // List<Course> courseList = Utility.parseTimeTableHtml(response.body().string());
                    //timeTableView.setCourses(courseList);
                    String html = Utility.parseTimeTableHtml1(response.body().string());
                   // Log.d(TAG, "onResponse: " + html);
                    scoreText.getSettings().setJavaScriptEnabled(true);
                    scoreText.setWebViewClient(new WebViewClient());
                    scoreText.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });

    }

    //可以用来获取成绩
    /*public void getScore(){
        String referer = null;
        String text = null;

        apiService.getScore(
                sessionId,
                "202.116.160.166",//这个不能加http!!!!!
                baseUrl + "xs_main.aspx?xh=" + userCredit,
                userCredit,
                name,
                "N121605",
                viewState,
                generator,
                "2015-2016",
                "1",
                btn
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    if (!response.isSuccess()){
                        Log.d(TAG, "onResponse: unsuccess");

                        scoreText.setText("unsuccess");
                    }else{
                        byte[] b = response.body().bytes();     //获取数据的bytes
                        String info = new String(b, "GB2312");

                        List<Score> scoreList = Utility.parseScoreHtml(info);

                        for (int i = 0; i < scoreList.size(); i++){
                            scoreText.append(scoreList.get(i) + " ");
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }*/

}
