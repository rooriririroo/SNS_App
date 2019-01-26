package com.example.soyeonlee.myapplication12;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ScheduleListItemAdapter extends BaseAdapter {

    Context context;
    private ArrayList<ScheduleListItem> scheduleListItemArrayList = new ArrayList<>();
    ViewHolder viewHolder;

    class ViewHolder {
        TextView scheduleDate;
        TextView scheduleDay;
        TextView scheduleTitle;
        TextView scheduleStartTime;
        TextView scheduleEndTime;
    }

    public ScheduleListItemAdapter(Context context, ArrayList<ScheduleListItem> scheduleListItemArrayList) {
        this.context = context;
        this.scheduleListItemArrayList = scheduleListItemArrayList;
    }

    @Override
    public int getCount() {
        return scheduleListItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return scheduleListItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_dialog,null);
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        return convertView;
    }
}
