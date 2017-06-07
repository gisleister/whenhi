package com.whenhi.hi.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.whenhi.hi.R;
import com.whenhi.hi.listener.GlideListener;

import java.lang.ref.WeakReference;

import uk.co.senab.photoview.PhotoView;


/**
 * Created by 王雷 on 2017/5/23.
 */

public class ImageFragment extends Fragment {


    private static final String IMAGE_URL = "image";
    PhotoView image;
    private String imageUrl;
    private GlideListener mGlideListener;

    public ImageFragment() {
        mGlideListener = new GlideListener();
    }

    public static ImageFragment newInstance(String param1) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_pic_new, container, false);
        image = (PhotoView) view.findViewById(R.id.image);
        //Glide.with(getContext()).load(imageUrl).into(image);

        WeakReference<PhotoView> imageViewWeakReference = new WeakReference<>(image);
        PhotoView target = imageViewWeakReference.get();

        Glide.with(getContext())
                .load(imageUrl)
                //.placeholder(R.drawable.shape_progressbar_mini)
                //.centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .listener(mGlideListener.getListener())
                .error(R.mipmap.bg_image)
                //.override(600, 200)
                //.fitCenter()
                .into(target);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
