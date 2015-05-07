package com.example.administrator.base.bayes;

import com.example.administrator.base.Service.MsgReceiver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 这个类实现再过滤模块
 * <p/>
 * 1.检测短信的长度
 * 2.检测短信中是否含有网址
 * 3.检测短信中是否含有电话号码
 * 4.检测短信中的标点符号是否多于3个
 * 5.检测短信中是否含有要求回复的信息
 */
public class FilterAgainModule {

    /**
     * 检测短信的长度
     *
     * @param text
     * @return
     */
    private int getLength(String text) {
        return text.length();
    }

    /**
     * 由短信求检测短信长度后的weight值
     *
     * @param text 短信的content
     */
    public int getLengthWeight(String text) {
        int length = getLength(text);
        int weight = 0;
        if (length >= 100) {
            weight += 5;
        } else if (length >= 70) {
            weight += 4;
        } else if (length >= 40) {
            weight += 3;
        }
        System.out.println("长度的weight:" + weight + "\n");
        return weight;
    }

    /**
     * 检测短信中的URL
     *
     * @param text
     */
    public int getUrlWeight(String text) {
        int weight = 0;
        String regex = "((http://)?([A-Za-z0-9]+[.])|(www.))\\w+[.]([A-Za-z0-9]{2,4})" +
                "?[[.]([A-Za-z0-9]{2,4})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.]" +
                "[A-Za-z0-9]{2,4}+|/?)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            weight += 4;
        }
        System.out.println("链接的weight:" + weight + "\n");
        return weight;
    }

    /**
     * 检测短信中的电话号码
     *
     * @param text
     */
    public int getPhoneNumberWeight(String text) {
        int weight = 0;
        String regexPhoneNumber = "(1[3458]\\d{9})|(((\\d{3,4})?\\d{7,8}))";
        Pattern pattern = Pattern.compile(regexPhoneNumber);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            weight += 3;
        }
        System.out.print("电话的weight=====" + weight + "\n");
        return weight;
    }

    /**
     * 检测文本中使用的标点符号的个数来增加权值
     */
    public int getPunctuationWeight(String text) {
        int weight = 0;
        int count = 0;//计数器
        String punctuations[] = {"~", "^", ">", "&", "\\", "【", "[", "《", "*", "‘"};
        for (String punctuation : punctuations) {
            if (text.contains(punctuation)) {
                count++;
            }
        }
        //有四个不同的标点
        if (count >= 3) {
            weight += 2;
        }
        System.out.println("weight-标点:" + weight + "\n");
        return weight;
    }

    /**
     *
     */
    public int getResponseFlagWeight() {
        MsgReceiver msgReceiver = new MsgReceiver();
        String text = msgReceiver.Body;
        int weight = 0;
        //待补充    这里要翻看训练集里的短信添加
        String[] flags = {"0元起", "欢迎来电", "幸运码", "密码过期", "中行e令升级", "梦想基金", "点播", "抽取", "出炉", "磁条坏",
                "中国梦想秀", "场外幸运用户", "幸运观众",
                "幸运码", "违章", "助您", "欲购",
                "您被选", "您被抽", "欢迎来店", "欢迎购买", "任您", "特邀", "旺铺", "首届", "欲购", "狂欢", "速抢", "返本",
                "底价", "数量有限", "送完即止", "滨江", "立减", "元起", "增值", "莅临", "巨献", "火爆", "火热", "获赠", "等你拿", "等你抢", "邀您"
        };
        for (String flag : flags) {

            if (text.contains(flag)) {
                weight += 4;
                break;
            }
        }
        System.out.print("Flag 的weight=====" + weight + "\n");
        return weight;
    }

    /**
     * ******根据文本中的繁体字个数来设置权值******************
     */
    public int getComplexWeight() {
        MsgReceiver msgReceiver = new MsgReceiver();
        int weight = 0;
        if (msgReceiver.complexcount >= 5)
            weight += 5;
        System.out.print("繁体字weight=====" + weight + "\n");
        return weight;
    }

    public int contain106Weight() {
        MsgReceiver msgReceiver = new MsgReceiver();
        int weight = 0;
        if (msgReceiver.contain106)
            weight += 3;
        System.out.print("包含106号码段weight=====" + weight + "\n");
        return weight;
    }

    public int getFinalWeight(String text) {
        int weight;
        weight = getLengthWeight(text) +
                getPhoneNumberWeight(text) +
                getUrlWeight(text) +
                getResponseFlagWeight() +
                getPunctuationWeight(text) +
                getComplexWeight() +
                contain106Weight();
        System.out.println("再过滤模块总权值：weigth====" + weight + "\n");
        return weight;
    }
}













































