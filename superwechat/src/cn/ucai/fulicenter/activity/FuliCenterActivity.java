package cn.ucai.fulicenter.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import cn.ucai.fulicenter.R;

public class FuliCenterActivity extends BaseActivity implements View.OnClickListener{
    Button mbtnNewGoods,mbtnBoutique,mbtnCategory,mbtnCart,mbtnContact;
    NewGoodsFragment mNewGoodsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuli_center);
        initView();
        setListener();
    }

    private void setListener() {
        findViewById(R.id.btnBoutique).setOnClickListener(this);
        findViewById(R.id.btnCart).setOnClickListener(this);
        findViewById(R.id.btnCategory).setOnClickListener(this);
        findViewById(R.id.btnContact).setOnClickListener(this);
        findViewById(R.id.btnNewGoods).setOnClickListener(this);
    }

    private void initView() {
        mbtnNewGoods= (Button) findViewById(R.id.btnNewGoods);
        mbtnBoutique= (Button) findViewById(R.id.btnBoutique);
        mbtnCategory= (Button) findViewById(R.id.btnCategory);
        mbtnCart= (Button) findViewById(R.id.btnCart);
        mbtnContact= (Button) findViewById(R.id.btnContact);

        mNewGoodsFragment=new NewGoodsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,mNewGoodsFragment)
//                .add(R.id.fragment_container,contactlistFragment)
//                  .hide(contactListFragment)
                  .show(mNewGoodsFragment)
                .commit();

    }
    public void onClick(View v){
        initDrawable();
        switch (v.getId()){
            case R.id.btnNewGoods:
                setDrawable(mbtnNewGoods,R.drawable.menu_item_new_good_selected,Color.BLACK);
                break;
            case R.id.btnBoutique:
                setDrawable(mbtnBoutique,R.drawable.boutique_selected,Color.BLACK);
                break;
            case R.id.btnCategory:
                setDrawable(mbtnCategory,R.drawable.menu_item_category_selected,Color.BLACK);
                break;
            case R.id.btnCart:
                setDrawable(mbtnCart,R.drawable.menu_item_cart_selected,Color.BLACK);
                break;
            case R.id.btnContact:
                setDrawable(mbtnContact,R.drawable.menu_item_personal_center_selected, Color.BLACK);
                break;
        }

    }
    private void initDrawable() {
        setDrawable(mbtnNewGoods, R.drawable.menu_item_new_good_normal, Color.GRAY);
        setDrawable(mbtnContact, R.drawable.menu_item_personal_center_normal, Color.GRAY);
        setDrawable(mbtnCart, R.drawable.menu_item_cart_normal, Color.GRAY);
        setDrawable(mbtnCategory, R.drawable.menu_item_category_normal, Color.GRAY);
        setDrawable(mbtnBoutique, R.drawable.boutique_normal, Color.GRAY);
    }
    private void setDrawable(Button button, int id, int color) {
        button.setTextColor(color);
        Drawable drawable = ContextCompat.getDrawable(this, id);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
        button.setCompoundDrawables(null,drawable,null,null);
    }


}
