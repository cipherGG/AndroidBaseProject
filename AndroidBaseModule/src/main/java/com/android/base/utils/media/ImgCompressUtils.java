package com.android.base.utils.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by gg on 2017/4/11.
 * 图片压缩工具类
 */
public class ImgCompressUtils {

    /**
     * 鲁班算法压缩
     */
    public static void compress(Context context, File src) {
        Luban.get(context)
                .load(src) // 压缩源文件
                .putGear(Luban.THIRD_GEAR) // 设定压缩档次，默认三挡
                .setCompressListener(new OnCompressListener() {

                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过去出现问题时调用
                    }
                })
                .launch(); // 启动压缩

    }

    /**
     * 获取bitmap(带压缩) 真正的压缩到200KB左右（建议200KB）
     */
    public static Bitmap compressFile(File file, long maxByteSize) {
        long length = file.length(); // 真正的文件大小
        if (length > maxByteSize) { // 这么算比较准
            int ratio = (int) (length / maxByteSize);
            int sample;
            if (ratio > 4) {
                sample = ratio / 2 + 1;
            } else {
                sample = 4;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            options.inSampleSize = sample;
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        } else {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
    }

    /**
     * 按缩放压缩(放大有锯齿)
     *
     * @param src       源图片
     * @param newWidth  新宽度
     * @param newHeight 新高度
     * @param recycle   是否回收
     * @return 缩放压缩后的图片
     */

    public static Bitmap compressByScale(Bitmap src, int newWidth,
                                         int newHeight, boolean recycle) {
        return ImgUtils.scale(src, newWidth, newHeight, recycle);
    }

    /**
     * 按缩放压缩(放大有锯齿)
     *
     * @param src         源图片
     * @param scaleWidth  缩放宽度倍数
     * @param scaleHeight 缩放高度倍数
     * @param recycle     是否回收
     * @return 缩放压缩后的图片
     */
    public static Bitmap compressByScale(Bitmap src, float scaleWidth,
                                         float scaleHeight, boolean recycle) {
        return ImgUtils.scale(src, scaleWidth, scaleHeight, recycle);
    }

    /**
     * 按质量压缩
     *
     * @param src     源图片
     * @param quality 质量
     * @param recycle 是否回收
     * @return 质量压缩后的图片
     */
    public static Bitmap compressByQuality(Bitmap src, int quality, boolean recycle) {
        if (ImgUtils.isEmptyBitmap(src) || quality < 0 || quality > 100) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(ImgConvertUtils.FORMAT, quality, baos);
        byte[] bytes = baos.toByteArray();
        if (recycle && !src.isRecycled()) src.recycle();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 按采样大小压缩
     *
     * @param src        源图片
     * @param sampleSize 采样率大小
     * @param recycle    是否回收
     * @return 按采样率压缩后的图片
     */
    public static Bitmap compressBySampleSize(Bitmap src, int sampleSize, boolean recycle) {
        if (ImgUtils.isEmptyBitmap(src)) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(ImgConvertUtils.FORMAT, 100, baos);
        byte[] bytes = baos.toByteArray();
        if (recycle && !src.isRecycled()) src.recycle();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

}
