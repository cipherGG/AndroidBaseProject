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
 * describe RecyclerViewAdapter管理类
 */
public class AdapterUtils {

    private Context mContext;
    private RecyclerView mRecycler;
    private RecyclerView.LayoutManager mLayoutManager;
    private BaseQuickAdapter mAdapter;
    private SwipeRefreshLayout mRefresh;
    private RefreshListener mRefreshListener;
    private RecyclerView.OnItemTouchListener mClickListener;
    private MoreListener mMoreListener;
    private View mLoading, mEmpty, mHead, mFoot;

    public AdapterUtils(Context context) {
        mContext = context;
    }

    /**
     * ************************************初始化***************************************
     */
    public AdapterUtils initRecycler(RecyclerView recyclerView) {
        mRecycler = recyclerView;
        return this;
    }

    /**
     * 布局管理器
     */
    public AdapterUtils initLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (mRecycler == null || layoutManager == null) return this;
        mLayoutManager = layoutManager;
        mRecycler.setLayoutManager(mLayoutManager);
        return this;
    }

    /**
     * SwipeRefreshLayout
     */
    public AdapterUtils initRefresh(SwipeRefreshLayout srl) {
        mRefresh = srl;
        return this;
    }

    /**
     * 设置适配器，这里只支持BaseRecyclerViewAdapterHelper
     */
    public AdapterUtils initAdapter(BaseQuickAdapter adapter) {
        if (adapter == null) return this;
        mAdapter = adapter;
        return this;
    }

    /**
     * ************************************VIEW***************************************
     * 加载更多视图
     */
    public AdapterUtils viewLoading(int loadingLayoutId) {
        if (mContext == null || mRecycler == null) return this;
        View head = LayoutInflater.from(mContext).inflate(loadingLayoutId, mRecycler, false);
        viewHeader(head);
        return this;
    }

    public AdapterUtils viewLoading(View loading) {
        if (mAdapter == null || loading == null) return this;
        mLoading = loading;
        mAdapter.setLoadingView(mLoading);
        return this;
    }

    /**
     * 无Data时显示的view
     */
    public AdapterUtils viewEmpty(int emptyLayoutId) {
        if (mRecycler == null || mContext == null || emptyLayoutId == 0) return this;
        View empty = LayoutInflater.from(mContext).inflate(emptyLayoutId, mRecycler, false);
        viewEmpty(empty);
        return this;
    }

    public AdapterUtils viewEmpty(View empty) {
        if (mAdapter == null || empty == null) return this;
        mEmpty = empty;
        mAdapter.setEmptyView(mEmpty);
        return this;
    }

    /**
     * list顶部view（可remove）
     */
    public AdapterUtils viewHeader(int headLayoutId) {
        if (mContext == null || mRecycler == null) return this;
        View head = LayoutInflater.from(mContext).inflate(headLayoutId, mRecycler, false);
        viewHeader(head);
        return this;
    }

    public AdapterUtils viewHeader(View head) {
        if (mAdapter == null || head == null) return this;
        mHead = head;
        mAdapter.addHeaderView(mHead);
        return this;
    }

    /**
     * list底部view（可remove）
     */
    public AdapterUtils viewFooter(int footLayoutId) {
        if (mContext == null || mRecycler == null) return this;
        View foot = LayoutInflater.from(mContext).inflate(footLayoutId, mRecycler, false);
        viewFooter(foot);
        return this;
    }

    public AdapterUtils viewFooter(View foot) {
        if (mAdapter == null || foot == null) return this;
        mFoot = foot;
        mAdapter.addFooterView(mFoot);
        return this;
    }

    /**
     * ************************************监听器***************************************
     * RecyclerView的item点击监听
     */
    public AdapterUtils listenerClick(RecyclerView.OnItemTouchListener listener) {
        if (mRecycler == null || listener == null) return this;
        mClickListener = listener;
        mRecycler.addOnItemTouchListener(mClickListener);
        return this;
    }

    public interface MoreListener {
        void onMore(int currentCount);
    }

    /**
     * 加载更多监听 回调时addData() 记得offset叠加
     */
    public AdapterUtils listenerMore(final MoreListener listener) {
        if (mRecycler == null || mAdapter == null || listener == null) return this;
        mMoreListener = listener;
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecycler.post(new Runnable() {
                    @Override
                    public void run() {
                        mMoreListener.onMore(mAdapter.getItemCount());
                    }
                });
            }
        });
        return this;
    }

    public interface RefreshListener {
        void onRefresh();
    }

    /**
     * 刷新监听 回调时newData() 记得offset重置
     */
    public AdapterUtils listenerRefresh(RefreshListener listener) {
        if (mRefresh == null || listener == null) return this;
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
     * ************************************变更***************************************
     * 变更布局管理器，需要紧接着changeAdapter
     */
    public void changeLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (mRecycler == null || layoutManager == null) return;
        mLayoutManager = layoutManager;
        mRecycler.setAdapter(null);
        mRecycler.setLayoutManager(mLayoutManager);
    }

    /**
     * 变更适配器，空数据的适配器刚开始会有加载更多的提示
     */
    public void changeAdapter(BaseQuickAdapter adapter) {
        if (mRecycler == null || adapter == null) return;
        mAdapter = adapter;
        mRecycler.setAdapter(mAdapter);
        viewLoading(mLoading);
        viewEmpty(mEmpty);
        viewHeader(mHead);
        viewFooter(mFoot);
        listenerRefresh(mRefreshListener);
//        listenerClick(mClickListener); recycler重复
        listenerMore(mMoreListener);
    }

    /**
     * ************************************实例获取***************************************
     */
    @SuppressWarnings("unchecked")
    public <A extends BaseQuickAdapter> A getAdapter() {
        return (A) mAdapter;
    }

    @SuppressWarnings("unchecked")
    public <A extends RecyclerView.LayoutManager> A getLayoutManager() {
        return (A) mLayoutManager;
    }

    public BaseViewHolder getHolder(int position) {
        if (mRecycler == null) return null;
        return (BaseViewHolder) mRecycler.findViewHolderForLayoutPosition(position);
    }

}
