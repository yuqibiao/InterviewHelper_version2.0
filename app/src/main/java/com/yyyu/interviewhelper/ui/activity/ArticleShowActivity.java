package com.yyyu.interviewhelper.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.animation.FABExpandAnim;
import com.yyyu.interviewhelper.bean.LoginJson;
import com.yyyu.interviewhelper.bean.LoveJson;
import com.yyyu.interviewhelper.net.MyBitmapUtils;
import com.yyyu.interviewhelper.net.MyHttpManager;
import com.yyyu.interviewhelper.net.MyHttpUrl;
import com.yyyu.interviewhelper.ui.widget.FloatingMenuLayout;
import com.yyyu.interviewhelper.ui.widget.MyFloatingActionButton;
import com.yyyu.interviewhelper.utils.CutViewUtils;
import com.yyyu.interviewhelper.utils.FileUtils;
import com.yyyu.interviewhelper.utils.LogicUtils;
import com.yyyu.interviewhelper.utils.MyLog;
import com.yyyu.interviewhelper.utils.MySnackBar;
import com.yyyu.interviewhelper.utils.MyToast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 功能：
 *
 * @author yyyu
 * @date 2016/5/22
 */
public class ArticleShowActivity extends BaseActivity {

    private static final String TAG = "ArticleShowActivity";
    private int articleId;
    private String articleIcon;
    private String articleTitle;

    private WebView wv_article;
    private MyFloatingActionButton fab_expand;
    private View v_master;
    private FloatingMenuLayout fml_article;
    private Toolbar tb_article;
    private CollapsingToolbarLayout ctl_article;
    private ImageView iv_article_icon;
    private TextView tv_article_title;
    private String articleContent;
    private NestedScrollView ns_content;
    private FloatingActionButton fab_love;
    private LoginJson.UserInfo userInfo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_article_show;
    }

    @Override
    protected void beforeInit() {
        articleId = getIntent().getIntExtra("articleId", 0);
        articleIcon = getIntent().getStringExtra("articleIcon");
        articleTitle = getIntent().getStringExtra("articleTitle");
        articleContent = getIntent().getStringExtra("articleContent");
        userInfo = LogicUtils.getUserInfo(this);
    }

    @Override
    protected void initView() {
        fab_expand = getView(R.id.fab_expand);
        v_master = getView(R.id.v_master);
        fml_article = getView(R.id.fml_article);
        ctl_article = getView(R.id.ctl_article);
        tb_article = getView(R.id.tb_article);
        wv_article = getView(R.id.wv_article);
        iv_article_icon = getView(R.id.iv_article_icon);
        tv_article_title = getView(R.id.tv_article_title);
        ns_content = getView(R.id.ns_content);
        fab_love = getView(R.id.fab_love);
        //--set
        ctl_article.setExpandedTitleColor(Color.parseColor("#00FFFFFF"));//设置还没收缩时状态下字体颜色
        ctl_article.setCollapsedTitleTextColor(Color.parseColor("#FFFFFF"));//设置收缩后Toolbar上字体的颜色
        initVebView(wv_article);
    }

    private void initVebView(WebView wv) {
        wv.getSettings().setDefaultTextEncodingName("utf-8");// 避免中文乱码
        wv.setDrawingCacheEnabled(true);
        WebSettings settings = wv.getSettings();
        settings.setTextSize(WebSettings.TextSize.NORMAL);
        settings.setJavaScriptEnabled(false);
        settings.setNeedInitialFocus(false);
        settings.setLoadsImagesAutomatically(true);//自动加载图片
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先使用缓存
    }

    @Override
    protected void initListener() {
        addOnClickListener(R.id.ib_comm, R.id.ib_dz, R.id.ib_share, R.id.tv_save);
        fab_expand.setFloatingMenu(fml_article);
        fab_expand.setMask(v_master);
        fab_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fab_expand.isOpen()) {//判断为打开时关闭
                    FABExpandAnim.closeExpand(v);
                    fml_article.switchMenu();
                    v_master.setVisibility(View.GONE);
                    fab_expand.setOpen(false);
                } else {//判断为关闭时
                    fml_article.switchMenu();
                    FABExpandAnim.openExpand(v);
                    v_master.setVisibility(View.VISIBLE);
                    fab_expand.setOpen(true);
                }
            }
        });

        tb_article.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleShowActivity.this.finish();
            }
        });

        wv_article.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                v_master.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                v_master.setVisibility(View.GONE);
            }
        });
        if (userInfo != null) {
            //--love
            fab_love.setOnClickListener(new View.OnClickListener() {
                final String url = MyHttpUrl.LOVE_OPERATION + "?articleId=" + articleId + "&userId=" + userInfo.userId + "&forWhat=operateLove";

                @Override
                public void onClick(View v) {
                    MyHttpManager.getAsyn(url, new MyHttpManager.ResultCallback<LoveJson>() {
                        @Override
                        public void onError(Request request, Exception e) {
                            MyLog.e("喜爱操作失败===" + e.getMessage());
                        }

                        @Override
                        public void onResponse(LoveJson loveJson) {
                            if (loveJson.success) {
                                fab_love.setImageResource(loveJson.add ? R.mipmap.icon_love : R.mipmap.icon_unlove);
                                MyToast.showShort(ArticleShowActivity.this, loveJson.message);
                            }
                        }
                    });
                }
            });
        } else {
            MyToast.showLong(this, "请先登录！！");
        }


    }

    @Override
    protected void initData() {
        //--header
        ctl_article.setTitle(articleTitle);
        tv_article_title.setText(articleTitle);
        MyBitmapUtils.getInstance(this).display(iv_article_icon, MyHttpUrl.IP_PORT + articleIcon);
        //--content
        String newHtmlContent =
                "<html>"+
                "<head>"+
                "<style type=\"text/css\">" +
                "body{" +
                "font-size:11pt;font-family: Helvetica, 'Hiragino Sans GB', 'Microsoft Yahei', '微软雅黑', Arial, sans-serif;overflow:hidden;padding-left:1%;padding-right:1%" +
                        "}"+
                "</style>" +
                        "</head>"+
                imgTofillDevice(articleContent).replace("<html>" , "").replace("</html>" , "").replace("<head>"  , "").replace("</head>" , "")
                +"</html>";
        wv_article.loadDataWithBaseURL(MyHttpUrl.URL_ADDRESS,
                newHtmlContent, "text/html", "utf-8", null);
       /* String newHtmlContent =""+"<link rel=\"stylesheet\" type=\"text/css\" href=\"newspage.css\"/>"
                +imgTofillDevice(articleContent) ;
        wv_article.loadDataWithBaseURL("file:///android_asset/",
                newHtmlContent, "text/html", "utf-8", null);*/
        //--love
        if (userInfo != null) {
            String url = MyHttpUrl.LOVE_OPERATION + "?articleId=" + articleId + "&userId=" + userInfo.userId + "&forWhat=isLove";
            MyHttpManager.getAsyn(url, new MyHttpManager.ResultCallback<LoveJson>() {
                @Override
                public void onError(Request request, Exception e) {
                    MyLog.e("请求喜爱状态失败===" + e.getMessage());
                }

                @Override
                public void onResponse(LoveJson loveJson) {
                    if (loveJson.success) {
                        fab_love.setImageResource(loveJson.add ? R.mipmap.icon_love : R.mipmap.icon_unlove);
                    }
                }
            });
        }
    }

    /*
     * webView图片自适应屏幕（android4.2）
	 */
    public String imgTofillDevice(String result) {
        Document doc_Dis = Jsoup.parse(result);
        Elements ele_Img = doc_Dis.getElementsByTag("img");
        if (ele_Img.size() != 0) {
            for (Element e_Img : ele_Img) {
                e_Img.attr("width", "100%");
                e_Img.attr("height", "auto");
            }
        }
        return (doc_Dis.toString()).trim();
    }

    /**
     * 启动该界面的方法(这种写法太赞了！！！)
     */
    public static void startAction(Context ctx, int articleId, String articleIcon, String articleTitle, String articleContent) {
        Intent intent = new Intent(ctx, ArticleShowActivity.class);
        intent.putExtra("articleId", articleId);
        intent.putExtra("articleIcon", articleIcon);
        intent.putExtra("articleTitle", articleTitle);
        intent.putExtra("articleContent", articleContent);
        ctx.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_comm: {
                fml_article.switchMenu();
                v_master.setVisibility(View.GONE);
                fab_expand.setOpen(false);
                CommentActivity.startAction(this, articleId);
                break;
            }
            case R.id.ib_dz: {
                fml_article.switchMenu();
                v_master.setVisibility(View.GONE);
                fab_expand.setOpen(false);
                Snackbar.make(fml_article, "点击了点赞", Snackbar.LENGTH_LONG).show();
                break;
            }
            case R.id.ib_share: {
                fml_article.switchMenu();
                v_master.setVisibility(View.GONE);
                fab_expand.setOpen(false);
                String url = MyHttpUrl.GET_ARTICLE_BY_ID + "?articleId=" + articleId;
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle("yyyu");
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                oks.setTitleUrl(url);
                // text是分享文本，所有平台都需要这个字段
                oks.setText("" + articleTitle);
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                oks.setImagePath(MyHttpUrl.IP_PORT + articleIcon);
                // url仅在微信（包括好友和朋友圈）中使用
                oks.setUrl(url);
                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                //oks.setComment("我是测试评论文本");
                // site是分享此内容的网站名称，仅在QQ空间使用
                oks.setSite(getString(R.string.app_name));
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                oks.setSiteUrl(url);
                // 启动分享GUI
                oks.show(this);
                break;
            }
            case R.id.tv_save: {
                final String imgName = articleId + ".jpg";
                if (FileUtils.isFileExists(imgName)) {
                    MySnackBar.showLongDef(ns_content, "该文章已经被保存过啦").setAction("任性再保存", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FileUtils.saveBitmap(ArticleShowActivity.this, CutViewUtils.getBitmapByView(ns_content), imgName);
                            MediaScannerConnection.scanFile(ArticleShowActivity.this, new String[]{FileUtils.savePath + imgName}, null, null);
                            MySnackBar.showLongDef(ns_content, "文章已保存为图片，去相册查看");
                        }
                    });
                    break;
                }
                FileUtils.saveBitmap(this, CutViewUtils.getBitmapByView(ns_content), imgName);
                MediaScannerConnection.scanFile(this, new String[]{FileUtils.savePath + imgName}, null, null);
                MySnackBar.showLongDef(ns_content, "文章已保存为图片，去相册查看");
                break;
            }
        }
    }
}
