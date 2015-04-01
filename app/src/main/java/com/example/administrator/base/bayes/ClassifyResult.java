package com.example.administrator.base.bayes;

/**
 * 分类结果
 */
public class ClassifyResult {
    public double probility;//分类的概率
    public String classification;//分类
    public double proportion;

    public ClassifyResult() {
        this.probility = 0;
        this.classification = null;
        this.proportion = 0.0;
    }
}
