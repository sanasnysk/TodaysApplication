package com.sansang.todaysapplication.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sansang.todaysapplication.Contents.SiteContents;
import com.sansang.todaysapplication.NumberTextWatcher.NumberTextWatcher;
import com.sansang.todaysapplication.R;
import com.sansang.todaysapplication.Sites.SiteAddActivity;
import com.sansang.todaysapplication.Sites.SiteEditActivity;
import com.sansang.todaysapplication.Sites.SiteTableActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SitesAdapter extends RecyclerView.Adapter<SitesAdapter.ViewHolder> {
    private final ArrayList<SiteContents> mList;
    private DecimalFormat formatPay;

    public SitesAdapter(ArrayList<SiteContents> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public SitesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_site_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n,RecyclerView")
    public void onBindViewHolder(@NonNull SitesAdapter.ViewHolder holder, int position ) {
        holder.site_name.setText(mList.get(position).getSiteName());
        holder.site_leader.setText(mList.get(position).getTeamLeader());
        holder.site_date.setText(mList.get(position).getSiteDate());
        holder.site_pay.setText(mList.get(position).getSitePay());


//        formatPay = new DecimalFormat("#,###");
//        int fpay = Integer.parseInt(holder.site_pay.getText().toString());
//        String pay_format = formatPay.format(fpay);
//        holder.site_pay.setText(pay_format);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Intent siteEdit = new Intent(v.getContext(), SiteEditActivity.class);
                siteEdit.putExtra("sId", mList.get(position).getId());
                siteEdit.putExtra("stId", mList.get(position).getSiteId());
                siteEdit.putExtra("stName", mList.get(position).getSiteName());
                siteEdit.putExtra("stPay", mList.get(position).getSitePay());
                siteEdit.putExtra("stManager", mList.get(position).getSiteManager());
                siteEdit.putExtra("stDate", mList.get(position).getSiteDate());
                siteEdit.putExtra("stMemo", mList.get(position).getSiteMemo());
                siteEdit.putExtra("tmLeader", mList.get(position).getTeamLeader());
                siteEdit.putExtra("tmId", mList.get(position).getTeamId());

                v.getContext().startActivity(siteEdit);

                ((Activity) v.getContext()).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size():0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView site_name;
        TextView site_leader;
        TextView site_date;
        TextView site_pay;

        public ViewHolder( @NonNull View itemView ) {
            super(itemView);
            this.site_name = itemView.findViewById(R.id.site_name);
            this.site_leader = itemView.findViewById(R.id.site_leader);
            this.site_date = itemView.findViewById(R.id.site_day);
            this.site_pay = itemView.findViewById(R.id.site_pay);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo ) {
            //다이얼로그 3. 메뉴 추가
            //편집 3. 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너를 등록해줍니다.
            // ID 1001, 1002로 어떤 메뉴를 선택했는지 리스너에서 구분하게 됩니다.
            MenuItem addMenu = menu.add(Menu.NONE, 1001, 1, "ADD(추가)");
            MenuItem tableMenu = menu.add(Menu.NONE, 1002, 2, "TABLE(테이블)");

            addMenu.setOnMenuItemClickListener(onAddMenu);

            tableMenu.setOnMenuItemClickListener(onAddMenu);

        }

        private final MenuItem.OnMenuItemClickListener onAddMenu = new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick( MenuItem item ) {
                switch (item.getItemId()){
                    case 1001:
                        Intent addIntent = new Intent(itemView.getContext(), SiteAddActivity.class);
                        itemView.getContext().startActivity(addIntent);

                        ((Activity)itemView.getContext()).finish();//kotlin 은 (context as Activity).finish();

                        break;

                    case 1002:

                        Intent tableIntent = new Intent(itemView.getContext(), SiteTableActivity.class);
                        tableIntent.putExtra("tmLeader", site_leader.getText().toString());
                        itemView.getContext().startActivity(tableIntent);

                        ((Activity)itemView.getContext()).finish();//kotlin 은 (context as Activity).finish();

                        break;
                }
                return true;
            }

        };
    }
}