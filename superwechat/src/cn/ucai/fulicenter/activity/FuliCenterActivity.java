package cn.ucai.fulicenter.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import cn.ucai.fulicenter.R;

public class FuliCenterActivity extends BaseActivity implements View.OnClickListener{
    Button mbtnNewGoods,mbtnBoutique,mbtnCategory,mbtnCart,mbtnContact;
    NewGoodsFragment mNewGoodsFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;
    private Fragment[] fragments;
    int index;
    int currentTabIndex;
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


        initFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout,mNewGoodsFragment)
                .add(R.id.layout,mBoutiqueFragment)
                .add(R.id.layout,mCategoryFragment)
                  .hide(mBoutiqueFragment).hide(mCategoryFragment)
                  .show(mNewGoodsFragment)
                .commit();

    }

    private void initFragment() {
        fragments=new Fragment[5];
        mNewGoodsFragment=new NewGoodsFragment();
        mBoutiqueFragment=new BoutiqueFragment();
        mCategoryFragment=new CategoryFragment();
        fragments[0] =  mNewGoodsFragment;
        fragments[1] =  mBoutiqueFragment;
        fragments[2]=mCategoryFragment;
    }

    public void onClick(View v){
        initDrawable();
        switch (v.getId()){
            case R.id.btnNewGoods:
                setDrawable(mbtnNewGoods,R.drawable.menu_item_new_good_selected,Color.BLACK);
                index = 0;
                break;
            case R.id.btnBoutique:
                setDrawable(mbtnBoutique,R.drawable.boutique_selected,Color.BLACK);
                index = 1;
                break;
            case R.id.btnCategory:
                setDrawable(mbtnCategory,R.drawable.menu_item_category_selected,Color.BLACK);
                index = 2;
                break;
            case R.id.btnCart:
                setDrawable(mbtnCart,R.drawable.menu_item_cart_selected,Color.BLACK);
                index = 3;
                break;
            case R.id.btnContact:
                setDrawable(mbtnContact,R.drawable.menu_item_personal_center_selected, Color.BLACK);
                index = 4;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
            currentTabIndex = index;
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
