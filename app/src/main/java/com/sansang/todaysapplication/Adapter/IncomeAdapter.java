package com.sansang.todaysapplication.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sansang.todaysapplication.Database.JournalTableContents;
import com.sansang.todaysapplication.Incmoes.IncomeAddActivity;
import com.sansang.todaysapplication.Incmoes.IncomeTableActivity;
import com.sansang.todaysapplication.R;

import java.text.DecimalFormat;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {
    private final Context context_m;
    private final Cursor cursor_m;
    private DecimalFormat formatPay, formatday;

    public IncomeAdapter( Context context, Cursor cursor ){
        this.context_m = context;
        cursor_m = cursor;
    }

    @NonNull
    @Override
    public IncomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_income_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull IncomeAdapter.ViewHolder holder, int position) {
        if (!cursor_m.moveToPosition(position))
            return;
        formatPay = new DecimalFormat("#,###");
        String leader = cursor_m.getString(cursor_m.getColumnIndex(JournalTableContents.TEAM_LEADER));
        String amount = cursor_m.getString(cursor_m.getColumnIndex("amounts"));

        holder.leader.setText(leader);

        String one = cursor_m.getString(cursor_m.getColumnIndex("oneDays"));
        holder.days.setText(one);

        int famount = Integer.parseInt(amount);
        String amount_format = formatPay.format(famount);
        holder.amount.setText(amount_format);

        if (cursor_m.getString(cursor_m.getColumnIndex("deposit")) != null) {
            String collect = cursor_m.getString(cursor_m.getColumnIndex("deposit"));
            int fcollect = Integer.parseInt(collect);
            String collect_format = formatPay.format(fcollect);
            holder.deposit.setText(collect_format);
        }else {
            holder.deposit.setText("0");
        }

        if (cursor_m.getString(cursor_m.getColumnIndex("taxs")) != null) {
            String tax = cursor_m.getString(cursor_m.getColumnIndex("taxs"));
            float ftax = Float.parseFloat(tax);
            String tax_format = formatPay.format(ftax);
            holder.tax.setText(tax_format);
        }else {
            holder.tax.setText("0");
        }

        if (cursor_m.getString(cursor_m.getColumnIndex("deposit")) != null) {
            String balance_day = cursor_m.getString(cursor_m.getColumnIndex("days"));
            holder.balanceday.setText(balance_day);
        }else {
            String balance_day = cursor_m.getString(cursor_m.getColumnIndex("oneDays"));
            holder.balanceday.setText(balance_day);
        }

        String site_balance = cursor_m.getString(cursor_m.getColumnIndex("balances"));
        if (cursor_m.getString(cursor_m.getColumnIndex("deposit")) != null) {
            int balances = Integer.parseInt(site_balance);
            String balance_format = formatPay.format(balances);
            holder.balanceamount.setText(balance_format);
        }else {
            holder.balanceamount.setText("0");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent siteTable = new Intent(v.getContext(), IncomeTableActivity.class);
                siteTable.putExtra("leader", holder.leader.getText().toString());
                v.getContext().startActivity(siteTable);

                ((Activity) v.getContext()).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (cursor_m.getCount());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView leader, days, amount, deposit, tax, balanceday, balanceamount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            leader = itemView.findViewById(R.id.edtxt_income_item_leader);
            days = itemView.findViewById(R.id.edtxt_income_item_days);
            amount = itemView.findViewById(R.id.edtxt_income_item_amount);
            deposit = itemView.findViewById(R.id.edtxt_income_item_deposit);
            tax = itemView.findViewById(R.id.edtxt_income_item_tax);
            balanceday = itemView.findViewById(R.id.edtxt_income_item_balanceday);
            balanceamount = itemView.findViewById(R.id.edtxt_income_item_balanceamount);


            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            //다이얼로그 3. 메뉴 추가
            //편집 3. 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너를 등록해줍니다.
            // ID 1001, 1002로 어떤 메뉴를 선택했는지 리스너에서 구분하게 됩니다.
            MenuItem addMenu = contextMenu.add(Menu.NONE, 1001, 1, "추가(ADD)");
            MenuItem tableMenu = contextMenu.add(Menu.NONE, 1002, 2, "테이블 보기(TABLE SHOW)");

            addMenu.setOnMenuItemClickListener(onAddMenu);

            tableMenu.setOnMenuItemClickListener(onAddMenu);

        }

        private final MenuItem.OnMenuItemClickListener onAddMenu = new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick( MenuItem item ) {
                switch (item.getItemId()){
                    case 1001:
                        Intent addIntent = new Intent(itemView.getContext(), IncomeAddActivity.class);
                        itemView.getContext().startActivity(addIntent);

                        ((Activity)itemView.getContext()).finish();//kotlin 은 (context as Activity).finish();

                        break;

                    case 1002:
                        Intent tableIntent = new Intent(itemView.getContext(), IncomeTableActivity.class);
                        tableIntent.putExtra("leader", leader.getText().toString());
                        itemView.getContext().startActivity(tableIntent);

                        ((Activity)itemView.getContext()).finish();//kotlin 은 (context as Activity).finish();

                        break;
                }
                return true;
            }

        };
    }
}
