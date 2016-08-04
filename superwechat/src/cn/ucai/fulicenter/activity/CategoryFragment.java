package cn.ucai.fulicenter.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.CategoryAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.CategoryGroupBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    private  static  final String TAG=CategoryFragment.class.getCanonicalName();
    FuliCenterActivity mContext;
    ExpandableListView mExpandableListView;
    List<CategoryGroupBean>mGroupList;
    List<ArrayList<CategoryChildBean>>mChildList;
    CategoryAdapter mAdapter;


    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = View.inflate(mContext, R.layout.fragment_category, null);
        mContext= (FuliCenterActivity) getContext();
        mGroupList=new ArrayList<CategoryGroupBean>();
        mChildList=new ArrayList<ArrayList<CategoryChildBean>>();
        mAdapter=new CategoryAdapter(mContext,mGroupList,mChildList);
        initView(layout);
        initData();
        return layout;
    }

    private void initData() {
        findCategoryGroupList(new OkHttpUtils2.OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                Log.e(TAG,"result="+result);
                if(result!=null){
                    ArrayList<CategoryGroupBean> groupList= Utils.array2List(result);
                    if(groupList!=null){
                        Log.e(TAG,"grouplist="+groupList.size());
                        mGroupList=groupList;
                        for(CategoryGroupBean g:groupList){
                         findCategoryChildList(new OkHttpUtils2.OnCompleteListener<CategoryChildBean[]>() {
                             @Override
                             public void onSuccess(CategoryChildBean[] result) {
                                 Log.e(TAG,"result="+result);
                                 if(result!=null){
                                     ArrayList<CategoryChildBean>childList=Utils.array2List(result);
                                     Log.e(TAG,"childList="+childList.size());
                                     mChildList.add(childList);
                                     mAdapter.notifyDataSetChanged();
                                 }
                             }

                             @Override
                             public void onError(String error) {
                                 Log.e(TAG,"child error="+error);
                             }
                         },g.getId());
                        }
                    }
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG,"group error="+error);
            }
        });
    }
    private void findCategoryChildList(OkHttpUtils2.OnCompleteListener<CategoryChildBean[]>listener,int parentId){
        OkHttpUtils2<CategoryChildBean[]>utils2=new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID,parentId+"")
                .addParam(I.PAGE_ID,I.PAGE_ID_DEFAULT+"")
                .addParam(I.PAGE_SIZE,I.PAGE_SIZE_DEFAULT+"")
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }
    private void findCategoryGroupList(OkHttpUtils2.OnCompleteListener<CategoryGroupBean[]>listener){
        OkHttpUtils2<CategoryGroupBean[]>utils2=new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }

    private void initView(View layout) {
        mExpandableListView= (ExpandableListView) layout.findViewById(R.id.elevCategory);
        mExpandableListView.setGroupIndicator(null);
        mExpandableListView.setAdapter(mAdapter);
    }

}
