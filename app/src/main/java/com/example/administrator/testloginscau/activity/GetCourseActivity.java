package com.example.administrator.testloginscau.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.administrator.testloginscau.R;
import com.example.administrator.testloginscau.api.Api;
import com.example.administrator.testloginscau.Interface.ApiService;
import com.example.administrator.testloginscau.bean.Score;
import com.example.administrator.testloginscau.myview.TimeTableView;
import com.example.administrator.testloginscau.util.Utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetCourseActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "GetCourseActivity";

    private Button btn_term;
    private Button btn_all;
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
        setContentView(R.layout.activity_get_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initYearChoose();
        //initTermChoose();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        userCredit = intent.getStringExtra("credit");
        sessionId = intent.getStringExtra("sessionId");

        try {
            name = URLEncoder.encode(name, "gb2312");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        btn_term = (Button) findViewById(R.id.button_term);
        btn_all = (Button) findViewById(R.id.button_all);
        btn_term.setOnClickListener(this);
        btn_all.setOnClickListener(this);

        // timeTableView = (TimeTableView) findViewById(R.id.timetable_view);


    }

    /*public void initYearChoose(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String date = simpleDateFormat.format(new java.util.Date());
        //初始化学年选择的下拉spinner
        List<String> years = new ArrayList<>();
        int year_tmp = Integer.parseInt(date) - 5;
        for(int i = 0;i<11;i++,year_tmp++){
            years.add(year_tmp+"-"+(year_tmp+1));
        }
        final Spinner yearsSpinner = (Spinner)findViewById(R.id.spinner_xn);
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
    }*/

   /* public void initTermChoose(){
        //初始化学期选择的spinner
        List<String> terms = new ArrayList<>();
        terms.add("1");
        terms.add("2");
        terms.add("3");
        Spinner termsSpinner = (Spinner) findViewById(R.id.spinner_xq);
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
    }*/

    public void getViewState(){
        apiService.toSearchCourse(
                sessionId,
                "zfsoft.jlmu.cn",//这个不能加http!!!!!
                //baseUrl + "xs_main.aspx?xh=" + userCredit,
                baseUrl + "xs_main.aspx?xh=" + userCredit+ "&xm=" + name + "&gnmkdm=N121605",
                userCredit,
                name,
                "N121605"
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    //scoreText.setText(response.body().string() + " ");
                    String info = response.body().string();


                    List<String> list = Utility.parseToSearchScore(info);
                    viewState = list.get(0);
                    Log.d("Utility", viewState);
                    // viewState2 = list.get(1);

                   // getScore();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    public void getScore(){
        String referer = null;
        String text = null;

        apiService.getScore(
                sessionId,
                "zfsoft.jlmu.cn",//这个不能加http!!!!!
                baseUrl + "xscjcx.aspx?xh=" + userCredit + "&xm=" + name + "&gnmkdm=N121605",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36",
                userCredit,
                name,
                "N121605",
                "",
                "",
                viewState,
                "",
                "",
                "",
                "",
                btn
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    byte[] b = response.body().bytes();//获取数据的bytes
                    String info = new String(b, "gb2312");
                    Log.d("Utility", info);
                    List<Map<String,String>> courses = Utility.parseScoreHtml(info);
                    ListView scoreListView = (ListView)findViewById(R.id.listView2);
                    SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),courses,R.layout.scorelist,new String[]{"name","credits","score","tag"},new int[]{R.id.textView71,R.id.textView72,R.id.textView73,R.id.textView74});
                    scoreListView.setAdapter(adapter);



                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Utility", t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_term:

                //viewState = "dDwxMzY4NjY5NjM7dDxwPGw8U29ydEV4cHJlcztzZmRjYms7ZGczO2R5YnlzY2o7U29ydERpcmU7eGg7c3RyX3RhYl9iamc7Y2pjeF9sc2I7enhjamN4eHM7PjtsPGtjbWM7XGU7YmpnO1xlO2FzYzsxNTE1NDEwNTAzMTt6Zl9jeGNqdGpfMTUxNTQxMDUwMzE7OzA7Pj47bDxpPDE+Oz47bDx0PDtsPGk8ND47aTwxMD47aTwxOT47aTwyND47aTwzMj47aTwzND47aTwzNj47aTwzOD47aTw0MD47aTw0Mj47aTw0ND47aTw0Nj47aTw0OD47aTw1Mj47aTw1ND47aTw1Nj47PjtsPHQ8dDxwPHA8bDxEYXRhVGV4dEZpZWxkO0RhdGFWYWx1ZUZpZWxkOz47bDxYTjtYTjs+Pjs+O3Q8aTw1PjtAPFxlOzIwMTgtMjAxOTsyMDE3LTIwMTg7MjAxNi0yMDE3OzIwMTUtMjAxNjs+O0A8XGU7MjAxOC0yMDE5OzIwMTctMjAxODsyMDE2LTIwMTc7MjAxNS0yMDE2Oz4+Oz47Oz47dDx0PHA8cDxsPERhdGFUZXh0RmllbGQ7RGF0YVZhbHVlRmllbGQ7PjtsPGtjeHptYztrY3h6ZG07Pj47Pjt0PGk8Nj47QDzlv4Xkv67or7476YCJ5L+u6K++O+W7uuiurumAieS/ruivvjvpnZ7pmZDlrprpgInkv67or7475Lu75oSP6YCJ5L+u6K++O1xlOz47QDwwMTswMjswMzswNDswNTtcZTs+Pjs+Ozs+O3Q8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+Pjs+Ozs+O3Q8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+Pjs+Ozs+O3Q8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w8XGU7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7VmlzaWJsZTs+O2w85a2m5Y+377yaMTUxNTQxMDUwMzE7bzx0Pjs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDtWaXNpYmxlOz47bDzlp5PlkI3vvJrpmYjlhqDlroc7bzx0Pjs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDtWaXNpYmxlOz47bDzlrabpmaLvvJrnrqHnkIblrabpmaI7bzx0Pjs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDtWaXNpYmxlOz47bDzkuJPkuJrvvJo7bzx0Pjs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDtWaXNpYmxlOz47bDzkv6Hmga/nrqHnkIbkuI7kv6Hmga/ns7vnu587bzx0Pjs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDs+O2w85LiT5Lia5pa55ZCROjs+Pjs+Ozs+O3Q8cDxwPGw8VGV4dDtWaXNpYmxlOz47bDzooYzmlL/nj63vvJoyMDE157qn5L+h5oGv566h55CG5LiO5L+h5oGv57O757uf5pys56eR54+tO288dD47Pj47Pjs7Pjt0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8O2w8aTwxPjtpPDM+O2k8NT47aTw3PjtpPDk+O2k8MTM+O2k8MTU+O2k8MTc+O2k8MjE+O2k8MjM+O2k8MjQ+O2k8MjU+O2k8Mjc+O2k8Mjk+O2k8MzE+O2k8MzM+O2k8MzU+O2k8NDM+O2k8NDk+O2k8NTE+O2k8NTI+Oz47bDx0PHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Pjt0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47cDxsPHN0eWxlOz47bDxESVNQTEFZOm5vbmU7Pj4+Ozs7Ozs7Ozs7Oz47Oz47dDw7bDxpPDEzPjs+O2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs+Pjt0PHA8cDxsPFRleHQ7VmlzaWJsZTs+O2w86Iez5LuK5pyq6YCa6L+H6K++56iL5oiQ57up77yaO288dD47Pj47Pjs7Pjt0PEAwPHA8cDxsPFBhZ2VDb3VudDtfIUl0ZW1Db3VudDtfIURhdGFTb3VyY2VJdGVtQ291bnQ7RGF0YUtleXM7PjtsPGk8MT47aTwwPjtpPDA+O2w8Pjs+PjtwPGw8c3R5bGU7PjtsPERJU1BMQVk6YmxvY2s7Pj4+Ozs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+O3A8bDxzdHlsZTs+O2w8RElTUExBWTpub25lOz4+Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8Ozs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+O3A8bDxzdHlsZTs+O2w8RElTUExBWTpub25lOz4+Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8Ozs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+O3A8bDxzdHlsZTs+O2w8RElTUExBWTpub25lOz4+Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+PjtwPGw8c3R5bGU7PjtsPERJU1BMQVk6bm9uZTs+Pj47Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+PjtwPGw8c3R5bGU7PjtsPERJU1BMQVk6bm9uZTs+Pj47Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47cDxsPHN0eWxlOz47bDxESVNQTEFZOm5vbmU7Pj4+Ozs7Ozs7Ozs7Oz47Oz47dDxAMDw7QDA8OztAMDxwPGw8SGVhZGVyVGV4dDs+O2w85Yib5paw5YaF5a65Oz4+Ozs7Oz47QDA8cDxsPEhlYWRlclRleHQ7PjtsPOWIm+aWsOWtpuWIhjs+Pjs7Ozs+O0AwPHA8bDxIZWFkZXJUZXh0Oz47bDzliJvmlrDmrKHmlbA7Pj47Ozs7Pjs7Oz47Ozs7Ozs7Ozs+Ozs+O3Q8cDxwPGw8VGV4dDtWaXNpYmxlOz47bDzmnKzkuJPkuJrlhbE0NeS6ujtvPGY+Oz4+Oz47Oz47dDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+Oz47Oz47dDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+Oz47Oz47dDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+Oz47Oz47dDxwPHA8bDxUZXh0Oz47bDx4Yzs+Pjs+Ozs+O3Q8cDxwPGw8SW1hZ2VVcmw7PjtsPC4vZXhjZWwvMTUxNTQxMDUwMzEuanBnOz4+Oz47Oz47Pj47dDw7bDxpPDM+Oz47bDx0PEAwPDs7Ozs7Ozs7Ozs+Ozs+Oz4+Oz4+Oz4+Oz6gdIrW1egh3Z85HVgdM2hMIrObQQ==";
                ListView scoreListView = (ListView)findViewById(R.id.listView2);
                //SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),null,R.layout.scorelist,new String[]{"name","credits","score","tag"},new int[]{R.id.textView71,R.id.textView72,R.id.textView73,R.id.textView74});
                scoreListView.setAdapter(null);
                break;
            case R.id.button_all:
                getViewState();
                //viewState = "dDwxMzY4NjY5NjM7dDxwPGw8U29ydEV4cHJlcztzZmRjYms7ZGczO2R5YnlzY2o7U29ydERpcmU7eGg7c3RyX3RhYl9iamc7Y2pjeF9sc2I7enhjamN4eHM7PjtsPGtjbWM7XGU7YmpnO1xlO2FzYzsxNTA3NDEwNTM0Njt6Zl9jeGNqdGpfMTUwNzQxMDUzNDY7OzA7Pj47bDxpPDE+Oz47bDx0PDtsPGk8ND47aTwxMD47aTwxOT47aTwyND47aTwzMj47aTwzND47aTwzNj47aTwzOD47aTw0MD47aTw0Mj47aTw0ND47aTw0Nj47aTw0OD47aTw1Mj47aTw1ND47aTw1Nj47PjtsPHQ8dDxwPHA8bDxEYXRhVGV4dEZpZWxkO0RhdGFWYWx1ZUZpZWxkOz47bDxYTjtYTjs+Pjs+O3Q8aTw0PjtAPFxlOzIwMTctMjAxODsyMDE2LTIwMTc7MjAxNS0yMDE2Oz47QDxcZTsyMDE3LTIwMTg7MjAxNi0yMDE3OzIwMTUtMjAxNjs+Pjs+Ozs+O3Q8dDxwPHA8bDxEYXRhVGV4dEZpZWxkO0RhdGFWYWx1ZUZpZWxkOz47bDxrY3h6bWM7a2N4emRtOz4+Oz47dDxpPDY+O0A85b+F5L+u6K++O+mAieS/ruivvjvlu7rorq7pgInkv67or7476Z2e6ZmQ5a6a6YCJ5L+u6K++O+S7u+aEj+mAieS/ruivvjtcZTs+O0A8MDE7MDI7MDM7MDQ7MDU7XGU7Pj47Pjs7Pjt0PHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Pjt0PHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Pjt0PHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPFxlOz4+Oz47Oz47dDxwPHA8bDxUZXh0O1Zpc2libGU7PjtsPOWtpuWPt++8mjE1MDc0MTA1MzQ2O288dD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7VmlzaWJsZTs+O2w85aeT5ZCN77ya5p2o5pm25YWDO288dD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7VmlzaWJsZTs+O2w85a2m6Zmi77ya5oqk55CG5a2m6ZmiO288dD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7VmlzaWJsZTs+O2w85LiT5Lia77yaO288dD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7VmlzaWJsZTs+O2w85oqk55CG5a2mO288dD47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOS4k+S4muaWueWQkTo7Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7VmlzaWJsZTs+O2w86KGM5pS/54+t77yaMjAxNee6p+aKpOeQhuacrOenkeePre+8iOiIn+Wxse+8iTtvPHQ+Oz4+Oz47Oz47dDxAMDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+Oz47Ozs7Ozs7Ozs7Pjs7Pjt0PDtsPGk8MT47aTwzPjtpPDU+O2k8Nz47aTw5PjtpPDEzPjtpPDE1PjtpPDE3PjtpPDIxPjtpPDIzPjtpPDI0PjtpPDI1PjtpPDI3PjtpPDI5PjtpPDMxPjtpPDMzPjtpPDM1PjtpPDQzPjtpPDQ5PjtpPDUxPjtpPDUyPjs+O2w8dDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+Oz47Oz47dDxAMDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+O3A8bDxzdHlsZTs+O2w8RElTUExBWTpub25lOz4+Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8O2w8aTwxMz47PjtsPHQ8QDA8Ozs7Ozs7Ozs7Oz47Oz47Pj47dDxwPHA8bDxUZXh0O1Zpc2libGU7PjtsPOiHs+S7iuacqumAmui/h+ivvueoi+aIkOe7qe+8mjtvPHQ+Oz4+Oz47Oz47dDxAMDxwPHA8bDxQYWdlQ291bnQ7XyFJdGVtQ291bnQ7XyFEYXRhU291cmNlSXRlbUNvdW50O0RhdGFLZXlzOz47bDxpPDE+O2k8MD47aTwwPjtsPD47Pj47cDxsPHN0eWxlOz47bDxESVNQTEFZOmJsb2NrOz4+Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+PjtwPGw8c3R5bGU7PjtsPERJU1BMQVk6bm9uZTs+Pj47Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPDs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+PjtwPGw8c3R5bGU7PjtsPERJU1BMQVk6bm9uZTs+Pj47Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPDs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8cDxwPGw8VmlzaWJsZTs+O2w8bzxmPjs+PjtwPGw8c3R5bGU7PjtsPERJU1BMQVk6bm9uZTs+Pj47Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47cDxsPHN0eWxlOz47bDxESVNQTEFZOm5vbmU7Pj4+Ozs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+Oz47Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47cDxsPHN0eWxlOz47bDxESVNQTEFZOm5vbmU7Pj4+Ozs7Ozs7Ozs7Oz47Oz47dDxAMDxwPHA8bDxWaXNpYmxlOz47bDxvPGY+Oz4+O3A8bDxzdHlsZTs+O2w8RElTUExBWTpub25lOz4+Pjs7Ozs7Ozs7Ozs+Ozs+O3Q8QDA8O0AwPDs7QDA8cDxsPEhlYWRlclRleHQ7PjtsPOWIm+aWsOWGheWuuTs+Pjs7Ozs+O0AwPHA8bDxIZWFkZXJUZXh0Oz47bDzliJvmlrDlrabliIY7Pj47Ozs7PjtAMDxwPGw8SGVhZGVyVGV4dDs+O2w85Yib5paw5qyh5pWwOz4+Ozs7Oz47Ozs+Ozs7Ozs7Ozs7Pjs7Pjt0PHA8cDxsPFRleHQ7VmlzaWJsZTs+O2w85pys5LiT5Lia5YWxNDU55Lq6O288Zj47Pj47Pjs7Pjt0PHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Pjt0PHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Pjt0PHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPHhjOz4+Oz47Oz47dDxwPHA8bDxJbWFnZVVybDs+O2w8Li9leGNlbC8xNTA3NDEwNTM0Ni5qcGc7Pj47Pjs7Pjs+Pjt0PDtsPGk8Mz47PjtsPHQ8QDA8Ozs7Ozs7Ozs7Oz47Oz47Pj47Pj47Pj47PrFMYOihzRrGnqxg+f/GDXPqFDNy " ;

                btn = "%C0%FA%C4%EA%B3%C9%BC%A8";
                getScore();
                break;
        }
    }
}
