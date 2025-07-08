package com.walhalla.horolib.adapter;

/**
 * Created by combo on 29.05.2017.
 */

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.walhalla.horolib.R;
import com.walhalla.horolib.beans.ZodiacSignAstro;
import com.walhalla.horolib.core.ChildItemClickListener;
import com.walhalla.ui.DLog;

import java.util.ArrayList;
import java.util.List;

public class SignAdapter extends RecyclerView.Adapter<SignAdapter.ViewHolder> {


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static String FORMAT_LESSON_STEPS;
    private static String LESSON_COMPLEXITY;
    private static String LESSON_ICON;
    private static String FORMAT_LESSON_FOLDER;
    private final int[] rainbow;

    private Context mContext;
    private List<ZodiacSignAstro> arr = new ArrayList<>();

    private ChildItemClickListener mPresenter;
    private String TAG = this.getClass().getSimpleName();

    public SignAdapter(Context context) {
        this.mContext = context;
        this.rainbow = context.getResources().getIntArray(R.array.rainbow);
    }

    public void setListener(ChildItemClickListener listener) {
        mPresenter = listener;
    }

    public SignAdapter(List<ZodiacSignAstro> data, Context context) {
        this.rainbow = context.getResources().getIntArray(R.array.rainbow);
        this.mContext = context;
        this.arr = data;
    }

    public void swap(List<ZodiacSignAstro> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        if (arr != null && arr.size() > 0) {
            arr.clear();
        }
        arr.addAll(data);
        notifyDataSetChanged();
        notifyItemRangeChanged(0, getItemCount());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_lesson_item, parent, false);

        return new ViewHolder(itemView, mPresenter);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        ZodiacSignAstro model = arr.get(holder.getAdapterPosition());
        // 3. set the data to your Views here

        int id = model.id;
//        String lessonFolder = String.format(Locale.CANADA, FORMAT_LESSON_FOLDER, id);
        //current dir
        // assets/lesson01


        // holder.tvId.setText(lessonFolder);


//        String icon = lessonFolder + File.separator + LESSON_ICON;
//        try {
//            InputStream is = mContext.getAssets().open(icon);
////            Bitmap bitmap = BitmapFactory.decodeStream(is);
////            holder.icon.setImageBitmap(bitmap);
//
//            Drawable d = Drawable.createFromStream(is, null);//20 fail
//            // set image to ImageView
//            holder.icon.setImageDrawable(d);


//            String url = ASSET_SCHEME + icon;

//            Glide.with(mContext).load(model.getIcon())
////                    .asBitmap().override(70, 70)
////                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(holder.icon);
////



        //holder.icon.setImageResource(model.icon);
        holder.icon.setImageDrawable(setTint(model.icon, model.color));

//        holder.icon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Random rnd = new Random();
//                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//                holder.icon.setColorFilter(color);
//            }
//        });


//
//
//        } catch (Exception e) {
//            //Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//            //Log.d(TAG, "onBindViewHolder: " + icon);
//        }


        holder.tvName.setText(model.name);
        holder.tvSteps.setText(model.date);

//        int rate = model.getRate();
////        holder.ratingBar.setImageResource(ids[rate]);
//        setRate(holder.ratingBar, rate);
//
//        holder.tvRate.setText(
//                String.format(Locale.CANADA, LESSON_COMPLEXITY, model.getRate())
//        );
//
//        if (model.getLock() == LessonState.LOCK.getValue()) {
//            holder.ivLock.setImageResource(R.drawable.lock);
//            holder.ivLock.setVisibility(View.VISIBLE);
//        } else holder.ivLock.setVisibility(View.GONE);

//        holder.tvOrder.setText(String.valueOf(model.getOrder()));
//        holder.tvBlack.setText(String.valueOf(model.getIcon()));
//        holder.tvDiamond.setText(String.valueOf(model.getDate()));

        holder.itemView.setOnClickListener(v -> {
            // Do something!
            try {
                mPresenter.onClick(v, holder.getAdapterPosition());
            } catch (Exception e) {
                DLog.handleException(e);
            }
        });
    }

    private Drawable setTint(int d, int color) {
        int ccc = ContextCompat.getColor(mContext, color);
        Drawable raw = AppCompatResources.getDrawable(mContext, d);
        Drawable wrappedDrawable = DrawableCompat.wrap(raw);
        DrawableCompat.setTint(wrappedDrawable, /*color*/ccc);
        return wrappedDrawable;
    }

    private void setRate(RatingBar ratingBar, int valCurrent) {

        float S_COUNT = 5f;
        float VAL_MAX = 5f;

        ratingBar.setMax((int) VAL_MAX);
        ratingBar.setNumStars((int) S_COUNT);
        ratingBar.setStepSize(VAL_MAX / S_COUNT / 3);

        ratingBar.setRating(S_COUNT * (valCurrent / VAL_MAX));
        ratingBar.setIsIndicator(true);
    }

    @Override
    public int getItemCount() {
        return (arr == null) ? 0 : arr.size();
    }

    //=============================================================================================
    final static class ViewHolder extends RecyclerView.ViewHolder {

        //        private final TextView tvRate;
        private final ImageView icon;
        private final TextView tvName;
        private final TextView tvSteps;
//        private final RatingBar ratingBar;
//        private final ImageView ivLock;

        /**
         * Callback
         */
        private final ChildItemClickListener mPresenter;


//        @BindView(R.id.tv_order)
//        TextView tvOrder;
//        @BindView(R.id.tv_black)
//        TextView tvBlack;
//        @BindView(R.id.tv_diamond)
//        TextView tvDiamond;

        ViewHolder(View v, ChildItemClickListener mPresenter) {
            super(v);
            this.mPresenter = mPresenter;

//            tvRate = v.findViewById(R.id.tv_rate);
            icon = v.findViewById(R.id.iv_icon);
            tvName = v.findViewById(R.id.tv_name);
            tvSteps = v.findViewById(R.id.tv_steps);
//            ratingBar = v.findViewById(R.id.rb_rate);
//            ivLock = v.findViewById(R.id.iv_lock);

            //v.setOnClickListener(this);
            //icon.setOnClickListener(this);

        }


//      implements View.OnClickListener   @Override
//        public void onClick(View v) {
//            this.mPresenter.onClick(v, getAdapterPosition());
//        }
    }
    //=============================================================================================

}

