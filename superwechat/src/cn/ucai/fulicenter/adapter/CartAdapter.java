package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.bean.CartBean;
import cn.ucai.fulicenter.utils.ImageUtils;

/**
 * Created by Administrator on 2016/8/1.
 */
public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<CartBean> mCartList;
    CartViewHolder mCartViewHolder;
    boolean isMore;

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public CartAdapter(Context Context, List<CartBean> list) {
        mContext = Context;
        mCartList = new ArrayList<CartBean>();
        mCartList.addAll(list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder= new CartViewHolder(inflater.inflate(R.layout.item_cart,parent,false));
        return  holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CartViewHolder){
            mCartViewHolder = (CartViewHolder) holder;
            final CartBean cart= mCartList.get(position);
            mCartViewHolder.cbCartSelected.setChecked(cart.isChecked());
            ImageUtils.setGoodThumb(mContext, mCartViewHolder.ivCartThumb,cart.getGoods().getGoodsThumb());
            mCartViewHolder.tvCartGoodName.setText(cart.getGoods().getGoodsName());
            mCartViewHolder.tvCartCount.setText("("+cart.getCount()+")");
            mCartViewHolder.tvCartPrice.setText(cart.getGoods().getCurrencyPrice());

        }

    }

    @Override
    public int getItemCount() {
//        return mCartList !=null? mCartList.size()+1:1;
        return mCartList !=null? mCartList.size():0;
    }
    public void initData(List<CartBean>list){
        if(mCartList !=null){
            mCartList.clear();
        }
        mCartList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(List<CartBean> list) {
        mCartList.addAll(list);
        notifyDataSetChanged();
    }

    class CartViewHolder extends RecyclerView.ViewHolder{
        ImageView ivCartThumb,ivCartAdd,ivCartDel;
        CheckBox cbCartSelected;
        TextView tvCartGoodName, tvCartCount,tvCartPrice;
        public CartViewHolder(View itemView) {
            super(itemView);
            ivCartThumb = (ImageView) itemView.findViewById(R.id.iv_cart_thumb);
            tvCartGoodName= (TextView) itemView.findViewById(R.id.tv_cart_good_name);
            tvCartCount = (TextView) itemView.findViewById(R.id.tv_cart_count);
            tvCartPrice= (TextView) itemView.findViewById(R.id.tvBoutiqueTitle);
            cbCartSelected= (CheckBox) itemView.findViewById(R.id.cb_cart_selected);
            ivCartAdd= (ImageView) itemView.findViewById(R.id.iv_cart_add);
            ivCartDel= (ImageView) itemView.findViewById(R.id.iv_cart_del);
        }
    }

}
