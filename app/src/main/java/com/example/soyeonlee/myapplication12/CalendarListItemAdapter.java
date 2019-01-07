package com.example.soyeonlee.myapplication12;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CalendarListItemAdapter extends BaseAdapter {

    Context context;
    private ArrayList<CalendarListItem> calendarListItemArrayList = new ArrayList<>();
    ViewHolder viewHolder;

    class ViewHolder {
        TextView calendarDate;
        TextView calendarDay;
        TextView calendarName;
        TextView calendarText;
        TextView calendarBirth;
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
            viewHolder.calendarName = convertView.findViewById(R.id.calendaritem_name);
            viewHolder.calendarText = convertView.findViewById(R.id.calendaritem_text);
            viewHolder.calendarBirth = convertView.findViewById(R.id.calendaritem_birth);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.calendarDate.setText(calendarListItemArrayList.get(position).getCalendarDate());
        viewHolder.calendarDay.setText(calendarListItemArrayList.get(position).getCalendarDay());
        viewHolder.calendarName.setText(calendarListItemArrayList.get(position).getCalendarName());

        return convertView;
    }
}
