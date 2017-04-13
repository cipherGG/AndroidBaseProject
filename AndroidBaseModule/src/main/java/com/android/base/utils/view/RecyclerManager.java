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
public class RecyclerManager<T> {

    private Context mContext;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private BaseQuickAdapter<T> mAdapter;
    private SwipeRefreshLayout mRefresh;
    private RefreshListener refreshListener;

    public RecyclerManager(Context context) {
        mContext = context;
    }

    /**
     * ************************************初始化***************************************
     */
    public void initRecycler(RecyclerView recyclerView) {
        mRecycler = recyclerView;
    }

    /**
     * 布局管理器
     */
    public void initLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (mRecycler == null || layoutManager == null) return;
        mLayoutManager = layoutManager;
        mRecycler.setLayoutManager(mLayoutManager);
    }

    /**
     * 设置适配器，这里只支持BaseRecyclerViewAdapterHelper
     */
    public void initAdapter(BaseQuickAdapter<T> adapter) {
        if (adapter == null) return;
        mAdapter = adapter;
        // TODO: 2017/4/11
//        mRecycler.setAdapter(adapter);
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
    public void listenerRefresh(SwipeRefreshLayout srl, RefreshListener listener) {
        if (srl == null || listener == null) return;
        mRefresh = srl;
        refreshListener = listener;
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshListener.onRefresh();
                    }
                });
            }
        });
    }

    /**
     * RecyclerView的item点击监听
     */
    public void listenerClick(RecyclerView.OnItemTouchListener listener) {
        if (mRecycler == null || listener == null) return;
        mRecycler.addOnItemTouchListener(listener);
    }

    /**
     * 加载更多监听 回调时addData() 记得offset叠加
     */
    public void listenerMore(final MoreListener listener) {
        if (mRecycler == null || mAdapter == null || listener == null) return;
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
    }

    /**
     * ************************************数据***************************************
     * 刷新数据
     */
    public void dataNew(List<T> list) {
        dataNew(list, 0);
    }

    public void dataNew(List<T> list, int totalCount) {
        if (mAdapter == null) return;
        if (null == list || 0 == list.size()) { // 没有数据
            mAdapter.setNewData(new ArrayList<T>());
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
    }

    /**
     * 更多数据
     */
    public void dataAdd(List<T> list) {
        dataAdd(list, 0);
    }

    public void dataAdd(List<T> list, int totalCount) {
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
     * ************************************VIEW***************************************
     * 主动刷新 页面进入时调用 记得offset重置
     */
    public void viewRefresh() {
        if (mRefresh == null || refreshListener == null) return;
        mRefresh.post(new Runnable() {
            @Override
            public void run() {
                mRefresh.setRefreshing(true); // 执行等待动画
                refreshListener.onRefresh();
            }
        });
    }

    /**
     * 无Data时显示的view
     */
    public void viewEmpty(int emptyLayoutId) {
        if (mRecycler == null || mContext == null || emptyLayoutId == 0) return;
        View empty = LayoutInflater.from(mContext).inflate(emptyLayoutId, mRecycler, false);
        viewEmpty(empty);
    }

    public void viewEmpty(View empty) {
        if (mAdapter == null || empty == null) return;
        mAdapter.setEmptyView(empty);
    }

    /**
     * list顶部view（可remove）
     */
    public void viewHeader(int headLayoutId) {
        if (mContext == null || mRecycler == null) return;
        View head = LayoutInflater.from(mContext).inflate(headLayoutId, mRecycler, false);
        viewHeader(head);
    }

    public void viewHeader(View head) {
        if (mAdapter == null || head == null) return;
        mAdapter.addHeaderView(head);
    }

    /**
     * list底部view（可remove）
     */
    public void viewFooter(int footLayoutId) {
        if (mContext == null || mRecycler == null) return;
        View foot = LayoutInflater.from(mContext).inflate(footLayoutId, mRecycler, false);
        viewFooter(foot);
    }

    public void viewFooter(View foot) {
        if (mAdapter == null || foot == null) return;
        mAdapter.addFooterView(foot);
    }

    /**
     * ************************************实例获取***************************************
     */
    public BaseQuickAdapter<T> getAdapter() {
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
