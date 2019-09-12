package com.mehrgan.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.mehrgan.Activity_Details_Mehrgan;
import com.mehrgan.Classes.CalendarTool;
import com.mehrgan.DataBase.DataSource.tb_BillsDataSource;
import com.mehrgan.DataBase.Tables.tb_Bills;
import com.mehrgan.R;

import java.util.List;


public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.ViewHolder> {

    private Context context;
    private List<tb_Bills> data;
    private int lastPosition = -1;

    public recyclerAdapter(Context context, List<tb_Bills> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_row_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        holder.itemView.startAnimation(animation);
        lastPosition = position;

        holder.txtBillNum.setText(data.get(position).PK_Bill);
        holder.txtFullName.setText(data.get(position).fullName);
        holder.txtPhoneNum.setText(data.get(position).phoneNum);
        holder.txtDate.setText(data.get(position).dateJalali);

        if (data.get(position).dateJalali.split("/").length == 3) {
            CalendarTool tool = new CalendarTool();
            tool.setIranianDate(data.get(position).dateJalali);
            holder.txtNameDate.setText(tool.getIranianWeekDayStr());
        } else
            holder.txtNameDate.setVisibility(View.GONE);

        holder.linearMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Activity_Details_Mehrgan.class);
                intent.putExtra("status", "show");
                intent.putExtra("PK", data.get(position).PK_Bill);
                context.startActivity(intent);
            }
        });

        holder.btnMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(context, holder.btnMenuItem);

                popup.getMenuInflater().inflate(R.menu.bottom_nav_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nvgEdit:
                                Intent intent = new Intent(context, Activity_Details_Mehrgan.class);
                                intent.putExtra("status", "edit");
                                intent.putExtra("PK", data.get(position).PK_Bill);
                                context.startActivity(intent);
                                break;
                            case R.id.nvgDelete:
                                deleteItem(data.get(position).PK_Bill);
//                                data.remove(position);
//                                notifyItemRemoved(position);
//                                notifyItemRangeChanged(position, data.size());
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void deleteItem(final String id) {
        new AlertDialog.Builder(context)
                .setTitle("حذف این قبض")
                .setMessage("آیا مطمئن هستید؟")
                .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tb_BillsDataSource tb_billsDataSource = new tb_BillsDataSource(context);
                        tb_billsDataSource.Open();
                        tb_billsDataSource.DeleteById(id);
                        tb_billsDataSource.Close();
                        SharedPreferences sharedPreferences = context.getSharedPreferences("Mehrgan", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("onRefresh", true);
                        editor.commit();
                    }
                })
                .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtBillNum, txtFullName, txtPhoneNum, txtDate, txtNameDate;
        ImageButton btnMenuItem;
        LinearLayout linearMain;

        ViewHolder(View view) {
            super(view);

            txtBillNum = view.findViewById(R.id.txtBillNum);
            txtFullName = view.findViewById(R.id.txtFullName);
            txtPhoneNum = view.findViewById(R.id.txtPhoneNum);
            txtDate = view.findViewById(R.id.txtDate);
            btnMenuItem = view.findViewById(R.id.btnMenuItem);
            linearMain = view.findViewById(R.id.linearMain);
            txtNameDate = view.findViewById(R.id.txtNameDate);
        }
    }
}