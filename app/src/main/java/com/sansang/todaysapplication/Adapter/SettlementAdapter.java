package com.sansang.todaysapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sansang.todaysapplication.R;

import java.text.DecimalFormat;

public class SettlementAdapter extends RecyclerView.Adapter<SettlementAdapter.ViewHolder> {
    private final Context context_m;
    private final Cursor cursor_m;
    private DecimalFormat formatPay, formatday;

    public SettlementAdapter(Context context, Cursor cursor_s) {
        this.context_m = context;
        this.cursor_m = cursor_s;
    }

    @NonNull
    @Override
    public SettlementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_settlement_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull SettlementAdapter.ViewHolder holder, int position) {
        formatday = new DecimalFormat("#,###.#");
        formatPay = new DecimalFormat("#,###");

        if (!cursor_m.moveToPosition(position))
            return;

        String year_y = cursor_m.getString(cursor_m.getColumnIndex("jyear"));
        //int year_a = cursor_m.getColumnIndex("jyear");
        holder.edt_year.setText(String.valueOf(year_y));

        float one = Float.parseFloat(cursor_m.getString(cursor_m.getColumnIndex("ones")));
        if (one != 0){
            holder.edt_one.setText(String.valueOf(one));
        }else {
            holder.edt_one.setText("0");
        }

        int amount = Integer.parseInt(cursor_m.getString(cursor_m.getColumnIndex("amount")));
        if (amount != 0){
            holder.edt_amount.setText(String.valueOf(formatPay.format(amount)));
        }else {
            holder.edt_amount.setText("0");
        }

        int cost_a = Integer.parseInt(cursor_m.getString(cursor_m.getColumnIndex("costs")));
        if (cost_a != 0){
            holder.edt_cost.setText(String.valueOf(formatPay.format(cost_a)));
        }else {
            holder.edt_cost.setText("0");
        }

        int deposit_a = Integer.parseInt(cursor_m.getString(cursor_m.getColumnIndex("deposit")));
        if (deposit_a != 0){
            holder.edt_income.setText(String.valueOf(formatPay.format(deposit_a)));
        }else {
            holder.edt_income.setText("0");
        }

        int tax_a = Integer.parseInt(cursor_m.getString(cursor_m.getColumnIndex("tax")));
        if (tax_a != 0){
            holder.edt_tax.setText(String.valueOf(formatPay.format(tax_a)));
        }else {
            holder.edt_tax.setText("0");
        }

    }

    @Override
    public int getItemCount() {
        return (cursor_m.getCount());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView edt_year, edt_one, edt_amount, edt_cost, edt_income, edt_tax;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.edt_year = itemView.findViewById(R.id.year_settlement_edt);
            this.edt_one = itemView.findViewById(R.id.one_settlement_edt);
            this.edt_amount = itemView.findViewById(R.id.amount_settlement_edt);
            this.edt_cost = itemView.findViewById(R.id.cost_settlement_edt);
            this.edt_income = itemView.findViewById(R.id.income_settlement_edt);
            this.edt_tax = itemView.findViewById(R.id.tax_settlement_edt);
        }
    }
}
