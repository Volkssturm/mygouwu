package com.example.mygouwu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ShoppingCartActivity extends BaseActivity {
    private List<Test> data;
    private ListView mListView;
    private ShoppingCartAdapter adapter;

    private RelativeLayout rlRefresh;
    private TextView tvRefresh;
    private ProgressBar barRefresh;
    private LinearLayout clear;
    private CheckBox checkBox_select_all;
    private CheckBox checkBox_add;
    private TextView integral_sum;
    private int sum = 0;
    private int[] sumIntegral;
    private Context context;
    private int showpage;
    private boolean isPermitFlag;


    protected void onCreate(Bundle bundle) {
        // TODO Auto-generated method stub
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        context = this;
        showpage = 1;
        isPermitFlag = true;
        data = new ArrayList<Test>();
        // 测试数据
        data.add(new Test("id", "color", "type", "100"));
        data.add(new Test("id", "color", "type", "200"));
        data.add(new Test("id", "color", "type", "300"));
        data.add(new Test("id", "color", "type", "0"));
        data.add(new Test("id", "color", "type", "300"));
        data.add(new Test("id", "color", "type", "100"));
        data.add(new Test("id", "color", "type", "500"));
        data.add(new Test("id", "color", "type", "0"));
        data.add(new Test("id", "color", "type", "900"));

        adapter = new ShoppingCartAdapter(context, handler, data);

        sumIntegral = new int[data.size() + 1];

        checkBox_add = (CheckBox) findViewById(R.id.checkbox_add);
        integral_sum = (TextView) findViewById(R.id.integral_sum);
        clear = (LinearLayout) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                data.clear();
                List<String> listclearalter= adapter.getclearlter();
                adapter.notifyDataSetChanged();
                integral_sum.setText(0 + "");
                checkBox_select_all.setChecked(false);
                checkBox_add.setClickable(false);
            }
        });
        checkBox_select_all = (CheckBox) findViewById(R.id.checkbox_select);
        checkBox_select_all.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                HashMap<Integer, Boolean> isSelected = ShoppingCartAdapter
                        .getIsSelected();
                Iterator iterator = isSelected.entrySet().iterator();
                List<Boolean> array = new ArrayList<Boolean>();//列表中checkbox选中状态
                List<Integer> nums = new ArrayList<Integer>();//列表中商品数量
                while (iterator.hasNext()) {
                    HashMap.Entry entry = (HashMap.Entry) iterator.next();
                    Integer key = (Integer) entry.getKey();
                    Boolean val = (Boolean) entry.getValue();
                    array.add(val);
                }
                for (int i = 0; i < data.size(); i++) {
                    int num = data.get(i).getNum();
                    int integral = Integer.valueOf(data.get(i).getIntegral());
                    nums.add(num);
                }
                if (checkBox_select_all.isChecked()) {

                    for (int i = 0; i < data.size(); i++) {
                        ShoppingCartAdapter.getIsSelected().put(i, true);
                    }
                    checkBox_add.setChecked(true);
                    adapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        ShoppingCartAdapter.getIsSelected().put(i, false);
                    }
                    checkBox_add.setChecked(false);
                    adapter.notifyDataSetChanged();
                    integral_sum.setText(0 + "");
                }

            }
        });
        mListView= (ListView) findViewById(R.id.finance_list);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                int pos = position;

                int num = data.get(pos).getNum();
                if (num == 0) {
                    Toast.makeText(context, "请选择商品数量", Toast.LENGTH_LONG)
                            .show();
                } else {
                    boolean cu = !ShoppingCartAdapter.getIsSelected().get(pos);
                    ShoppingCartAdapter.getIsSelected().put(pos, cu);
                    adapter.notifyDataSetChanged();
                    //遍历获取列表中checkbox的选中状态
                    HashMap<Integer, Boolean> isSelected = ShoppingCartAdapter
                            .getIsSelected();
                    Iterator iterator = isSelected.entrySet().iterator();
                    List<Boolean> array = new ArrayList<Boolean>();
                    while (iterator.hasNext()) {
                        HashMap.Entry entry = (HashMap.Entry) iterator.next();
                        Integer key = (Integer) entry.getKey();
                        Boolean val = (Boolean) entry.getValue();
                        array.add(val);
                    }
                    if (Test.isAllFalse(array)) {
                        checkBox_select_all.setChecked(false);
                        checkBox_add.setChecked(false);
                    }
                    if (Test.isAllTrue(array)) {
                        checkBox_select_all.setChecked(true);
                        checkBox_add.setChecked(true);
                    }
                    if (Test.isHaveOneFasle(array)) {
                        checkBox_select_all.setChecked(false);
                    }
                    if (Test.isHaveOneTrue(array)) {
                        checkBox_add.setChecked(true);
                    }
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 10){ //更改选中商品的总价格
                float price = (Float)msg.obj;
                if(price > 0){
                    integral_sum.setText(price+"");
                }else{
                    integral_sum.setText("0");
                }
            }
            else if(msg.what == 11){
                //列表选中状态
                List<Boolean> array = (List<Boolean>) msg.obj;
                if (com.example.mygouwu.Test.isAllFalse(array)) {
                    checkBox_select_all.setChecked(false);
                    checkBox_add.setChecked(false);
                }
                if (Test.isAllTrue(array)) {
                    checkBox_select_all.setChecked(true);
                    checkBox_add.setChecked(true);
                }
                if (Test.isHaveOneFasle(array)) {
                    checkBox_select_all.setChecked(false);
                }
                if (Test.isHaveOneTrue(array)) {
                    checkBox_add.setChecked(true);
                }
            }
        }
    };
}