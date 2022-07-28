package com.zj.baselib.viewutils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View

/**
 *作者：create by 张金 on 2022/6/27 16:34
 *邮箱：564813746@qq.com
 */
class ViewUtils {
    /*
    * View转换为Image
    * */
    fun saveViewToImage(view: View,width: Int, height: Int): Bitmap{
        view.layout(0,0,width,height)
        val mesureWidth=View.MeasureSpec.makeMeasureSpec(width,View.MeasureSpec.EXACTLY)
        val mesureHeight=View.MeasureSpec.makeMeasureSpec(10000,View.MeasureSpec.AT_MOST)
        view.measure(mesureWidth,mesureHeight)
        view.layout(0,0,view.measuredWidth,view.measuredHeight)
        val bitmap= Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
        val canvas=Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        view.layout(0,0,width,height)
        view.draw(canvas)

        return bitmap
    }

}