package com.dyl.base_lib.data.file

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import java.io.*
import java.text.DecimalFormat


/**
 * Created by dengyulin on 2018/4/8.
 */
fun createNewFile(path: String): File {
    val file = File(path)
    if(!file.parentFile.exists()){
        file.parentFile.mkdirs()
    }
    if (file.exists()) {
        return file
    }else{
        file.createNewFile()
        return file
    }
}
fun View.savaBitmap() {
    var bitmap: Bitmap? = null
    try {
        val width = width
        val height = height
        if (width != 0 && height != 0) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            draw(canvas)
            val fileName=createNewFile( "${Environment
                    .getExternalStorageDirectory().path}/cjypcache/${System.currentTimeMillis()}.jpg")
            val out=FileOutputStream(fileName)
            Log.w("test","fileName="+fileName)
            bitmap?.compress(Bitmap.CompressFormat.JPEG,100,out)
            out.flush()
            out.close()
            MediaStore.Images.Media.insertImage(context.contentResolver,
                    bitmap, fileName.name, null)
           /* MediaStore.Images.Media.insertImage(context.contentResolver,
                    bitmap, fileName.name, null)
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = Uri.fromFile(fileName)
            context.sendBroadcast(mediaScanIntent)*/
        }
    } catch (e: Exception) {
    }
}
fun View.savaBitmap(i: Int) {
    var bitmap: Bitmap? = null
    try {
        val width = width
        val height = height
        if (width != 0 && height != 0) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            draw(canvas)
            val fileName=createNewFile( "${Environment
                    .getExternalStorageDirectory().path}/cjypcache/${System.currentTimeMillis()+i}.jpg")
            val out=FileOutputStream(fileName)
            Log.w("test","fileName="+fileName)
            bitmap?.compress(Bitmap.CompressFormat.JPEG,100,out)
            out.flush()
            out.close()
            MediaStore.Images.Media.insertImage(context.contentResolver,
                    bitmap, fileName.name, null)
            /* MediaStore.Images.Media.insertImage(context.contentResolver,
                     bitmap, fileName.name, null)
             val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
             mediaScanIntent.data = Uri.fromFile(fileName)
             context.sendBroadcast(mediaScanIntent)*/
        }
    } catch (e: Exception) {
    }
}
fun View.getBitmap(): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val width = width
        val height = height
        if (width != 0 && height != 0) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            draw(canvas)
            val fileName=createNewFile( "${Environment
                    .getExternalStorageDirectory()}/cjypcache/${System.currentTimeMillis()}.jpg")
            val out=FileOutputStream(fileName)
            bitmap?.compress(Bitmap.CompressFormat.JPEG,100,out)
            out.flush()
            out.close()
            return bitmap
        }
    } catch (e: Exception) {

    }
    return bitmap
}
fun fileChannelCopy(s: File, t: File) {
    var fi: FileInputStream? = null
    var fo: FileOutputStream? = null
    try {
        fi = FileInputStream(s)
        fo = FileOutputStream(t)
        val `in` = fi!!.channel//得到对应的文件通道
        val out = fo!!.channel//得到对应的文件通道
        `in`.transferTo(0, `in`.size(), out)//连接两个通道，并且从in通道读取，然后写入out通道
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        try {
            if (fo != null) fo!!.close()
            if (fi != null) fi!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}

fun readInputStream(input: InputStream) {
    val bf = BufferedInputStream(input)
    bf.read()
}

fun formatFileSizeToString(fileLen: Long): String {
    val df = DecimalFormat("#.00")
    var fileSizeString = ""
    if (fileLen < 1024) {
        fileSizeString = df.format(fileLen.toDouble()) + "B"
    } else if (fileLen < 1048576) {
        fileSizeString = df.format(fileLen.toDouble()/ 1024) + "K"
    } else if (fileLen < 1073741824) {
        fileSizeString = df.format(fileLen.toDouble()/ 1048576) + "M"
    } else {
        fileSizeString = df.format(fileLen.toDouble() / 1073741824) + "G"
    }
    return fileSizeString
}

fun deleteFile(file: File?) {
    file?.delete()
}
