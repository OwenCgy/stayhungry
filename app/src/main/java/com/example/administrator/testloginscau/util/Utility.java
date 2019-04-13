package com.example.administrator.testloginscau.util;

import android.util.Log;

import com.example.administrator.testloginscau.bean.Course;
import com.example.administrator.testloginscau.bean.Score;
import com.example.administrator.testloginscau.bean.TimeTableBean;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.name;

public class Utility {

    private static final String TAG = "Utility";

    public static List<Map<String,String>> parseScoreHtml(String html) {
        /*
        **方法一
        * List<Score> courses = new ArrayList<>();
        Document document = Jsoup.parse(html);
        //得到存放成绩的表格
        Element element = document.getElementsByClass("datelist").get(0);
        //得到所有的行
        Elements trs = element.getElementsByTag("tr");
        for (int i = 0; i < trs.size(); i++){
            Score score = new Score();
            Element e = trs.get(i);
            //得到一行中的所有列
            Elements tds = e.getElementsByTag("td");
            score.setName(tds.get(3).text());
            score.setCredit(tds.get(6).text());
            score.setScore(tds.get(8).text());
            score.setTag(tds.get(14).text());

            courses.add(score);*/
        html = html.replaceAll("&nbsp;","");
        Pattern p,p1;
        Matcher m,m1;
        p = Pattern.compile("<td>[^<]*?</td><td>[^<]*?</td><td>[^<]*?</td><td>[^<]*?</td><td>[^<]*?</td><td>[^<]*?</td><td>[^<]*?</td><td>[^<]*?</td><td>[^<]*?</td><td>[^<]*?</td><td>[^<]*?</td><td>[^<]*?</td><td>[^<]*?</td><td>[^<]*?</td><td>[^<]*?</td>");
        m = p.matcher(html);
        //去掉第一行
        m.find();
        List<Map<String,String>> scoreList = new ArrayList<Map<String,String>>();
        while (m.find()){
//            System.out.println(m.group());
            p1 = Pattern.compile("<td>([^<]*?)</td>");
            m1 = p1.matcher(m.group(0));
            Map<String,String> scoreMap = new HashMap<String,String>();
            for(int i=0;i<15;i++){
                m1.find();
                if(i==3)scoreMap.put("name",m1.group(1));
                if(i==6)scoreMap.put("credits",m1.group(1));
                if(i==8)scoreMap.put("score",m1.group(1));
                if(i==14)scoreMap.put("tag",m1.group(1).isEmpty()?"　　":"重修");
                Log.d("********************"+i,m1.group(1));

            }
            scoreList.add(scoreMap);
        }
        return scoreList;
    }

    public static String parseLoginHtml(String html){
        Document document = Jsoup.parse(html);

        Element element = document.getElementsByClass("info").get(0);
        Element trs = element.getElementById("xhxm");

        String name = trs.text().substring(0, 3);
        Log.d(TAG, "paseLoginHtml: " + name);

        return name;
    }

    public static List<String> parseToSearchScore(String html){
        String viewState = null;
        List<String> list = new ArrayList<>();

        Document document = Jsoup.parse(html);

        Elements elements = document.getElementsByTag("input");

        for (int i = 0; i < elements.size(); i++){
            Element element = elements.get(i);
            String name = element.attr("name");

            if (name.equals("__VIEWSTATE")){
                list.add(element.attr("value"));

                Log.d(TAG, list.get(0));
            }

            /*if (name.equals("__VIEWSTATEGENERATOR")) {
                list.add(element.attr("value"));
                Log.d(TAG, "parseToSearchScore: " + list.get(1));
            }*/
        }

        return  list;
    }


    public static String parseTimeTableHtml1(String html) {
        Document document = Jsoup.parse(html);
        Element element = document.getElementById("Table1");
        String content = element.html();
        StringBuilder stringbuilder = new StringBuilder(content);
        String head = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "</head>\n" +
                "<body>\n" +
                "\t<table id=\"Table1\" class=\"blacktab\" bordercolor=\"Black\" border=\"6\" width=\"100%\">\n";
        stringbuilder.insert(0, head);
        String tail = "</table>\n" +
                "</body>\n" +
                "</html>";
        stringbuilder.append(tail);
        return stringbuilder.toString();
    }

    public static List<Course> parseTimeTableHtml(String html) {
        List<Course> courses = new ArrayList<>();
        //先解析
        Document document = Jsoup.parse(html);
        //先解析出年级、学院、专业、班级代号
        //getParm(document);
        //再解析__VIEWSTATE
        Element viewstateTag = document.getElementsByTag("input").get(2);
       // __VIEWSTATE = viewstateTag.val();
        //---------------------------------
        Element element = document.getElementById("Table1");
        //得到所有的行
        Elements trs = element.getElementsByTag("tr");
        for (int i = 0; i < trs.size(); i++) {
            //只需要处理行号为2（第一节）、4（第三节课）、6（第五节课）、8（第七节课）、10（第九节课）
            if (i == 2 || i == 4 || i == 6 || i == 8 || i == 10) {
                //得到显示第一节课的那一行
                Element e1 = trs.get(i);
                //得到所有的列
                Elements tds = e1.getElementsByTag("td");
                if (i==2||i==8||i==12){
                    //先清除显示上午、下午、晚上的一行
                    tds.remove(0);
                }
                tds.remove(0);
                for (int j = 0; j < tds.size(); j++) {
                    String msg = tds.get(j).text();
                    //判断是否有课程
                    if (msg.length() == 1) {
                        //没有课程就跳过
                        Log.i("xsx", "msg为空跳过");
                        continue;
                    }
                    //处理一个td里的字符，如：计算机组成原理 2节/周(1-17) 张凤英 田师208
                    //周一第3,4节{第1-7周}
                    String strings[] = msg.split(" ");
                    //多少节
                   /* int duringNum = Integer.parseInt(strings[1].substring(0, 1));
                    int startNum = i-1;
                    int endNum = startNum + duringNum - 1;*/
                    Log.d(TAG, "parseTimeTableHtml: " + msg);
                    int startNum = Integer.parseInt(strings[1].substring(3, 4));
                    int endNum = Integer.parseInt(strings[1].substring(5, 6));
                    int duringNum = endNum - startNum + 1;
                    String day = "";
                    switch (j) {
                        case 0:
                            day = "星期一";
                            break;
                        case 1:
                            day = "星期二";
                            break;
                        case 2:
                            day = "星期三";
                            break;
                        case 3:
                            day = "星期四";
                            break;
                        case 4:
                            day = "星期五";
                            break;
                        case 5:
                            day = "星期六";
                            break;
                        case 6:
                            day = "星期天";
                            break;
                    }
                    courses.add(new Course(strings[0], startNum, endNum, day, msg));
                }
            }
        }
        return courses;
    }
    /**
     * 是否是第一次查询课表
     */
 /*   static boolean isFirstTime = true;
    *//**
     * 第一次查询课表的时候需要查询
     *//*
    static String nj;
    static String xy;
    static String zy;
    static String kb;
    *//**
     * 每次都有查询出来，为下次查询做准备
     *//*
    static String __VIEWSTATE;


    *//**
     * 得到年级、学院、专业、班级代号等参数
     *
     * @param document
     *//*
    private static void getParm(Document document) {
        if (isFirstTime) {
            Elements elements1 = document.getElementById("nj").getAllElements();
            for (Element e : elements1) {
                Element element = e.getElementsByAttribute("selected").get(0);
                if (element != null) {
                    nj = element.val();
                    break;
                }
            }
            Elements elements2 = document.getElementById("xy").getAllElements();
            for (Element e : elements2) {
                Element element = e.getElementsByAttribute("selected").get(0);
                if (element != null) {
                    xy = element.val();
                    break;
                }
            }
            Elements elements3 = document.getElementById("zy").getAllElements();
            for (Element e : elements3) {
                Element element = e.getElementsByAttribute("selected").get(0);
                if (element != null) {
                    zy = element.val();
                    break;
                }
            }
            Elements elements4 = document.getElementById("kb").getAllElements();
            for (Element e : elements4) {
                Element element = e.getElementsByAttribute("selected").get(0);
                if (element != null) {
                    kb = element.val();
                    break;
                }
            }
            isFirstTime = false;
            Log.i("xsx", "nj=" + nj + ",xy=" + xy + ",zy=" + zy + ",kb=" + kb);
            Log.i("xsx", "第一次查询");
        }
    }*/


}
