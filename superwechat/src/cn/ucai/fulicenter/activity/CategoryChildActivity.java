package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.CatChildFilterButton;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.adapter.GoodAdapter;
import cn.ucai.fulicenter.bean.CategoryChildBean;
import cn.ucai.fulicenter.bean.NewGoodBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.Utils;

public class CategoryChildActivity extends Activity {
    private final static String TAG=BoutiqueDetailsActivity.class.getCanonicalName();
    CategoryChildActivity mContext;
    List<NewGoodBean> mGoodList;

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    GridLayoutManager mGridLayoutManager;
    GoodAdapter mAdapter;
    TextView tvHint;
    Button btnSortPrice,btnSortAddTime;
    boolean mSortPriceAsc,mSortAddTimeAsc;
    int sortBy;
    int pageId=0;
    int action= I.ACTION_DOWNLOAD;
    CatChildFilterButton mCatChildFilterButton;
    int catId=0;
    String name;
    ArrayList<CategoryChildBean>childList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext= this;
        setContentView(R.layout.activity_category_child);
        mGoodList=new ArrayList<NewGoodBean>();
        sortBy=I.SORT_BY_ADDTIME_DESC;
        initView();
        initData();
        setListener();
    }
    private void setListener() {
        setPullDownRefreshListener();
        setPullUpRefreshListener();
        SortStatusChangeedListener listener=new SortStatusChangeedListener();
        btnSortPrice.setOnClickListener(listener);
        btnSortAddTime.setOnClickListener(listener);
        mCatChildFilterButton.setOnCatFilterClickListener(name,childList);
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
        catId=getIntent().getIntExtra(I.NewAndBoutiqueGood.CAT_ID,0);
        Log.e(TAG,"catId="+catId);
        childList= (ArrayList<CategoryChildBean>) getIntent().getSerializableExtra("childList");
        if(catId<0){
            finish();
        }
        try {
            findBoutiqueDetailsList(new OkHttpUtils2.OnCompleteListener<NewGoodBean[]>() {
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

    private void findBoutiqueDetailsList(OkHttpUtils2.OnCompleteListener<NewGoodBean[]>listener) throws Exception{
        OkHttpUtils2<NewGoodBean[]>utils2=new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_GOODS_DETAILS)
                .addParam(I.NewAndBoutiqueGood.CAT_ID,String.valueOf(catId))
                .addParam(I.PAGE_ID,String.valueOf(pageId))
                .addParam(I.PAGE_SIZE,String.valueOf(I.PAGE_SIZE_DEFAULT))
                .targetClass(NewGoodBean[].class)
                .execute(listener);
    }

    private void initView() {
        mSwipeRefreshLayout= (SwipeRefreshLayout)findViewById(R.id.srl_category_details);
        mSwipeRefreshLayout.setColorSchemeColors(
                R.color.google_blue,
                R.color.google_yellow,
                R.color.google_red,
                R.color.google_green
        );
        mGridLayoutManager=new GridLayoutManager(mContext, I.COLUM_NUM);
        mGridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView= (RecyclerView)findViewById(R.id.rv_category_details);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter=new GoodAdapter(mContext,mGoodList);
        mRecyclerView.setAdapter(mAdapter);
        tvHint= (TextView)findViewById(R.id.tv_refresh_hint);
        btnSortPrice= (Button) findViewById(R.id.btn_sort_price);
        btnSortAddTime= (Button) findViewById(R.id.btn_sort_addtime);
        mCatChildFilterButton= (CatChildFilterButton) findViewById(R.id.btnCatChildFilter);
        name=getIntent().getStringExtra(I.CategoryGroup.NAME);
        mCatChildFilterButton.setText(name);
    }
    class SortStatusChangeedListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Drawable right;
            switch (v.getId()){
                case R.id.btn_sort_price:
                    if(mSortPriceAsc){
                        sortBy=I.SORT_BY_PRICE_ASC;
                        right=getResources().getDrawable(R.drawable.arrow_order_up);
                    }else {
                        sortBy=I.SORT_BY_PRICE_DESC;
                        right=getResources().getDrawable(R.drawable.arrow_order_down);
                    }
                    mSortPriceAsc=!mSortPriceAsc;
                    right.setBounds(0,0,right.getIntrinsicWidth(),right.getIntrinsicHeight());
                    btnSortPrice.setCompoundDrawablesWithIntrinsicBounds(null,null,right,null);
                    break;
                case R.id.btn_sort_addtime:
                    if(mSortAddTimeAsc){
                        sortBy=I.SORT_BY_ADDTIME_ASC;
                        right=getResources().getDrawable(R.drawable.arrow_order_up);
                    }else {
                        sortBy=I.SORT_BY_ADDTIME_DESC;
                        right=getResources().getDrawable(R.drawable.arrow_order_down);
                    }
                    mSortAddTimeAsc=!mSortAddTimeAsc;
                    right.setBounds(0,0,right.getIntrinsicWidth(),right.getIntrinsicHeight());
                    btnSortAddTime.setCompoundDrawablesWithIntrinsicBounds(null,null,right,null);
                    break;
            }
            mAdapter.setSortBy(sortBy);
        }
    }

}
