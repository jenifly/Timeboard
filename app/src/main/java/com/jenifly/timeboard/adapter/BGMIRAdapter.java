package com.jenifly.timeboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jenifly.timeboard.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jenifly on 2017/6/14.
 */


public class BGMIRAdapter extends RecyclerView.Adapter<BGMIRAdapter.ViewHolder> {

    private List<Integer[]> mList = new ArrayList<>();
    private BGMIRAdapter.onSelectListener mSelectListener;

    public BGMIRAdapter() {
        mList.add(new Integer[]{R.drawable.p1, R.drawable.p1, R.drawable.p1, R.drawable.p1, R.drawable.p1,});
        mList.add(new Integer[]{R.drawable.p1, R.drawable.p1, R.drawable.p1, R.drawable.p1, R.drawable.p1,});
        mList.add(new Integer[]{R.drawable.p1, R.drawable.p1, R.drawable.p1, R.drawable.p1, R.drawable.p1,});
        mList.add(new Integer[]{R.drawable.p1, R.drawable.p1, R.drawable.p1, R.drawable.p1, R.drawable.p1,});
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    public void setOnSelectListener(BGMIRAdapter.onSelectListener listener) {
        mSelectListener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.imageView1.setImageResource(mList.get(i)[0]);
        viewHolder.imageView2.setImageResource(mList.get(i)[1]);
        viewHolder.imageView3.setImageResource(mList.get(i)[2]);
        if(mList.get(i).length > 3){
            viewHolder.textView.setVisibility(View.VISIBLE);
            viewHolder.textView.setText("+" + (mList.get(i).length - 3));
        }else
            viewHolder.textView.setVisibility(View.GONE);
        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectListener != null)
                    mSelectListener.onSelect(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.bg_more_intent_list_p1) ImageView imageView1;
        @BindView(R.id.bg_more_intent_list_p2) ImageView imageView2;
        @BindView(R.id.bg_more_intent_list_p3) ImageView imageView3;
        @BindView(R.id.bg_more_intent_list_tv) TextView textView;
        @BindView(R.id.bg_more_intent_list_item) LinearLayout linearLayout;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
        }
    }

    public interface onSelectListener {
        void onSelect(int position);
    }
}
