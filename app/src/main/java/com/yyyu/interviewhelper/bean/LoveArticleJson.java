package com.yyyu.interviewhelper.bean;

import java.util.List;

/**
 *喜爱文章的json bean
 * @author yyyu
 */
public class LoveArticleJson {
    public boolean succes;
    public String msg;
    public List<LoveArticle> loves;

    public class LoveArticle {
        public Integer loveId;
        public String loveTime;
        public Integer articleId;
        public Integer boardId;
        public Integer userId;
        public String articleTitle;
        public String articleIcon;
        public String articleContent;
    }

}
