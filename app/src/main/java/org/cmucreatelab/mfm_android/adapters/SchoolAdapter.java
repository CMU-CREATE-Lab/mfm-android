package org.cmucreatelab.mfm_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.classes.School;

import java.util.ArrayList;

/**
 * Created by Steve on 5/25/2016.
 */
public class SchoolAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<School> mSchools;


    private static class ViewHolder {
        TextView schoolName;
    }


    public SchoolAdapter(Context context, ArrayList<School> schools) {
        this.mContext = context;
        this.mSchools = schools;
    }

    @Override
    public int getCount() {
        return mSchools.size();
    }

    @Override
    public Object getItem(int i) {
        return mSchools.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.school_list_item, null);
            holder = new ViewHolder();
            holder.schoolName = (TextView) view.findViewById(R.id.schoolNameTextView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        School school  = mSchools.get(i);
        holder.schoolName.setText(school.getName());

        return view;
    }
}
