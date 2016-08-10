package cn.ucai.fulicenter.task;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import cn.ucai.fulicenter.FuLiCenterApplication;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.bean.MessageBean;
import cn.ucai.fulicenter.data.OkHttpUtils2;
import cn.ucai.fulicenter.utils.I;

/**
 * Created by Administrator on 2016/8/10.
 */
public class UpdateCartTask {
    private final static String TAG=UpdateCartTask.class.getCanonicalName();
    CartBean mCart;
    Context mContext;

    public UpdateCartTask(CartBean Cart, Context Context) {
        mCart = Cart;
        mContext = Context;
    }
    public void execute(){
        final List<CartBean>cartList= FuLiCenterApplication.getInstance().getCartList();
        if(cartList.contains(mCart)){
            if(mCart.getCount()>0){
                //更新购物车数据
                updateCart(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if(result!=null&&result.isSuccess()){
                            cartList.set(cartList.indexOf(mCart),mCart);
                            mContext.sendStickyBroadcast(new Intent("update_cart_list"));
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }else {
                //删除购物车数据
                deleteCart(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if(result!=null&&result.isSuccess()){
                            cartList.remove(cartList.indexOf(mCart));
                            mContext.sendStickyBroadcast(new Intent("update_cart_list"));
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
            }
        }else {
            //新增购物车数量
            addCart(new OkHttpUtils2.OnCompleteListener<MessageBean>() {
                @Override
                public void onSuccess(MessageBean result) {
                    if(result!=null&&result.isSuccess()){
                        mCart.setId(Integer.valueOf(result.getMsg()));
                        cartList.add(mCart);
                        mContext.sendStickyBroadcast(new Intent("update_cart_list"));
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }
    private void updateCart(OkHttpUtils2.OnCompleteListener<MessageBean>listener){
        OkHttpUtils2<MessageBean>utils2=new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID,mCart.getId()+"")
                .addParam(I.Cart.COUNT,mCart.getCount()+"")
                .addParam(I.Cart.IS_CHECKED,String.valueOf(mCart.isChecked()))
                .targetClass(MessageBean.class)
                .execute(listener);
    }
    private void deleteCart(OkHttpUtils2.OnCompleteListener<MessageBean>listener){
        OkHttpUtils2<MessageBean>utils2=new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_DELETE_CART)
                .addParam(I.Cart.ID,mCart.getId()+"")
                .targetClass(MessageBean.class)
                .execute(listener);
    }
    private void addCart(OkHttpUtils2.OnCompleteListener<MessageBean>listener){
        OkHttpUtils2<MessageBean>utils2=new OkHttpUtils2<>();
        utils2.setRequestUrl(I.REQUEST_ADD_CART)
                .addParam(I.Cart.GOODS_ID,mCart.getGoods().getGoodsId()+"")
                .addParam(I.Cart.COUNT,mCart.getCount()+"")
                .addParam(I.Cart.IS_CHECKED,String.valueOf(mCart.isChecked()))
                .addParam(I.Cart.USER_NAME,FuLiCenterApplication.getInstance().getUserName())
                .targetClass(MessageBean.class)
                .execute(listener);
    }
}
