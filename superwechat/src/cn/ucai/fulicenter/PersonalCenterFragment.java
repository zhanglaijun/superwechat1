package cn.ucai.fulicenter;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.ucai.fulicenter.activity.FuliCenterActivity;
import cn.ucai.fulicenter.activity.SettingsActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalCenterFragment extends Fragment {
     private static final String TAG=PersonalCenterFragment.class.getCanonicalName();
    FuliCenterActivity mContext;

    ImageView mivUserAvatar;
    TextView mtvUserName;
    TextView mtvSetting;
    ImageView mivMSG;
    TextView mtvCollectCount;
    RelativeLayout layoutUserCenter;
    LinearLayout layoutCollect;

    public PersonalCenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext= (FuliCenterActivity) getContext();
        View layout=View.inflate(mContext,R.layout.fragment_personal_center,null);
        initView(layout);
        setListener();
        return layout;
    }

    private void setListener() {
        MyClickListener listener=new MyClickListener();
        mtvSetting.setOnClickListener(listener);
        layoutUserCenter.setOnClickListener(listener);
    }
    class MyClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(DemoHXSDKHelper.getInstance().isLogined()){
                switch (v.getId()){
                    case R.id.tv_center_setting:
                    case R.id.center_user_info:
                        startActivity(new Intent(mContext, SettingsActivity.class));
                        break;
                }
            }
        }
    }

    private void initView(View layout) {
        mivUserAvatar= (ImageView) layout.findViewById(R.id.iv_user_avatar);
        mtvUserName= (TextView) layout.findViewById(R.id.tv_user_name);
        mtvSetting= (TextView) layout.findViewById(R.id.tv_center_setting);
        mivMSG= (ImageView) layout.findViewById(R.id.iv_persona_center_msg);
        mtvCollectCount= (TextView) layout.findViewById(R.id.tv_collect_count);
        layoutUserCenter= (RelativeLayout) layout.findViewById(R.id.center_user_info);
        layoutCollect= (LinearLayout) layout.findViewById(R.id.layout_center_collect);

        initOrderList(layout);
    }

    private void initOrderList(View layout) {
        GridView gvOrderList= (GridView) layout.findViewById(R.id.center_user_order_list);
        ArrayList<HashMap<String,Object>>data=new ArrayList<>();
        HashMap<String,Object>order1=new HashMap<>();
        order1.put("order",R.drawable.order_list1);
        data.add(order1);
        HashMap<String,Object>order2=new HashMap<>();
        order2.put("order",R.drawable.order_list2);
        data.add(order2);
        HashMap<String,Object>order3=new HashMap<>();
        order3.put("order",R.drawable.order_list3);
        data.add(order3);
        HashMap<String,Object>order4=new HashMap<>();
        order4.put("order",R.drawable.order_list4);
        data.add(order4);
        HashMap<String,Object>order5=new HashMap<>();
        order5.put("order",R.drawable.order_list5);
        data.add(order5);

        SimpleAdapter adapter=new SimpleAdapter(mContext,data,R.layout.simple_adapter,
                new String[]{"order"},new int[]{R.id.iv_order});
        gvOrderList.setAdapter(adapter);
    }

}
