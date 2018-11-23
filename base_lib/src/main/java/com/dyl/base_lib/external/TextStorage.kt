package com.dyl.base_lib.external

import android.graphics.Color
import android.graphics.Typeface
import android.text.Layout
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
import android.text.style.*
import android.view.View


/**
 * Created by dengyulin on 2017/6/29.
 */

class TextStorage {

    val spb: SpannableStringBuilder by lazy {
        SpannableStringBuilder()
    }
    fun addText(str: String, size: Int = -1, color: String = "", backgroundColor: String = "", typeface:Int= Typeface.NORMAL, alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL, click: ((String) -> Unit)? =null):TextStorage {
        val start = spb.length
        spb.append(str)
        val end = spb.length
        //color
        if(color.isNotBlank())spb.setSpan(ForegroundColorSpan(Color.parseColor(color)),start,end, SPAN_EXCLUSIVE_EXCLUSIVE)
        //alignment
        if(alignment!=Layout.Alignment.ALIGN_NORMAL) spb.setSpan(AlignmentSpan.Standard(alignment),start,end,SPAN_EXCLUSIVE_EXCLUSIVE)
        //size
        if(size!=-1) spb.setSpan(AbsoluteSizeSpan(size, true),start,end,SPAN_EXCLUSIVE_EXCLUSIVE)
        //typeface
        if(typeface!=Typeface.NORMAL) spb.setSpan(StyleSpan(typeface),start,end,SPAN_EXCLUSIVE_EXCLUSIVE)
        //backgroundColor
        if(backgroundColor.isNotBlank()) spb.setSpan(BackgroundColorSpan(Color.parseColor(backgroundColor)),start,end,SPAN_EXCLUSIVE_EXCLUSIVE)
        //click
        if(click!=null) {
            spb.setSpan(object : ClickableSpan(){
                override fun onClick(widget: View?) {
                    click.invoke(str)
                }
            },start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return this
    }


}
