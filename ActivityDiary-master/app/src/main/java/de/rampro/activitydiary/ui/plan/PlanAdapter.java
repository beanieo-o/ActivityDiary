package de.rampro.activitydiary.ui.plan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.rampro.activitydiary.R;

public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.PlanViewHolder> {
    private List<Plan> planList; // 存储计划数据的列表

    // 构造函数
    public PlanAdapter(List<Plan> planList) {
        this.planList = planList;
    }

    // 创建 ViewHolder
    @Override
    public PlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_list_item, parent, false);
        return new PlanViewHolder(view);
    }

    // 绑定数据到 ViewHolder
    @Override
    public void onBindViewHolder(PlanViewHolder holder, int position) {
        Plan plan = planList.get(position);
        holder.timeTextView.setText(plan.getTime());
        holder.titleTextView.setText(plan.getTitle());
    }

    // 返回列表项的数量
    @Override
    public int getItemCount() {
        return planList.size();
    }

    // 更新数据并通知改变
    public void setPlans(List<Plan> plans) {
        this.planList = plans;
        notifyDataSetChanged();
    }

    // ViewHolder 类
    public static class PlanViewHolder extends RecyclerView.ViewHolder {
        public TextView timeTextView;
        public TextView titleTextView;

        public PlanViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
        }
    }
}

