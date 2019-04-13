package com.example.administrator.testloginscau.myview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.testloginscau.R;
import com.example.administrator.testloginscau.bean.Course;

import java.util.List;
import java.util.Random;

import static java.security.AccessController.getContext;


public class TimeTableView extends LinearLayout {

    /**
     * 显示周几的LinearLayout
     */
    private LinearLayout dayLinearLayout;
    /**
     * 下方内容的LinearLayout
     */
    private LinearLayout contentLinearLayout;
    /**
     * 下方左边显示节数的LinearLayout
     */
    private LinearLayout numberLinearLayout;
    /**
     * 课程表格的LinearLayout
     */
    private RelativeLayout tableLinearLayout;
    /**
     * 一个格子的宽度
     */
    private static final int GRID_WIDTH = 60;
    /**
     * 一个格子的高度
     */
    private static final int GRID_HEIGHT = 70;
    /**
     * 侧边宽度
     */
    private static final int SIDE_WIDTH = 30;
    /**
     * 侧边高度
     */
    private static final int SIDE_HEIGHT = 40;
   /**
     * 一周的课程
     */
    private List<Course> courses;
    /**
     * 一周的天数+一个用来显示几月份的格子，总共8个格子
     */
    private static final int DAY_COUNT = 7;
    /**
     * 一天最多11节课
     */
    private static final int COURSE_COUNT = 11;


   /* private static final int[] bg=new int[]{
            R.drawable.course_bg1,
            R.drawable.course_bg2,
            R.drawable.course_bg3,
            R.drawable.course_bg4,
            R.drawable.course_bg5,
            R.drawable.course_bg6,
            R.drawable.course_bg7,
            R.drawable.course_bg8
    };*/

    private static final int textColor= Color.parseColor("#ffffff");
    private static final int otherTextColor=Color.parseColor("#8ec6f4");

    public TimeTableView(Context context) {
        this(context, null);
    }

    public TimeTableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeTableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 外界如果没有设置课程的话界面是空的
     *
     * @param courses
     */
    public void setCourses(List<Course> courses) {
        this.courses = courses;
        initTimeTable();
    }

    /**
     * 初始化课程表
     */
    private void initTimeTable() {
        //-----------------初始化最上方一栏-----------------------
        dayLinearLayout = new LinearLayout(getContext());
        //设置宽高
        dayLinearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(SIDE_HEIGHT)));
        //设置方向
        dayLinearLayout.setOrientation(HORIZONTAL);
        //添加显示周几的textView
        for (int i = 0; i <= DAY_COUNT; i++) {
            if (i == 0) {
                //先添加一个显示当前月数的TextView
                TextView tv0 = new TextView(getContext());
                //设置宽高
                tv0.setWidth(dp2px(SIDE_WIDTH));
                tv0.setHeight(dp2px(SIDE_HEIGHT));
                tv0.setText("2月");
                tv0.setTextColor(otherTextColor);
                tv0.setBackgroundResource(R.drawable.tv_border);
                tv0.setGravity(Gravity.CENTER);
                dayLinearLayout.addView(tv0);
            }
            //添加显示周几的textView
            else {
                TextView tv = new TextView(getContext());
                tv.setWidth(dp2px(GRID_WIDTH));
                tv.setHeight(dp2px(SIDE_HEIGHT));
                tv.setText("星期" + i);
                tv.setTextColor(otherTextColor);
                tv.setBackgroundResource(R.drawable.tv_border);
                tv.setGravity(Gravity.CENTER);
                dayLinearLayout.addView(tv);
            }
        }
        this.addView(dayLinearLayout);

        //-------------------初始化左边一栏-------------------
        contentLinearLayout = new LinearLayout(getContext());
        contentLinearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentLinearLayout.setOrientation(HORIZONTAL);

        numberLinearLayout = new LinearLayout(getContext());
        numberLinearLayout.setLayoutParams(new LayoutParams(dp2px(SIDE_WIDTH), ViewGroup.LayoutParams.WRAP_CONTENT));
        numberLinearLayout.setOrientation(VERTICAL);

        //添加显示节数的TextView
        for (int i = 1; i <= COURSE_COUNT; i++) {
            TextView tv = new TextView(getContext());
            tv.setWidth(dp2px(SIDE_WIDTH));
            tv.setHeight(dp2px(GRID_HEIGHT));
            tv.setText(i + "");
            tv.setTextColor(otherTextColor);
            tv.setGravity(Gravity.CENTER);
            tv.setBackgroundResource(R.drawable.tv_border);
            numberLinearLayout.addView(tv);
        }
        contentLinearLayout.addView(numberLinearLayout);

        //--------------初始化课程内容-----------------------
        tableLinearLayout = new RelativeLayout(getContext());
        tableLinearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        for (int i = 0; i < courses.size(); i++) {
            Course course=courses.get(i);
            TextView tv=new TextView(getContext());
            tv.setWidth(dp2px(GRID_WIDTH));
            //tv.setHeight(dp2px(GRID_HEIGHT));
            tv.setGravity(Gravity.CENTER);
            //tv.setText(course.getcName()+"@"+course.getClassroom());
            tv.setText(course.getContent());
            tv.setTextColor(textColor);
            //tv.setBackgroundResource(bg[new Random().nextInt(7)]);
            tv.setBackgroundResource(R.drawable.course1_bg);
            //tv.setBackgroundResource(R.drawable.course_bg1);
            //设置top和left
            int left = 0;
            String day=course.getDay();
            //判断星期几决定left
            switch (day){
                case "星期一":
                    left=0;
                    break;
                case "星期二":
                    left=GRID_WIDTH;
                    break;
                case "星期三":
                    left=GRID_WIDTH*2;
                    break;
                case "星期四":
                    left=GRID_WIDTH*3;
                    break;
                case "星期五":
                    left=GRID_WIDTH*4;
                    break;
                case "星期六":
                    left=GRID_WIDTH*5;
                    break;
                case "星期天":
                    left=GRID_WIDTH*6;
                    break;
            }
            //tv.setLeft(dp2px(left));
            //决定top
            int startNum=course.getStartNum();
            int endNum=course.getEndNum();
            int top=0,height=0;
            switch (startNum){
                case 1:
                    top=0;
                    break;
                case 3:
                    top=GRID_HEIGHT*2;
                    break;
                case 5:
                    top=GRID_HEIGHT*4;
                    break;
                case 7:
                    top=GRID_HEIGHT*6;
                    break;
                case 9:
                    top=GRID_HEIGHT*8;
                    break;
            }
            //tv.setTop(dp2px(top));
            //决定height
            switch (endNum){
                //有2节课，有3节课的
                case 2:
                case 4:
                case 6:
                case 8:
                case 10:
                    height=GRID_HEIGHT*2;
                    break;
                case 3:
                case 7:
                case 11:
                    height=GRID_HEIGHT*3;
                    break;
            }
            Log.i("xsx","left="+left+",top="+top+",height="+height);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            //lp.setMargins(left, top, left+GRID_WIDTH, top+height);
            lp.leftMargin=dp2px(left);
            lp.topMargin=dp2px(top);
//            lp.height=height;
            tv.setLayoutParams(lp);
            tv.setHeight(dp2px(height));
            tableLinearLayout.addView(tv);
        }
        contentLinearLayout.addView(tableLinearLayout);
        this.addView(contentLinearLayout);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int dp2px(int dpValue) {
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (density * dpValue + 0.5f);
    }
}
