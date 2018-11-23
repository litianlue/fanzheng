package fanzhen.first.com.fanzhen.external.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import fanzhen.first.com.fanzhen.R;


@SuppressLint("AppCompatCustomView")
public class TimerTextView extends TextView implements Runnable {
    // 时间变量
    private int day, hour, minute, second;
    // 当前计时器是否运行
    private boolean isRun = false;
    private  int color  = Color.BLACK;
    private  int scolor  = Color.WHITE;
    private int model  =0;
    private String leftstring="";
    private String rightstring ="";
    private int timerSColor = Color.BLACK;

    public TimerTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerTextView(Context context) {
        super(context);
    }

    /**
     * 将倒计时时间毫秒数转换为自身变量
     *
     * @param time
     *            时间间隔毫秒数
     */
    public void setTimes(long time) {
        //将毫秒数转化为时间
        this.second = (int) (time / 1000) % 60;
        this.minute = (int) (time / (60 * 1000) % 60);
        this.hour = (int) (time / (60 * 60 * 1000) % 24);
        this.day = (int) (time / (24 * 60 * 60 * 1000));
    }

    /**
     * 显示当前时间
     *
     * @return
     */
    public String showTime() {
        StringBuilder time = new StringBuilder();
        if(model ==0) {
            time.append(day);
            time.append("天");
            time.append(hour);
            time.append("小时");
            time.append(minute);
            time.append("分钟");
            time.append(second);
            time.append("秒");
        }else if(model ==1){
            //time.append(day);
          //  time.append(":");

            if(hour<10){
                time.append("0"+hour);
            }else
                time.append(hour);
            time.append(":");
            if(minute<10)
            time.append("0"+minute);
            else
                time.append(minute);
            time.append(":");
            if(second<10)
            time.append("0"+second);
            else
                time.append(second);

        }else if(model ==3) {

                time.append(day);
                time.append("天");
            if(hour<10){
                time.append(" 0"+hour+" ");
            }else
                time.append(" "+hour+" ");
            time.append(":");
            if(minute<10)
                time.append(" 0"+minute+" ");
            else
                time.append(" "+minute+" ");
            time.append(":");
            if(second<10)
                time.append(" 0"+second+" ");
            else
                time.append(" "+second+" ");

        }else {
            if(hour<10){
                time.append(" 0"+hour+" ");
            }else
                time.append(" "+hour+" ");
            time.append(" : ");
            if(minute<10)
                time.append(" 0"+minute+" ");
            else
                time.append(" "+minute+" ");
            time.append(" : ");
            if(second<10)
                time.append(" 0"+second+" ");
            else
                time.append(" "+second+" ");

        }
        return time.toString();
    }
    public void addTextToLeft(String string){
        this.leftstring = string;
    }
    public void addTextToRight(String string){
        this.rightstring  = string;
    }

    /**
     * 实现倒计时
     */
    private void countdown() {
        if (second == 0) {
            if (minute == 0) {
                if (hour == 0) {
                    if (day == 0) {
                        //当时间归零时停止倒计时
                        isRun = false;
                        return;
                    } else {
                        day--;
                    }
                    hour = 23;
                } else {
                    hour--;
                }
                minute = 59;
            } else {
                minute--;
            }
            second = 60;
        }

        second--;
    }

    public boolean isRun() {
        return isRun;
    }
    public void setModle(int modle) {
            this.model = modle;
    }
    /**
     * 开始计时
     */
    public void start() {
        isRun = true;
        run();
    }

    /**
     * 结束计时
     */
    public void stop() {
        isRun = false;

    }
    //设置 时分秒背景颜色
    public void setBackColorTimer(int color){
            this.color = color;
    }
    //设置 “:”背景颜色
    public void setBackColorTimerS(int scolor){
        this.scolor = scolor;
    }
    //设置 ":"字体颜色
    public void setTimerSColor(int timerSColor){
        this.timerSColor = timerSColor;
    }
    private Bitmap bitmap;
    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    /**
     * 实现计时循环
     */
    @Override
    public void run() {
        if (isRun) {
            countdown();
            if(model==2){
                SpannableString builder = new SpannableString(showTime());
                BackgroundColorSpan onecolor = new BackgroundColorSpan(color);
                BackgroundColorSpan tonescolor = new BackgroundColorSpan(scolor);
                BackgroundColorSpan twoscolor = new BackgroundColorSpan(color);
                BackgroundColorSpan ttwoscolor = new BackgroundColorSpan(scolor);
                BackgroundColorSpan treescolor = new BackgroundColorSpan(color);
                ForegroundColorSpan tone = new ForegroundColorSpan(timerSColor);
                ForegroundColorSpan ttwo = new ForegroundColorSpan(timerSColor);


                String[] split = showTime().split(":");
                int length = split[0].length();

                builder.setSpan(onecolor,0,length-1, Spanned.SPAN_INCLUSIVE_INCLUSIVE); //00
                builder.setSpan(tonescolor,length-1,length+3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);//:
                builder.setSpan(tone,length-1,length+2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);//:
                builder.setSpan(twoscolor,length+2,length+6, Spanned.SPAN_INCLUSIVE_INCLUSIVE);// 00
                builder.setSpan(ttwoscolor,length+6,length+9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// :
                builder.setSpan(ttwo,length+6,length+9, Spanned.SPAN_INCLUSIVE_INCLUSIVE);//:
                builder.setSpan(treescolor,length+9,length+13, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//00
                this.setText(builder);
                postDelayed(this, 1000);

            }else {
                this.setText(leftstring+showTime()+rightstring);
                postDelayed(this, 1000);

            }

        } else {

            removeCallbacks(this);
        }
    }
    private Timer timer;
    private TimerTask timerTask ;
    private int mPosition =0;
    private void setTimer(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {

            }
        };
        timer.schedule(timerTask,3000,3000);
    }
    private void sotpTimer(){
        if(timerTask!=null) {
            timerTask.cancel();
            timerTask = null;
        }
        if(timerTask!=null) {
            timer.cancel();
            timer  = null;
        }
    }

}