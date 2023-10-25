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

import com.sansang.todaysapplication.Journals.JournalAddActivity;
import com.sansang.todaysapplication.Journals.JournalTableActivity;
import com.sansang.todaysapplication.R;

import java.text.DecimalFormat;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {
    private final Context context_m;
    private final Cursor cursor_m;
    private DecimalFormat formatPay, formatday;

    public JournalAdapter( Context context, Cursor cursor ){
        this.context_m = context;
        this.cursor_m = cursor;
    }

    @NonNull
    @Override
    public JournalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_journal_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("Range")
    @Override
    public void onBindViewHolder(@NonNull JournalAdapter.ViewHolder holder, int position) {
        formatday = new DecimalFormat("#,###.#");
        formatPay = new DecimalFormat("#,###");
        if (!cursor_m.moveToPosition(position))
            return;
        String site = cursor_m.getString(cursor_m.getColumnIndex("sites"));
        holder.site_name.setText(site);

        String tday = cursor_m.getString(cursor_m.getColumnIndex("ones"));
        double fday = Double.parseDouble(tday);
        String day_format = formatday.format(fday);
        holder.site_total_day.setText(day_format);

        String amount = cursor_m.getString(cursor_m.getColumnIndex("amounts"));
        int famount = Integer.parseInt(amount);
        String amount_format = formatPay.format(famount);
        holder.site_amount.setText(amount_format);

        String leader = cursor_m.getString(cursor_m.getColumnIndex("leader"));
        holder.site_income.setText(leader);

        String cost = cursor_m.getString(cursor_m.getColumnIndex("costs"));
        if (cursor_m.getString(cursor_m.getColumnIndex("costs")) != null) {
            int fcost = Integer.parseInt(cost);
            String cost_format = formatPay.format(fcost);
            holder.site_cost.setText(cost_format);
        }else {
            holder.site_cost.setText("0");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent siteTable = new Intent(v.getContext(), JournalTableActivity.class);
                siteTable.putExtra("site", holder.site_name.getText().toString());
                v.getContext().startActivity(siteTable);

                ((Activity) v.getContext()).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (cursor_m.getCount());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView site_name;
        TextView site_total_day;
        TextView site_amount;
        TextView site_income;
        TextView site_cost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.site_name = itemView.findViewById(R.id.journal_site_name);
            this.site_total_day = itemView.findViewById(R.id.journal_site_total_day);
            this.site_amount = itemView.findViewById(R.id.journal_site_amount);
            this.site_income = itemView.findViewById(R.id.journal_site_collect);
            this.site_cost = itemView.findViewById(R.id.journal_site_cost);

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
                        Intent addIntent = new Intent(itemView.getContext(), JournalAddActivity.class);
                        itemView.getContext().startActivity(addIntent);

                        ((Activity)itemView.getContext()).finish();//kotlin 은 (context as Activity).finish();

                        break;

                    case 1002:
                        Intent tableIntent = new Intent(itemView.getContext(), JournalTableActivity.class);
                        tableIntent.putExtra("site", site_name.getText().toString());
                        itemView.getContext().startActivity(tableIntent);

                        ((Activity)itemView.getContext()).finish();//kotlin 은 (context as Activity).finish();

                        break;
                }
                return true;
            }
        };
    }
}
