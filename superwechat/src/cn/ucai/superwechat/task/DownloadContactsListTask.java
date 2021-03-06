package cn.ucai.superwechat.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

import cn.ucai.superwechat.SuperWeChatApplication;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.bean.UserAvatar;
import cn.ucai.superwechat.data.OkHttpUtils2;
import cn.ucai.superwechat.utils.I;
import cn.ucai.superwechat.utils.Utils;

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
                            SuperWeChatApplication.getInstance().setUserList(list);
                            mContext.sendStickyBroadcast(new Intent("update_contact_list"));
                            Map<String, UserAvatar> userMap = SuperWeChatApplication.getInstance().getUserMap();
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
