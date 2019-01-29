package com.example.soyeonlee.myapplication12;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.GONE;

public class CalendarListItemAdapter extends BaseAdapter {

    Context context;
    private ArrayList<CalendarListItem> calendarListItemArrayList = new ArrayList<>();
    ViewHolder viewHolder;

    class ViewHolder {
        TextView calendarDate;
        TextView calendarDay;
        TextView calendarTitle;
        TextView calendarTime;
        TextView calendarMap;
        TextView calendarIcon;
    }

    public CalendarListItemAdapter(Context context, ArrayList<CalendarListItem> calendarListItemArrayList) {
        this.context = context;
        this.calendarListItemArrayList = calendarListItemArrayList;
    }

    @Override
    public int getCount() {
        return calendarListItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return calendarListItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_calendaritem,null);
            viewHolder = new ViewHolder();
            viewHolder.calendarDate = convertView.findViewById(R.id.calendaritem_date);
            viewHolder.calendarDay = convertView.findViewById(R.id.calendaritem_day);
            viewHolder.calendarTitle = convertView.findViewById(R.id.calendaritem_title);
            viewHolder.calendarTime = convertView.findViewById(R.id.calendaritem_time);
            viewHolder.calendarMap = convertView.findViewById(R.id.calendaritem_map);
            viewHolder.calendarIcon = convertView.findViewById(R.id.calendaritem_icon);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.calendarDate.setText(calendarListItemArrayList.get(position).getCalendarDate());
        viewHolder.calendarDay.setText(calendarListItemArrayList.get(position).getCalendarDay());
        viewHolder.calendarTitle.setText(calendarListItemArrayList.get(position).getCalendarTitle());
        if(calendarListItemArrayList.get(position).getCalendarTitle().contains("생일")) {
            viewHolder.calendarTime.setVisibility(View.GONE);
            viewHolder.calendarMap.setVisibility(View.GONE);
            viewHolder.calendarIcon.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.calendarTime.setVisibility(View.VISIBLE);
            viewHolder.calendarMap.setVisibility(View.VISIBLE);
            viewHolder.calendarIcon.setVisibility(View.GONE);
        }
        viewHolder.calendarTime.setText(calendarListItemArrayList.get(position).getCalendarTime());
        viewHolder.calendarMap.setText(calendarListItemArrayList.get(position).getCalendarMap());

        return convertView;
    }
}
