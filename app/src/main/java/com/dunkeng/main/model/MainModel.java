package com.dunkeng.main.model;

import com.dunkeng.common.Config;
import com.dunkeng.common.api.IpApi;
import com.dunkeng.common.api.TianxingApi;
import com.dunkeng.main.contract.MainContract;
import com.oklib.utils.assist.DateUtil;
import com.oklib.utils.date.lunar.LunarCalendar;
import com.oklib.utils.helper.RxUtil;
import com.oklib.utils.network.http.ViseHttp;

import io.reactivex.Observable;


/**
 * Created by Damon.Han on 2017/1/12 0012.
 *
 * @author Damon
 */

public class MainModel implements MainContract.Model {

    @Override
    public Observable<LunarModel> getLunarData() {
        long time = System.currentTimeMillis();
        int year = DateUtil.getYear(time);
        int month = DateUtil.getMonth(time);
        int day = DateUtil.getDay(time);
        LunarCalendar lunarCalender = LunarCalendar.obtainCalendar(year, month, day);
        String cd = lunarCalender.getLunar().year + "-" + lunarCalender.getLunar().month + "-" + lunarCalender.getLunar().day;
        Observable<LunarModel> observable = ViseHttp.RETROFIT().baseUrl(Config.BASE_URL_TIANXING)
                .create(TianxingApi.class).getLunar(Config.API_KEY_TIANXING, cd);
        return observable.compose(RxUtil.<LunarModel>rxSchedulerHelper());
    }

    @Override
    public Observable<IpModel> getIpData() {
        Observable< IpModel> observable =
                ViseHttp.RETROFIT().setHttpCache(true)
                        .baseUrl(Config.BASE_URL_ip)
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36 LBBROWSER")
                        .create(IpApi.class).getIpData("myip");
        return observable.compose(RxUtil.<IpModel>rxSchedulerHelper());
    }
}
