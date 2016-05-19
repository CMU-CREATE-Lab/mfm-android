package org.cmucreatelab.mfm_android.activities.fragments;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.SessionActivity;
import org.cmucreatelab.mfm_android.adapters.StudentAdapter;
import org.cmucreatelab.mfm_android.classes.AudioRecorder;
import org.cmucreatelab.mfm_android.classes.Group;
import org.cmucreatelab.mfm_android.classes.Sender;
import org.cmucreatelab.mfm_android.classes.Student;
import org.cmucreatelab.mfm_android.classes.User;
import org.cmucreatelab.mfm_android.helpers.GlobalHandler;
import org.cmucreatelab.mfm_android.helpers.static_classes.Constants;

import java.io.File;
import java.util.ArrayList;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        globalHandler = GlobalHandler.getInstance(this.getActivity().getApplicationContext());
        File messagePhoto = globalHandler.sessionHandler.getMessagePhoto();
        File messageAudio = globalHandler.sessionHandler.getMessageAudio();
        Sender sender = globalHandler.sessionHandler.getMessageSender();
        rootView =  inflater.inflate(R.layout.fragment_session_info, container, false);
        ButterKnife.bind(this, rootView);
        isReadyToSend = false;
        isReadyToRecordAudio = false;
        audioRecorder = new AudioRecorder(globalHandler.appContext);
        ImageView fromImage = (ImageView) rootView.findViewById(R.id.fromImage);
        TextView fromText = (TextView) rootView.findViewById(R.id.fromText);
        LinearLayout toUser = (LinearLayout) rootView.findViewById(R.id.toUsersLinearLayout);

        fromImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        fromImage.setAdjustViewBounds(true);
        fromText.setPadding(10, 10, 10, 10);
        fromText.setTextColor(Color.BLACK);
        fromText.setBackgroundColor(Color.WHITE);

        if (sender.getSenderType() == Sender.Type.Student) {
            mStudent = (Student)sender;
            String photoUrl = mStudent.getPhotoUrl();
            String url = Constants.MFM_API_URL + photoUrl;
            Picasso.with(globalHandler.appContext).load(url).into(fromImage);
            fromText.setText(mStudent.getFirstName());

            for (User user : globalHandler.sessionHandler.getRecipients()) {
                LinearLayout userLayout = new LinearLayout(this.getActivity());
                userLayout.setOrientation(LinearLayout.VERTICAL);
                userLayout.setPadding(2,2,2,2);
                ImageView image = new ImageView(this.getActivity());
                TextView name = new TextView(this.getActivity());
                photoUrl = user.getPhotoUrl();
                url = Constants.MFM_API_URL + photoUrl;

                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setAdjustViewBounds(true);
                Picasso.with(globalHandler.appContext).load(url).into(image);
                name.setText(user.getFirstName());
                name.setPadding(10, 10, 10, 10);
                name.setTextColor(Color.BLACK);
                name.setBackgroundColor(Color.WHITE);
                userLayout.addView(image);
                userLayout.addView(name);
                toUser.addView(userLayout);
            }
        } else {
            LinearLayout groupLayout = new LinearLayout(this.getActivity());
            groupLayout.setOrientation(LinearLayout.VERTICAL);
            groupLayout.setPadding(2,2,2,2);
            TextView name = new TextView(this.getActivity());
            ImageView image = new ImageView(this.getActivity());
            mGroup = (Group) sender;
            String photoUrl = mGroup.getPhotoUrl();
            String url = Constants.MFM_API_URL + photoUrl;

            fromText.setText(mGroup.getName());
            Picasso.with(globalHandler.appContext).load(url).into(fromImage);
            Picasso.with(globalHandler.appContext).load(url).into(image);
            name.setText(mGroup.getName());
            name.setPadding(10, 10, 10, 10);
            name.setBackgroundColor(Color.WHITE);
            name.setTextColor(Color.BLACK);
            groupLayout.addView(image);
            groupLayout.addView(name);
            toUser.addView(groupLayout);
        }

        // choosing whether or not to display the photo taken
        ImageView picture = (ImageView) rootView.findViewById(R.id.pictureTaken);
        if (messagePhoto != null) {
            int rotation = CameraFragment.getCameraOrientation(this.getActivity());
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap bitmap = BitmapFactory.decodeFile(globalHandler.sessionHandler.getMessagePhoto().getAbsolutePath());
            Bitmap rotated = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
            picture.setImageBitmap(rotated);
            isReadyToRecordAudio = true;
        } else {
            picture.setImageResource(R.drawable.button_disabled_photo_193x216px);
        }

        // choosing which audio Image to show
        audioPhoto = (ImageView) rootView.findViewById(R.id.audioRecorded);
        if (messagePhoto != null && messageAudio != null) {
            isReadyToSend = true;
            audioPhoto.setImageResource(R.drawable.soundwave_final);
        } else if (messagePhoto != null) {
            audioPhoto.setImageResource(R.drawable.button_up_talk);
            isReadyToRecordAudio = true;
        } else {
            audioPhoto.setImageResource(R.drawable.button_disabled_talk_193x216px);
        }

        // choosing whether or not to show the recipients button
        if (globalHandler.sessionHandler.getMessageSender().getSenderType() == Sender.Type.Student)
            ((ImageView) rootView.findViewById(R.id.fragment_sessionInfo_recipients)).setVisibility(View.VISIBLE);
        else
            ((ImageView) rootView.findViewById(R.id.fragment_sessionInfo_recipients)).setVisibility(View.INVISIBLE);

        this.getActivity().findViewById(R.id.selection_done_selecting_users).setVisibility(View.INVISIBLE);

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

    @OnClick(R.id.fragment_sessionInfo_recipients)
    public void chooseRecipients() {
        ((SessionActivity) this.getActivity()).chooseUsers();
    }

}
