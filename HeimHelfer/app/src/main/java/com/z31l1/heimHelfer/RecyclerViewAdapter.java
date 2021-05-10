package com.z31l1.heimHelfer;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Measure> measureList;
    private RecyclerViewAdapter.ClickListener clickListener;
    public RecyclerViewAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
        measureList = new ArrayList<>();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_layout, parent, false);
        RecyclerViewAdapter.ViewHolder viewHolder = new RecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Measure measure = measureList.get(position);
        holder.txtName.setText(measure.name);
        holder.txtNo.setText("#" + String.valueOf(measure.measureId));
        holder.txtDesc.setText(measure.description);
        holder.txtFilterTag.setText(measure.filterTag);
        holder.txtTimestamp.setText(measure.timeStamp);
    }

    @Override
    public int getItemCount() {
        return measureList.size();
    }

    public void updateMeasureList(List<Measure> data) {
        measureList.clear();
        measureList.addAll(data);
        notifyDataSetChanged();
    }

    public void addRow(Measure data) {
        measureList.add(data);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName;
        public TextView txtNo;
        public TextView txtDesc;
        public TextView txtFilterTag;
        public TextView txtTimestamp;
        public CardView cardView;
        public ViewHolder(View view) {
            super(view);
            txtNo = view.findViewById(R.id.txtNo);
            txtName = view.findViewById(R.id.txtName);
            txtDesc = view.findViewById(R.id.txtDesc);
            txtFilterTag = view.findViewById(R.id.txtFilterTag);
            txtTimestamp = view.findViewById(R.id.txtTimeStamp);
            cardView = view.findViewById(R.id.cardView);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.launchIntent(measureList.get(getAdapterPosition()).measureId);
                }
            });
        }
    }

    public interface ClickListener {
        void launchIntent(int id);
    }
}
