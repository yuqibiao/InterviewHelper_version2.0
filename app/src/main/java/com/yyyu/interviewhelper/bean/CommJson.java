package com.yyyu.interviewhelper.bean;

import java.util.List;

/**
 * 评论数据json
 */
public class CommJson {
	
	public boolean success;
	public  String message;
	public List<Comm> comms;
	
	public class Comm{
		public Integer commId;
		public String commTime;
		public String commContent;
		public String userIcon;
		public Integer userId;
		public String username;
	}
}
