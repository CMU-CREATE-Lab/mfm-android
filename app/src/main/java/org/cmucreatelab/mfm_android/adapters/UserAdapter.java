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
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<User> mUsers;

    //allows to reuse views when the list is long
    private static class ViewHolder {
        ImageView userPhotoView;
        TextView userName;
    }


    public UserAdapter(Context context, ArrayList<User> users) {
        mContext = context;
        mUsers = users;
    }


    @Override
    public int getCount() {
        return mUsers.size();
    }


    @Override
    public Object getItem(int i) {
        return mUsers.get(i);
    }


    @Override
    public long getItemId(int i) {
        return 0; //not using this
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.user_list_item, null);
            holder = new ViewHolder();
            holder.userPhotoView = (ImageView) view.findViewById(R.id.userPhotoImageView);
            holder.userName = (TextView) view.findViewById(R.id.userNameTextView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        User user  = mUsers.get(position);
        String photoUrl = user.getPhotoUrl();
        String url = Constants.MFM_API_URL + photoUrl;

        holder.userName.setText(user.getFirstName() + " " + user.getLastName());
        Picasso.with(mContext).load(url).into(holder.userPhotoView);

        return view;
    }

}
