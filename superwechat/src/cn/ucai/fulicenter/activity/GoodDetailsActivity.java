package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.DemoHXSDKHelper;
import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.AlbumBean;
import cn.ucai.fulicenter.bean.GoodDetailsBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.task.DownloadCollectCountTask;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.viewholder.DisplayUtils;
import cn.ucai.fulicenter.viewholder.FlowIndicator;
import cn.ucai.fulicenter.viewholder.SlideAutoLoopView;

/**
 * Created by Administrator on 2016/8/3.
 */
public class GoodDetailsActivity extends BaseActivity {
private static final String TAG=GoodDetailsActivity.class.getCanonicalName();
    ImageView ivShare,ivCollect,ivCart;
    TextView tvCartCount;

    TextView tvGoodEnglishName,tvGoodName,tvGoodPriceCurrent,tvGoodPriceShop;

    SlideAutoLoopView mSlideAutoLoopView;

    FlowIndicator mFlowIndicator;

    WebView wvGoodBrief;

    int mGoodId;
    GoodDetailsBean mGoodDetail;
    boolean isCollect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_details);
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        MyOnClickListener listener=new MyOnClickListener();
        ivCollect.setOnClickListener(listener);
    }

    private void initData() {
        mGoodId=getIntent().getIntExtra(D.GoodDetails.KEY_GOODS_ID,0);
        Log.e(TAG,"mGoodId="+mGoodId);
        if(mGoodId>0){
            getGoodDetailsByGoodId(new OkHttpUtils2.OnCompleteListener<GoodDetailsBean>() {
                @Override
                public void onSuccess(GoodDetailsBean result) {
                    Log.e(TAG,"result="+result);
                    if(result!=null){
                        mGoodDetail=result;
                        showGoodDetails();
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG,"error="+error);
                    finish();
                    Toast.makeText(GoodDetailsActivity.this,"获取商品详情数据失败",Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            finish();
            Toast.makeText(GoodDetailsActivity.this,"获取商品详情数据失败",Toast.LENGTH_SHORT).show();
        }
    }

    private void showGoodDetails() {
       tvGoodEnglishName.setText(mGoodDetail.getGoodsEnglishName());
        tvGoodName.setText(mGoodDetail.getGoodsName());
        tvGoodPriceCurrent.setText(mGoodDetail.getCurrencyPrice());
        tvGoodPriceShop.setText(mGoodDetail.getShopPrice());
        mSlideAutoLoopView.startPlayLoop(mFlowIndicator,getAlbumImageUrl(),getAlbumImageSize());
        wvGoodBrief.loadDataWithBaseURL(null,mGoodDetail.getGoodsBrief(),D.TEXT_HTML,D.UTF_8,null);
    }

    private String[] getAlbumImageUrl() {
        String[] albumImageUrl = new String[]{};
        if (mGoodDetail.getProperties() != null && mGoodDetail.getProperties().length > 0) {
            AlbumBean[] album = mGoodDetail.getProperties()[0].getAlbums();
            albumImageUrl = new String[album.length];
            for (int i = 0; i < albumImageUrl.length; i++) {
                albumImageUrl[i] = album[i].getImgUrl();
            }
        }
        return albumImageUrl;
    }

    private int getAlbumImageSize() {
        if(mGoodDetail.getProperties()!=null&&mGoodDetail.getProperties().length>0){
            return mGoodDetail.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private void getGoodDetailsByGoodId(OkHttpUtils2.OnCompleteListener<GoodDetailsBean> listener){
        OkHttpUtils2<GoodDetailsBean>utils2=new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_FIND_GOOD_DETAILS)
                .addParam(D.GoodDetails.KEY_GOODS_ID,String.valueOf(mGoodId))
                .targetClass(GoodDetailsBean.class)
                .execute(listener);
    }

    private void initView() {
        DisplayUtils.initBack(GoodDetailsActivity.this);
        ivShare= (ImageView) findViewById(R.id.iv_good_share);
        ivCollect= (ImageView) findViewById(R.id.iv_good_collect);
        ivCart= (ImageView) findViewById(R.id.iv_good_cart);

        tvCartCount= (TextView) findViewById(R.id.tv_cart_count);

        tvGoodEnglishName= (TextView) findViewById(R.id.tv_good_name_english);
        tvGoodName= (TextView) findViewById(R.id.tv_good_name);
        tvGoodPriceCurrent= (TextView) findViewById(R.id.tv_good_price_current);
        tvGoodPriceShop= (TextView) findViewById(R.id.tv_good_price_shop);

        mSlideAutoLoopView= (SlideAutoLoopView) findViewById(R.id.salv);

        mFlowIndicator= (FlowIndicator) findViewById(R.id.indicator);

        wvGoodBrief= (WebView) findViewById(R.id.wv_good_brief);
        WebSettings settings=wvGoodBrief.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCollectStatus();
    }

    private void initCollectStatus() {
        if(DemoHXSDKHelper.getInstance().isLogined()){
            String userName= FuLiCenterApplication.getInstance().getUserName();
            OkHttpUtils2<MessageBean>utils2=new OkHttpUtils2<>();
            utils2.setRequestUrl(I.REQUEST_IS_COLLECT)
                    .addParam(I.Collect.USER_NAME,userName)
                    .addParam(I.Collect.GOODS_ID,mGoodId+"")
                    .targetClass(MessageBean.class)
                    .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean result) {
                            Log.e(TAG,"result="+result);
                            if(result!=null&&result.isSuccess()){
                                isCollect=true;
                            }else {
                                isCollect=false;
                            }
                            updateCollectStatus();
                        }

                        @Override
                        public void onError(String error) {
                            Log.e(TAG,"error="+error);
                        }
                    });
        }
    }
    class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_good_collect:
                    goodCollect();
                    break;
            }
        }
    }
    private void goodCollect(){
        if(DemoHXSDKHelper.getInstance().isLogined()){
            if(isCollect){
                OkHttpUtils2<MessageBean>utils2=new OkHttpUtils2<>();
                utils2.setRequestUrl(I.REQUEST_DELETE_COLLECT)
                        .addParam(I.Collect.USER_NAME,FuLiCenterApplication.getInstance().getUserName())
                        .addParam(I.Collect.GOODS_ID,mGoodId+"")
                        .targetClass(MessageBean.class)
                        .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if(result!=null&&result.isSuccess()){
                                    isCollect=false;
                                    new DownloadCollectCountTask(GoodDetailsActivity.this,FuLiCenterApplication.getInstance().getUserName()).getContacts();
                                    sendStickyBroadcast(new Intent("update_collect_list"));
                                }else {
                                    Log.e(TAG,"delete fail");
                                }
                                updateCollectStatus();
                                Toast.makeText(GoodDetailsActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(String error) {

                            }
                        });

            }else {
                OkHttpUtils2<MessageBean>utils2=new OkHttpUtils2<>();
                utils2.setRequestUrl(I.REQUEST_ADD_COLLECT)
                        .addParam(I.Collect.USER_NAME,FuLiCenterApplication.getInstance().getUserName())
                        .addParam(I.Collect.GOODS_ID,mGoodDetail.getGoodsId()+"")
                        .addParam(I.Collect.ADD_TIME,mGoodDetail.getAddTime()+"")
                        .addParam(I.Collect.GOODS_ENGLISH_NAME,mGoodDetail.getGoodsEnglishName())
                        .addParam(I.Collect.GOODS_IMG,mGoodDetail.getGoodsImg())
                        .addParam(I.Collect.GOODS_THUMB,mGoodDetail.getGoodsThumb())
                        .addParam(I.Collect.GOODS_NAME,mGoodDetail.getGoodsName())
                        .targetClass(MessageBean.class)
                        .execute(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                            @Override
                            public void onSuccess(MessageBean result) {
                                if(result!=null&&result.isSuccess()){
                                    isCollect=true;
                                    new DownloadCollectCountTask(GoodDetailsActivity.this,FuLiCenterApplication.getInstance().getUserName()).getContacts();
                                    sendStickyBroadcast(new Intent("update_collect_list"));
                                }else {
                                    Log.e(TAG,"delete fail");
                                }
                                updateCollectStatus();
                                Toast.makeText(GoodDetailsActivity.this,result.getMsg(),Toast.LENGTH_SHORT).show();
//                                mContext.sendStickyBroadcast(new Intent("update_collect"));
                            }

                            @Override
                            public void onError(String error) {
                                Log.e(TAG,"error="+error);
                            }
                        });
            }
        }else {
            startActivity(new Intent(GoodDetailsActivity.this,LoginActivity.class));
        }
    }
    private void updateCollectStatus(){
        if(isCollect){
            ivCollect.setImageResource(R.drawable.bg_collect_out);
        }else {
            ivCollect.setImageResource(R.drawable.bg_collect_in);
        }
    }
}