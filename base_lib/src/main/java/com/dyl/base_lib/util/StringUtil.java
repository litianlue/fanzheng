package com.dyl.base_lib.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

public class StringUtil {
    /**
     * 单独设置内部字体颜色
     * @param text
     * @param keyworld
     * @return
     */
    public static SpannableStringBuilder getSpannableTextColor(String text, String keyworld,int color){
        SpannableStringBuilder spannableStringBuilder=new SpannableStringBuilder(text);
        if(text.contains(keyworld)){
            int spanStartIndex=text.indexOf(keyworld);
            int spacEndIndex=spanStartIndex+keyworld.length();
            spannableStringBuilder.setSpan(new ForegroundColorSpan(color),spanStartIndex,spacEndIndex, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return spannableStringBuilder;
    }
    /**
     *单独设置内部字体背景颜色
     * @param text
     * @param keyworld
     * @return
     */
    public static SpannableStringBuilder getSpannableTextBackgroundColor(String text, String keyworld,int color){
        SpannableStringBuilder spannableStringBuilder=new SpannableStringBuilder(text);
        if(text.contains(keyworld)){
            int spanStartIndex=text.indexOf(keyworld);
            int spacEndIndex=spanStartIndex+keyworld.length();
            spannableStringBuilder.setSpan(new BackgroundColorSpan(color),spanStartIndex,spacEndIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableStringBuilder;
    }

}
