package com.dunkeng.common.api;


import com.dunkeng.zhihu.model.DailyListBean;
import com.dunkeng.zhihu.model.SectionChildListBean;
import com.dunkeng.zhihu.model.SectionListBean;
import com.dunkeng.zhihu.model.ZhihuDetailBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 */

public interface ZhiHuApi {
//    /**
//     * 启动界面图片
//     */
//    @GET("start-image/{res}")
//    Observable<WelcomeBean> getWelcomeInfo(@Path("res") String res);

    /**
     * 最新日报
     */
    @GET("news/latest")
    Observable<DailyListBean> getDailyList();

//    /**
//     * 往期日报
//     */
//    @GET("news/before/{date}")
//    Observable<DailyBeforeListBean> getDailyBeforeList(@Path("date") String date);

//    /**
//     * 主题日报
//     */
//    @GET("themes")
//    Observable<ThemeListBean> getThemeList();

//    /**
//     * 主题日报详情
//     */
//    @GET("theme/{id}")
//    Observable<ThemeChildListBean> getThemeChildList(@Path("id") int id);

    /**
     * 专栏日报
     */
    @GET("sections")
    Observable<SectionListBean> getSectionList();

    /**
     * 专栏日报详情
     */
    @GET("section/{id}")
    Observable<SectionChildListBean> getSectionChildList(@Path("id") int id);

//    /**
//     * 热门日报
//     */
//    @GET("news/hot")
//    Observable<HotListBean> getHotList();

    /**
     * 日报详情
     */
    @GET("news/{id}")
    Observable<ZhihuDetailBean> getDetailInfo(@Path("id") int id);

//    /**
//     * 日报的额外信息
//     */
//    @GET("story-extra/{id}")
//    Observable<DetailExtraBean> getDetailExtraInfo(@Path("id") int id);

//    /**
//     * 日报的长评论
//     */
//    @GET("story/{id}/long-comments")
//    Observable<CommentBean> getLongCommentInfo(@Path("id") int id);

//    /**
//     * 日报的短评论
//     */
//    @GET("story/{id}/short-comments")
//    Observable<CommentBean> getShortCommentInfo(@Path("id") int id);
}
