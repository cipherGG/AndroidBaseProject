package com.android.base.utils.sys;

import android.content.Context;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by gg on 2017/4/3.
 */

public class PhoneUtils {

    /**
     * SIM信息，服务商，数据连接
     */
    public static TelephonyManager getTelephonyManager(Context context) {

        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 判断当前设备是否为手机
     */
    public static boolean isPhone(Context context) {

        return getTelephonyManager(context).getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>SIM卡<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * SIM_STATE_UNKNOWN 　 未知状态
     * SIM_STATE_ABSENT 　 未插卡
     * SIM_STATE_READY 　 准备就绪
     * SIM_STATE_PIN_REQUIRED 　 需要PIN码，需要SIM卡PIN码解锁
     * SIM_STATE_PUK_REQUIRED 　需要PUK码，需要SIM卡PUK码解锁
     * SIM_STATE_NETWORK_LOCKED 网络被锁定，需要网络PIN解锁
     */
    public static int getSimState(Context context) {

        return getTelephonyManager(context).getSimState();
    }

    /**
     * SIM卡的序列号(IMEI)
     */
    public static String getSimNumber(Context context) {
        if (!isPhone(context)) {
            return "请换成手机";
        }
        if (getSimState(context) != TelephonyManager.SIM_STATE_READY) {
            return "请检查SIM卡状态";
        }
        return getTelephonyManager(context).getSimSerialNumber();
    }

    /**
     * 返回手机号码，对于GSM网络来说即MSISDN
     */
    public static String getLine1Number(Context context) {
        if (!isPhone(context)) {
            return "请换成手机";
        }
        if (getSimState(context) != TelephonyManager.SIM_STATE_READY) {
            return "请检查SIM卡状态";
        }
        return getTelephonyManager(context).getLine1Number();
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Device<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * 返回当前移动终端的唯一标识(IMEI) 如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID
     */
    public static String getDeviceId(Context context) {
        String deviceId;
        if (isPhone(context)) {
            deviceId = getTelephonyManager(context).getDeviceId();

        } else {
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

        }
        return deviceId;
    }

    /**
     * 返回用户唯一标识，比如GSM网络的IMSI编号
     */
    public static String getSubscriberId(Context context) {

        return getTelephonyManager(context).getSubscriberId();
    }

    /**
     * 返回移动终端的软件版本，例如：GSM手机的IMEI/SV码。
     */
    public static String getDeviceSoftwareVersion(Context context) {

        return getTelephonyManager(context).getDeviceSoftwareVersion();
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>运营商<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * 获取MCC+MNC代码 (SIM卡运营商国家代码和运营商网络代码)
     * 仅当用户已在网络注册时有效, CDMA 可能会无效
     * （中国移动：46000 46002, 中国联通：46001,中国电信：46003）
     */
    public static String getNetworkOperator(Context context) {

        return getTelephonyManager(context).getNetworkOperator();
    }

    /**
     * 返回移动网络运营商的名字 (例：中国联通、中国移动、中国电信)
     */
    public static String getNetworkOperatorName(Context context) {

        return getTelephonyManager(context).getNetworkOperatorName();
    }

    /**
     * 返回ISO标准的国家码，即国际长途区号
     */
    public static String getNetworkCountryIso(Context context) {

        return getTelephonyManager(context).getNetworkCountryIso();
    }

    /**
     * 返回手机是否处于漫游状态
     */
    public static boolean isNetworkRoaming(Context context) {

        return isPhone(context) && getTelephonyManager(context).isNetworkRoaming();
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>PhoneState<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * 返回电话状态
     * CALL_STATE_IDLE 无任何状态时
     * CALL_STATE_OFFHOOK 接起电话时
     * CALL_STATE_RINGING 电话进来时
     */
    public static int getCallState(Context context) {

        return getTelephonyManager(context).getCallState();
    }

    /**
     * 获取数据活动状态
     * DATA_ACTIVITY_IN 数据连接状态：活动，正在接受数据
     * DATA_ACTIVITY_OUT 数据连接状态：活动，正在发送数据
     * DATA_ACTIVITY_INOUT 数据连接状态：活动，正在接受和发送数据
     * DATA_ACTIVITY_NONE 数据连接状态：活动，但无数据发送和接受
     */
    public static int getDataActivity(Context context) {

        return getTelephonyManager(context).getDataActivity();
    }

    /**
     * 获取数据连接状态
     * DATA_CONNECTED 数据连接状态：已连接
     * DATA_CONNECTING 数据连接状态：正在连接
     * DATA_DISCONNECTED 数据连接状态：断开
     * DATA_SUSPENDED 数据连接状态：暂停
     */
    public static int getDataState(Context context) {

        return getTelephonyManager(context).getDataState();
    }

    /**
     * 监听网络信号强度
     */
    public static void startListenStrength(Context context, PhoneStateListener listener) {

        getTelephonyManager(context).listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    /**
     * 监听来电状态
     */
    public static void startListenCall(Context context, PhoneStateListener listener) {

        getTelephonyManager(context).listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 停止PhoneState所有监听
     */
    public static void stopListen(Context context, PhoneStateListener listener) {

        getTelephonyManager(context).listen(listener, PhoneStateListener.LISTEN_NONE);
    }
}
