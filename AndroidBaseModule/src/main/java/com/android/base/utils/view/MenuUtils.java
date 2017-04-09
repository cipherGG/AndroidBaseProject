package com.android.base.utils.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by gg on 2017/4/3.
 */

public class MenuUtils {
    /**
     * 获取Menu的打气筒,这是实例化xml的menu，也可以动态add和remove
     */
    public static MenuInflater getMenuInflater(Activity activity) {

        return activity.getMenuInflater();
    }

    /**
     * 用来显示Menu布局, 在onCreateOptionsMenu中调用
     */
    public static void inflateMenu(Activity activity, int menuID, Menu menu) {

        getMenuInflater(activity).inflate(menuID, menu);
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>选项菜单<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * 内部调用onCreateOptionsMenu(Menu menu) ,用于动态变换menu选项
     */
    public static void invalidateMenu(AppCompatActivity activity) {

        activity.supportInvalidateOptionsMenu();
    }

    /**
     * 当fragment被hide之类的时候，需要连带着它的Menu一起消失
     */
    public static void setFragmentMenuVisibility(Fragment fragment, boolean visibility) {

        fragment.setMenuVisibility(visibility);
    }

    /**
     * 打开选项菜单
     */
    public static void openOptionsMenu(Activity activity) {

        activity.openOptionsMenu();
    }

    /**
     * 关闭选项菜单
     */
    public static void closeOptionsMenu(Activity activity) {

        activity.closeOptionsMenu();
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>上下文菜单<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * View注册上下文菜单 ,长按出结果
     */
    public static void registerContextMenu(Activity activity, View view) {

        activity.registerForContextMenu(view);
    }

    /**
     * fragment也能注册
     */
    public static void registerContextMenu(Fragment fragment, View view) {

        fragment.registerForContextMenu(view);
    }

    /**
     * View注销上下文菜单
     */
    public static void unregisterContextMenu(Activity activity, View view) {

        activity.unregisterForContextMenu(view);
    }

    /**
     * fragment也能注销
     */
    public static void unregisterContextMenu(Fragment fragment, View view) {

        fragment.unregisterForContextMenu(view);
    }

    /**
     * 打开上下文菜单
     */
    public static void openContextMenu(Activity activity, View view) {

        activity.openContextMenu(view);
    }

    /**
     * 关闭上下文菜单
     */
    public static void closeContextMenu(Activity activity) {

        activity.closeContextMenu();
    }

    /**
     * 主要是为注册ContextMenu的列表获取点击的item的position
     */
    public static int getItemPosition(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        return info.position;
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>PopMenu<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * 创建PopMenu 可以调用menu.show();显示
     */
    public static PopupMenu createPopMenu(Context context, View view, int menuID) {
        PopupMenu menu = new PopupMenu(context, view);
        menu.getMenuInflater().inflate(menuID, menu.getMenu());

        return menu;
    }

    /**
     * PopMenu加载监听器
     */
    public static void setPopupMenuListener(PopupMenu menu,
                                            PopupMenu.OnMenuItemClickListener listener) {

        menu.setOnMenuItemClickListener(listener);
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ActionMode<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<,
     * <p>
     * activity.startActionMode() 或者 view.startActionMode()
     */
    public static ActionMode.Callback getActionMode(Activity activity, final int menuID,
                                                    final ActionModeCallBack callback) {

        //listView.setMultiChoiceModeListener()里的参数是ActionMode.Callback的子类
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(menuID, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                callback.onActionItemClicked(mode, item);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                callback.onDestroyActionMode(mode);
            }
        };
    }

    /**
     * ActionMode回调事件
     */
    public interface ActionModeCallBack {
        void onActionItemClicked(ActionMode mode, MenuItem item);

        void onDestroyActionMode(ActionMode mode);
    }
}
