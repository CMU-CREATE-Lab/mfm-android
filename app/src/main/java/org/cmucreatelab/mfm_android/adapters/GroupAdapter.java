package org.cmucreatelab.mfm_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by Steve on 3/10/2016.
 */
public class GroupAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Group> mGroups;


    private static class ViewHolder {
        ImageView groupPhotoView;
        TextView groupName;
    }


    public GroupAdapter(Context context, ArrayList<Group> groups) {
        this.mContext = context;
        this.mGroups = groups;
    }


    @Override
    public int getCount() {
        return mGroups.size();
    }


    @Override
    public Object getItem(int i) {
        return mGroups.get(i);
    }


    @Override
    public long getItemId(int i) {
        return 0; // not using
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.group_list_item, null);
            holder = new ViewHolder();
            holder.groupPhotoView = (ImageView) view.findViewById(R.id.groupPhotoImageView);
            holder.groupName = (TextView) view.findViewById(R.id.groupNameTextView);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Group group  = mGroups.get(i);
        String photoUrl = group.getPhotoUrl();
        String url = Constants.MFM_API_URL + photoUrl;

        holder.groupName.setText(group.getName());
        Picasso.with(mContext).load(url).into(holder.groupPhotoView);

        return view;
    }

}
