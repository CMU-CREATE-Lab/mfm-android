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
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;
import java.util.ArrayList;

/**
 * Created by mohaknahta on 2/20/16.
 */
public class StudentAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Student> mStudents;

    //allows to reuse views when the list is long
    private static class ViewHolder {
        ImageView studentPhotoView;
        TextView studentName;
    }


    public StudentAdapter(Context context, ArrayList<Student> students) {
        mContext = context;
        mStudents = students;
    }


    @Override
    public int getCount() {
        return mStudents.size();
    }


    @Override
    public Object getItem(int i) {
        return mStudents.get(i);
    }


    @Override
    public long getItemId(int i) {
        return 0; //not using this
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.student_list_item, null);
            holder = new ViewHolder();
            holder.studentPhotoView = (ImageView) view.findViewById(R.id.studentPhotoImageView);
            holder.studentName = (TextView) view.findViewById(R.id.studentNameTextView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Student student  = mStudents.get(position);
        String photoUrl = student.getPhotoUrl();
        String url = Constants.MFM_API_URL + photoUrl;

        holder.studentName.setText(student.getFirstName() + " " + student.getLastName());
        Picasso.with(mContext).load(url).into(holder.studentPhotoView);

        return view;
    }

}
