package com.oklib.widget.recyclerview.holder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;


import com.oklib.widget.recyclerview.adapter.RecyclerArrayAdapter;
import com.oklib.widget.recyclerview.inter.OnItemChildClickListener;
import com.oklib.widget.recyclerview.utils.RefreshLogUtils;

import java.lang.reflect.Field;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/4/22
 *     desc  : ViewHolder的简单封装，M为这个itemView对应的model，使用RecyclerArrayAdapter就一定要用
 *             这个ViewHolder
 *             推荐子类继承第二个构造函数。并将子类的构造函数设为一个ViewGroup parent
 *     revise: 参考鸿洋大神的baseAdapter封装库
 *             具体可以参考我的adapter封装库：https://github.com/yangchong211/YCBaseAdapter
 * </pre>
 */
public class BaseViewHolder<M> extends RecyclerView.ViewHolder {


    // SparseArray 比 HashMap 更省内存，在某些条件下性能更好，只能存储 key 为 int 类型的数据，
    // 用来存放 View 以减少 findViewById 的次数

    private SparseArray<View> viewSparseArray;

    public BaseViewHolder(View itemView) {
        super(itemView);
        if(viewSparseArray==null){
            viewSparseArray = new SparseArray<>();
        }
    }

    public BaseViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(LayoutInflater.from(parent.getContext()).inflate(res, parent, false));
        if(viewSparseArray==null){
            viewSparseArray = new SparseArray<>();
        }
    }

    /**
     * 子类设置数据方法
     * @param data                 data
     */
    public void setData(M data) {}

    /**
     * findViewById方式
     * 根据 ID 来获取 View
     * @param viewId viewID
     * @param <T>    泛型
     * @return 将结果强转为 View 或 View 的子类型
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T getView(int viewId) {
        // 先从缓存中找，找打的话则直接返回
        // 如果找不到则 findViewById ，再把结果存入缓存中
        View view = viewSparseArray.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            viewSparseArray.put(viewId, view);
        }
        return (T) view;
    }


    /**
     * 获取上下文context
     * @return                  context
     */
    protected Context getContext(){
        return itemView.getContext();
    }


    /**
     * 获取数据索引的位置
     * @return                  position
     */
    protected int getDataPosition(){
        RecyclerView.Adapter adapter = getOwnerAdapter();
        if (adapter instanceof RecyclerArrayAdapter){
            int headerCount = ((RecyclerArrayAdapter) adapter).getHeaderCount();
            //注意需要减去header的count，否则造成索引错乱
            return getAdapterPosition() - headerCount;
        }
        return getAdapterPosition();
    }


    /**
     * 获取adapter对象
     * @param <T>               adapter
     * @return                  adapter
     */
    @Nullable
    private  <T extends RecyclerView.Adapter> T getOwnerAdapter(){
        RecyclerView recyclerView = getOwnerRecyclerView();
        //noinspection unchecked
        return recyclerView != null ? (T) recyclerView.getAdapter() : null;
    }


    @SuppressWarnings("CatchMayIgnoreException")
    @Nullable
    private RecyclerView getOwnerRecyclerView(){
        try {
            //使用反射
            Field field = RecyclerView.ViewHolder.class.getDeclaredField("mOwnerRecyclerView");
            //设置暴力访问权限
            field.setAccessible(true);
            return (RecyclerView) field.get(this);
        } catch (NoSuchFieldException ignored) {
            RefreshLogUtils.e(ignored.getLocalizedMessage());
        } catch (IllegalAccessException ignored) {
            RefreshLogUtils.e(ignored.getLocalizedMessage());
        }
        return null;
    }

    /**
     * 添加子控件的点击事件
     * @param viewId                        控件id
     */
    protected void addOnClickListener(@IdRes final int viewId) {
        final View view = getView(viewId);
        if (view != null) {
            if (!view.isClickable()) {
                view.setClickable(true);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getOwnerAdapter()!=null){
                        OnItemChildClickListener onItemChildClickListener = ((RecyclerArrayAdapter)
                                getOwnerAdapter()).getOnItemChildClickListener();
                        if (onItemChildClickListener != null) {
                            onItemChildClickListener.onItemChildClick(v, getDataPosition());
                        }
                    }
                }
            });
        }
    }

    /**
     * 设置TextView的值
     */
    public BaseViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }


    /**
     * 设置imageView图片
     */
    public BaseViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }


    /**
     * 设置imageView图片
     */
    public BaseViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }


    /**
     * 设置imageView图片
     */
    public BaseViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }


    /**
     * 设置背景颜色
     */
    public BaseViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }


    /**
     * 设置背景颜色
     */
    public BaseViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }


    /**
     * 设置text颜色
     */
    public BaseViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * 设置透明度
     */
    @SuppressLint({"NewApi", "ObsoleteSdkInt"})
    public BaseViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }


    /**
     * 设置是否可见
     */
    public BaseViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }


    public BaseViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public BaseViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public BaseViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public BaseViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public BaseViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public BaseViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public BaseViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public BaseViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public BaseViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public BaseViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }
}