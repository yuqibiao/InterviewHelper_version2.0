package com.yyyu.interviewhelper.bean;

import java.util.List;

/**
 * 菜单数据对应的json bean
 */
public class MenuDataJson {

	public boolean success;
	public List<ClazzJson> menuData;
	public List<ContentData> contentData;

	public class ClazzJson {
		public Integer clazzId;
		public String clazzIntro;
		public String clazzName;
		public List<BoardJson> boards;

		public class BoardJson {
			public Integer boardId;
			public String boardIntro;
			public String boardName;
		}
	}
	
	public class ContentData{
		public Integer clazzId;
		public List<Article> newArticles;
		
	}

}
