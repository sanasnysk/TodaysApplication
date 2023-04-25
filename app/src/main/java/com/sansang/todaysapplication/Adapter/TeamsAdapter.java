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

import com.sansang.todaysapplication.Contents.TeamContents;
import com.sansang.todaysapplication.R;
import com.sansang.todaysapplication.Teams.TeamAddActivity;
import com.sansang.todaysapplication.Teams.TeamEditActivity;

import java.util.ArrayList;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.ViewHolder> {
    private final ArrayList<TeamContents> mList;

    public TeamsAdapter( ArrayList<TeamContents> list ){
        this.mList = list;
    }

    @NonNull
    @Override
    public TeamsAdapter.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int viewType ) {
        Context mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_team_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "RecyclerView"})
    public void onBindViewHolder(@NonNull TeamsAdapter.ViewHolder holder,  int position ) {
        holder.team_leader.setText(mList.get(position).getTeamLeader());
        holder.team_mobile.setText(mList.get(position).getTeamPhone());
        holder.team_date.setText(mList.get(position).getTeamDate());
        holder.team_memo.setText(mList.get(position).getTeamMemo());
        holder.team_id.setText(mList.get(position).getId());
        holder.team_tid.setText(mList.get(position).getTeamId());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick( View v ) {
//                Intent teamTable = new Intent(v.getContext(), TeamAddActivity.class);
//                teamTable.putExtra("team_leader", mList.get(position).getTeamLeader());
//                v.getContext().startActivity(teamTable);
//
//                ((Activity) v.getContext()).finish();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size():0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView team_id, team_tid, team_leader, team_mobile, team_date, team_memo;

        public ViewHolder( @NonNull View itemView ) {
            super(itemView);
            this.team_id = itemView.findViewById(R.id.team_list_id);
            this.team_tid = itemView.findViewById(R.id.team_list_tid);
            this.team_leader = itemView.findViewById(R.id.team_list_name);
            this.team_mobile = itemView.findViewById(R.id.team_list_mobile);
            this.team_date = itemView.findViewById(R.id.team_list_date);
            this.team_memo = itemView.findViewById(R.id.team_list_memo);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo ) {
            //다이얼로그 3. 메뉴 추가
            //편집 3. 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너를 등록해줍니다.
            // ID 1001, 1002로 어떤 메뉴를 선택했는지 리스너에서 구분하게 됩니다.
            MenuItem addMenu = menu.add(Menu.NONE, 1001, 1, "ADD(추가)");
            MenuItem editMenu = menu.add(Menu.NONE, 1002, 2, "Edit(수정)");

            addMenu.setOnMenuItemClickListener(onAddMenu);

            editMenu.setOnMenuItemClickListener(onAddMenu);

        }

        private final MenuItem.OnMenuItemClickListener onAddMenu = new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick( MenuItem item ) {
                switch (item.getItemId()){
                    case 1001:
                        Intent addIntent = new Intent(itemView.getContext(), TeamAddActivity.class);
                        addIntent.putExtra("name", team_leader.getText().toString());
                        itemView.getContext().startActivity(addIntent);

                        ((Activity)itemView.getContext()).finish();//kotlin 은 (context as Activity).finish();

                        break;

                    case 1002:
                        Intent editIntent = new Intent(itemView.getContext(), TeamEditActivity.class);

                        editIntent.putExtra("id", team_id.getText().toString());
                        editIntent.putExtra("tid", team_tid.getText().toString());
                        editIntent.putExtra("leader", team_leader.getText().toString());
                        editIntent.putExtra("mobile", team_mobile .getText().toString());
                        editIntent.putExtra("date", team_date.getText().toString());
                        editIntent.putExtra("memo", team_memo.getText().toString());
                        itemView.getContext().startActivity(editIntent);

                        ((Activity)itemView.getContext()).finish();//kotlin 은 (context as Activity).finish();

                        break;
                }
                return true;
            }

        };
    }
}
