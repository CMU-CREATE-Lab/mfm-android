package org.cmucreatelab.mfm_android.activities.fragments;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.cmucreatelab.mfm_android.R;
import org.cmucreatelab.mfm_android.activities.SessionActivity;
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

    private Student mStudent;
    private Group mGroup;
    private View rootView;
    private GlobalHandler globalHandler;
    private boolean isReadyToSend;


    public static Fragment newInstance() {
        SessionInfoFragment fragment = new SessionInfoFragment();
        return fragment;
    }


    public SessionInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_session_info, container, false);
        ButterKnife.bind(this, rootView);
        globalHandler = GlobalHandler.getInstance(this.getActivity().getApplicationContext());
        File messagePhoto = globalHandler.sessionHandler.getMessagePhoto();
        File messageAudio = globalHandler.sessionHandler.getMessageAudio();
        isReadyToSend = false;

        Sender sender = globalHandler.sessionHandler.getMessageSender();
        TextView fromName = (TextView) rootView.findViewById(R.id.fromText);
        LinearLayout toImages = (LinearLayout) rootView.findViewById(R.id.toImagesLinearLayout);
        if (sender.getSenderType() == Sender.Type.Student) {
            mStudent = (Student)sender;
            String photoUrl = mStudent.getPhotoUrl();
            String url = Constants.MFM_API_URL + photoUrl;
            Picasso.with(this.getActivity().getApplicationContext()).load(url).into((ImageView) rootView.findViewById(R.id.fromImage));
            fromName.setText("From: " + mStudent.getFirstName());

            for (User user : globalHandler.sessionHandler.getRecipients()) {
                ImageView image = new ImageView(this.getActivity());
                TextView name = new TextView(this.getActivity());
                name.setText(" " + user.getFirstName());
                Picasso.with(this.getActivity().getApplicationContext()).load(url).into(image);
                toImages.addView(name);
                toImages.addView(image);
            }
        } else {
            mGroup = (Group) sender;
            String photoUrl = mGroup.getPhotoUrl();
            String url = Constants.MFM_API_URL + photoUrl;
            Picasso.with(this.getActivity().getApplicationContext()).load(url).into((ImageView) rootView.findViewById(R.id.fromImage));
            fromName.setText("From: " + mGroup.getName());
            ImageView image = new ImageView(this.getActivity());
            Picasso.with(this.getActivity().getApplicationContext()).load(url).into(image);
            TextView toName = new TextView(this.getActivity());
            toName.setText(" " + mGroup.getName());
            toImages.addView(toName);
            toImages.addView(image);
        }

        // TODO - change the images to appropriate ones
        ImageView picture = (ImageView) rootView.findViewById(R.id.pictureTaken);
        if (messagePhoto != null) {
            Matrix matrix = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeFile(globalHandler.sessionHandler.getMessagePhoto().getAbsolutePath());
            matrix.postRotate(90);
            Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            picture.setImageBitmap(rotated);
        } else {
            picture.setImageResource(R.drawable.button_disabled_photo_193x216px);
        }

        ImageView audio = (ImageView) rootView.findViewById(R.id.audioRecorded);
        if (messagePhoto != null && messageAudio != null) {
            isReadyToSend = true;
        } else if (messagePhoto != null) {
            // TODO - update the audio picture to be able to record a message.
            audio.setImageResource(R.drawable.soundwave_final);
        } else {
            audio.setImageResource(R.drawable.soundwave_mask);
        }


        return rootView;
    }


    @OnClick(R.id.audioRecorded)
    public void recordMessage() {
        ((SessionActivity) this.getActivity()).recordAudio();
    }


    @OnClick(R.id.sendMessageButton)
    public void sendMessage() {
        if (isReadyToSend){
            globalHandler.sessionHandler.sendMessage();
        }
    }

}
