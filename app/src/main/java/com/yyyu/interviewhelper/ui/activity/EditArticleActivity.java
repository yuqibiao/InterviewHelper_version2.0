package com.yyyu.interviewhelper.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yyyu.interviewhelper.MyApplication;
import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.adapter.EditPopAdapter;
import com.yyyu.interviewhelper.bean.ImgUploadJson;
import com.yyyu.interviewhelper.bean.LoginJson;
import com.yyyu.interviewhelper.bean.MenuDataJson;
import com.yyyu.interviewhelper.callback.OnRvItemClickListener;
import com.yyyu.interviewhelper.net.MyHttpManager;
import com.yyyu.interviewhelper.net.MyHttpUrl;
import com.yyyu.interviewhelper.ui.widget.CustomProressBar;
import com.yyyu.interviewhelper.ui.widget.PicChoiceWithPop;
import com.yyyu.interviewhelper.ui.widget.dialog.LoadingDialog;
import com.yyyu.interviewhelper.ui.widget.rich_edit.RichTextEditor;
import com.yyyu.interviewhelper.utils.FileUtils;
import com.yyyu.interviewhelper.utils.LogicUtils;
import com.yyyu.interviewhelper.utils.MyLog;
import com.yyyu.interviewhelper.utils.MyToast;
import com.yyyu.interviewhelper.utils.PicChoiceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 功能：文章编辑Activity
 *
 * @author yyyu
 * @date 2016/5/31
 */
public class EditArticleActivity extends BaseActivity {

    private static final String TAG = "EditArticleActivity";

    private Toolbar tb_edit_article;
    private RichTextEditor re_article;
    private LinearLayout ll_btm;
    private TextView tv_clazz_choice, tv_board_choice;
    private EditText et_title;
    private LinearLayout rl_root;
    private PicChoiceWithPop picChoiceWithPop;
    private ImageButton ib_add_article_icon;
    private File file_article_icon;
    private EditPopAdapter<MenuDataJson.ClazzJson> clazzAdapter;
    private EditPopAdapter<MenuDataJson.ClazzJson.BoardJson> boardAdapter;
    private PopupWindow pop;

    private List<MenuDataJson.ClazzJson.BoardJson> boards;
    private String boardId;
    private String userId;
    private ImageButton ib_send;
    private CustomProressBar pb_edit;

    @Override
    protected void beforeInit() {
        LoginJson.UserInfo userInfo = LogicUtils.getUserInfo(this);
        userId = "" + userInfo.userId;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_article;
    }

    @Override
    protected void initView() {
        rl_root = getView(R.id.rl_root);
        tb_edit_article = getView(R.id.tb_edit_article);
        re_article = getView(R.id.re_article);
        ll_btm = getView(R.id.ll_btm);
        tv_clazz_choice = getView(R.id.tv_clazz_choice);
        tv_board_choice = getView(R.id.tv_board_choice);
        et_title = getView(R.id.et_title);
        ib_add_article_icon = getView(R.id.ib_add_article_icon);
        pb_edit = getView(R.id.pb_edit);
        ib_send = getView(R.id.ib_send);
        //----set
        picChoiceWithPop = new PicChoiceWithPop(this, rl_root);
        setSupportActionBar(tb_edit_article);
    }

    @Override
    protected void initListener() {
        addOnClickListener(R.id.ib_send, R.id.ib_gallery, R.id.ib_camera,
                R.id.tv_clazz_choice, R.id.tv_board_choice, R.id.ib_add_article_icon);
        tb_edit_article.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditArticleActivity.this.finish();
            }
        });


    }

    /**
     * 显示分类、板块选择的pop
     */
    private void showPopAbove(Context context, View target, EditPopAdapter adapter) {
        //---显示Pop
        View contentView = LayoutInflater.from(context)
                .inflate(R.layout.pop_edit, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        RecyclerView rv_clazz_choice = (RecyclerView) contentView
                .findViewById(R.id.rv_clazz_choice);
        rv_clazz_choice.setLayoutManager(layoutManager);
        rv_clazz_choice.setAdapter(adapter);
        pop = new PopupWindow(contentView,
                getResources().getDimensionPixelSize(R.dimen.pop_width),
                getResources().getDimensionPixelSize(R.dimen.pop_height),
                true);
        pop.setTouchable(true);
        pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_bg));
        int[] location = new int[2];
        target.getLocationOnScreen(location);
        pop.showAtLocation(target, Gravity.LEFT | Gravity.TOP,
                location[0] - pop.getWidth() + target.getWidth() / 2,
                location[1] - pop.getHeight() - getResources().getDimensionPixelSize(R.dimen.pop_margin_btm));
    }

    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, EditArticleActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_add_article_icon: {
                picChoiceWithPop.showPop();
                picChoiceWithPop.setPopAction("add_article_icon");
                break;
            }
            case R.id.ib_gallery: {
                PicChoiceUtils.toGallery(this);
                break;
            }
            case R.id.ib_camera: {
                PicChoiceUtils.toCamera(this);
                break;
            }
            case R.id.tv_clazz_choice: {
                //--请求网络
                MyHttpManager.getAsyn(MyHttpUrl.GET_MENU_DATA, new MyHttpManager.ResultCallback<MenuDataJson>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        MyLog.e(TAG, "网络请求错误：" + e.getMessage());
                    }

                    @Override
                    public void onResponse(final MenuDataJson menuDataJson) {
                        clazzAdapter = new EditPopAdapter<MenuDataJson.ClazzJson>
                                (EditArticleActivity.this, menuDataJson.menuData);
                        showPopAbove(EditArticleActivity.this, tv_clazz_choice, clazzAdapter);
                        clazzAdapter.setOnRvClickListener(new OnRvItemClickListener() {
                            @Override
                            public void OnItemClick(int pos) {
                                boards = menuDataJson.menuData.get(pos).boards;
                                boardAdapter = new EditPopAdapter<MenuDataJson.ClazzJson.BoardJson>
                                        (EditArticleActivity.this, boards);
                                pop.dismiss();
                            }
                        });
                    }
                });
                break;
            }
            case R.id.tv_board_choice: {
                if (boardAdapter == null) {
                    MyToast.showLong(this, "请先选择分类！！");
                    break;
                }
                showPopAbove(this, tv_board_choice, boardAdapter);
                boardAdapter.setOnRvClickListener(new OnRvItemClickListener() {
                    @Override
                    public void OnItemClick(int pos) {
                        boardId = "" + boards.get(pos).boardId;
                        pop.dismiss();
                    }
                });
                break;
            }
            case R.id.ib_send: {
                final LoadingDialog dialog = new LoadingDialog(this);
                dialog.setTips("提交数据中");
                dialog.show();
                String article_title = et_title.getText().toString();
                String article_content = re_article.getContent();
                if (TextUtils.isEmpty(article_title)) {
                    MyToast.showShort(this, "请添加文章标题！");
                    break;
                } else if (TextUtils.isEmpty(article_content)) {
                    MyToast.showShort(this, "请添加文章内容！");
                    break;
                } else if (file_article_icon == null) {
                    MyToast.showShort(this, "请为文章添加一个图标！");
                    break;
                } else if (TextUtils.isEmpty(boardId)) {
                    MyToast.showShort(this, "请为文章添加板块信息！");
                    break;
                }
                //---添加文章
                try {
                    MyHttpManager.postAsynUpload(MyHttpUrl.ADD_ARTICLE, new MyHttpManager.ResultCallback<String>() {
                                @Override
                                public void onError(Request request, Exception e) {
                                    MyLog.e("====" + e.getMessage());
                                    dialog.dismiss();
                                }

                                @Override
                                public void onResponse(String response) {
                                    EditArticleActivity.this.finish();
                                    dialog.dismiss();
                                }
                            }, new File[]{file_article_icon}, new String[]{"article_icon"},
                            new MyHttpManager.Param("userId", "1"),
                            new MyHttpManager.Param("boardId", boardId),
                            new MyHttpManager.Param("article_title", article_title),
                            new MyHttpManager.Param("article_content", article_content));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void uploadArticleImg(final String imgPath) {
        pb_edit.setVisibility(View.VISIBLE);
        ib_send.setVisibility(View.GONE);
        try {
            MyHttpManager.postAsynUpload(MyHttpUrl.UPLOAD_IMG, new MyHttpManager.ResultCallback<ImgUploadJson>() {
                @Override
                public void onError(Request request, Exception e) {
                    pb_edit.setVisibility(View.GONE);
                    ib_send.setVisibility(View.VISIBLE);
                    MyToast.showShort(EditArticleActivity.this, "图片上传失败！！！");
                }

                @Override
                public void onResponse(ImgUploadJson imgUploadJson) {
                    re_article.insertImage(imgPath, imgUploadJson.savePath);
                    pb_edit.setVisibility(View.GONE);
                    ib_send.setVisibility(View.VISIBLE);
                }
            }, new File[]{new File(imgPath)}, new String[]{"img"}, new MyHttpManager.Param[]{});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PicChoiceUtils.PHOTO_REQUEST_GALLERY: {
                if (RESULT_OK == resultCode) {
                    Uri uri = data.getData();
                    if ("add_article_icon".equals(picChoiceWithPop.getPopAction())) {
                        PicChoiceUtils.crop(this, uri);
                    } else {
                        String imgPath = FileUtils.getImagePathFromUri(this, uri);
                        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                        String savePath = FileUtils.saveBitmap(this, bitmap, "edit_temp.jpg");
                        uploadArticleImg(savePath);
                    }
                }
                break;
            }
            case PicChoiceUtils.PHOTO_REQUEST_CAMERA: {
                if (RESULT_OK == resultCode) {
                    File iconFile = new File(((MyApplication) getApplication()).filePath, PicChoiceUtils.PHOTO_FILE_NAME);
                    if ("add_article_icon".equals(picChoiceWithPop.getPopAction())) {
                        PicChoiceUtils.crop(this, Uri.fromFile(iconFile));
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeFile(iconFile.getPath());
                        String savePath = FileUtils.saveBitmap(this, bitmap, "edit_temp.jpg");
                        uploadArticleImg(savePath);
                    }
                }
                break;
            }
            case PicChoiceUtils.PHOTO_REQUEST_CUT: {
                if (RESULT_OK != resultCode) break;
                Bitmap bitmap = data.getParcelableExtra("data");
                if ("add_article_icon".equals(picChoiceWithPop.getPopAction())) {//--添加article icon
                    FileUtils.saveBitmap(this, bitmap, "temp.jpg");
                    ib_add_article_icon.setImageBitmap(bitmap);
                    file_article_icon = FileUtils.getFIleByName("temp.jpg");
                    //---一定要设置为空
                    picChoiceWithPop.setPopAction("");
                }
                break;
            }
        }
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            MyToast.showShort(this, "再按一次退出");
            exitTime = System.currentTimeMillis();
        } else {
            this.finish();
        }
    }
}
