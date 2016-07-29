package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.bean.MemberUserAvatar;
import cn.ucai.fulicenter.bean.Result;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.Utils;

/**
 * Created by Administrator on 2016/7/20.
 */
public class DownloadMemberMaoTask {
    Context mContext;
    String hxid;

    public DownloadMemberMaoTask(Context mContext, String userName) {
        this.mContext = mContext;
        this.hxid = userName;
    }

    public void getContacts() {
        final OkHttpUtils2<String> utils = new OkHttpUtils2<String>();
        utils.setRequestUrl(I.REQUEST_DOWNLOAD_GROUP_MEMBERS_BY_HXID)
                .addParam(I.Member.GROUP_HX_ID,hxid)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String str) {
                        Result result = Utils.getListResultFromJson(str, MemberUserAvatar.class);
                        ArrayList<MemberUserAvatar> list = (ArrayList<MemberUserAvatar>) result.getRetData();
                        if (list != null && list.size() > 0) {
                            Map<String, HashMap<String, MemberUserAvatar>> memberMap =
                                    FuLiCenterApplication.getInstance().getMemberMap();
                            if(!memberMap.containsKey(hxid)){
                                memberMap.put(hxid,new HashMap<String, MemberUserAvatar>());
                            }
                           HashMap<String,MemberUserAvatar>hxidMembers=memberMap.get(hxid);
                            for (MemberUserAvatar u : list) {
                                Log.i("main", u.getMUserName());
                                hxidMembers.put(u.getMUserName(), u);
                            }
                            mContext.sendStickyBroadcast(new Intent("update_member_list"));
                        }
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("main", error);
                    }
                });
    }
}
