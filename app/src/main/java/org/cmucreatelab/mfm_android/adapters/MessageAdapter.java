package org.cmucreatelab.mfm_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import java.util.ArrayList;

/**
 * Created by mohaknahta on 2/21/16.
 */
public class MessageAdapter extends BaseAdapter {

    private Context mContext;
    private Student mStudent;

    public MessageAdapter(Context context, Student student) {
        mContext = context;
        mStudent = student;
    }


    //allows to reuse views when the list is long
    private static class ViewHolder {
        TextView userName;
    }


    @Override
    public int getCount() {
        return mStudent.getUsers().size();
    }


    @Override
    public Object getItem(int i) {
        return mStudent.getUsers().get(i);
    }


    @Override
    public long getItemId(int i) {
        return 0; //not using this
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.message, null);
            holder = new ViewHolder();
            holder.userName = (TextView) view.findViewById(R.id.UserNameTextView);

            view.setTag(holder);
        }

        else{
            holder = (ViewHolder) view.getTag();
        }

        ArrayList<User> users = mStudent.getUsers();
        String name = "";
        if (users.size() > 0){
            User user = users.get(0);
            name = user.getFirstName() + " " + user.getLastName();
        }

        holder.userName.setText(name);
        return view;
    }

}
