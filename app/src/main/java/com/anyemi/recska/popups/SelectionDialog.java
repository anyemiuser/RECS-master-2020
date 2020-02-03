package com.anyemi.recska.popups;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.anyemi.recska.LoginActivity;
import com.anyemi.recska.R;
import com.anyemi.recska.activities.RegisterActivity;
import com.anyemi.recska.model.VerifyOtpModel;
import com.anyemi.recska.utils.Globals;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by SuryaTejaChalla on 20-08-2017.
 */

public class SelectionDialog extends Dialog {
    Dialog mBottomSheetDialog;
    Context contex;
    ViewGroup header, footer;
    Button add, skip;
    ListingAdapter mAdapter;
    //ArrayList<String> mitems_ary = new ArrayList<String>();
    String Selection = "";

    public SelectionDialog(Context context) {
        super(context);
        this.contex = context;
    }

    public void showDialog(final VerifyOtpModel verifyOtpModel, final ArrayList<VerifyOtpModel.UserVpasBean> mitems_ary) {
        View view = getLayoutInflater().inflate(R.layout.custom_dialog_selection, null);
        mBottomSheetDialog = new Dialog(getContext(), R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setTitle("Avaiable Vpas");
        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

        ListView selection = view.findViewById(R.id._lv_selection_list_view);


        LayoutInflater inflater = getLayoutInflater();
        header = (ViewGroup) inflater.inflate(R.layout.lv_upi_header, selection, false);
        footer = (ViewGroup) inflater.inflate(R.layout.lv_upi_footer, selection, false);

        add = footer.findViewById(R.id.add);
        skip = footer.findViewById(R.id.skip);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(contex, LoginActivity.class);
                contex.startActivity(intent);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<VerifyOtpModel.UserVpasBean> selected = new ArrayList<>();


                for (int i = 0; i < mitems_ary.size(); i++) {
                    if (mAdapter.arrayList.contains(mitems_ary.get(i).getVpa())) {
                        selected.add(mitems_ary.get(i));
                    }
                }

                VerifyOtpModel id = new VerifyOtpModel();
                id.setId(verifyOtpModel.getId());
                id.setUser_vpas(selected);


                if(selected.size()>0) {
                    ((RegisterActivity) contex).sumit(new Gson().toJson(id));
                }else{
                    Globals.showToast(contex,"Please AtLeast one UPI ID.");
                }
            }
        });


        selection.addHeaderView(header, null, false);
        selection.addFooterView(footer, null, false);

        ArrayList<String> arrayList = new ArrayList<>();

        for (int i = 0; i < mitems_ary.size(); i++) {
            arrayList.add(mitems_ary.get(i).getVpa());
        }

        mAdapter = new ListingAdapter(contex, mitems_ary,arrayList);
        selection.setAdapter(mAdapter);
        mBottomSheetDialog.show();


    }

    public class ListingAdapter extends BaseAdapter {

        private Context c;
        private LayoutInflater mInflater;
        ViewHolder _listView;
        ArrayList<VerifyOtpModel.UserVpasBean> items;
        public ArrayList<String> arrayList = new ArrayList<>();


        public ListingAdapter(Context c, ArrayList<VerifyOtpModel.UserVpasBean> mListings,ArrayList<String> arrayLis) {
            this.items = mListings;
            this.c = c;
            mInflater = LayoutInflater.from(c);
            arrayList = arrayLis;
        }


        @Override
        public int getCount() {
            //return tenant_matches_listings.size();
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
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
                convertView = mInflater.inflate(R.layout.lv_pick_upi, null, false);
                _listView = new ViewHolder();

                _listView.tv_c_name = (TextView) convertView.findViewById(R.id.tv_c_name);
                _listView.chk_selectio = convertView.findViewById(R.id.chk_selectio);
                _listView.iv_item = convertView.findViewById(R.id.iv_item);

                convertView.setTag(_listView);
            } else {
                _listView = (ViewHolder) convertView.getTag();
            }


            try {
                String url = items.get(position).getIcon().toString();
                Glide.with(getContext())
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        // .centerCrop()
                        .error(R.drawable.any_emi_launcher)
                        .into(_listView.iv_item);
            } catch (Exception e) {
            }

            try {
                _listView.tv_c_name.setText(items.get(position).getVpa());

                if (arrayList.contains(items.get(position).getVpa())) {
                    _listView.chk_selectio.setChecked(true);
                } else {
                    _listView.chk_selectio.setChecked(false);
                }

                _listView.tv_c_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (arrayList.contains(items.get(position).getVpa())) {
                            arrayList.remove(items.get(position).getVpa());
                        } else {
                            arrayList.add(items.get(position).getVpa());
                        }
                        notifyDataSetChanged();
                    }
                });
                _listView.chk_selectio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (arrayList.contains(items.get(position).getVpa())) {
                            arrayList.remove(items.get(position).getVpa());
                        } else {
                            arrayList.add(items.get(position).getVpa());
                        }
                        notifyDataSetChanged();
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder {
            CheckBox chk_selectio;
            TextView tv_c_name;
            ImageView iv_item;

        }
    }


}
