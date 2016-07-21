package cn.ucai.superwechat.task;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Map;

import cn.ucai.superwechat.I;
import cn.ucai.superwechat.SuperWeChatApplication;
import cn.ucai.superwechat.bean.Result;
import cn.ucai.superwechat.bean.UserAvatar;
import cn.ucai.superwechat.utils.OkHttpUtils2;
import cn.ucai.superwechat.utils.Utils;

/**
 * Created by Administrator on 2016/7/20.
 */
public class DownloadContactListTask {
    private final static String TAG=DownloadContactListTask.class.getSimpleName();
    String username;
    Context mContext;

    public DownloadContactListTask(Context Context, String username) {
       mContext = Context;
        this.username = username;
    }
    public void execute(){
        final OkHttpUtils2<String>utils2=new OkHttpUtils2<String>();
        utils2.setRequestUrl(I.REQUEST_DOWNLOAD_CONTACT_ALL_LIST)
                .addParam(I.Contact.USER_NAME,username)
                .targetClass(String.class)
                .execute(new OkHttpUtils2.OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Result result= Utils.getResultFromJson(s, UserAvatar.class);
                        ArrayList<UserAvatar> list= (ArrayList<UserAvatar>) result.getRetData();
                        if(list!=null&&list.size()>0){
                            SuperWeChatApplication.getInstance().setUserList(list);
                            mContext.sendStickyBroadcast(new Intent("update_contact_list"));
                            Map<String,UserAvatar>userMap=SuperWeChatApplication.getInstance().getUserMap();
                            for(UserAvatar u:list){
                                userMap.put(u.getMUserName(),u);
                            }
                        }

                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }
}
