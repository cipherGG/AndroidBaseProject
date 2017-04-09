package com.jiangzg.project.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.android.base.utils.func.LocationUtils;
import com.android.base.utils.func.PermUtils;
import com.android.base.utils.view.ToastUtils;
import com.jiangzg.project.MyApp;
import com.jiangzg.project.R;
import com.jiangzg.project.utils.MapUtils;

public class LocationService extends Service {

    public static void startService(final Context from, final boolean once) {
        PermUtils.requestMap(from, new PermUtils.PermissionListener() {
            @Override
            public void onAgree() {
                Intent intent = new Intent(from, LocationService.class);
                intent.putExtra("once", once);
                from.startService(intent);
            }
        });
    }

    public static void bindService(final Context from) {
        PermUtils.requestMap(from, new PermUtils.PermissionListener() {
            @Override
            public void onAgree() {
                Intent intent = new Intent(from, LocationService.class);
                from.startService(intent);
            }
        });
    }

    @Override
    public void onCreate() {

    }

    /* startService才走这个 不走下面的 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean once = intent.getBooleanExtra("once", true);
        MapUtils.get().initLocation(this);
        AMapLocationListener locationListener = MapUtils.get()
                .getAMapLocationListener(new MapUtils.LocationCallBack() {
                    @Override
                    public void onSuccess(AMapLocation aMapLocation) {
                        double longitude = aMapLocation.getLongitude();
                        double latitude = aMapLocation.getLatitude();
                        String province = aMapLocation.getProvince();
                        String city = aMapLocation.getCity();
                        String district = aMapLocation.getDistrict();
                        String address = aMapLocation.getAddress();

                        LocationUtils locationUtils = LocationUtils.getInfo();
                        locationUtils.setLongitude(longitude);
                        locationUtils.setLatitude(latitude);
                        locationUtils.setProvince(province);
                        locationUtils.setCity(city);
                        locationUtils.setDistrict(district);
                        locationUtils.setAddress(address);
                        stopSelf();
                    }

                    @Override
                    public void onFailed(AMapLocation aMapLocation) {
                        int errorCode = aMapLocation.getErrorCode();
                        String toast = MyApp.get().getString(R.string.location_error);
                        ToastUtils.get().show(toast + errorCode);
                    }
                });
        MapUtils.get().startLocation(locationListener);
        return super.onStartCommand(intent, flags, startId);
    }

    /* bindService不走上面的 走这个 */
    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
