package com.example.administrator.base.bayes;

/**
 * 停用词处理器
 * @author phinecos
 *
 */
public class StopWordsHandler
{
    private static String stopWordsList[] ={
            "啊","我", "哪儿","尊敬","您的","至",
            "你","你们","的", "我们","要","自己","之","将",
            "“","”","，","（","）","后","应","到","某",
            "后","个","是","位","新","一","两","在","中",
            "或","有","更","好","请","您","关于","出现","从",
            "自","往","朝","向","到","在","于","自从","当",
            "按照","以","为","为了","为着","对","连","同","跟","和",
            "与","比","把","对于","关于","至于","由于","及","以及","而",
            "或者","或是","并","并且","地","得","着","了","过",
            "呢","吗","啊","吧","嘛","所","被","喽","哎","哼",
            "喂","哦","呀","恩","嗯","如","请","中","的","将",
            "是","后","那","就","好","在","今","由"};//常用停用词


    public static boolean IsStopWord(String word)
    {
        for(int i=0;i<stopWordsList.length;++i)
        {
            if(word.equalsIgnoreCase(stopWordsList[i]))
                return true;
        }
        return false;
    }

}
























