package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.bean.UserAvatar;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/7/20.
 */
public class DownloadContactsListTask {
    Context mContext;
    String userName;

    public DownloadContactsListTask(Context mContext, String userName) {
        this.mContext = mContext;
        this.userName = userName;
    }

    public void getContacts() {
        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST)
                .addParam(I.Contact.USER_NAME,userName)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String str) {
                        Result result = Utils.getListResultFromJson(str, UserAvatar.class);
                        ArrayList<UserAvatar> list = (ArrayList<UserAvatar>) result.getRetData();
                        if (list != null && list.size() > 0) {
                            FuLiCenterApplication.getInstance().setUserList(list);
                            mContext.sendStickyBroadcast(new Intent("update_contact_list"));
                            Map<String, UserAvatar> userMap = FuLiCenterApplication.getInstance().getUserMap();
                            for (UserAvatar u : list) {
                                Log.i("main", u.getMUserName());
                                userMap.put(u.getMUserName(), u);
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("main", error);
                    }
                });
    }
}
