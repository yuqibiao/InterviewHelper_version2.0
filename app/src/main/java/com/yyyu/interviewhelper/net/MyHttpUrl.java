package com.yyyu.interviewhelper.net;

/**
 * 功能：全局变量
 *
 * @author yyyu
 * @date 2016/5/19
 */
public class MyHttpUrl {
    public static final String IP_PORT ="http://120.27.107.208:8080";//"http://192.168.2.118:8080";
    public static final String URL_ADDRESS =IP_PORT+"/InterviewHelper-Server/";
    //-------menu数据请求
    public static final String GET_MENU_DATA = URL_ADDRESS+"android/android_menuData";
    //-------板块下的文章
    public static final String GET_ARTICLES_BY_BOARD_ID =URL_ADDRESS+"android/android_articleData";
    //-------获取具体的某一文章
    public static final String GET_ARTICLE_BY_ID = URL_ADDRESS+"android/android_show_article";
    //-------登录
    public static final String LOGIN_URL= URL_ADDRESS+"android/android_login.action";
    //-------注册
    public static final String REGISTER_URL=URL_ADDRESS+"android/android_register";
    //-------修改用户信息
    public static final String MODIFY_USER_INFO = URL_ADDRESS+"android/android_modify_userInfo";
    //-------图片上传
    public static final String UPLOAD_IMG = URL_ADDRESS+"android/android_upload_img";
    //-------添加文章
    public static final String ADD_ARTICLE = URL_ADDRESS+"android/android_add_article";
    //-------喜爱文章操作
    public static final String LOVE_OPERATION = URL_ADDRESS+"android/android_love_option";
    //-------获取某个用户的喜爱文章
    public static final String GET_LOVE_ARTICLES = URL_ADDRESS+"android/android_get_love_articles";
    //-------发送评论
    public static final String SEND_COMM = URL_ADDRESS+"android/android_comm";
    //-------获取某个文章的评论
    public static final String GET_COMM = URL_ADDRESS+"android/android_get_comms";
    //-------添加关注
    public static final String ADD_FOLLOW =URL_ADDRESS+"android/android_operate_follow";
    //-------获取题目分类
    public static final String GET_EXE_CATE = URL_ADDRESS+"android/android_get_exe_cate";
    //-------根据分类获取题目
    public static final String GET_EXES_BY_CATE_ID = URL_ADDRESS+"android/android_get_exes_by_cateId";

}
