package com.example.administrator.base.bayes;

/**
 * 该类是一个整合类
 * 实现短信过滤，其中包括基于最小风险策略的朴素bayes与权重再过滤模块
 * 1.长度匹配   小于15的短信直接判断为正常
 * 2.贝叶斯过滤模块---->如果
 * 3.权值再过滤系统
 *
 * @author chongrui
 */
public class SMSFilterSystem {


    public String SMSFilter(String text) {
        String classify;
        //1.首先加载bayes模块和权值再过滤模块
        ClassifyResult result;
        BayesClassifier bayesClassifier = new BayesClassifier();
        //长度小于15的短信判断为正常短信
        if (text.length() <= 15) {
            classify = "good";
            System.out.println("长度小于15直接判定为正常" + text.length());
            return classify;
        }
        //2.进行bayes计算概率
        result = bayesClassifier.badPro(text);
        System.out.println("贝叶斯结果----" + result.classification);
        System.out.println("两个类别的比例----" + result.proportion);
        System.out.println("朴素贝叶斯的结论---文本属于：[" + result.classification + "]....概率为：" + result.probility);
        //3.基于最小风险进行判定  如果两个类别概率倍数关系为3------>直接判定，否则进入再过滤模块

        if (result.proportion > 2) {
            System.out.println("特征明显直接判断!");
            classify = result.classification;
        } else {
            //再过滤模块
            //特征不明显，进入再过滤模块
            System.out.println("特征不明显进入再过滤模块!");
            FilterAgainModule filterAgainModule = new FilterAgainModule();
            int weight = filterAgainModule.getFinalWeight(text);
            //权值
            if (weight >= 5) {
                System.out.println("总权值为： " + weight);
                classify = "bad";
            } else {
                System.out.println("总权值为： " + weight);
                classify = "good";
            }
        }
        return classify;
    }

}