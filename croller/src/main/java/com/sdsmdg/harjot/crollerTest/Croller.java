package com.sdsmdg.harjot.crollerTest;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.sdsmdg.harjot.croller.R;
import com.sdsmdg.harjot.crollerTest.utilities.Utils;

public class Croller extends View {

    private float midx, midy;
    private Paint textPaint, circlePaint, circlePaint2, linePaint;
    private float currdeg = 0, deg = 3, downdeg = 0;

    private boolean isContinuous = false;

    private int backCircleColor = Color.parseColor("#222222");
    private int mainCircleColor = Color.parseColor("#000000");
    private int indicatorColor = Color.parseColor("#FFA036");
    private int progressPrimaryColor = Color.parseColor("#FFA036");
    private int progressSecondaryColor = Color.parseColor("#111111");

    private int backCircleDisabledColor = Color.parseColor("#82222222");
    private int mainCircleDisabledColor = Color.parseColor("#82000000");
    private int indicatorDisabledColor = Color.parseColor("#82FFA036");
    private int progressPrimaryDisabledColor = Color.parseColor("#82FFA036");
    private int progressSecondaryDisabledColor = Color.parseColor("#82111111");

    private float progressPrimaryCircleSize = -1;
    private float progressSecondaryCircleSize = -1;

    private float progressPrimaryStrokeWidth = 25;
    private float progressSecondaryStrokeWidth = 10;

    private float mainCircleRadius = -1;
    private float backCircleRadius = -1;
    private float progressRadius = -1;

    private int max = 25;
    private int min = 1;

    private float indicatorWidth = 7;

    private String label = "Label";
    private String labelFont;
    private int labelStyle = 0;
    private float labelSize = 14;
    private int labelColor = Color.WHITE;

    private int labelDisabledColor = Color.BLACK;

    private int startOffset = 30;
    private int startOffset2 = 0;
    private int sweepAngle = -1;

    private float touchOutsideCircleSensibility = 1f;

    private boolean isEnabled = true;

    private boolean isAntiClockwise = false;

    private boolean startEventSent = false;

    RectF oval;

    private onProgressChangedListener mProgressChangeListener;
    private OnCrollerChangeListener mCrollerChangeListener;

    public interface onProgressChangedListener {
        void onProgressChanged(int progress);
    }

    public void setOnProgressChangedListener(onProgressChangedListener mProgressChangeListener) {
        this.mProgressChangeListener = mProgressChangeListener;
    }

    public void setOnCrollerChangeListener(OnCrollerChangeListener mCrollerChangeListener) {
        this.mCrollerChangeListener = mCrollerChangeListener;
    }

    public Croller(Context context) {
        super(context);
        init();
    }

    public Croller(Context context, AttributeSet attrs) {
        super(context, attrs);
        initXMLAttrs(context, attrs);
        init();
    }

    public Croller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initXMLAttrs(context, attrs);
        init();
    }

    private void init() {

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setFakeBoldText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(labelSize);

        generateTypeface();

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(progressSecondaryStrokeWidth);
        circlePaint.setStyle(Paint.Style.FILL);

        circlePaint2 = new Paint();
        circlePaint2.setAntiAlias(true);
        circlePaint2.setStrokeWidth(progressPrimaryStrokeWidth);
        circlePaint2.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(indicatorWidth);

        if (isEnabled) {
            circlePaint2.setColor(progressPrimaryColor);
            circlePaint.setColor(progressSecondaryColor);
            linePaint.setColor(indicatorColor);
            textPaint.setColor(labelColor);
        } else {
            circlePaint2.setColor(progressPrimaryDisabledColor);
            circlePaint.setColor(progressSecondaryDisabledColor);
            linePaint.setColor(indicatorDisabledColor);
            textPaint.setColor(labelDisabledColor);
        }

        oval = new RectF();

    }

    private void generateTypeface() {
        Typeface plainLabel = Typeface.DEFAULT;
        if (getLabelFont() != null && !getLabelFont().isEmpty()) {
            AssetManager assetMgr = getContext().getAssets();
            plainLabel = Typeface.createFromAsset(assetMgr, getLabelFont());
        }

        switch (getLabelStyle()) {
            case 0:
                textPaint.setTypeface(plainLabel);
                break;
            case 1:
                textPaint.setTypeface(Typeface.create(plainLabel, Typeface.BOLD));
                break;
            case 2:
                textPaint.setTypeface(Typeface.create(plainLabel, Typeface.ITALIC));
                break;
            case 3:
                textPaint.setTypeface(Typeface.create(plainLabel, Typeface.BOLD_ITALIC));
                break;

        }

    }

    private void initXMLAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Croller);

        setEnabled(a.getBoolean(R.styleable.Croller_enabled, true));
        setProgress(a.getInt(R.styleable.Croller_start_progress, 1));
        setLabel(a.getString(R.styleable.Croller_label));

        setBackCircleColor(a.getColor(R.styleable.Croller_back_circle_color, backCircleColor));
        setMainCircleColor(a.getColor(R.styleable.Croller_main_circle_color, mainCircleColor));
        setIndicatorColor(a.getColor(R.styleable.Croller_indicator_color, indicatorColor));
        setProgressPrimaryColor(a.getColor(R.styleable.Croller_progress_primary_color, progressPrimaryColor));
        setProgressSecondaryColor(a.getColor(R.styleable.Croller_progress_secondary_color, progressSecondaryColor));

        setBackCircleDisabledColor(a.getColor(R.styleable.Croller_back_circle_disable_color, backCircleDisabledColor));
        setMainCircleDisabledColor(a.getColor(R.styleable.Croller_main_circle_disable_color, mainCircleDisabledColor));
        setIndicatorDisabledColor(a.getColor(R.styleable.Croller_indicator_disable_color, indicatorDisabledColor));
        setProgressPrimaryDisabledColor(a.getColor(R.styleable.Croller_progress_primary_disable_color, progressPrimaryDisabledColor));
        setProgressSecondaryDisabledColor(a.getColor(R.styleable.Croller_progress_secondary_disable_color, progressSecondaryDisabledColor));

        setLabelSize(a.getDimension(R.styleable.Croller_label_size, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                labelSize, getResources().getDisplayMetrics())));
        setLabelColor(a.getColor(R.styleable.Croller_label_color, labelColor));
        setlabelDisabledColor(a.getColor(R.styleable.Croller_label_disabled_color, labelDisabledColor));
        setLabelFont(a.getString(R.styleable.Croller_label_font));
        setLabelStyle(a.getInt(R.styleable.Croller_label_style, 0));
        setIndicatorWidth(a.getFloat(R.styleable.Croller_indicator_width, 7));
        setIsContinuous(a.getBoolean(R.styleable.Croller_is_continuous, false));
        setProgressPrimaryCircleSize(a.getFloat(R.styleable.Croller_progress_primary_circle_size, -1));
        setProgressSecondaryCircleSize(a.getFloat(R.styleable.Croller_progress_secondary_circle_size, -1));
        setProgressPrimaryStrokeWidth(a.getFloat(R.styleable.Croller_progress_primary_stroke_width, 25));
        setProgressSecondaryStrokeWidth(a.getFloat(R.styleable.Croller_progress_secondary_stroke_width, 10));
        setSweepAngle(a.getInt(R.styleable.Croller_sweep_angle, -1));
        setStartOffset(a.getInt(R.styleable.Croller_start_offset, 30));
        setMax(a.getInt(R.styleable.Croller_max, 25));
        setMin(a.getInt(R.styleable.Croller_min, 1));
        deg = min + 2;
        setBackCircleRadius(a.getFloat(R.styleable.Croller_back_circle_radius, -1));
        setProgressRadius(a.getFloat(R.styleable.Croller_progress_radius, -1));
        setTouchOutsideCircleSensibility(a.getFloat(R.styleable.Croller_touch_outsize_circle_sensibility, touchOutsideCircleSensibility));
        setAntiClockwise(a.getBoolean(R.styleable.Croller_anticlockwise, false));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int minWidth = (int) Utils.convertDpToPixel(160, getContext());
        int minHeight = (int) Utils.convertDpToPixel(160, getContext());

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(minWidth, widthSize);
        } else {
            // only in case of ScrollViews, otherwise MeasureSpec.UNSPECIFIED is never triggered
            // If width is wrap_content i.e. MeasureSpec.UNSPECIFIED, then make width equal to height
            width = heightSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(minHeight, heightSize);
        } else {
            // only in case of ScrollViews, otherwise MeasureSpec.UNSPECIFIED is never triggered
            // If height is wrap_content i.e. MeasureSpec.UNSPECIFIED, then make height equal to width
            height = widthSize;
        }

        if (widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
            width = minWidth;
            height = minHeight;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        midx = getWidth() / 2;
        midy = getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mProgressChangeListener != null)
            mProgressChangeListener.onProgressChanged((int) (deg - 2));

        if (mCrollerChangeListener != null)
            mCrollerChangeListener.onProgressChanged(this, (int) (deg - 2));

        if (isEnabled) {
            circlePaint2.setColor(progressPrimaryColor);
            circlePaint.setColor(progressSecondaryColor);
            linePaint.setColor(indicatorColor);
            textPaint.setColor(labelColor);
        } else {
            circlePaint2.setColor(progressPrimaryDisabledColor);
            circlePaint.setColor(progressSecondaryDisabledColor);
            linePaint.setColor(indicatorDisabledColor);
            textPaint.setColor(labelDisabledColor);
        }

        if (!isContinuous) {

            startOffset2 = startOffset - 15;

            linePaint.setStrokeWidth(indicatorWidth);
            textPaint.setTextSize(labelSize);

            int radius = (int) (Math.min(midx, midy) * ((float) 14.5 / 16));

            if (sweepAngle == -1) {
                sweepAngle = 360 - (2 * startOffset2);
            }

            if (mainCircleRadius == -1) {
                mainCircleRadius = radius * ((float) 11 / 15);
            }
            if (backCircleRadius == -1) {
                backCircleRadius = radius * ((float) 13 / 15);
            }
            if (progressRadius == -1) {
                progressRadius = radius;
            }

            float x, y;
            float deg2 = Math.max(3, deg);
            float deg3 = Math.min(deg, max + 2);
            for (int i = (int) (deg2); i < max + 3; i++) {
                float tmp = ((float) startOffset2 / 360) + ((float) sweepAngle / 360) * (float) i / (max + 5);

                if (isAntiClockwise) {
                    tmp = 1.0f - tmp;
                }

                x = midx + (float) (progressRadius * Math.sin(2 * Math.PI * (1.0 - tmp)));
                y = midy + (float) (progressRadius * Math.cos(2 * Math.PI * (1.0 - tmp)));
                if (progressSecondaryCircleSize == -1)
                    canvas.drawCircle(x, y, ((float) radius / 30 * ((float) 20 / max) * ((float) sweepAngle / 270)), circlePaint);
                else
                    canvas.drawCircle(x, y, progressSecondaryCircleSize, circlePaint);
            }
            for (int i = 3; i <= deg3; i++) {
                float tmp = ((float) startOffset2 / 360) + ((float) sweepAngle / 360) * (float) i / (max + 5);

                if (isAntiClockwise) {
                    tmp = 1.0f - tmp;
                }

                x = midx + (float) (progressRadius * Math.sin(2 * Math.PI * (1.0 - tmp)));
                y = midy + (float) (progressRadius * Math.cos(2 * Math.PI * (1.0 - tmp)));
                if (progressPrimaryCircleSize == -1)
                    canvas.drawCircle(x, y, (progressRadius / 15 * ((float) 20 / max) * ((float) sweepAngle / 270)), circlePaint2);
                else
                    canvas.drawCircle(x, y, progressPrimaryCircleSize, circlePaint2);
            }

            float tmp2 = ((float) startOffset2 / 360) + ((float) sweepAngle / 360) * deg / (max + 5);

            if (isAntiClockwise) {
                tmp2 = 1.0f - tmp2;
            }

            float x1 = midx + (float) (radius * ((float) 2 / 5) * Math.sin(2 * Math.PI * (1.0 - tmp2)));
            float y1 = midy + (float) (radius * ((float) 2 / 5) * Math.cos(2 * Math.PI * (1.0 - tmp2)));
            float x2 = midx + (float) (radius * ((float) 3 / 5) * Math.sin(2 * Math.PI * (1.0 - tmp2)));
            float y2 = midy + (float) (radius * ((float) 3 / 5) * Math.cos(2 * Math.PI * (1.0 - tmp2)));

            if (isEnabled)
                circlePaint.setColor(backCircleColor);
            else
                circlePaint.setColor(backCircleDisabledColor);
            canvas.drawCircle(midx, midy, backCircleRadius, circlePaint);
            if (isEnabled)
                circlePaint.setColor(mainCircleColor);
            else
                circlePaint.setColor(mainCircleDisabledColor);
            canvas.drawCircle(midx, midy, mainCircleRadius, circlePaint);
            canvas.drawText(label, midx, midy + (float) (radius * 1.1)-textPaint.getFontMetrics().descent, textPaint);
            canvas.drawLine(x1, y1, x2, y2, linePaint);

        } else {

            int radius = (int) (Math.min(midx, midy) * ((float) 14.5 / 16));

            if (sweepAngle == -1) {
                sweepAngle = 360 - (2 * startOffset);
            }

            if (mainCircleRadius == -1) {
                mainCircleRadius = radius * ((float) 11 / 15);
            }
            if (backCircleRadius == -1) {
                backCircleRadius = radius * ((float) 13 / 15);
            }
            if (progressRadius == -1) {
                progressRadius = radius;
            }

            circlePaint.setStrokeWidth(progressSecondaryStrokeWidth);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint2.setStrokeWidth(progressPrimaryStrokeWidth);
            circlePaint2.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeWidth(indicatorWidth);
            textPaint.setTextSize(labelSize);

            float deg3 = Math.min(deg, max + 2);

            oval.set(midx - progressRadius, midy - progressRadius, midx + progressRadius, midy + progressRadius);

            canvas.drawArc(oval, (float) 90 + startOffset, (float) sweepAngle, false, circlePaint);
            if (isAntiClockwise) {
                canvas.drawArc(oval, (float) 90 - startOffset, -1 * ((deg3 - 2) * ((float) sweepAngle / max)), false, circlePaint2);
            } else {
                canvas.drawArc(oval, (float) 90 + startOffset, ((deg3 - 2) * ((float) sweepAngle / max)), false, circlePaint2);
            }

            float tmp2 = ((float) startOffset / 360) + (((float) sweepAngle / 360) * ((deg - 2) / (max)));

            if (isAntiClockwise) {
                tmp2 = 1.0f - tmp2;
            }

            float x1 = midx + (float) (radius * ((float) 2 / 5) * Math.sin(2 * Math.PI * (1.0 - tmp2)));
            float y1 = midy + (float) (radius * ((float) 2 / 5) * Math.cos(2 * Math.PI * (1.0 - tmp2)));
            float x2 = midx + (float) (radius * ((float) 3 / 5) * Math.sin(2 * Math.PI * (1.0 - tmp2)));
            float y2 = midy + (float) (radius * ((float) 3 / 5) * Math.cos(2 * Math.PI * (1.0 - tmp2)));

            circlePaint.setStyle(Paint.Style.FILL);

            if (isEnabled)
                circlePaint.setColor(backCircleColor);
            else
                circlePaint.setColor(backCircleDisabledColor);
            canvas.drawCircle(midx, midy, backCircleRadius, circlePaint);
            if (isEnabled)
                circlePaint.setColor(mainCircleColor);
            else
                circlePaint.setColor(mainCircleDisabledColor);
            canvas.drawCircle(midx, midy, mainCircleRadius, circlePaint);
            canvas.drawText(label, midx, midy + (float) (radius * 1.1)-textPaint.getFontMetrics().descent, textPaint);
            canvas.drawLine(x1, y1, x2, y2, linePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (!isEnabled)
            return false;

        if (Utils.getDistance(e.getX(), e.getY(), midx, midy) > getTouchOutsideCircleSensibility() * Math.max(mainCircleRadius, Math.max(backCircleRadius, progressRadius))) {
            if (startEventSent && mCrollerChangeListener != null) {
                mCrollerChangeListener.onStopTrackingTouch(this);
                startEventSent = false;
            }
            return super.onTouchEvent(e);
        }

        if (e.getAction() == MotionEvent.ACTION_DOWN) {

            float dx = e.getX() - midx;
            float dy = e.getY() - midy;
            downdeg = (float) ((Math.atan2(dy, dx) * 180) / Math.PI);
            downdeg -= 90;
            if (downdeg < 0) {
                downdeg += 360;
            }
            downdeg = (float) Math.floor((downdeg / 360) * (max + 5));

            if (mCrollerChangeListener != null) {
                mCrollerChangeListener.onStartTrackingTouch(this);
                startEventSent = true;
            }

            return true;
        }
        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = e.getX() - midx;
            float dy = e.getY() - midy;
            currdeg = (float) ((Math.atan2(dy, dx) * 180) / Math.PI);
            currdeg -= 90;
            if (currdeg < 0) {
                currdeg += 360;
            }
            currdeg = (float) Math.floor((currdeg / 360) * (max + 5));

            if ((currdeg / (max + 4)) > 0.75f && ((downdeg - 0) / (max + 4)) < 0.25f) {
                if (isAntiClockwise) {
                    deg++;
                    if (deg > max + 2) {
                        deg = max + 2;
                    }
                } else {
                    deg--;
                    if (deg < (min + 2)) {
                        deg = (min + 2);
                    }
                }
            } else if ((downdeg / (max + 4)) > 0.75f && ((currdeg - 0) / (max + 4)) < 0.25f) {
                if (isAntiClockwise) {
                    deg--;
                    if (deg < (min + 2)) {
                        deg = (min + 2);
                    }
                } else {
                    deg++;
                    if (deg > max + 2) {
                        deg = max + 2;
                    }
                }
            } else {
                if (isAntiClockwise) {
                    deg -= (currdeg - downdeg);
                } else {
                    deg += (currdeg - downdeg);
                }
                if (deg > max + 2) {
                    deg = max + 2;
                }
                if (deg < (min + 2)) {
                    deg = (min + 2);
                }
            }

            downdeg = currdeg;

            invalidate();
            return true;

        }
        if (e.getAction() == MotionEvent.ACTION_UP) {
            if (mCrollerChangeListener != null) {
                mCrollerChangeListener.onStopTrackingTouch(this);
                startEventSent = false;
            }
            return true;
        }
        return super.onTouchEvent(e);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (getParent() != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.dispatchTouchEvent(event);
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
        invalidate();
    }

    public int getProgress() {
        return (int) (deg - 2);
    }

    public void setProgress(int x) {
        deg = x + 2;
        invalidate();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String txt) {
        label = txt;
        invalidate();
    }

    public int getBackCircleColor() {
        return backCircleColor;
    }

    public void setBackCircleColor(int backCircleColor) {
        this.backCircleColor = backCircleColor;
        invalidate();
    }

    public int getMainCircleColor() {
        return mainCircleColor;
    }

    public void setMainCircleColor(int mainCircleColor) {
        this.mainCircleColor = mainCircleColor;
        invalidate();
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public int getProgressPrimaryColor() {
        return progressPrimaryColor;
    }

    public void setProgressPrimaryColor(int progressPrimaryColor) {
        this.progressPrimaryColor = progressPrimaryColor;
        invalidate();
    }

    public int getProgressSecondaryColor() {
        return progressSecondaryColor;
    }

    public void setProgressSecondaryColor(int progressSecondaryColor) {
        this.progressSecondaryColor = progressSecondaryColor;
        invalidate();
    }

    public int getBackCircleDisabledColor() {
        return backCircleDisabledColor;
    }

    public void setBackCircleDisabledColor(int backCircleDisabledColor) {
        this.backCircleDisabledColor = backCircleDisabledColor;
        invalidate();
    }

    public int getMainCircleDisabledColor() {
        return mainCircleDisabledColor;
    }

    public void setMainCircleDisabledColor(int mainCircleDisabledColor) {
        this.mainCircleDisabledColor = mainCircleDisabledColor;
        invalidate();
    }

    public int getIndicatorDisabledColor() {
        return indicatorDisabledColor;
    }

    public void setIndicatorDisabledColor(int indicatorDisabledColor) {
        this.indicatorDisabledColor = indicatorDisabledColor;
        invalidate();
    }

    public int getProgressPrimaryDisabledColor() {
        return progressPrimaryDisabledColor;
    }

    public void setProgressPrimaryDisabledColor(int progressPrimaryDisabledColor) {
        this.progressPrimaryDisabledColor = progressPrimaryDisabledColor;
        invalidate();
    }

    public int getProgressSecondaryDisabledColor() {
        return progressSecondaryDisabledColor;
    }

    public void setProgressSecondaryDisabledColor(int progressSecondaryDisabledColor) {
        this.progressSecondaryDisabledColor = progressSecondaryDisabledColor;
        invalidate();
    }

    public float getLabelSize() {
        return labelSize;
    }

    public void setLabelSize(float labelSize) {
        this.labelSize = labelSize;
        invalidate();
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
        invalidate();
    }

    public int getlabelDisabledColor() {
        return labelDisabledColor;
    }

    public void setlabelDisabledColor(int labelDisabledColor) {
        this.labelDisabledColor = labelDisabledColor;
        invalidate();
    }

    public String getLabelFont() {
        return labelFont;
    }

    public void setLabelFont(String labelFont) {
        this.labelFont = labelFont;
        if (textPaint != null)
            generateTypeface();
        invalidate();
    }

    public int getLabelStyle() {
        return labelStyle;
    }

    public void setLabelStyle(int labelStyle) {
        this.labelStyle = labelStyle;
        invalidate();
    }

    public float getIndicatorWidth() {
        return indicatorWidth;
    }

    public void setIndicatorWidth(float indicatorWidth) {
        this.indicatorWidth = indicatorWidth;
        invalidate();
    }

    public boolean isContinuous() {
        return isContinuous;
    }

    public void setIsContinuous(boolean isContinuous) {
        this.isContinuous = isContinuous;
        invalidate();
    }

    public float getProgressPrimaryCircleSize() {
        return progressPrimaryCircleSize;
    }

    public void setProgressPrimaryCircleSize(float progressPrimaryCircleSize) {
        this.progressPrimaryCircleSize = progressPrimaryCircleSize;
        invalidate();
    }

    public float getProgressSecondaryCircleSize() {
        return progressSecondaryCircleSize;
    }

    public void setProgressSecondaryCircleSize(float progressSecondaryCircleSize) {
        this.progressSecondaryCircleSize = progressSecondaryCircleSize;
        invalidate();
    }

    public float getProgressPrimaryStrokeWidth() {
        return progressPrimaryStrokeWidth;
    }

    public void setProgressPrimaryStrokeWidth(float progressPrimaryStrokeWidth) {
        this.progressPrimaryStrokeWidth = progressPrimaryStrokeWidth;
        invalidate();
    }

    public float getProgressSecondaryStrokeWidth() {
        return progressSecondaryStrokeWidth;
    }

    public void setProgressSecondaryStrokeWidth(float progressSecondaryStrokeWidth) {
        this.progressSecondaryStrokeWidth = progressSecondaryStrokeWidth;
        invalidate();
    }

    public int getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(int sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
    }

    public int getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
        invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max < min) {
            this.max = min;
        } else {
            this.max = max;
        }
        invalidate();
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        if (min < 0) {
            this.min = 0;
        } else if (min > max) {
            this.min = max;
        } else {
            this.min = min;
        }
        invalidate();
    }

    public float getMainCircleRadius() {
        return mainCircleRadius;
    }

    public void setMainCircleRadius(float mainCircleRadius) {
        this.mainCircleRadius = mainCircleRadius;
        invalidate();
    }

    public float getBackCircleRadius() {
        return backCircleRadius;
    }

    public void setBackCircleRadius(float backCircleRadius) {
        this.backCircleRadius = backCircleRadius;
        invalidate();
    }

    public float getProgressRadius() {
        return progressRadius;
    }

    public void setProgressRadius(float progressRadius) {
        this.progressRadius = progressRadius;
        invalidate();
    }

    public double getTouchOutsideCircleSensibility() {
        return touchOutsideCircleSensibility;
    }

    public void setTouchOutsideCircleSensibility(float touchOutsideCircleSensibility) {
        this.touchOutsideCircleSensibility = touchOutsideCircleSensibility;
    }

    public boolean isAntiClockwise() {
        return isAntiClockwise;
    }

    public void setAntiClockwise(boolean antiClockwise) {
        isAntiClockwise = antiClockwise;
        invalidate();
    }
}
