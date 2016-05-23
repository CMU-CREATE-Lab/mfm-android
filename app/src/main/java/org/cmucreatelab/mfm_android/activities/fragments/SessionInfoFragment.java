package org.cmucreatelab.mfm_android.activities.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.adapters.GroupAdapter;
import org.cmucreatelab.mfm_android.adapters.StudentAdapter;
import org.cmucreatelab.mfm_android.adapters.UserAdapter;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.AudioPlayer;
import org.cmucreatelab.mfm_android.ui.ExtendedHeightGridView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SessionInfoFragment extends Fragment {

    private GlobalHandler globalHandler;
    private Activity parentActivity;
    private View rootView;
    private ExtendedHeightGridView recipientsView;
    private ExtendedHeightGridView fromView;
    private AudioPlayer audioPlayer;


    public static Fragment newInstance() {
        SessionInfoFragment fragment = new SessionInfoFragment();
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
        globalHandler = GlobalHandler.getInstance(parentActivity.getApplicationContext());
        audioPlayer = new AudioPlayer(globalHandler.appContext);
    }


    public SessionInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_session_info, container, false);
        ButterKnife.bind(this, rootView);

        if (globalHandler.sessionHandler.getMessageSender().getSenderType() == Sender.Type.student) {
            // From content
            ArrayList<Student> studentList = new ArrayList<>();
            studentList.add((Student) globalHandler.sessionHandler.getMessageSender());
            fromView = (ExtendedHeightGridView) rootView.findViewById(R.id.f_session_info_from);
            fromView.setAdapter(new StudentAdapter(parentActivity.getApplicationContext(), studentList));

            // To content
            recipientsView = (ExtendedHeightGridView) rootView.findViewById(R.id.f_session_info_recipients);
            recipientsView.setAdapter(new UserAdapter(parentActivity.getApplicationContext(), globalHandler.sessionHandler.getRecipients()));
            rootView.findViewById(R.id.f_session_info_to_recipients_button).setVisibility(View.VISIBLE);
        } else {
            // From content
            ArrayList<Group> groupList = new ArrayList<>();
            groupList.add((Group) globalHandler.sessionHandler.getMessageSender());
            fromView = (ExtendedHeightGridView) rootView.findViewById(R.id.f_session_info_from);
            fromView.setAdapter(new GroupAdapter(parentActivity.getApplicationContext(), groupList));

            // To content
            recipientsView = (ExtendedHeightGridView) rootView.findViewById(R.id.f_session_info_recipients);
            recipientsView.setAdapter(new GroupAdapter(parentActivity.getApplicationContext(), groupList));
            rootView.findViewById(R.id.f_session_info_to_recipients_button).setVisibility(View.INVISIBLE);
        }

        // Media content and send
        if (globalHandler.sessionHandler.getMessagePhoto() != null) {
            int rotation = CameraFragment.getCameraOrientation(this.getActivity());
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bitmap = BitmapFactory.decodeFile(globalHandler.sessionHandler.getMessagePhoto().getAbsolutePath());
            Bitmap rotated = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
            ((ImageView) rootView.findViewById(R.id.f_session_info_media_photo)).setImageBitmap(rotated);
            ((ImageView) rootView.findViewById(R.id.f_session_info_media_audio)).setImageResource(R.drawable.button_up_talk);
            audioPlayer.addAudio(R.raw.what_did_take);
            audioPlayer.playAudio();
        }
        if (globalHandler.sessionHandler.getMessageAudio() != null) {
            ((ImageView) rootView.findViewById(R.id.f_session_info_media_audio)).setImageResource(R.drawable.soundwave_final);
            ((ImageView) rootView.findViewById(R.id.f_session_info_send)).setImageResource(R.drawable.send_up);
        }

        return rootView;
    }


    @OnClick(R.id.f_session_info_to_recipients_button)
    public void onClickRecipients() {
        if (globalHandler.sessionHandler.getMessageSender().getSenderType() == Sender.Type.student) {
            ((ImageView) rootView.findViewById(R.id.f_session_info_to_recipients_button)).setImageResource(R.drawable.button_down_recipient);
            ((SessionInfoListener) parentActivity).onRecipients();
        }
    }


    @OnClick(R.id.f_session_info_media_photo)
    public void onClickPhoto() {
        ((ImageView) rootView.findViewById(R.id.f_session_info_media_photo)).setImageResource(R.drawable.button_down_photo);
        ((SessionInfoListener) parentActivity).onPhoto();
    }


    @OnClick(R.id.f_session_info_media_audio)
    public void onClickAudio() {
        if (globalHandler.sessionHandler.getMessagePhoto() != null) {
            ((ImageView) rootView.findViewById(R.id.f_session_info_media_audio)).setImageResource(R.drawable.button_down_talk);
            ((SessionInfoListener) parentActivity).onAudio(audioPlayer);
        } else {
            // TODO let the user know to take a picture first
        }
    }


    @OnClick(R.id.f_session_info_send)
    public void onClickSend() {
        if (globalHandler.sessionHandler.getMessageAudio() != null) {
            ((SessionInfoListener) parentActivity).onSend();
        } else {
            // TODO Let the user know what they need to do.
        }
    }

    public interface SessionInfoListener {
        public void onRecipients();
        public void onPhoto();
        public void onAudio(AudioPlayer audioPlayer);
        public void onSend();
    }

}
