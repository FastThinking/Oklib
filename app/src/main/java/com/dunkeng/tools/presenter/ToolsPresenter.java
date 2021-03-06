package com.dunkeng.tools.presenter;

import com.dunkeng.tools.contract.ToolsContract;
import com.oklib.base.CoreBasePresenter;


/**
 * Created by Damon.Han on 2017/3/29 0029.
 *
 * @author Damon
 */

public class ToolsPresenter extends CoreBasePresenter<ToolsContract.Model, ToolsContract.View> {
    @Override
    public void onStart() {

    }

    public void getData() {
        mModel.getData().subscribe(bean -> mView.showContent(bean),
                e -> mView.showMsg("数据加载失败"));
    }

}
