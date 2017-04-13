package com.android.base.utils.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiangZhiGuo on 2016-11-9.
 * describe RecyclerView管理类
 */
public class QuickManager {

    private Context mContext;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private BaseQuickAdapter mAdapter;
    private SwipeRefreshLayout mRefresh;
    private RefreshListener mRefreshListener;

    public QuickManager(Context context) {
        mContext = context;
    }

    /**
     * ************************************初始化***************************************
     */
    public QuickManager initRecycler(RecyclerView recyclerView) {
        mRecycler = recyclerView;
        return this;
    }

    /**
     * 布局管理器
     */
    public QuickManager initLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (mRecycler == null || layoutManager == null) return this;
        mLayoutManager = layoutManager;
        mRecycler.setLayoutManager(mLayoutManager);
        return this;
    }

    /**
     * 设置适配器，这里只支持BaseRecyclerViewAdapterHelper
     */
    public QuickManager initAdapter(BaseQuickAdapter adapter) {
        if (adapter == null) return this;
        mAdapter = adapter;
        return this;
    }

    /**
     * 直接设置适配器，空数据的适配器刚开始会有加载更多的提示
     */
    public QuickManager setAdapter(BaseQuickAdapter adapter) {
        if (mRecycler == null || adapter == null) return this;
        mAdapter = adapter;
        mRecycler.setAdapter(mAdapter);
        return this;
    }

    /**
     * ************************************VIEW***************************************
     * 加载更多视图
     */
    public QuickManager viewLoading(int loadingLayoutId) {
        if (mContext == null || mRecycler == null) return this;
        View head = LayoutInflater.from(mContext).inflate(loadingLayoutId, mRecycler, false);
        viewHeader(head);
        return this;
    }

    public QuickManager viewLoading(View loading) {
        if (mAdapter == null || loading == null) return this;
        mAdapter.setLoadingView(loading);
        return this;
    }

    /**
     * 无Data时显示的view
     */
    public QuickManager viewEmpty(int emptyLayoutId) {
        if (mRecycler == null || mContext == null || emptyLayoutId == 0) return this;
        View empty = LayoutInflater.from(mContext).inflate(emptyLayoutId, mRecycler, false);
        viewEmpty(empty);
        return this;
    }

    public QuickManager viewEmpty(View empty) {
        if (mAdapter == null || empty == null) return this;
        mAdapter.setEmptyView(empty);
        return this;
    }

    /**
     * list顶部view（可remove）
     */
    public QuickManager viewHeader(int headLayoutId) {
        if (mContext == null || mRecycler == null) return this;
        View head = LayoutInflater.from(mContext).inflate(headLayoutId, mRecycler, false);
        viewHeader(head);
        return this;
    }

    public QuickManager viewHeader(View head) {
        if (mAdapter == null || head == null) return this;
        mAdapter.addHeaderView(head);
        return this;
    }

    /**
     * list底部view（可remove）
     */
    public QuickManager viewFooter(int footLayoutId) {
        if (mContext == null || mRecycler == null) return this;
        View foot = LayoutInflater.from(mContext).inflate(footLayoutId, mRecycler, false);
        viewFooter(foot);
        return this;
    }

    public QuickManager viewFooter(View foot) {
        if (mAdapter == null || foot == null) return this;
        mAdapter.addFooterView(foot);
        return this;
    }

    /**
     * ************************************监听器***************************************
     */
    public interface MoreListener {
        void onMore(int currentCount);
    }

    public interface RefreshListener {
        void onRefresh();
    }

    /**
     * 刷新监听 回调时newData() 记得offset重置
     */
    public QuickManager listenerRefresh(SwipeRefreshLayout srl, RefreshListener listener) {
        if (srl == null || listener == null) return this;
        mRefresh = srl;
        mRefreshListener = listener;
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshListener.onRefresh();
                    }
                });
            }
        });
        return this;
    }

    /**
     * RecyclerView的item点击监听
     */
    public QuickManager listenerClick(RecyclerView.OnItemTouchListener listener) {
        if (mRecycler == null || listener == null) return this;
        mRecycler.addOnItemTouchListener(listener);
        return this;
    }

    /**
     * 加载更多监听 回调时addData() 记得offset叠加
     */
    public QuickManager listenerMore(final MoreListener listener) {
        if (mRecycler == null || mAdapter == null || listener == null) return this;
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecycler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onMore(mAdapter.getItemCount());
                    }
                });
            }
        });
        return this;
    }

    /**
     * ************************************数据***************************************
     * 主动刷新 页面进入时调用 记得offset重置
     */
    public void dataRefresh() {
        if (mRefresh == null || mRefreshListener == null) return;
        mRefresh.post(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(true); // 执行等待动画
                mRefreshListener.onRefresh();
            }
        });
    }

    public void data(List list, boolean more) {
        data(list, 0, more);
    }

    public void data(List list, int totalCount, boolean more) {
        if (more) {
            dataAdd(list, totalCount);
        } else {
            dataNew(list, totalCount);
        }
    }

    /**
     * 刷新数据
     */
    public void dataNew(List list) {
        dataNew(list, 0);
    }

    public void dataNew(List list, int totalCount) {
        if (mAdapter == null) return;
        if (null == list || 0 == list.size()) { // 没有数据
            mAdapter.setNewData(new ArrayList());
            mAdapter.loadComplete(); // 关闭更多
        } else { // 有数据
            mAdapter.setNewData(list);
            if (list.size() >= totalCount) {
                mAdapter.loadComplete(); // 关闭更多
            }
        }
        if (null != mRefresh) { // 停止刷新
            mRefresh.setRefreshing(false);
        }
        if (mRecycler == null) return; // 提前加载空adapter会有loadMore的标示
        if (mRecycler.getAdapter() == null) {
            mRecycler.setAdapter(mAdapter);
        }
    }

    /**
     * 更多数据
     */
    public void dataAdd(List list) {
        dataAdd(list, 0);
    }

    public void dataAdd(List list, int totalCount) {
        if (mAdapter == null) return;
        if (null == list || 0 == list.size()) { // 没有数据
            mAdapter.loadComplete();
        } else { // 有数据
            mAdapter.addData(list);
            if (mAdapter.getItemCount() >= totalCount) {
                mAdapter.loadComplete(); // 关闭更多
            }
        }
        if (null != mRefresh) { // 停止刷新
            mRefresh.setRefreshing(false);
        }
    }

    /**
     * ************************************实例获取***************************************
     */
    public BaseQuickAdapter getAdapter() {
        return mAdapter;
    }

    public BaseViewHolder getHolder(int position) {
        if (mRecycler == null) return null;
        return (BaseViewHolder) mRecycler.findViewHolderForLayoutPosition(position);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

}