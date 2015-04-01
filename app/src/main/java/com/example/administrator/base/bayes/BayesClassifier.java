package com.example.administrator.base.bayes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;


/**
 * 朴素贝叶斯分类器
 */
public class BayesClassifier {
    private TrainingDataManager tdm;//训练集管理器

    /**
     * 默认的构造器，初始化训练集
     */
    public BayesClassifier() {
        tdm = new TrainingDataManager();
    }

    /**
     * 计算给定的文本属性向量X在给定的分类Cj中的类条件概率
     * <code>ClassConditionalProbability</code>连乘值
     *
     * @param X  给定的文本属性向量
     * @param Cj 给定的类别
     * @return 分类条件概率连乘值，即<br>
     * X[]是分词后的短语数组，CJ是类别
     */
    float calcProd(String[] X, String Cj) {
        float ret = 1.0F;
        // 类条件概率连乘
        for (String Xi : X) {
            double zoomFactor = 10.0f;
            System.out.print("循环体当前短语是" + Xi + "\n");
            ret *= ClassConditionalProbability.calculatePxc(Xi, Cj) * zoomFactor;
        }
        // 再乘以先验概率
        ret *= PriorProbability.calculatePc(Cj);
        //int bitNum = MathUtil.getBitNum(ret);
        return ret;
    }

    /**
     * 去掉停用词
     *
     * @return 去停用词后结果
     */
    public String[] DropStopWords(String[] oldWords) {
        Vector<String> v1 = new Vector<String>();
        for (String oldWord : oldWords) {
            if (!StopWordsHandler.IsStopWord(oldWord)) {//不是停用词
                v1.add(oldWord);
            }
        }
        String[] newWords = new String[v1.size()];
        v1.toArray(newWords);
        return newWords;
    }

    /**
     * 对给定的文本进行分类
     *
     * @param text 给定的文本
     * @return 分类结果
     */

    public ClassifyResult badPro(String text) {
        System.out.println("badPro start!!");
        //计算垃圾
        String[] terms = IKWordSplit.splitWithIKAnalyzer(text, " ").split(" ");
        String[] newterms = DropStopWords(terms);//去掉停用词，以免影响分类
        System.out.print("短信中DropStopWord后的newtrerms数量是是" + newterms.length + "\n");
        String[] Classes = tdm.getTraningClassifications();//分类
        float probility;
        List<ClassifyResult> crs = new ArrayList<ClassifyResult>();//分类结果
        for (String Classify : Classes) {
            String Ci = Classify;//第i个分类
            System.out.print("短信中分次后的newtrerms数量是是" + newterms.length + "\n");
            probility = calcProd(newterms, Ci);//计算给定的文本属性向量terms在给定的分类Ci中的分类条件概率
            System.out.println("分类概率计算完成");
            //保存分类结果
            ClassifyResult cr = new ClassifyResult();
            cr.classification = Ci;//分类
            cr.probility = probility;//关键字在分类的条件概率
            System.out.println("关键词在" + Ci + "的概率是：" + probility);
            crs.add(cr);
        }
        //对最后概率结果进行排序
        java.util.Collections.sort(crs, new Comparator() {
            public int compare(final Object o1, final Object o2) {
                final ClassifyResult m1 = (ClassifyResult) o1;
                final ClassifyResult m2 = (ClassifyResult) o2;
                System.out.println("m1=============" + m1 + "\n" + "m2============" + m2);
                final double ret = m1.probility - m2.probility;
                if (ret < 0) {
                    System.out.println("两者的比例m2:m1:" + m2.probility / m1.probility);
                    m1.proportion = m2.probility / m1.probility;
                    m2.proportion = m2.probility / m1.probility;
                    return 1;
                } else {
                    System.out.println("两者的比例m1:m2:" + (m1.probility / m2.probility));
                    m1.proportion = m1.probility / m2.probility;
                    m2.proportion = m1.probility / m2.probility;
                    return -1;
                }
            }
        });

        System.out.println("badPro end!!");
        return crs.get(0);//返回crs列表中的第一项，即为排序后最大概率项,返回类型为ClasifyResult类型，包含（double probility;//分类的概率 ，classification;//分类 ，proportion;）

    }

}