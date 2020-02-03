package com.anyemi.recska.utils;

/**
 * Created by SuryaTejaChalla on 21-02-2018.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anyemi.recska.R;
import com.anyemi.recska.fragments.home_fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


import java.util.ArrayList;


public class ImageSlideAdapter extends PagerAdapter {

    FragmentActivity activity;
    ArrayList<String> products;
    home_fragment homeFragment;

    public ImageSlideAdapter(FragmentActivity activity, ArrayList<String> products,
                             home_fragment homeFragment) {
        this.activity = activity;
        this.homeFragment = homeFragment;
        this.products = products;
//        options = new DisplayImageOptions.Builder()
//                .showImageOnFail(R.drawable.ic_error)
//                .showStubImage(R.drawable.ic_launcher)
//                .showImageForEmptyUri(R.drawable.ic_empty).cacheInMemory()
//                .cacheOnDisc().build();
//
//        imageListener = new ImageDisplayListener();
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slide_vp_image, container, false);

        ImageView mImageView = view
                .findViewById(R.id.image_display);
        mImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Bundle arguments = new Bundle();
                Fragment fragment = null;
                Log.d("position adapter", "" + position);
               // Product product = (Product) products.get(position);
                //arguments.putParcelable("singleProduct", product);

                // Start a new fragment
//                fragment = new ProductDetailFragment();
//                fragment.setArguments(arguments);
//
//                FragmentTransaction transaction = activity
//                        .getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.content_frame, fragment,
//                        ProductDetailFragment.ARG_ITEM_ID);
//                transaction.addToBackStack(ProductDetailFragment.ARG_ITEM_ID);
//                transaction.commit();
            }
        });
//        imageLoader.displayImage(
//                ((Product) products.get(position)).getImageUrl(), mImageView,
//                options, imageListener);
String base_url=products.get(position);

        Glide.with(activity)
                .load(base_url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                // .centerCrop()
                .error(R.drawable.any_emi_launcher)
                .into(mImageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
