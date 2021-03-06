package com.android.base.function;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.android.base.R;
import com.android.base.component.application.AppContext;
import com.android.base.component.intent.IntentConstant;
import com.android.base.view.widget.ToastUtils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static com.android.base.component.application.AppContext.getConnectivityManager;

/**
 * Created by jiang on 2016/10/12
 * 网络工具类
 */
public class NetUtils {

    private static NetUtils instance;
    private ConnectListener mListener;

    public static NetUtils get() {
        if (instance == null) {
            synchronized (NetUtils.class) {
                if (instance == null) {
                    instance = new NetUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 网络状态监听器
     */
    public interface ConnectListener {
        void onStateChange(int type, String name, NetworkInfo.State state,
                           String operator);
    }

    /**
     * 注册网络监听,listener监听回调（网络状态变化）
     */
    public void registerReceiver(Context context, ConnectListener listener) {
        mListener = listener;
        context.registerReceiver(receiver, new IntentFilter(IntentConstant.action_connectivity));
    }

    /**
     * 注销网络监听
     */
    public void unregisterReceiver(Context context) {
        context.unregisterReceiver(receiver);
    }

    /**
     * 广播,监听网络变化
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mListener == null) return;
            int type = getNetworkType();
            String name = getNetworkName();
            NetworkInfo.State state = getNetworkState();
            String operator = getNetworkOperator();
            mListener.onStateChange(type, name, state, operator);
        }
    };

    /**
     * 网络是否可用
     */
    public static boolean isAvailable() {
        NetworkInfo networkInfo = getNetworkInfo();
        boolean available = (networkInfo != null && getNetworkInfo().isAvailable());
        if (!available) {
            String show = AppContext.get().getString(R.string.no_network_title);
            ToastUtils.show(show);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断wifi是否连接状态
     */
    public static boolean isWifi() {
        ConnectivityManager cm = AppContext.getConnectivityManager();
        return cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    private static NetworkInfo getNetworkInfo() {
        return getConnectivityManager().getActiveNetworkInfo();
    }

    /**
     * 获取当前网络类型
     *
     * @return {@link ConnectivityManager#TYPE_MOBILE}
     */
    public static int getNetworkType() {
        NetworkInfo networkInfo = getNetworkInfo();
        if (networkInfo == null) return -1;
        else return networkInfo.getType();
    }

    /**
     * 获取当前网络类型名称
     */
    public static String getNetworkName() {
        int networkType = getNetworkType();
        String name;
        if (networkType == ConnectivityManager.TYPE_MOBILE) {
            name = "移动";
        } else if (networkType == ConnectivityManager.TYPE_WIFI) {
            name = "WIFI";
        } else if (networkType == ConnectivityManager.TYPE_BLUETOOTH) {
            name = "蓝牙";
        } else {
            name = "未知";
        }
        return name;
    }

    /**
     * 获取当前网络状态
     *
     * @return {@link NetworkInfo.State State}
     */
    public static NetworkInfo.State getNetworkState() {
        NetworkInfo networkInfo = getNetworkInfo();
        if (networkInfo == null) return NetworkInfo.State.UNKNOWN;
        else return networkInfo.getState();
    }

    /**
     * 获取移动网络运营商名称
     *
     * @return 如中国联通、中国移动、中国电信
     */
    public static String getNetworkOperator() {
        TelephonyManager tm = AppContext.getTelephonyManager();
        return tm != null ? tm.getNetworkOperatorName() : null;
    }

    /**
     * 获取IP地址 eg:127.168.x.x
     */
    public static String getIpAddress() {
        String ipAddress = "";
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = ias.nextElement();
                    if (ia instanceof Inet6Address) continue; // skip ipv6
                    String ip = ia.getHostAddress();
                    String host = "127.0.0.1";
                    if (!host.equals(ip)) {
                        ipAddress = ip;
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

}
