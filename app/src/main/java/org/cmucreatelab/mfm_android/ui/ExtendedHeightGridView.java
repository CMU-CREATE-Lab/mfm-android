package org.cmucreatelab.mfm_android.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Steve on 4/28/2016.
 */
public class ExtendedHeightGridView extends GridView {


    public ExtendedHeightGridView(Context context) {
        super(context);
    }


    public ExtendedHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public ExtendedHeightGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
