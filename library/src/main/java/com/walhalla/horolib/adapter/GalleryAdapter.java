package com.walhalla.horolib.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.walhalla.horolib.R;
import com.walhalla.horolib.beans.ZodiacSignAstro;
import com.walhalla.horolib.ui.helper.SquareLayout;

import com.walhalla.ui.DLog;

import java.util.List;

/**
 * Created by combo on 18.09.2017.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {


    public interface Presenter {
        void onItemClicked(View view, int lessonPosition);
    }

    private List<ZodiacSignAstro> signs;
    private Context mContext;

    private Presenter mPresenter;

    /**
     * Event Callback
     *
     * @param presenter
     */

    public void setPresenter(@NonNull Presenter presenter) {
        this.mPresenter = presenter;
    }


    public GalleryAdapter(Context context, List<ZodiacSignAstro> signs) {
        mContext = context;
        this.signs = signs;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);
        return new MyViewHolder(itemView, mPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ZodiacSignAstro model = signs.get(position);
        try {
//            Glide.with(mContext)
//                    .load(Uri.parse("file:///android_asset/" + signIcon))
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .error(R.mipmap.ic_launcher)
//                    //.centerCrop()
//                    .into(holder.thumbnail);


//            Glide.with(mContext)
//                    .load(Uri.parse(ASSET_SCHEME + signIcon))
//                    //.error(R.mipmap.ic_launcher)
//                    //.centerCrop()
//                    .into(holder.thumbnail);

            //int color = ContextCompat.getColor(mContext, model.color);
            //holder.container.setBackgroundColor(color);
            holder.name.setText(model.name);

//            holder.thumbnail.setImageResource(model.icon); //-- false in android 10
//            Drawable drawable = AppCompatResources.getDrawable(mContext, model.icon);
//            holder.thumbnail.setImageDrawable(drawable);
            //Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            //holder.thumbnail.setImageDrawable(wrappedDrawable);
            //holder.thumbnail.setImageResource(R.mipmap.ic_launcher);
            holder.thumbnail.setImageDrawable(setTint(model.icon, model.color));

        } catch (Exception e) {
            DLog.handleException(e);
        }
    }
    private Drawable setTint(int d, int color) {
        int ccc = ContextCompat.getColor(mContext, color);
        Drawable raw = AppCompatResources.getDrawable(mContext, d);
        Drawable wrappedDrawable = DrawableCompat.wrap(raw);
        DrawableCompat.setTint(wrappedDrawable, /*color*/ccc);
        return wrappedDrawable;
    }

    @Override
    public int getItemCount() {
        return signs.size();
    }

    interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private GalleryAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    /**
     * Recycler item view
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        SquareLayout container;
        TextView name;

        MyViewHolder(View view, Presenter mPresenter) {
            super(view);
            thumbnail = view.findViewById(R.id.thumbnail);
            container = view.findViewById(R.id.container);
            name = view.findViewById(R.id.tv_name);

            view.setOnClickListener(view1 -> mPresenter.onItemClicked(view1, getAdapterPosition()));
        }
    }
}
