package com.yyyu.interviewhelper.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yyyu.interviewhelper.MyApplication;
import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.bean.LoginJson;
import com.yyyu.interviewhelper.bean.ModifyUserInfoJson;
import com.yyyu.interviewhelper.callback.OnModifyUserInfoListener;
import com.yyyu.interviewhelper.net.MyBitmapUtils;
import com.yyyu.interviewhelper.net.MyHttpManager;
import com.yyyu.interviewhelper.net.MyHttpUrl;
import com.yyyu.interviewhelper.ui.widget.PicChoiceWithPop;
import com.yyyu.interviewhelper.ui.widget.dialog.UserInfoInputDialog;
import com.yyyu.interviewhelper.ui.widget.dialog.UserSexModifyDialog;
import com.yyyu.interviewhelper.utils.FileUtils;
import com.yyyu.interviewhelper.utils.LogicUtils;
import com.yyyu.interviewhelper.utils.MyLog;
import com.yyyu.interviewhelper.utils.MySPUtils;
import com.yyyu.interviewhelper.utils.MyToast;
import com.yyyu.interviewhelper.utils.PicChoiceUtils;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 功能：修改用户资料activity
 *
 * @author yyyu
 * @date 2016/5/29
 */
public class ModifyUserInfoActivity extends BaseActivity {

    public static final String MODIFY_USERINRO_ACTION =
            "com.yyyu.interviewHelper.MODIFY_USERINRO_ACTION";

    private Toolbar tb_modify_user;
    private CircleImageView civ_icon;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_intro;
    private LinearLayout ll_modify_root;
    private PicChoiceWithPop picPop;
    private TextView tv_modify_finished;
    private LoginJson.UserInfo userInfo;
    private String userId, username, userSex, userIntro;
    private UserInfoInputDialog nameDialog;
    private UserInfoInputDialog introDialog;
    private UserSexModifyDialog userSexModifyDialog;
    private File[] imgs;
    private String[] paths;
    private File icon_temp;
    private File bg_temp;

    @Override
    protected void beforeInit() {
        userInfo = LogicUtils.getUserInfo(this);
        userId = "" + userInfo.userId;
        username = userInfo.username;
        userSex = userInfo.userSex;
        userIntro = userInfo.userIntro;
        if (TextUtils.isEmpty(username)) {
            username = "还没有用户名";
        }
        if (TextUtils.isEmpty(userSex)) {
            userSex = "男";
        }
        if (TextUtils.isEmpty(userIntro)) {
            userIntro = "快完善你的个性签名吧";
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_userinfo;
    }

    @Override
    protected void initView() {
        ll_modify_root = getView(R.id.ll_modify_root);
        tb_modify_user = getView(R.id.tb_modify_user);
        civ_icon = getView(R.id.civ_icon);
        tv_name = getView(R.id.tv_name);
        tv_sex = getView(R.id.tv_sex);
        tv_intro = getView(R.id.tv_modify_intro);
        tv_modify_finished = getView(R.id.tv_modify_finished);
        //---set
        MyBitmapUtils.getInstance(this).display(civ_icon, MyHttpUrl.IP_PORT + userInfo.userIcon);
        tv_name.setText(username);
        tv_intro.setText(userIntro);
        tv_sex.setText(userSex);
        picPop = new PicChoiceWithPop(this, ll_modify_root);
        setSupportActionBar(tb_modify_user);
    }

    @Override
    protected void initListener() {
        addOnClickListener(R.id.rl_modify_icon, R.id.rl_modify_bg,
                R.id.rl_modify_name, R.id.rl_modify_sex, R.id.rl_modify_intro,
                R.id.tv_modify_finished);
        tb_modify_user.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyUserInfoActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_modify_icon: {
                //选择图片
                picPop.setPopAction("to_set_icon");
                picPop.showPop();
                break;
            }
            case R.id.rl_modify_bg: {
                picPop.setPopAction("to_set_bg");
                picPop.showPop();
                break;
            }
            case R.id.rl_modify_name: {
                nameDialog = new UserInfoInputDialog(this);
                nameDialog.show();
                nameDialog.setOnModifyUserInfoListener(new OnModifyUserInfoListener() {
                    @Override
                    public void onSureModify(String inputStr) {
                        if (inputStr.length() > 50) {
                            MyToast.showShort(ModifyUserInfoActivity.this, "名字在50个字符以内！！");
                            return;
                        }
                        username = inputStr;
                        tv_name.setText(username);
                        nameDialog.dismiss();
                    }
                });
                break;
            }
            case R.id.rl_modify_sex: {
                userSexModifyDialog = new UserSexModifyDialog(this);
                userSexModifyDialog.show();
                userSexModifyDialog.setChecked(userSex);
                userSexModifyDialog.setOnModifyUserInfoListener(new OnModifyUserInfoListener() {
                    @Override
                    public void onSureModify(String inputStr) {
                        userSex = inputStr;
                        tv_sex.setText(userSex);
                    }
                });
                break;
            }
            case R.id.rl_modify_intro: {
                introDialog = new UserInfoInputDialog(this);
                introDialog.show();
                introDialog.setOnModifyUserInfoListener(new OnModifyUserInfoListener() {
                    @Override
                    public void onSureModify(String inputStr) {
                        if (inputStr.length() > 200) {
                            MyToast.showShort(ModifyUserInfoActivity.this, "个性签名在200个字符以内！！");
                            return;
                        }
                        userIntro = inputStr;
                        tv_intro.setText(userIntro);
                        introDialog.dismiss();
                    }
                });
                break;
            }
            case R.id.tv_modify_finished: {
                //---提交
                icon_temp = FileUtils.getFIleByName("icon_temp.jpg");
                bg_temp = FileUtils.getFIleByName("bg_temp.jpg");
                if (icon_temp == null && bg_temp != null) {
                    imgs = new File[]{bg_temp};
                    paths = new String[]{"userInfoBg"};
                } else if (icon_temp != null && bg_temp == null) {
                    imgs = new File[]{icon_temp};
                    paths = new String[]{"userIcon"};
                } else if ((icon_temp == null && bg_temp == null)) {
                    imgs = null;
                    paths = null;
                } else if ((icon_temp != null && bg_temp != null)) {
                    imgs = new File[]{icon_temp, bg_temp};
                    paths = new String[]{"userIcon", "userInfoBg"};
                }
                //发送网络请求
                try {
                    MyHttpManager.postAsynUpload(MyHttpUrl.MODIFY_USER_INFO, new MyHttpManager.ResultCallback<LoginJson>() {
                                @Override
                                public void onError(Request request, Exception e) {
                                    MyLog.e("pic upload=========" + e.getMessage());
                                }
                                @Override
                                public void onResponse(LoginJson loginJson) {
                                    if (loginJson.success) {
                                        if (icon_temp!=null){
                                            icon_temp.delete();
                                        }
                                        if (bg_temp!=null){
                                            bg_temp.delete();
                                        }
                                        //--发送修改成功的广播
                                        MySPUtils.put(ModifyUserInfoActivity.this,
                                                LoginActivity.LOGIN_JSON,
                                                new Gson().toJson(loginJson));
                                        Intent intent = new Intent();
                                        intent.setAction(MODIFY_USERINRO_ACTION);
                                        ModifyUserInfoActivity.this.sendBroadcast(intent);
                                        ModifyUserInfoActivity.this.finish();
                                    }
                                    MyToast.showLong(ModifyUserInfoActivity.this, loginJson.msg);
                                }
                            }, imgs, paths,
                            new MyHttpManager.Param("userId", userId),
                            new MyHttpManager.Param("username", username),
                            new MyHttpManager.Param("userSex", userSex),
                            new MyHttpManager.Param("userIntro", userIntro));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PicChoiceUtils.PHOTO_REQUEST_GALLERY: {//相册
                if (resultCode == RESULT_OK && data != null) {
                    Uri uri = data.getData();
                    if ("to_set_icon".equals(picPop.getPopAction())) {
                        //---上传头像
                        PicChoiceUtils.crop(this, uri);
                    } else if ("to_set_bg".equals(picPop.getPopAction())) {
                        //---上传背景
                        String imgPath = FileUtils.getImagePathFromUri(this, uri);
                        Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                        FileUtils.saveBitmap(this, bitmap, "bg_temp.jpg");
                    }
                }
                break;
            }
            case PicChoiceUtils.PHOTO_REQUEST_CAMERA: {//相机
                if (RESULT_OK != resultCode) break;
                File iconFile = new File(((MyApplication) getApplication()).filePath, PicChoiceUtils.PHOTO_FILE_NAME);
                if ("to_set_icon".equals(picPop.getPopAction())) {
                    //---上传头像
                    PicChoiceUtils.crop(this, Uri.fromFile(iconFile));
                } else if ("to_set_bg".equals(picPop.getPopAction())) {
                    //---上传背景
                    Bitmap bitmap = BitmapFactory.decodeFile(iconFile.getPath());
                    FileUtils.saveBitmap(this, bitmap, "bg_temp.jpg");
                }
                break;
            }
            case PicChoiceUtils.PHOTO_REQUEST_CUT: {//图片剪切
                if (RESULT_OK != resultCode) break;
                Bitmap bitmap = data.getParcelableExtra("data");
                if ("to_set_icon".equals(picPop.getPopAction())) {
                    //---上传头像
                    FileUtils.saveBitmap(this, bitmap, "icon_temp.jpg");
                    civ_icon.setImageBitmap(bitmap);
                }
                break;
            }
        }
    }

}
