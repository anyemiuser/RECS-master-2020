package com.anyemi.recska.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anyemi.recska.R;
import com.anyemi.recska.model.InstamojoPaymentModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class PaymentModesAdapter extends BaseAdapter {

    private Context c;
    private LayoutInflater mInflater;
    ViewHolder _listView;
    ArrayList<InstamojoPaymentModel.InstamojoPaymentModesBean> tenant_matches_listings;

    public PaymentModesAdapter(Context c, ArrayList<InstamojoPaymentModel.InstamojoPaymentModesBean> mListings) {
        this.tenant_matches_listings = mListings;
        this.c = c;
        mInflater = LayoutInflater.from(c);
    }


    @Override
    public int getCount() {
        return tenant_matches_listings.size();
    }

    @Override
    public Object getItem(int position) {
        return tenant_matches_listings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // final MyAccountListingsResponse2 studentList = tenant_matches_listings.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lv_payment_tile, null, false);
            _listView = new ViewHolder();
            _listView.tv_shop_name = convertView.findViewById(R.id.tv_shop_name);
            _listView.iv_item = convertView.findViewById(R.id.iv_item);


            convertView.setTag(_listView);
        } else {
            _listView = (ViewHolder) convertView.getTag();
        }

        try {

            _listView.tv_shop_name.setText(tenant_matches_listings.get(position).getName());

            try {
                String url = tenant_matches_listings.get(position).getIcon();
                Glide.with(c)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        // .centerCrop()
                        .error(R.drawable.any_emi_launcher)
                        .into(_listView.iv_item);
            } catch (Exception e) {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_shop_name;
        ImageView iv_item;
    }
}