package com.android.base.component.intent;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.android.base.component.application.AppContext;
import com.android.base.file.FileUtils;
import com.android.base.other.ConvertUtils;

import java.io.File;

/**
 * Created by gg on 2017/3/13.
 * 意图管理
 */
public class IntentUtils {

    /**
     * 拍照 ,不加保存路径，图片会被压缩
     * (Permission)
     */
    public static Intent getCamera(File cameraFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        if (cameraFile == null) return intent;
        Uri uri = ConvertUtils.File2URI(cameraFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    /**
     * 相册 ,可以自定义保存路径
     */
    public static Intent getPicture() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            if (intent.resolveActivity(AppContext.get().getPackageManager()) == null) {
                intent.setAction(Intent.ACTION_GET_CONTENT);
            }
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        intent.setType("image/*");
        return intent;
    }

    /**
     * 裁剪(通用) 1.启动拍照/相册 2.在onActivityForResult里调用此方法，启动裁剪功能
     */
    public static Intent getCrop(File from, File save) {
        return getCrop(from, save, 0, 0, 300, 300);
    }

    public static Intent getCrop(File from, File save, int aspectX, int aspectY,
                                 int outputX, int outputY) {
        if (FileUtils.isFileEmpty(from)) { // 源文件不存在
            FileUtils.deleteFile(from);
            FileUtils.deleteFile(save);
            return null;
        }
        Uri uri1 = ConvertUtils.File2URI(from);
        Uri uri2 = ConvertUtils.File2URI(save);
        return getCrop(uri1, uri2, aspectX, aspectY, outputX, outputY);
    }

    public static Intent getCrop(Uri from, Uri save, int aspectX, int aspectY,
                                 int outputX, int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(from, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框比例
        if (aspectX != 0 && aspectY != 0) {
            intent.putExtra("aspectX", aspectX);
            intent.putExtra("aspectY", aspectY);
        }
        // 输出图片大小(太大会传输失败)
        if (outputX != 0 && outputY != 0) {
            intent.putExtra("outputX", outputX);
            intent.putExtra("outputY", outputY);
        }
        // 裁剪选项
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        // 数据返回
        intent.putExtra("return-data", false); // 不从intent里面拿
        intent.putExtra(MediaStore.EXTRA_OUTPUT, save);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }

    /**
     * 获取打开当前App的意图
     */
    public static Intent getApp(String appPackageManager) {
        return AppContext.getPackageManager().getLaunchIntentForPackage(appPackageManager);
    }

    /**
     * 回到Home
     */
    public static Intent getHome() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return intent;
    }

    /**
     * 获取安装App的意图
     */
    public static Intent getInstall(File file) {
        if (file == null) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            type = "application/vnd.android.package-archive";
        } else {
            type = MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(FileUtils.getFileExtension(file));
        }
        return intent.setDataAndType(Uri.fromFile(file), type);
    }

    /**
     * 获取卸载App的意图
     */
    public static Intent getUninstall(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取分享意图
     */
    public static Intent getShare(String content, File image) {
        if (!FileUtils.isFileExists(image)) return null;
        return getShare(content, Uri.fromFile(image));
    }

    public static Intent getShare(String content, Uri uri) {
        if (uri == null) return getShare(content);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/*");
        return intent;
    }

    public static Intent getShare(String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content); // 设置分享信息
        return intent;
    }

    /**
     * 跳至填充好phoneNumber的拨号界面
     */
    public static Intent getDial(String phoneNumber) {
        return new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
    }

    /**
     * 直接拨打phoneNumber
     * (Permission)
     */
    public static Intent getCall(String phoneNumber) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        return intent;
    }

    /**
     * 短信发送界面
     */
    public static Intent getSMS(String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + (TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", TextUtils.isEmpty(content) ? "" : content);
        return intent;
    }

    /**
     * 彩信发送界面
     */
    public static Intent getSMS(String phoneNumber, String content, File img) {
        Intent intent = getSMS(phoneNumber, content);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(img));
        intent.setType("image/png");
        return intent;
    }

    /**
     * 打开手机联系人界面点击联系人后便获取该号码
     * 启动方式 startActivityForResult
     * 获取返回值需要在ContactUtils里有方法获取
     */
    public static Intent getContacts() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        return intent;
    }

    /**
     * 跳转应用市场的意图
     */
    public static Intent getMarket() {
        String str = "market://details?id=" + AppContext.get().getPackageName();
        return new Intent("android.intent.action.VIEW", Uri.parse(str));
    }

    /**
     * 获取打开浏览器的意图
     */
    public static Intent getWebBrowse(String url) {
        Uri address = Uri.parse(url);
        return new Intent(Intent.ACTION_VIEW, address);
    }

    /**
     * 打开网络设置界面
     */
    public static Intent getNetSettings() {
        return new Intent(Settings.ACTION_SETTINGS);
    }

    /**
     * 获取App系统设置
     */
    public static Intent getSetings(String packageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        return intent.setData(Uri.parse("package:" + packageName));
    }

    /**
     * 打开Gps设置界面
     */
    public static Intent getGps() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    /**
     * activity跳转intent
     *
     * @param packageName app包名
     * @param className   Activity全名
     * @param bundle      可为null
     * @return 直接startActivity即可
     */
    private static Intent getComponent(String packageName, String className, Bundle bundle) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ComponentName cn = new ComponentName(packageName, className);
        return intent.setComponent(cn);
    }

}
