package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

import cn.ucai.fulicenter.SuperWeChatApplication;
import cn.ucai.fulicenter.bean.GroupAvatar;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/7/20.
 */
public class DownloadGroupListTask {
    Context mContext;
    String userName;

    public DownloadGroupListTask(Context mContext, String userName) {
        this.mContext = mContext;
        this.userName = userName;
    }

    public void getContacts() {
        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_FIND_GROUP_BY_USER_NAME)
                .addParam(I.User.USER_NAME,userName)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String str) {
                        Result result = Utils.getListResultFromJson(str, GroupAvatar.class);
                        ArrayList<GroupAvatar> list = (ArrayList<GroupAvatar>) result.getRetData();
                        if (list != null && list.size() > 0) {
                            SuperWeChatApplication.getInstance().setGroupList(list);
                            for(GroupAvatar g:list){
                                SuperWeChatApplication.getInstance().getGroupMap().put(g.getMGroupHxid(),g);
                            }
                            mContext.sendStickyBroadcast(new Intent("update_group_list"));

                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("main", error);
                    }
                });
    }
}
