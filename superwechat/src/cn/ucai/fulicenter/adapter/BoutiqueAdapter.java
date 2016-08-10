package cn.ucai.fulicenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ucai.fulicenter.D;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.activity.BoutiqueDetailsActivity;
import cn.ucai.fulicenter.bean.BoutiqueBean;
import cn.ucai.fulicenter.utils.I;
import cn.ucai.fulicenter.utils.ImageUtils;
import cn.ucai.fulicenter.viewholder.FooterViewHolder;

/**
 * Created by Administrator on 2016/8/1.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<BoutiqueBean> mBoutiqueList;
    BoutiqueViewHolder mBoutiqueViewHolder;
//    FooterViewHolder mFooterViewHolder;
    boolean isMore;
    String footerString;


    public BoutiqueAdapter(Context Context, List<BoutiqueBean> list) {
        mContext = Context;
        mBoutiqueList = new ArrayList<BoutiqueBean>();
        mBoutiqueList.addAll(list);
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public void setFooterString(String footerString) {
        this.footerString = footerString;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mContext);
        RecyclerView.ViewHolder holder=null;
        switch (viewType){
            case I.TYPE_FOOTER:
            holder=new FooterViewHolder(inflater.inflate(R.layout.item_footer,parent,false));
                break;
            case I.TYPE_ITEM:
                holder=new BoutiqueViewHolder(inflater.inflate(R.layout.item_boutique,parent,false));
                break;
        }
        return  holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BoutiqueViewHolder){
            mBoutiqueViewHolder = (BoutiqueViewHolder) holder;
            final BoutiqueBean boutique= mBoutiqueList.get(position);
            ImageUtils.setGoodThumb(mContext, mBoutiqueViewHolder.ivBoutiqueThumb,boutique.getImageurl());
            mBoutiqueViewHolder.tvBoutiqueName.setText(boutique.getName());
            mBoutiqueViewHolder.tvBoutiqueDesc.setText(boutique.getDescription());
            mBoutiqueViewHolder.tvBoutiqueTitle.setText(boutique.getTitle());
            mBoutiqueViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext,BoutiqueDetailsActivity.class)
                    .putExtra(D.Boutique.KEY_GOODS_ID,boutique.getId())
                    .putExtra(D.Boutique.KEY_NAME,boutique.getName()) );

                }
            });
        }
      /*  if(holder instanceof FooterViewHolder){
            mFooterViewHolder= (FooterViewHolder) holder;
            mFooterViewHolder.tvFooter.setText(footerString);
        }*/

    }

    @Override
    public int getItemViewType(int position) {
       /* if(position==getItemCount()-1){
            return I.TYPE_FOOTER;
        }else {*/
            return I.TYPE_ITEM;
//        }
    }

    @Override
    public int getItemCount() {
//        return mCartList !=null? mCartList.size()+1:1;
        return mBoutiqueList !=null? mBoutiqueList.size():0;
    }
    public void initData(ArrayList<BoutiqueBean>list){
        if(mBoutiqueList !=null){
            mBoutiqueList.clear();
        }
        mBoutiqueList.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<BoutiqueBean> list) {
        mBoutiqueList.addAll(list);
        notifyDataSetChanged();
    }

    class BoutiqueViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout layout;
        ImageView ivBoutiqueThumb;
        TextView tvBoutiqueName, tvBoutiqueDesc,tvBoutiqueTitle;
        public BoutiqueViewHolder(View itemView) {
            super(itemView);
            layout= (RelativeLayout) itemView.findViewById(R.id.layout_boutique_item);
            ivBoutiqueThumb = (ImageView) itemView.findViewById(R.id.ivBoutiqueImg);
            tvBoutiqueName= (TextView) itemView.findViewById(R.id.tvBoutiqueName);
            tvBoutiqueDesc = (TextView) itemView.findViewById(R.id.tvBoutiqueDescription);
            tvBoutiqueTitle= (TextView) itemView.findViewById(R.id.tvBoutiqueTitle);
        }
    }

}
