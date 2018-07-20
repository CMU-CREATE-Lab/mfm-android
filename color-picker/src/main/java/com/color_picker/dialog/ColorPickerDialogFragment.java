/*
 * Copyright (C) 2015 Daniel Nilsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.color_picker.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.color_picker.R;
import com.color_picker.view.ColorPanelView;
import com.color_picker.view.ColorPickerView;

import java.io.Serializable;

public class ColorPickerDialogFragment extends DialogFragment {

    private int mDialogId = -1;
    private ColorPickerView mColorPicker;
    private ColorPanelView mOldColorPanel;
    private ColorPanelView mNewColorPanel;
    private Button mOkButton;
    private ImageView closeButton;
    private ColorPickerDialogListener mListener;
    private SeekBar strokeWidthSeekBar;
    private int strokeWidth;
    private TextView textViewStrokeWidth;

    public static ColorPickerDialogFragment newInstance(int dialogId, int initialColor) {
        return newInstance(dialogId, null, null, initialColor, false, null);
    }

    public static ColorPickerDialogFragment newInstance(
            int dialogId, String title, String okButtonText, int initialColor, boolean showAlphaSlider, Serializable serializable) {

        ColorPickerDialogFragment frag = new ColorPickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt("id", dialogId);
        args.putString("title", title);
        args.putString("ok_button", okButtonText);
        args.putBoolean("alpha", showAlphaSlider);
        args.putInt("init_color", initialColor);
        args.putSerializable("serializable_class", serializable);

        frag.setArguments(args);

        return frag;
    }

    public static ColorPickerDialogFragment newInstance(
            int dialogId, String title, String okButtonText, int initialColor, boolean showAlphaSlider, float strokeWidth, Serializable serializable) {

        ColorPickerDialogFragment frag = new ColorPickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt("id", dialogId);
        args.putString("title", title);
        args.putString("ok_button", okButtonText);
        args.putBoolean("alpha", showAlphaSlider);
        args.putInt("init_color", initialColor);
        args.putInt("stroke_width", (int) strokeWidth);
        args.putSerializable("serializable_class", serializable);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogId = getArguments().getInt("id");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Log.d("color-picker-view", "onAttach()");

        mListener = (ColorPickerDialogListener) getArguments().getSerializable("serializable_class");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        setCancelable(false);


        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        return d;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.colorpickerview__dialog_color_picker, container);


        TextView titleView = (TextView) v.findViewById(android.R.id.title);

        mColorPicker = (ColorPickerView)
                v.findViewById(R.id.colorpickerview__color_picker_view);
        mOldColorPanel = (ColorPanelView)
                v.findViewById(R.id.colorpickerview__color_panel_old);
        mNewColorPanel = (ColorPanelView)
                v.findViewById(R.id.colorpickerview__color_panel_new);
        mOkButton = (Button) v.findViewById(android.R.id.button1);
        closeButton = (ImageView) v.findViewById(R.id.button_close);
        strokeWidthSeekBar = (SeekBar) v.findViewById(R.id.seek_bar_stroke_width);
        textViewStrokeWidth = (TextView) v.findViewById(R.id.text_view_stroke_width);


        mColorPicker.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {

            @Override
            public void onColorChanged(int newColor) {
                mNewColorPanel.setColor(newColor);
            }
        });

        OnClickListener closeListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onColorSelected(mDialogId, mColorPicker.getColor(), strokeWidthSeekBar.getProgress() + 10);
                getDialog().dismiss();
            }

        };

        mOkButton.setOnClickListener(closeListener);
        closeButton.setOnClickListener(closeListener);

        String title = getArguments().getString("title");

        if (title != null) {
            titleView.setText(title);
        } else {
            titleView.setVisibility(View.GONE);
        }

        strokeWidthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b)
			{
				textViewStrokeWidth.setText("Thickness of the stroke: " + Integer.toString(i + 10));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{

			}
		});

		int strokeWidth = getArguments().getInt("stroke_width");
		strokeWidthSeekBar.setProgress(strokeWidth - 10);
		textViewStrokeWidth.setText("Thickness of the stroke: " + Integer.toString(strokeWidth));

		if (savedInstanceState == null) {
            mColorPicker.setAlphaSliderVisible(
                    getArguments().getBoolean("alpha"));


            String ok = getArguments().getString("ok_button");
            if (ok != null) {
                mOkButton.setText(ok);
            }

            int initColor = getArguments().getInt("init_color");

            mOldColorPanel.setColor(initColor);
            mColorPicker.setColor(initColor, true);
        }


        return v;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public interface ColorPickerDialogListener {
        void onColorSelected(int dialogId, int color, int strokeSeekBarValue);
    }

}
