package com.example.administrator.base.bayes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import android.os.Environment;

/**
 * 训练集管理器
 */

public class TrainingDataManager {
    private static String defaultPath = Environment.getExternalStorageDirectory() + "/trainingTest";
    private static String adpath = Environment.getExternalStorageDirectory() + "/trainingTest/广告类/ad.txt";
    private static String cheatpath = Environment.getExternalStorageDirectory() + "/trainingTest/诈骗类/cheat.txt";
    String[] cheat = {"话费", "优惠", "账户",
            "汇钱", ".钱汇", "打钱", "开房", "被抓", "被骗", "车祸", "扣除",
            "钱打", "汇款", "款汇", "订单", "冻结", "退款", "贷款", "免息", "获赠",
            "打款", "款打", "存款", "中奖", "大奖", "大礼", "订购", "股票",
            "款存", "邮政", "包裹", "您被", "密码", "过期", "中行e令升级", "梦想基金", "中国梦想秀", "场外幸运用户",
            "幸运码", "机号", "幸运观众", "违章", "代孕", "港商", "违约",
            "银行", "建行", "工商", "农行", "卡号", "抽中", "抽奖", "法院", "窃听",
            "有奖", "抽奖", "发售", "账号", "详情", "咨询"};
    private String[] traningFileClassifications;//训练语料分类集合
    private File traningTextDir;//训练语料存放目录

    public TrainingDataManager() {

        traningTextDir = new File(defaultPath);
        String[] ad = {
                "专卖店", "楼盘", "上门", "按摩", "酬宾", "放款", "特价", "机票", "预定",
                "订座", "抵押", "中心", "丰田", "本田", "铃木", "航空", "民航", "置业", "户型",
                "买就送", "五百强", "赠品", "购房", "开盘", "现房", "现楼", "花园", "豪宅",
                "别墅", "海景房", "五佰强", "首付", "精装", "均价", "平米", "发票",
                "代办", "办理", "代开", "担保", "恭候", "光临", "直销", "热销", "促销",
                "周年", "香烟", "让利", "名酒", "商场", "减肥", "换季", "垂询", "康佳", "培训",
                "开讲", "主讲", "物料", "家教", "会馆", "会所", "热线", "预约",
                "保真", "保值", "报名", "本公司", "必备", "滨江", "万每", "千每",
                "彩铃", "参会", "超大", "超低", "车程", "车型", "诚招", "助您", "预购", "欲购",
                "冲刺", "抽取", "出炉", "磁条坏", "从速", "存到", "错过", "答谢", "打造",
                "大赛", "登场", "登陆", "等你拿", "等你抢", "低至", "地产", "邀您",
                "点播", "电话卡", "店内", "顶级", "订车", "独栋", "独家", "独享",
                "渡假", "二手", "返本", "风险", "高档车",
                "公益", "公职", "恭迎", "共赴", "专线", "贵公司", "海关",
                "欢迎来电", "欢迎来店", "欢迎购买", "换购", "回报", "回馈", "火爆", "火热", "获赠",
                "巨献", "绝无仅有", "看房", "看楼", "莅临", "连连", "靓景", "名牌", "打折",
                "亏本", "甩卖", "名校", "凭本", "凭此", "全城", "热卖", "厂家", "任您", "商机",
                "商铺", "商务", "升级", "实惠", "首届", "双赢", "水货", "特邀", "旺铺",
                "我公司", "推出", "点击", "限量", "详询", "详电", "详见", "详情", "业主",
                "欲购", "元/平", "元起", "增值", "赠送", "长期为", "涨停", "帐号",
                "帐户", "折扣", "助您", "装饰", "装修", "资深", "总价", "租售", "0起",
                "元/条", "以旧换新", "0元起", "非诚", "勿扰", "税务", "培訓",
                "事务所", "清仓 ", "百平", "幸运", "高价", "有资金", "全场", "立减", "面积",
                "制作费", "1元/条", "首页", "仅剩", "有礼", "盛邀", "买再送", "折起", "秒杀",
                "权威", "狂欢", "速抢", "底价", "数量有限", "送完即止"
        };
        traningTextDir.mkdirs();
        createFile(adpath, ad);
        createFile(cheatpath, cheat);
        if (!traningTextDir.isDirectory())

        {
            throw new IllegalArgumentException("训练语料库搜索失败！ [" + defaultPath + "]");
        }

        this.traningFileClassifications = traningTextDir.list();
        System.out.println("当前目录数: " + traningTextDir.list().length);
    }

    /**
     * 返回给定路径的文本文件内容
     *
     * @param filePath 给定的文本文件路径
     * @return 文本内容
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static String getText(String filePath) throws IOException {

        InputStreamReader isReader = new InputStreamReader(new FileInputStream(filePath));
        BufferedReader reader = new BufferedReader(isReader);
        String aline;
        StringBuilder sb = new StringBuilder();

        while ((aline = reader.readLine()) != null) {
            sb.append(aline).append(" ");
        }
        isReader.close();
        reader.close();
        return sb.toString().trim();//trim--删除字符串首尾的空白
    }

    private void createFile(String filepath, String[] type) {
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()));
                BufferedWriter bw = new BufferedWriter(osw);
                for (String word : type) {
                    bw.write(word + " ");
                }
                bw.close();
            }
            System.out.println("文件创建????  " + file.exists() + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回训练文本类别，这个类别就是目录名
     *
     * @return 训练文本类别
     */
    public String[] getTraningClassifications() {
        return this.traningFileClassifications;
    }

    /**
     * 根据训练文本类别返回这个类别下的所有训练文本路径（full path）
     *
     * @param classification 给定的分类
     * @return 给定分类下所有文件的路径（full path）
     */
    public String[] getFilesPath(String classification) {
        File classDir = new File(traningTextDir.getPath() + File.separator + classification);
        String[] ret = classDir.list();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = traningTextDir.getPath() + File.separator + classification + File.separator + ret[i];
        }
        return ret;
    }

    /**
     * 返回训练文本集中所有的文本数目
     *
     * @return 训练文本集中所有的文本数目
     */
    public int getTrainingFileCount() {
        int ret = 0;
        for (String traningFileClassification : traningFileClassifications) {
            ret += getTrainingFileCountOfClassification(traningFileClassification);
            System.out.println("文本集中有" + ret + "个分类" + "\n");
        }
        return ret;
    }

    /**
     * 返回训练文本集中在给定分类下的训练文本数目
     *
     * @param classification 给定的分类
     * @return 训练文本集中在给定分类下的训练文本数目
     */
    public int getTrainingFileCountOfClassification(String classification) {
        File classDir = new File(traningTextDir.getPath() + File.separator + classification);
        return classDir.list().length;
    }

    /**
     * 返回给定分类中包含关键字／词的训练文本的数目
     *
     * @param classification 给定的分类
     * @param key            给定的关键字／词
     * @return 给定分类中包含关键字／词的训练文本的数目
     */
    public int getCountContainKeyOfClassification(String classification, String key) {
        int ret = 0;
        try {
            String[] filePath = getFilesPath(classification);

            for (String aFilePath : filePath) {
                String text = getText(aFilePath);

                if (text.contains(key)) {
                    ret++;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(TrainingDataManager.class.getName()).log(Level.SEVERE, null, ex);

        }
        System.out.println("文本集中包含短语  " + key + "1(true)/0(false)" + ret);
        return ret;
    }
}