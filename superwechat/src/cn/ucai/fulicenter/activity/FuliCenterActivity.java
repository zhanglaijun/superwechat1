package cn.ucai.fulicenter.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import cn.ucai.fulicenter.DemoHXSDKHelper;
import cn.ucai.fulicenter.PersonalCenterFragment;
import cn.ucai.fulicenter.R;

public class FuliCenterActivity extends BaseActivity implements View.OnClickListener{
    Button mbtnNewGoods,mbtnBoutique,mbtnCategory,mbtnCart,mbtnContact;
    NewGoodsFragment mNewGoodsFragment;
    BoutiqueFragment mBoutiqueFragment;
    CategoryFragment mCategoryFragment;
    PersonalCenterFragment mPersonalCenterFragment;
    private Fragment[] fragments;
    int index;
    int currentTabIndex;
    public static final int ACTION_LOGIN=100;
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

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.layout,mNewGoodsFragment)
                .add(R.id.layout,mBoutiqueFragment)
                .add(R.id.layout,mCategoryFragment)
                .add(R.id.layout,mPersonalCenterFragment)
                .hide(mBoutiqueFragment).hide(mCategoryFragment).hide(mPersonalCenterFragment)
                .show(mNewGoodsFragment)
                .commit();

    }

    private void initFragment() {
        fragments=new Fragment[5];
        mNewGoodsFragment=new NewGoodsFragment();
        mBoutiqueFragment=new BoutiqueFragment();
        mCategoryFragment=new CategoryFragment();
        mPersonalCenterFragment=new PersonalCenterFragment();
        fragments[0] =  mNewGoodsFragment;
        fragments[1] =  mBoutiqueFragment;
        fragments[2]=mCategoryFragment;
        fragments[4]=mPersonalCenterFragment;

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
                setDrawable(mbtnContact, R.drawable.menu_item_personal_center_selected, Color.BLACK);
                if(DemoHXSDKHelper.getInstance().isLogined()) {
                    index = 4;
                }else {
                    gotoLogin();
                }
                break;
        }
        setFragment();

    }

    private void setFragment() {
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

    private void gotoLogin() {
        startActivityForResult(new Intent(this,LoginActivity.class),ACTION_LOGIN);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(DemoHXSDKHelper.getInstance().isLogined()){
//              startActivity(new Intent(this, PersonalCenterFragment.class));
        }else {
            index=currentTabIndex;
            if(index==4){
                index=0;
            }
            setFragment();
        }
    }
}
