package com.newsolution.almhrab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by hp on 2/5/2018.
 */
        import android.content.Context;
        import android.content.res.TypedArray;
        import android.graphics.Typeface;
        import android.os.Handler;
        import android.os.Looper;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.ViewGroup;
        import android.view.Window;
        import android.widget.ScrollView;
        import android.widget.TextView;

/**
 * A {@link TextView} with vertical marquee effect. The animation speed can be set using
 * {@link #setMarqueeSpeed(int)} or through XML declaration. By default, the marquee effect
 * animation starts automatically when this view is attached to a {@link Window}.
 * Set {@code autoStartMarquee} to {@code false} to disable this behavior.
 */
public class HorizontalMarqueeTextView extends ScrollView {
    private static final String TAG = HorizontalMarqueeTextView.class.getName();

    private static final int MIN_MARQUEE_SPEED = 1;
    private static final int MAX_MARQUEE_SPEED = 1000;

    private static final double SECOND = 1000;

    private Handler handler;

    private TextView textView;

    private int     marqueeSpeed;
    private boolean marqueeStarted;
    private boolean marqueePaused;
    private boolean isAnimating;

    private int unitDisplacement;

    public HorizontalMarqueeTextView(final Context context) {
        super(context);

        this.init(context, null);
    }

    public HorizontalMarqueeTextView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

        this.init(context, attrs);
    }

    public HorizontalMarqueeTextView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        this.init(context, attrs);
    }

    /**
     * Returns the text content of this text view.
     * @return The text content of this text view.
     */
    public CharSequence getText() {
        return this.textView.getText();
    }

    /**
     * Sets a string for this text view.
     * @param text The text to set.
     */
    public void setText(final CharSequence text) {
        this.textView.setText(text);
    }

    /**
     * Returns the speed of the marquee effect animation.
     * @return The speed of the marquee effect animation.
     */
    public int getMarqueeSpeed() {
        return this.marqueeSpeed;
    }

    /**
     * Sets the speed of the marquee effect animation. Valid range is [1, 1000].
     * @param marqueeSpeed The speed of the marquee effect animation to set. Valid range is [1, 1000].
     */
    public void setMarqueeSpeed(final int marqueeSpeed) {
        this.marqueeSpeed = Math.min( HorizontalMarqueeTextView.MAX_MARQUEE_SPEED, Math.max( HorizontalMarqueeTextView.MIN_MARQUEE_SPEED, marqueeSpeed));
    }

    /**
     * Starts the marquee effect animation.
     */
    public void startMarquee() {
        this.marqueeStarted = true;
        this.marqueePaused  = false;

        if (!this.isAnimating) {
            this.isAnimating = true;

            new Thread(new Runnable() {
                @Override
                public void run() {
                     HorizontalMarqueeTextView.this.animateTextView();
                }
            }).start();
        }
    }

    /**
     * Stops the marquee effect animation.
     */
    public void stopMarquee() {
        this.marqueeStarted = false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (this.marqueeStarted) {
            this.startMarquee();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        this.marqueePaused = true;
    }

    @Override
    protected float getTopFadingEdgeStrength() {
        if (this.getChildCount() == 0) {
            return 0;
        }

        final int length  = this.getHorizontalFadingEdgeLength();
        final int scrollX = this.textView.getScrollX();

        if (scrollX < length) {
            return scrollX / (float)length;
        }

        return 1;
    }

    private void init(final Context context, final AttributeSet attrs) {
        this.handler = new Handler(Looper.getMainLooper());

        // 1dp per cycle
        this.unitDisplacement = Math.round(this.getResources().getDisplayMetrics().density);

        this.textView = new TextView(context);
        this.textView.setGravity(Gravity.CENTER);
        this.textView.setSingleLine(true);
        this.addView(this.textView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.textView.scrollTo(0, -this.getWidth());

        this.setHorizontalScrollBarEnabled(false);
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalFadingEdgeEnabled(true);
        this.setVerticalFadingEdgeEnabled(false);
        this.setFadingEdgeLength(10);

        if (attrs != null) {
            final TypedArray array = context.obtainStyledAttributes(attrs,  R.styleable.HorizontalMarqueeTextView, 0, 0);

            this.textView.setText(array.getText( R.styleable.HorizontalMarqueeTextView_text));

            final int textRes = array.getResourceId( R.styleable.HorizontalMarqueeTextView_text, 0);
            if (textRes > 0) {
                this.textView.setText(array.getText(textRes));
            }

            this.textView.setTextColor(array.getColor( R.styleable.HorizontalMarqueeTextView_textColor, context.getResources().getColor(android.R.color.white)));

            final int textColorRes = array.getResourceId( R.styleable.HorizontalMarqueeTextView_textColor, 0);
            if (textColorRes > 0) {
                this.textView.setTextColor(context.getResources().getColor(textColorRes));
            }

            final float textSize = array.getDimension( R.styleable.HorizontalMarqueeTextView_textSize, 0);
            if (textSize > 0) {
                this.textView.setTextSize(textSize);
            }

            final int textSizeRes = array.getResourceId( R.styleable.HorizontalMarqueeTextView_textSize, 0);
            if (textSizeRes > 0) {
                this.textView.setTextSize(context.getResources().getDimension(textSizeRes));
            }

            final int typeface = array.getInt( R.styleable.HorizontalMarqueeTextView_typeface, 0);
            this.textView.setTypeface(typeface == 1 ? Typeface.SANS_SERIF : typeface == 2 ? Typeface.SERIF : typeface == 3 ? Typeface.MONOSPACE : Typeface.DEFAULT, array.getInt( R.styleable.HorizontalMarqueeTextView_textStyle, Typeface.NORMAL));

            final int textAppearance = array.getResourceId( R.styleable.HorizontalMarqueeTextView_textAppearance, 0);
            if (textAppearance > 0) {
                this.textView.setTextAppearance(context, textAppearance);
            }

            this.setMarqueeSpeed(array.getInt( R.styleable.HorizontalMarqueeTextView_marqueeSpeed, 0));

            final int marqueeSpeedRes = array.getResourceId( R.styleable.HorizontalMarqueeTextView_marqueeSpeed, 0);
            if (marqueeSpeedRes > 0) {
                this.setMarqueeSpeed(context.getResources().getInteger(marqueeSpeedRes));
            }

            final boolean autoStartMarquee = array.getBoolean( R.styleable.HorizontalMarqueeTextView_autoStartMarquee, true);

            if (autoStartMarquee) {
                this.marqueeStarted = true;
            }

            array.recycle();
        }
    }

    private void animateTextView() {
        final Runnable runnable = new  HorizontalMarqueeTextView.MarqueeRunnable(this.textView);

        long previousMillis = 0;

        while ( HorizontalMarqueeTextView.this.marqueeStarted && ! HorizontalMarqueeTextView.this.marqueePaused) {
            final long currentMillis = System.currentTimeMillis();

            if (currentMillis >= previousMillis) {
                 HorizontalMarqueeTextView.this.handler.post(runnable);

                previousMillis = currentMillis + (long)( HorizontalMarqueeTextView.SECOND /  HorizontalMarqueeTextView.this.marqueeSpeed);
            }

            try {
                Thread.sleep((long)( HorizontalMarqueeTextView.SECOND /  HorizontalMarqueeTextView.this.marqueeSpeed));
            } catch (final InterruptedException e) {
                Log.v( HorizontalMarqueeTextView.TAG, e.getMessage(), e);
            }
        }

        this.isAnimating = false;
    }

    private final class MarqueeRunnable implements Runnable {
        private final ViewGroup parent;
        private final TextView  textView;

        /**
         * Creates a new instance of {@link  HorizontalMarqueeTextView.MarqueeRunnable}.
         * @param textView The {@link TextView} to apply marquee effect.
         */
        public MarqueeRunnable(final TextView textView) {
            this.parent   = (ViewGroup)textView.getParent();
            this.textView = textView;
        }

        @Override
        public void run() {
            final int width       = this.widthOf(this.textView);
            final int parentWidth = this.parent.getWidth();

            if (width > 0 && parentWidth > 0 && width > parentWidth) {
                if (this.textView.getScrollX() >= width) {
                    this.textView.scrollTo(0, -parentWidth);
                } else {
                    this.textView.scrollBy(0,  HorizontalMarqueeTextView.this.unitDisplacement);
                }

                this.textView.invalidate();
            }
        }

        /**
         * Returns the standard height (i.e. without text effects such as shadow) of all the text of
         * the given {@link TextView}.
         * @param textView The {@link TextView} to determine the height of all its text content.
         * @return The standard height of all the text content of the given {@link TextView}.
         */
        private int widthOf(final TextView textView) {
//            return textView.getLineCount() > 0 ? textView.getLineHeight() * textView.getLineCount() : 0;
            Rect bounds = new Rect();
            Paint textPaint = textView.getPaint();
            textPaint.getTextBounds(textView.getText().toString(),0,textView.getText().toString().length(),bounds);
            int width = bounds.width();
            return width;
        }
    }
}
