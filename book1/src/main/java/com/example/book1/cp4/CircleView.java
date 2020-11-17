package com.example.book1.cp4;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.TimeZoneFormat;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.book1.R;

/*********************************************
 * @author daiyh
 * 创建日期：2020-10-26
 * 描述：
 *********************************************
 */
public class CircleView extends View {

    private final int mColor = Color.RED;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint.setColor(mColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;
        canvas.drawCircle(width >> 1, height >> 1, radius, mPaint);
    }
}
