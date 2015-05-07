package com.example.administrator.base.bayes;


/**
 * 类条件概率计算
 * 类条件概率
 * <p/>
 * P(xj|cj)=( N(X=xi, C=cj )+1 ) / ( N(C=cj)+M+V )
 * 其中，N(X=xi, C=cj）表示类别cj中包含属性x i的训练文本数量；
 * N(C=cj)表示类别cj中的训练文本数量；
 * M值用于避免 N(X=xi, C=cj）过小所引发的问题；
 * V表示类别的总数。
 * <p/>
 * 条件概率
 * 定义 设A, B是两个事件，且P(A)>0 称
 * P(B∣A)=P(AB)/P(A)
 *
 * @author Administrator
 */

public class ClassConditionalProbability {
    private static final float M = 0F;
    private static TrainingDataManager tdm = new TrainingDataManager();

    /**
     * 计算类条件概率
     *
     * @param x 给定的文本属性
     * @param c 给定的分类
     * @return 给定条件下的类条件概率
     * x是分词后的短语，c是类别
     */
    public static float calculatePxc(String x, String c) {

        //x是sms文本分词后的String[]中的 X[i]，在BayesClassify中遍历X[i]，getCountContainKeyOfClassification(c, x)用于获得分词后的关键词在分类中数目；
        float ret;
        //返回给定分类中包含关键字／词的训练文本的数目
        System.out.print("短信中包含了" + x + "的短语");
        float Nxc = tdm.getCountContainKeyOfClassification(c, x);
        System.out.print("短信中包含了" + Nxc + "个关键词");
        //返回训练文本集中在给定分类下的训练文本数目
        float Nc = tdm.getTrainingFileCountOfClassification(c);
        System.out.print("NXC===========" + Nxc);
        //返回训练文本类别，这个类别就是目录名
        float V = tdm.getTraningClassifications().length;
        System.out.print("V===========" + V);
        ret = (Nxc + 1) / (Nc + M + 2);
        System.out.print("ClassConditionProbability=======" + ret);
        return ret;
    }
}
