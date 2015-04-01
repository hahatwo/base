package com.example.administrator.base.bayes;

import java.io.IOException;
import java.io.StringReader;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class IKWordSplit {

    public static String splitWithIKAnalyzer(String sInput, String splitSeperator) {
        String sOutput = "";
        StringReader re = new StringReader(sInput);
        IKSegmenter ik = new IKSegmenter(re, true);
        Lexeme lex;
        try {
            while ((lex = ik.next()) != null) {
                sOutput = sOutput + lex.getLexemeText() + splitSeperator;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.print("短信中IKWordSplit后的output是" + sOutput + "\n");
        return sOutput;
    }
}
































