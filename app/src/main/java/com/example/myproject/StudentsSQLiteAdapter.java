package com.example.myproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class StudentsSQLiteRecyclerAdapter extends RecyclerView.Adapter<StudentsSQLiteRecyclerAdapter.ViewHolder> {
    private List<Student> listdata;
    sqliteInterface interface_1;

    public StudentsSQLiteRecyclerAdapter(List<Student> listdata1, sqliteInterface action) {
        this.listdata = listdata1;
        this.interface_1 = action;
    }

    @NonNull
    @Override
    public StudentsSQLiteRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item_for_student, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = listdata.get(position);
        holder.textView.setText(listdata.get(position).getName());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interface_1.updateRecord(student, holder.getAdapterPosition());
            }
        });
        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                interface_1.deleteRecode(student.getId(), holder.getAdapterPosition());
                return true;
            }
        });
    }

    public void notifyData(List<Student> studentInformation) {

        this.listdata = studentInformation;
        this.notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageView);
            this.textView = itemView.findViewById(R.id.textView);
            this.relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}
