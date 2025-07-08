package com.walhalla.horolib.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.walhalla.horolib.R;
import com.walhalla.horolib.beans.ZodiacSignAstro;
import com.walhalla.horolib.core.ChildItemClickListener;

import java.util.List;


/**
 * Created by combo on 29.05.2017.
 */
public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_LESSON = 0, ERROR = 1;
    private ChildItemClickListener childItemClickListener;
    // The items to display in your RecyclerView
    private List<ZodiacSignAstro> items;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ComplexRecyclerViewAdapter(List<ZodiacSignAstro> items) {
        this.items = items;
    }

    public void setChildItemClickListener(ChildItemClickListener listener) {
        childItemClickListener = listener;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.items.size();
    }

    //Returns the view type of the item at position for the purposes of view recycling.
    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof ZodiacSignAstro) {
            return TYPE_LESSON;
        }
//        else if (items.get(position) instanceof APIError) {
//            return ERROR;
//        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        if (viewType == TYPE_LESSON) {
            View v1 = inflater.inflate(R.layout.row_lesson_item, viewGroup, false);
            viewHolder = new LessonViewHolder(v1);
            //            case ERROR:
//                View v2 = inflater.inflate(R.layout.row_item_error, viewGroup, false);
//                viewHolder = new ViewHolder2(v2);
//                break;
        } else {
            View v = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
            viewHolder = new RecyclerViewSimpleTextViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TYPE_LESSON:
                LessonViewHolder vh1 = (LessonViewHolder) viewHolder;
                configureLessonViewHolder(vh1, position);
                break;
//            case ERROR:
//                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
//                configureViewHolder2(vh2, position);
//                break;
            default:
                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) viewHolder;
                configureDefaultViewHolder(vh, position);
                break;
        }
    }


    //private void configureDefaultViewHolder(RecyclerViewSimpleTextViewHolder vh, int position) {
    //vh.getLabel().setText((CharSequence) items.get(position));
    //}

    private void configureLessonViewHolder(LessonViewHolder holder, int position) {
        ZodiacSignAstro zodiacSignAstro = items.get(position);
        if (zodiacSignAstro != null) {
//            holder.tvId.setText(zodiacSign.getId());
//            holder.tvName.setText(zodiacSign.getName());
//            holder.tvSteps.setText(zodiacSign.getSteps());
//            holder.tvRate.setText(zodiacSign.getRate());
//            holder.tvLock.setText(zodiacSign.getLock());
//            holder.tvOrder.setText(zodiacSign.getOrder());
//            holder.tvBlack.setText(zodiacSign.getIcon());
//            holder.tvDiamond.setText(zodiacSign.getDate());

        }
    }

//    private void configureViewHolder2(ViewHolder2 vh2, int positon) {
//        //vh2.getImageView().setImageResource(R.drawable.sample_golden_gate);
//        APIError error = (APIError) items.get(positon);
//        if (error != null) {
//            vh2.error_msg.setText(error.getErrorMsg());
//        }
//    }

    //========================================================================================
    private class LessonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        @BindView(R.id.tv_id)
//        TextView tvId;
//        @BindView(R.id.tv_name)
//        TextView tvName;
//        @BindView(R.id.tv_steps)
//        TextView tvSteps;
//        @BindView(R.id.tv_rate)
//        TextView tvRate;
//        @BindView(R.id.tv_lock)
//        TextView tvLock;
//        @BindView(R.id.tv_order)
//        TextView tvOrder;
//        @BindView(R.id.tv_black)
//        TextView tvBlack;
//        @BindView(R.id.tv_diamond)
//        TextView tvDiamond;

        LessonViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            childItemClickListener.onClick(v, getAdapterPosition());//clicker...
        }
    }

    private void configureDefaultViewHolder(RecyclerViewSimpleTextViewHolder vh, int position) {
        //vh.getLabel().setText((CharSequence) items.get(position));
    }

    class RecyclerViewSimpleTextViewHolder extends RecyclerView.ViewHolder {

        TextView text1;

        RecyclerViewSimpleTextViewHolder(View itemView) {
            super(itemView);
        }
    }
}
