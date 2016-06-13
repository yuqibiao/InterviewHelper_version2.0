package com.yyyu.interviewhelper.bean;

import java.util.List;

/**
 * 功能：题目数据对应Json
 *
 * @author yyyu
 * @date 2016/6/7
 */
public class ExeJson {
    public boolean success;
    public String msg;
    public List<Exe> exes;

    public class Exe{
        public int exeId ;
        public int typeId;
        public int cateId;
        public String exeTitle;
        public String exeOption;
        public String exeAnswer;
        public String exeAnalyze;
    }

}
