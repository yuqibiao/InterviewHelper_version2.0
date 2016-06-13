package com.yyyu.interviewhelper.callback;

import com.yyyu.interviewhelper.bean.MenuDataJson;

import java.util.List;

/**
 * 功能：
 *
 * @author yyyu
 * @date 2016/5/26
 */
public interface OnMenuBoardLoadedListener {
    /**
     * 主要是为了让HomeFragment能拿到MainActivity中
     * 请求到的板块数据
     */
    public void onMenuBoardLoaded(List<MenuDataJson.ClazzJson.BoardJson> boardData);
}
