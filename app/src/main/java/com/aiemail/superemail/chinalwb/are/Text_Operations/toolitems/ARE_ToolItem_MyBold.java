package com.aiemail.superemail.chinalwb.are.Text_Operations.toolitems;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinalwb.are.Util;
import com.chinalwb.are.styles.toolitems.ARE_ToolItem_Bold;
import com.aiemail.superemail.R;

public class ARE_ToolItem_MyBold extends ARE_ToolItem_Bold {

    @Override
    public View getView(Context context) {
        if (null == context) {
            return mToolItemView;
        }
        if (mToolItemView == null) {
            ImageView imageView = new ImageView(context);
            int size = Util.getPixelByDp(context, 40);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.drawable.my_bold);
            imageView.bringToFront();
            mToolItemView = imageView;
        }

        return mToolItemView;
    }
}
