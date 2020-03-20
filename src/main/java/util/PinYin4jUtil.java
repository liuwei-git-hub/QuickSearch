package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import java.util.HashSet;
import java.util.Set;
public class PinYin4jUtil {
    public static boolean containsChinese(String str){
        return str.matches(".*" + CHINESE_PATTERN + ".*");

    }
    private static final String CHINESE_PATTERN = "[\\u4E00-\\u9FA5]";

    //从类内部提出来的主要作用是，复用，提高效率，建成属性
    private static final HanyuPinyinOutputFormat FORMAT = new HanyuPinyinOutputFormat();
    //使用静态代码块进行初始化
    static {
        //设置小写
        FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //设置不带音调
        FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //设置v字符
        FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }
    /*1.取每一个汉字的拼音的第一个然后拼接在一起返回
     *
     * */
    public static String[] get(String hanyu){
        String[] array = new String[2];
        //全拼
        StringBuilder pinyin = new StringBuilder();
        //拼音首字母
        StringBuilder pinyin_first = new StringBuilder();
        for (int i = 0; i <hanyu.length() ; i++) {
            try {
                String[] pingyin = PinyinHelper.
                        toHanyuPinyinStringArray(hanyu.charAt(i),FORMAT);
                //中文字符返回的字符串数组，可能为null或者长度为0.
                //返回原始数值
                if (pingyin.length==0 || pingyin==null){
                    //假如拼接英文和数字，加原始字符
                    pinyin.append(hanyu.charAt(i));
                    pinyin_first.append(hanyu.charAt(i));
                }else {
                    //可以转换为拼音，只取第一个返回
                    pinyin.append(pingyin[0]);
                    pinyin_first.append(pingyin[0].charAt(0));
                }
            } catch (Exception e) {
                //出现异常返回原始字符
                pinyin.append(hanyu.charAt(i));
                pinyin_first.append(hanyu.charAt(i));
            }
        }
        array[0] = pinyin.toString();
        array[1] = pinyin_first.toString();
        return array;
    }
    /*
     * 返回所有字符串拼音
     * 排列组合
     * hanyu 字符串
     * fullSpell 是否为全拼
     * */
    public static String[][] get(String hanyu,boolean fullSpell){
        String[][] result = new String[hanyu.length()][];
        for (int i = 0;i < hanyu.length();i++) {
            try {
                String[] pingyin = PinyinHelper.
                        toHanyuPinyinStringArray(hanyu.charAt(i),FORMAT);
                //中文字符返回的字符串数组，可能为null或者长度为0.
                //返回原始数值
                if (pingyin.length==0 || pingyin==null){//a->["a"]
                    //二维元素的某一行
                    result[i] = new String[]{String.valueOf(hanyu.charAt(i))};
                }else {//和he he
                    //可以转换为拼音，只取第一个返回
                    result[i] = unique(pingyin, fullSpell);
                }
            } catch (Exception e) {
                //出现异常返回原始字符
                result[i] = new String[]{String.valueOf(hanyu.charAt(i))};
            }
        }
        return result;
    }
    /*
     * 拼字符串
     * */
    private static String[] unique(String[] pinyins, boolean fullSpell) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < pinyins.length ; i++) {
            if (fullSpell){
                set.add(pinyins[i]);
            }else {
                set.add(String.valueOf(pinyins[i].charAt(0)));
            }
        }
        return set.toArray(new String[set.size()]);
    }

    public static void main(String[] args)  {
//        String[] pingyins = get("中华人民共和国");
//        System.out.println(Arrays.toString(pingyins));
//        for (String[] s : get("中华人a民1共和国", true)) {
//            System.out.println(Arrays.toString(s));
//        }
        System.out.println(containsChinese("abc")); //false
        System.out.println(containsChinese("a闪c")); //true
    }

}