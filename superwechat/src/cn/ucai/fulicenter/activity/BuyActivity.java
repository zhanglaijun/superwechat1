package cn.ucai.fulicenter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.viewholder.DisplayUtils;

public class BuyActivity extends Activity {
    BuyActivity mContext;
    EditText edOrderName,edOrderPhone,edOrderStreet;
    Spinner spinProvince;
    Button btnBuy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_buy);
        initView();
    }

    private void initView() {
        DisplayUtils.initBackWithTitle(mContext,"填写收货地址");
        edOrderName= (EditText) findViewById(R.id.et_address_buyer);
        edOrderPhone= (EditText) findViewById(R.id.et_address_tel);
        edOrderStreet= (EditText) findViewById(R.id.et_address_street);
        spinProvince= (Spinner) findViewById(R.id.spi_address_city);
        btnBuy= (Button) findViewById(R.id.btn_address);
    }

}
