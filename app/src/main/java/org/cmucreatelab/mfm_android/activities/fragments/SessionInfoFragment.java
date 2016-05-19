package org.cmucreatelab.mfm_android.activities.fragments;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.SessionActivity;
import org.cmucreatelab.mfm_android.classes.AudioRecorder;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SessionInfoFragment extends Fragment {

    private GlobalHandler globalHandler;
    private AudioRecorder audioRecorder;
    private Student mStudent;
    private Group mGroup;
    private View rootView;
    private ImageView audioPhoto;
    private boolean isReadyToSend;
    private boolean isReadyToRecordAudio;


    public static Fragment newInstance() {
        SessionInfoFragment fragment = new SessionInfoFragment();
        return fragment;
    }


    public SessionInfoFragment() {
        // Required empty public constructor
    }


    // TODO use an adapter..this is ugly...very ugly
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_session_info, container, false);
        ButterKnife.bind(this, rootView);
        globalHandler = GlobalHandler.getInstance(this.getActivity().getApplicationContext());
        File messagePhoto = globalHandler.sessionHandler.getMessagePhoto();
        File messageAudio = globalHandler.sessionHandler.getMessageAudio();
        isReadyToSend = false;
        isReadyToRecordAudio = false;
        audioRecorder = new AudioRecorder(globalHandler.appContext);
        Sender sender = globalHandler.sessionHandler.getMessageSender();
        TextView fromText = (TextView) rootView.findViewById(R.id.fromText);
        LinearLayout toUsers = (LinearLayout) rootView.findViewById(R.id.toUsersLinearLayout);

        if (sender.getSenderType() == Sender.Type.Student) {
            mStudent = (Student)sender;
            String photoUrl = mStudent.getPhotoUrl();
            String url = Constants.MFM_API_URL + photoUrl;
            Picasso.with(globalHandler.appContext).load(url).into((ImageView) rootView.findViewById(R.id.fromImage));
            fromText.setText(mStudent.getFirstName());

            for (User user : globalHandler.sessionHandler.getRecipients()) {
                ImageView image = new ImageView(this.getActivity());
                TextView name = new TextView(this.getActivity());
                photoUrl = user.getPhotoUrl();
                url = Constants.MFM_API_URL + photoUrl;

                name.setText(user.getFirstName());
                Picasso.with(globalHandler.appContext).load(url).into(image);
                toUsers.addView(name);
                toUsers.addView(image);
            }
        } else {
            TextView name = new TextView(this.getActivity());
            ImageView image = new ImageView(this.getActivity());
            mGroup = (Group) sender;
            String photoUrl = mGroup.getPhotoUrl();
            String url = Constants.MFM_API_URL + photoUrl;

            fromText.setText(mGroup.getName());
            Picasso.with(globalHandler.appContext).load(url).into((ImageView) rootView.findViewById(R.id.fromImage));
            Picasso.with(globalHandler.appContext).load(url).into(image);
            name.setText(mGroup.getName());
            toUsers.addView(name);
            toUsers.addView(image);
        }

        ImageView picture = (ImageView) rootView.findViewById(R.id.pictureTaken);
        if (messagePhoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(globalHandler.sessionHandler.getMessagePhoto().getAbsolutePath());
            picture.setImageBitmap(bitmap);
            isReadyToRecordAudio = true;
        } else {
            picture.setImageResource(R.drawable.button_disabled_photo_193x216px);
        }

        audioPhoto = (ImageView) rootView.findViewById(R.id.audioRecorded);
        if (messagePhoto != null && messageAudio != null) {
            isReadyToSend = true;
        } else if (messagePhoto != null) {
            audioPhoto.setImageResource(R.drawable.button_up_talk);
            isReadyToRecordAudio = true;
        } else {
            audioPhoto.setImageResource(R.drawable.button_disabled_talk_193x216px);
        }


        return rootView;
    }


    @OnClick(R.id.audioRecorded)
    public void audioListener() {
        if (isReadyToRecordAudio && !audioRecorder.isRecording) {
            audioPhoto.setImageResource(R.drawable.button_up_talkstop);
            audioRecorder.startRecording();
        } else if (audioRecorder.isRecording) {
            audioPhoto.setImageResource(R.drawable.soundwave_final);
            audioRecorder.stopRecording();
        }
    }


    @OnClick(R.id.sendMessageButton)
    public void sendMessage() {
        if (isReadyToSend){
            globalHandler.sessionHandler.sendMessage();
        }
    }


    @OnClick(R.id.pictureTaken)
    public void takePicture() {
        if (globalHandler.sessionHandler.getMessagePhoto() == null) {
            ((SessionActivity) this.getActivity()).startCamera();
        }
    }

}
