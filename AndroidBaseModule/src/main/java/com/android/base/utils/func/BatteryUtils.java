package com.android.base.utils.func;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * Created by gg on 2017/4/3.
 * 电量管理类
 */
public class BatteryUtils {
//
//    public static final int FALSE = -1; // 无数据返回
//    private static BatteryListener mListener;
//    private static BatteryReceiver receiver;
//
//    /**
//     * 广播,监听电量变化
//     */
//    public static class BatteryReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (mListener == null)
//                return;
//
//            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, FALSE);
//            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, FALSE);
//            int percent = (level * 100) / scale;
//            mListener.percent(percent);
//
//            int voltage = intent.getIntExtra(BatteryManager.EXTRA_STATUS, FALSE);
//            if (voltage == BatteryManager.BATTERY_STATUS_CHARGING) {
//                mListener.up();
//
//            } else if (voltage == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
//                mListener.no();
//
//            } else if (voltage == BatteryManager.BATTERY_STATUS_DISCHARGING) {
//                mListener.down();
//
//            } else if (voltage == BatteryManager.BATTERY_STATUS_FULL) {
//                mListener.full();
//
//            } else {
//                mListener.noKnow();
//            }
//        }
//    }
//
//    /**
//     * context 注册
//     */
//    public static void registerReceiver(Context context) {
//        if (receiver == null)
//            receiver = new BatteryReceiver();
//
//        context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//    }
//
//    /**
//     * context 注销
//     */
//    public static void unregisterReceiver(Context context) {
//        if (receiver == null)
//            return;
//
//        context.unregisterReceiver(receiver);
//        receiver = null;
//    }
//
//    /**
//     * listener 设置电量广播监听器
//     */
//    public static void setBatteryListener(BatteryListener listener) {
//
//        mListener = listener;
//    }
//
//    /**
//     * 监听器
//     */
//    public interface BatteryListener {
//
//        void percent(int percent);
//
//        void up();
//
//        void no();
//
//        void down();
//
//        void full();
//
//        void noKnow();
//    }
//
//    /**
//     * 主动-->获取电池状态, BatteryUtils.FALSE 为失败
//     */
//    public static int getVoltage(Context context) {
//
//        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//
//        Intent intent = context.registerReceiver(null, filter);
//
//        if (intent == null)
//            return FALSE;
//
//        return intent.getIntExtra(BatteryManager.EXTRA_STATUS, FALSE);
//    }
//
//    /**
//     * 主动-->获取电量百分比, BatteryUtils.FALSE 为失败
//     */
//    public static int getPercent(Context context) {
//
//        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//
//        Intent intent = context.registerReceiver(null, filter);
//
//        if (intent == null)
//            return FALSE;
//
//        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, FALSE);
//        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, FALSE);
//
//        if (level == FALSE || scale == FALSE)
//            return FALSE;
//        else
//            return (level * 100) / scale;
//    }
}
