package com.base.project.utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author IMXU
 * @time 2017/12/7 18:05
 * @des ${TODO}
 * 邮箱：butterfly_xu@sina.com
 */

public class FileCacheUtil {
    //定义缓存文件的名字，方便外部调用
    public static final String docCache = "docs_cache.txt";//缓存文件
    //缓存超时时间
    public static final int CACHE_SHORT_TIMEOUT=1000 * 60 * 5; // 5 分钟
    /**设置缓存
     content是要存储的内容，可以是任意格式的，不一定是字符串。

     // 打开文件，该文件只能由调用该方法的应用程序访问
     // MODE_PRIVATE 该文件只能由调用该方法的应用程序访问
     // MODE_APPEND 如果文件已存在，就在结尾追加内容，而不是覆盖文件
     // MODE_WORLD_READABLE 赋予所有应用程序读权限
     // MODE_WORLD_WRITEABLE 赋予所有应用程序写权限
     */
    public static void setCache(String content, Context context, String cacheFileName, int mode) {
        FileOutputStream fos = null;
        try {
            //打开文件输出流，接收参数是文件名和模式
            fos = context.openFileOutput(cacheFileName,mode);
            fos.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //读取缓存，返回字符串（JSON）
    public static String getCache(Context context, String cacheFileName) {
        FileInputStream fis = null;
        StringBuffer sBuf = new StringBuffer();
        try {
            fis = context.openFileInput(cacheFileName);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) != -1) {
                sBuf.append(new String(buf,0,len));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(sBuf != null) {
            return sBuf.toString();
        }
        return null;
    }

    public static String getCachePath(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    //判断缓存是否过期,比较文件最后修改时间
    private boolean cacheIsOutDate(String cacheFileName, Context context) {
        File file = new File(FileCacheUtil.getCachePath(context) + "/" + cacheFileName);
        //获取缓存文件最后修改的时间，判断是是否从缓存读取
        long date = file.lastModified();
        long time_out = (System.currentTimeMillis() - date);
        if (time_out > FileCacheUtil.CACHE_SHORT_TIMEOUT) {
            return true;
        }
        return false;//未过期
    }

}
