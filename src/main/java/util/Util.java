package util;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static final String DATE_PATTERN="yyyy-MM-dd HH-mm-ss";

    /*文件大小转化为KB,GB.....
    为了不让每次都要创建SIZE_NAMES   DF  我们就将他们提到外部。
    * */
    private static final String []SIZE_NAMES ={"B","KB","MB","GB"};
    //主要是转化格式   因为直接打印出来不是我们要的
    //DateFormat 日期格式化类
    private static final DateFormat DF= new SimpleDateFormat(DATE_PATTERN);
    public static String parseSize(long size) {
        int n=0;
        while (size>=1024){
            size=size/1024;
            n++;
        }
        return size+SIZE_NAMES[n];
    }
/*
* 日期类型的解析
* */
    public static String parseDate(long last_modified) {
        return DF.format(new Date(last_modified));
    }

    /*public static void main(String[] args) {
        System.out.println(parseDate(213343L));
        System.out.println(parseSize(32234324L));
    }*/
}
