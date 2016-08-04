package cn.ucai.fulicenter.activity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewGoodsFragment extends Fragment {
    private final static String TAG=NewGoodsFragment.class.getCanonicalName();
    FuliCenterActivity mContext;
    
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    GoodAdapter mAdapter;
    List<NewGoodBean> mGoodList;
    int pageId=0;
    int action=I.ACTION_DOWNLOAD;
    TextView tvHint;
    public NewGoodsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       mContext= (FuliCenterActivity) getContext();
        View layout = View.inflate(mContext, R.layout.fragment_new_goods, null);
        mGoodList=new ArrayList<NewGoodBean>();
        initView(layout);
        initData();
        setListener();
        return layout;
    }

    private void setListener() {
        setPullDownRefreshListener();
        setPullUpRefreshListener();
    }

    private void setPullUpRefreshListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastItemPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int a=RecyclerView.SCROLL_STATE_DRAGGING;
                int b=RecyclerView.SCROLL_STATE_IDLE;
                int c=RecyclerView.SCROLL_STATE_SETTLING;
                Log.e(TAG,"new State="+newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE
                        && lastItemPosition==mAdapter.getItemCount()-1){
                    if(mAdapter.isMore()){
                        action=I.ACTION_PULL_UP;
                        pageId +=I.PAGE_SIZE_DEFAULT;
                        initData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int f=mGridLayoutManager.findFirstVisibleItemPosition();
                int l=mGridLayoutManager.findLastVisibleItemPosition();
                Log.e(TAG,"f="+f+",l="+l);
                lastItemPosition=mGridLayoutManager.findLastVisibleItemPosition();
                mSwipeRefreshLayout.setEnabled(mGridLayoutManager.findFirstCompletelyVisibleItemPosition()==0);
                if(f==-1||l==-1){
                    lastItemPosition=mAdapter.getItemCount()-1;
                }
            }
        });
    }

    private void setPullDownRefreshListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                action=I.ACTION_PULL_DOWN;
                pageId=0;
                mSwipeRefreshLayout.setRefreshing(true);
                tvHint.setVisibility(View.VISIBLE);
                initData();
            }
        });
    }

    private void initData() {
        try {
            findNewGoodList(new OkHttpUtils2.OnCompleteListener<NewGoodBean[]>() {
                @Override
                public void onSuccess(NewGoodBean[] result) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    tvHint.setVisibility(View.GONE);
                    mAdapter.setMore(true);
                    mAdapter.setFooterString(getResources().getString(R.string.load_more));
                    Log.e(TAG, "result=" + result);
                    if (result != null) {
                        Log.e(TAG, "result.length=" + result.length);
                        ArrayList<NewGoodBean> goodBeanArrayList = Utils.array2List(result);
                        if(action==I.ACTION_DOWNLOAD || action==I.ACTION_PULL_DOWN){
                            mAdapter.initData(goodBeanArrayList);
                        }else {
                            mAdapter.addItem(goodBeanArrayList);
                        }
                        if(goodBeanArrayList.size()<I.PAGE_SIZE_DEFAULT){
                            mAdapter.setMore(false);
                            mAdapter.setFooterString(getResources().getString(R.string.no_more));
                        }
                    }else {
                        mAdapter.setMore(false);
                        mAdapter.setFooterString(getResources().getString(R.string.no_more));
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "error=" + error);
                    mSwipeRefreshLayout.setRefreshing(false);
                    tvHint.setVisibility(View.GONE);
                    Toast.makeText(mContext,error,Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findNewGoodList(OkHttpUtils2.OnCompleteListener<NewGoodBean[]>listener) throws Exception{
        OkHttpUtils2<NewGoodBean[]>utils2=new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_NEW_BOUTIQUE_GOODS)
                .addParam(I.NewAndBoutiqueGood.CAT_ID,String.valueOf(I.CAT_ID))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodBean[].class)
                .execute(listener);
    }

    private void initView(View layout) {
        mSwipeRefreshLayout= (SwipeRefreshLayout) layout.findViewById(R.id.srl_new_good);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_yellow,
                R.color.google_red,
                R.color.google_green
        );
        mRecyclerView= (RecyclerView) layout.findViewById(R.id.rv_new_good);
        mGridLayoutManager=new GridLayoutManager(mContext, I.COLUM_NUM);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter=new GoodAdapter(mContext,mGoodList);
        mRecyclerView.setAdapter(mAdapter);
        tvHint= (TextView) layout.findViewById(R.id.tv_refresh_hint);
    }

}
