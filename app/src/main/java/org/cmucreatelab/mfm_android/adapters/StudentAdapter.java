package org.cmucreatelab.mfm_android.adapters;

import android.content.Context;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.ViewStudentsActivity;
import org.cmucreatelab.mfm_android.classes.Student;

/**
 * Created by mohaknahta on 2/20/16.
 */
public class StudentAdapter extends BaseAdapter {

    private Context mContext;
    private Student[] mStudents;

    public StudentAdapter(Context context, Student[] students) {
        mContext = context;
        mStudents = students;
    }


    @Override
    public int getCount() {
        return mStudents.length;
    }

    @Override
    public Object getItem(int i) {
        return mStudents[i];
    }

    @Override
    public long getItemId(int i) {
        return 0; //not using this
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.student_list_item, null);
            holder = new ViewHolder();
            holder.studentPhotoView = (ImageView) view.findViewById(R.id.studentPhotoImageView);
            holder.studentName = (TextView) view.findViewById(R.id.studentNameTextView);

            view.setTag(holder);
        }

        else{
            holder = (ViewHolder) view.getTag();
        }

        Student student  = mStudents[i];
        String photoUrl = student.getPhotoUrl();
        String url = "http://dev.messagefromme.org" + photoUrl;

        holder.studentName.setText(student.getFirstName() + " " + student.getLastName());
        Picasso.with(mContext)
                .load(url)
                .into(holder.studentPhotoView);

        return view;
    }

    //allows to reuse views when the list is long
    private static class ViewHolder {
        ImageView studentPhotoView;
        TextView studentName;
    }
}
