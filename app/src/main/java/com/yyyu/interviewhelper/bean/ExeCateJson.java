package com.yyyu.interviewhelper.bean;

import java.util.List;

/**
 * 功能：题目分类Json
 *
 * @author yyyu
 * @date 2016/6/7
 */
public class ExeCateJson {
    public boolean success ;
    public String  msg;
    public List<Exe> exeCates;

    public class Exe{
        public Integer cateId ;
        public String  cateName;
    }
}
