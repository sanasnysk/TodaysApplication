package com.sansang.todaysapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sansang.todaysapplication.R;

import java.util.List;

public class CustomSpinnerAdapter extends BaseAdapter {
    private final List<String> list;
    private final LayoutInflater inflater;
    private String text;

    public CustomSpinnerAdapter(Context context, List<String> list) {
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size()-1;
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 화면에 들어왔을 때 보여지는 텍스트뷰 설정
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = inflater.inflate(R.layout.spinner_normal, viewGroup, false);
        if (list != null) {
            text = list.get(position);
            ((TextView) view.findViewById(R.id.spinner_inner_text)).setText(text);
        }
        return view;
    }

    // 클릭 후 나타나는 텍스트뷰 설정
    @Override
    public View getDropDownView(int position, View view, ViewGroup viewGroup) {
        if (view == null)
            view = inflater.inflate(R.layout.spinner_dropdown, viewGroup, false);
        if (list != null) {
            text = list.get(position);
            ((TextView) view.findViewById(R.id.spinner_text)).setText(text);
        }

        return view;
    }

    // 스피너에서 선택된 아이템을 액티비티에서 꺼내오는 메서드
    public String getItem() {
        return text;
    }
}
