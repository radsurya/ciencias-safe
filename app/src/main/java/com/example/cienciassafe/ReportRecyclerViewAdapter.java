package com.example.cienciassafe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter used to show a simple grid of products.
 */
public class ReportRecyclerViewAdapter extends RecyclerView.Adapter<ReportCardViewHolder> {

    private List<String> reportsList;

    ReportRecyclerViewAdapter(List<String> reportsList) {
        this.reportsList = reportsList;
    }

    @NonNull
    @Override
    public ReportCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_card, parent, false);
        return new ReportCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportCardViewHolder holder, int position) {
        if (reportsList != null && position < reportsList.size()) {
            final String report = reportsList.get(position);
            holder.Date.setText(report.split(";")[1]);
            holder.Building.setText(holder.itemView.getContext().getString(R.string.building_report, report.split(";")[2].split("\\n")[0].split(": ")[1]));
            holder.Room.setText(holder.itemView.getContext().getString(R.string.room_report, report.split(";")[2].split("\\n")[1].split(": ")[1]));
            if (report.split(";")[2].split("\\n")[2].split(": ")[1].equals("Álcool Gel")) {
                holder.Resource.setText(R.string.alcohol_gel);
            } else {
                holder.Resource.setText(holder.itemView.getContext().getString(R.string.resource_report, report.split(";")[2].split("\\n")[2].split(": ")[1]));
            }
        }
    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }
}