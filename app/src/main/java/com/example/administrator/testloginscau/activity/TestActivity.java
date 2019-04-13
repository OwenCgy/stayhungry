package com.example.administrator.testloginscau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.testloginscau.R;
import com.example.administrator.testloginscau.api.Api;
import com.example.administrator.testloginscau.Interface.ApiService;
import com.example.administrator.testloginscau.myview.TimeTableView;
import com.example.administrator.testloginscau.util.Utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TextActivity";

    private Button commit;
    private TextView scoreText;
    private TimeTableView timeTableView;
    private ApiService apiService = Api.getApi();
    private String userCredit;
    private String sessionId = "null";
    private String name;
    private String btn = null;
    private String baseUrl = "http://202.116.160.166/";
    private String generator = "9727EB43";
    private String viewState = null;
    private String viewState2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



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

        // timeTableView = (TimeTableView) findViewById(R.id.timetable_view);

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getViewState();
            }
        });
    }

    public void getViewState(){
        apiService.toSearchScore(
                sessionId,
                "202.116.160.166",//这个不能加http!!!!!
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
                    //viewState2 = list.get(1);

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
                "202.116.160.166",//这个不能加http!!!!!
                baseUrl + "xskbcx.aspx?xh=" + userCredit + "&xm=" + name + "&gnmkdm=N121603",
                userCredit,
                name,
                "N121603",
                "xqd",
                "",
                viewState,
                "2016-2017",
                "2"
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //scoreText.setText(response.body().string());
                    // List<Course> courseList = Utility.parseTimeTableHtml(response.body().string());
                    //timeTableView.setCourses(courseList);
                    String html = Utility.parseTimeTableHtml1(response.body().string());
                    Log.d(TAG, "onResponse: " + html);
                    scoreText.setText(html);
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

