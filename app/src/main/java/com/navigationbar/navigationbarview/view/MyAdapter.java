package com.navigationbar.navigationbarview.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.navigationbar.navigationbarview.CustomSectionIndexer;
import com.navigationbar.navigationbarview.R;

import java.util.List;

/**
 * Created by 陈韶鹏 on 2017/1/19.
 */

public class MyAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mData;
    private CustomSectionIndexer sectionIndexer;

    public MyAdapter(Context context, List data) {
        mContext = context;
        mData = data;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view =LayoutInflater.from(mContext).inflate(R.layout.item_listview, viewGroup, false);
            viewHolder.tv = (TextView) view.findViewById(R.id.tv);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (i == sectionIndexer.getFirstSectionPosition(i)) {
            viewHolder.tvTitle.setText(sectionIndexer.getPositionChart(i));
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvTitle.setVisibility(View.GONE);
        }
        viewHolder.tv.setText(mData.get(i));
        return view;
    }

    static class ViewHolder {
        TextView tv;
        TextView tvTitle;
    }


    public void setSectionIndexer(CustomSectionIndexer customSectionIndexer) {
        this.sectionIndexer = customSectionIndexer;
    }


}
