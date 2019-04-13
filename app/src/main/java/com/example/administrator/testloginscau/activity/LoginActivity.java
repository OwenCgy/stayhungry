package com.example.administrator.testloginscau.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.testloginscau.R;
import com.example.administrator.testloginscau.api.Api;
import com.example.administrator.testloginscau.Interface.ApiService;
import com.example.administrator.testloginscau.bean.UserInfo;
import com.example.administrator.testloginscau.util.Utility;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView codeImage;
    private EditText nameText;
    private EditText passwordText;
    private EditText codeText;
    private Button commitBut;
    private ProgressDialog progressDialog ;
    private static final String TAG = "LoginActivity";

    private String sessionId = "";

    private ApiService apiService = Api.getApi();

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            sessionId = (String)msg.obj;
            Log.d("LogActivity", sessionId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initCode();
        codeImage.setOnClickListener(this);
        commitBut.setOnClickListener(this);

        /*codeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initCode();
            }
        });

        commitBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postData();
            }
        });*/
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.code_image :
                initCode();
                break;
            case R.id.commit:
                String name = nameText.toString().trim();
                String pw = passwordText.toString().trim();
                String code = codeText.toString().trim();
                if(name.equals("")||name==null|| pw.equals("")||pw == null||code.equals("")|| code==null){
                    Toast.makeText(LoginActivity.this,"账号密码验证码不可为空",Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("正在获取数据");
                    progressDialog.setMessage("Loading");
                    progressDialog.show();
                    postData();
                }
                break;
        }
    }

    public void postData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final UserInfo userInfo = new UserInfo();
                userInfo.setCode(codeText.getText().toString());
                userInfo.setName(nameText.getText().toString());
                userInfo.setPassword(passwordText.getText().toString());


                apiService.login(
                        sessionId,
                        "dDwyODE2NTM0OTg7Oz7ZNOrHH3tF+dVgEhlWwbNujIonsQ==",
                        userInfo.getName(),
                        userInfo.getPassword(),
                        userInfo.getCode(),
                        "%D1%A7%C9%FA",
                        "",
                        "",
                        "",
                        ""
                ).enqueue(new Callback<ResponseBody>() {
                    String info = "";
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            info = response.body().string();
                            if(info.contains("验证码不正确！！")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {progressDialog.dismiss();}
                                });
                                Toast.makeText(LoginActivity.this,"验证码错误",Toast.LENGTH_SHORT).show();
                            }else if(info.contains("密码错误！！")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {progressDialog.dismiss();}
                                });
                                Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                            }else if(info.contains("用户名不存在或未按照要求参加教学活动！！")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {progressDialog.dismiss();}
                                });
                                Toast.makeText(LoginActivity.this,"用户不存在！！",Toast.LENGTH_SHORT).show();
                            }else if(info.contains("欢迎您：")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {progressDialog.dismiss();}
                                });
                                String name = Utility.parseLoginHtml(info);
                                Log.d(TAG, "intent");
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtra("credit", userInfo.getName());
                                intent.putExtra("name", name);
                                intent.putExtra("sessionId", sessionId);
                                startActivity(intent);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            }
        }).start();

    }

    private void initView() {
        codeImage = (ImageView) findViewById(R.id.code_image);
        nameText = (EditText) findViewById(R.id.user_name);
        passwordText = (EditText) findViewById(R.id.user_password);
        codeText = (EditText) findViewById(R.id.code);
        commitBut = (Button) findViewById(R.id.commit);
    }

    private void initCode() {

        apiService.getCodeImage().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Headers headers = response.headers();

                String sessionID = headers.get("Set-Cookie");

                Message msg = new Message();
                msg.obj = sessionID;
                handler.handleMessage(msg);

                byte[] bytes = new byte[0];
                try {
                    bytes = response.body().bytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //把byte字节组装成图片
                final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //网络图片请求成功，更新到主线程的ImageView
                        codeImage.setImageBitmap(bmp);
                    }
                });
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
