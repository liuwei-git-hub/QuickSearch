package util;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
public class pinyintest {
    private static final HanyuPinyinOutputFormat FORMAT=new HanyuPinyinOutputFormat();
    static {
        FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }
    /**
     * 中文字符格式
     */
    private static final String CHINESE_PATTERN = "[\\u4E00-\\u9FA5]";
    //【】代表的是一个区间，如果在这个区间内，就表示有中文字符。
    public static boolean containsChinese(String str){
        //.* 表示意思是0到任何。
        return str.matches(".*"+CHINESE_PATTERN+".*");
    }
    /*
    * 简单实现：取每一个汉字拼音的第一个然后拼接起来
    * */
    public static String []get(String hanyu) {
        String []array=new String[2];
        StringBuilder pinyin=new StringBuilder();
        StringBuilder pinyin_first=new StringBuilder();
        for (int i = 0; i <hanyu.length() ; i++) {
            //我们不能直接将异常抛除，否则无法进行添加为英文数字等原字符需要catch到执行
            try{  String []result=PinyinHelper.toHanyuPinyinStringArray(hanyu.charAt(i),FORMAT);
                //我们在传入的字符串中只能识别汉字，如果是英文或者是数字，就会出现，返回为空，如果
                //在使用pinyin.append(result[0]);添加元素，会出现数组越界异常，
                //我们在考虑到这些情况呢就，如果遇到直接就去返回传入字符串中的原有元素。
                if (result.length==0||result==null){
                    pinyin.append(hanyu.charAt(i));
                    pinyin_first.append(hanyu.charAt(i));
                }else {//汉字转化为拼音只取第一个
                    pinyin.append(result[0]);
                    pinyin_first.append(result[0].charAt(0));
                }
            }catch (Exception e){//如果发生异常，我们去添加原字符串中的字符
                pinyin.append(hanyu.charAt(i));
                pinyin_first.append(hanyu.charAt(i));
            }
        }
        array[0]=pinyin.toString();
        array[1]=pinyin_first.toString();
        return array;
    }
    public static String[][]get1(String hanyu,boolean fullSpell){
        String[][]result=new String[hanyu.length()][];
        for (int i = 0; i <hanyu.length() ; i++) {
            //我们不能直接将异常抛除，否则无法进行添加为英文数字等原字符需要catch到执行
            try{  String []result1=PinyinHelper.toHanyuPinyinStringArray(hanyu.charAt(i),FORMAT);
                //我们在传入的字符串中只能识别汉字，如果是英文或者是数字，就会出现，返回为空，如果
                //在使用pinyin.append(result[0]);添加元素，会出现数组越界异常，
                //我们在考虑到这些情况呢就，如果遇到直接就去返回传入字符串中的原有元素。
                if (result1.length==0||result1==null){
                    //将英文数字等添加到数组中  a-》["a"];
                   result[i]=new String[]{String.valueOf(hanyu.charAt(i))};
                }else {//我们取到的值为去掉音调的全拼，所以我们需要去重
                    result[i]=unique(result1,fullSpell);
                }
            }catch (Exception e){//如果发生异常，我们去添加原字符串中的字符
                result[i]=new String[]{String.valueOf(hanyu.charAt(i))};
            }
        }
        return result;
    }
/*
* unique去重操作
* fullSpell 是否为全拼   true为全拼，false取首字母
* */
    private static String[] unique(String[]result1,boolean fullSpell) {
        Set<String> set = new HashSet<>();
        //也是完成一个去重的操作。
        for (int i = 0; i <result1.length ; i++) {
            //判断如果是全拼，我们就添加每一个字符
            if (fullSpell) {
                set.add(result1[i]);
            }else {
                //不是全拼的情况我们只添加第一个字符串的第一个字符
                set.add(String.valueOf(result1[i].charAt(0)));
            }
        }
        //使用set去重后我们传回给result数组
        return set.toArray(new String[set.size()]);
    }

    public static void main(String[] args)  {
       String []pingyins=get("中2k国人1s");
            System.out.println(Arrays.toString(pingyins));
            //将去重后所有以元素的形式  展现出来   如果是字母或者数组其他字符   直接打印不会转拼音
        for (String[] pin:get1("中华人民a共和%国", true)) {
            System.out.println(Arrays.toString(pin));
        }
        System.out.println(containsChinese("231frer"));
        //false
        System.out.println(containsChinese("中国"));
        //true
    }
}
