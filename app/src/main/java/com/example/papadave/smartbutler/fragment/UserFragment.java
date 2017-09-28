package com.example.papadave.smartbutler.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import com.example.papadave.smartbutler.R;
import com.example.papadave.smartbutler.entity.MyUser;
import com.example.papadave.smartbutler.ui.LoginActivity;
import com.example.papadave.smartbutler.utils.L;
import com.example.papadave.smartbutler.utils.ShareUtils;
import com.example.papadave.smartbutler.utils.UtilTools;
import com.example.papadave.smartbutler.view.CustomDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment implements View.OnClickListener {

    //头像
    private CircleImageView profile_image;
    private CustomDialog dialog;
    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;


    private Button btn_update_ok;
    private Button btn_exit_user;
    private TextView edit_user;
    private EditText et_username;
    private EditText et_sex;
    private EditText et_age;
    private EditText et_desc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_user, container, false);
        View view = inflater.inflate(R.layout.fragment_user, null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        btn_exit_user = view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);
        edit_user = view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);

        et_username = view.findViewById(R.id.et_username);
        et_sex = view.findViewById(R.id.et_sex);
        et_age = view.findViewById(R.id.et_age);
        et_desc = view.findViewById(R.id.et_desc);

        btn_update_ok = view.findViewById(R.id.btn_update_ok);
        btn_update_ok.setOnClickListener(this);

        profile_image = view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        //初始化dialog

        dialog = new CustomDialog(getActivity(), 500, 500,
                R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0);
        //提示框以外点击无效
        dialog.setCancelable(false);

        btn_camera = dialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_picture = dialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);

        UtilTools.getImageToShare(getActivity(),profile_image);


        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);


        //设置为不可点击
        setEnabled(false);

        //设置具体的值
        try {
            MyUser userinfo = BmobUser.getCurrentUser(MyUser.class);
            et_username.setText(userinfo.getUsername() + "");
            et_sex.setText(userinfo.isSex() ? "男" : "女");
            et_age.setText(userinfo.getAge() + "");
            et_desc.setText(userinfo.getDesc());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void setEnabled(boolean is) {
        et_username.setEnabled(is);
        et_sex.setEnabled(is);
        et_age.setEnabled(is);
        et_desc.setEnabled(is);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_exit_user:
                //清除缓存用户对象
                MyUser.logOut();
                // 现在的currentUser是null了
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;

            case R.id.edit_user:
                setEnabled(true);
                btn_update_ok.setVisibility(view.VISIBLE);
                break;

            case R.id.btn_update_ok:
                String username = et_username.getText().toString();
                String age = et_age.getText().toString();
                String sex = et_sex.getText().toString();
                String desc = et_desc.getText().toString();

                if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(age) & !TextUtils.isEmpty(sex) & !TextUtils.isEmpty(desc)) {
                    MyUser user = new MyUser();
                    user.setUsername(username);
                    user.setAge(Integer.parseInt(age));
                    if (sex.equals("男")) {
                        user.setSex(true);
                    } else {
                        user.setSex(false);
                    }
                    if (!TextUtils.isEmpty(desc)) {
                        user.setDesc(desc);
                    } else {
                        user.setDesc("这个人很懒，什么也没留下");
                    }

                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //修改成功
                                setEnabled(false);
                                btn_update_ok.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "修改成功",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getActivity(), "修改失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "输入框不能为空",
                            Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.profile_image:
                dialog.show();
                break;

            case R.id.btn_camera:
                toCamera();
                break;

            case R.id.btn_picture:
                toPicture();
                break;

            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
    }

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_RESULT_CODE = 102;

    private File tempfile;

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(Environment.getExternalStorageDirectory()));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //相册数据
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;

                case CAMERA_REQUEST_CODE:
                    tempfile = new File(Environment.getExternalStorageDirectory(),
                            PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempfile));
                    break;

                case RESULT_RESULT_CODE:
                    if (data!=null){
                        setImageToView(data);
                        if (tempfile!=null){
                            tempfile.delete();
                        }
                    }break;
            }
        }
    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.e("uri==null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data",true);
        startActivityForResult(intent,RESULT_RESULT_CODE);

    }
    private void setImageToView(Intent data){
       Bundle bundle = data.getExtras();
       if (bundle!=null){
          Bitmap bitmap = bundle.getParcelable("data");
          profile_image.setImageBitmap(bitmap);
       }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UtilTools.putImageToShare(getActivity(),profile_image);}
}
