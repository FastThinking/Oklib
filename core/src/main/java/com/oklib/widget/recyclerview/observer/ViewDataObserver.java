package com.oklib.widget.recyclerview.observer;

import androidx.recyclerview.widget.RecyclerView;

import com.oklib.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.oklib.widget.recyclerview.CoreRecyclerView;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/4/28
 *     desc  : 自定义AdapterDataObserver
 *     revise:
 * </pre>
 */
public class ViewDataObserver extends RecyclerView.AdapterDataObserver {

    private CoreRecyclerView recyclerView;
    private RecyclerArrayAdapter adapter;

    public ViewDataObserver(CoreRecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        if (recyclerView.getAdapter() instanceof RecyclerArrayAdapter) {
            adapter = (RecyclerArrayAdapter) recyclerView.getAdapter();
        }
    }

    private boolean isHeaderFooter(int position) {
        return adapter != null && (position < adapter.getHeaderCount()
                || position >= adapter.getHeaderCount() + adapter.getCount());
    }


    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
        if (!isHeaderFooter(positionStart)) {
            update();
        }
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        if (!isHeaderFooter(positionStart)) {
            update();
        }
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        if (!isHeaderFooter(positionStart)) {
            update();
        }
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount);
        //header&footer不会有移动操作
        update();
    }

    @Override
    public void onChanged() {
        super.onChanged();
        //header&footer不会引起changed
        update();
    }


    /**
     * 自动更改Container的样式
     */
    private void update() {
        int count;
        if (recyclerView.getAdapter() instanceof RecyclerArrayAdapter) {
            count = ((RecyclerArrayAdapter) recyclerView.getAdapter()).getCount();
        } else {
            count = recyclerView.getAdapter().getItemCount();
        }
        if (count == 0) {
            recyclerView.showEmpty();
        } else {
            recyclerView.showRecycler();
        }
    }

}