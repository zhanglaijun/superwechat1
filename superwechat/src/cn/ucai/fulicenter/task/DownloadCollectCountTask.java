package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.I;

/**
 * Created by Administrator on 2016/7/20.
 */
public class DownloadCollectCountTask {
    Context mContext;
    String userName;

    public DownloadCollectCountTask(Context mContext, String userName) {
        this.mContext = mContext;
        this.userName = userName;
    }

    public void getContacts() {
        final OkHttpUtils2<MessageBean> utils = new OkHttpUtils2<MessageBean>();
        utils.setRequestUrl(I.REQUEST_FIND_COLLECT_COUNT)
                .addParam(I.Contact.USER_NAME,userName)
                .targetClass(MessageBean.class)
                .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean msg) {
                        if(msg!=null) {
                            if (msg.isSuccess()) {
                                FuLiCenterApplication.getInstance().setCollectCount(Integer.valueOf(msg.getMsg()));
                            }else {
                                FuLiCenterApplication.getInstance().setCollectCount(0);
                            }
                                mContext.sendStickyBroadcast(new Intent("update_collect"));

                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("main", error);
                    }
                });
    }
}
