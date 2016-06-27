package lavalse.kr.floatingoptionbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @author LaValse
 * @date 2016-06-21
 */
public class FloatingOptionButton extends View {
    private static final String TAG = "FOB";
    private static final int DEFAULT_PADDING = 10;

    // Please modify it if you want to size up/down
    private static final int DEFAULT_BOUNDS = 400;

    // Center Object Radius
    private static final int DEFAULT_RADIUS = 150;

    private Drawable innerDrawable;
    private int innerColor, outerColor;

    private Paint mPaint, cPaint;
    private RectF bound;
    private float startAngle = 0.0f;
    private float boundAngle = 0.0f;

    private RectF centerObj;
    private PointF center;

    private BoundsAnimation anim;
    private boolean isOn = false;

    private OuterOptionView[] optViews;
    private int optCount = 0;

    public FloatingOptionButton(Context context){
        super(context);

        Log.d(TAG, "Context");

        init();
    }

    public FloatingOptionButton(Context context, AttributeSet attrs){
        super(context, attrs);
        Log.d(TAG, "Context, AttributeSet");

        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.FloatingOptionButton);

        innerDrawable = attrArray.getDrawable(R.styleable.FloatingOptionButton_centerSrc);
        innerColor = attrArray.getColor(R.styleable.FloatingOptionButton_innerBackgroundColor, Color.BLACK);
        outerColor = attrArray.getColor(R.styleable.FloatingOptionButton_outerBackgroundColor, Color.WHITE);

        Drawable option1Src = attrArray.getDrawable(R.styleable.FloatingOptionButton_Option1Src);
        Drawable option2Src = attrArray.getDrawable(R.styleable.FloatingOptionButton_Option2Src);
        Drawable option3Src = attrArray.getDrawable(R.styleable.FloatingOptionButton_Option3Src);
        Drawable option4Src = attrArray.getDrawable(R.styleable.FloatingOptionButton_Option4Src);

        Drawable[] optionSrcs = new Drawable[]{ option1Src, option2Src, option3Src, option4Src };
        int size=0;
        for(Drawable src : optionSrcs){
            if(src == null) break;
            size++;
        }

        if(size != 0) {
            OptionObject[] objs = new OptionObject[size];

            for(int i=0; i<size; i++){
                objs[i] = new OptionObject(optionSrcs[i], null);
            }

            setOptionButton(objs);
        }

        init();
    }

    private void init(){
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        anim = new BoundsAnimation(this);
        anim.setDuration(1000);
        anim.setRepeatMode(Animation.REVERSE);

        setBackground(null);

        mPaint = new Paint();
        mPaint.setShadowLayer(10.0f, 0.0f, 2.0f, Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setColor(outerColor);

        cPaint = new Paint();
        cPaint.setAntiAlias(true);
        cPaint.setShadowLayer(10.0f, 10.0f, 5.0f, Color.argb(50,0,0,0));
        cPaint.setColor(innerColor);

        bound = new RectF();

        centerObj = new RectF();
        center = new PointF();
    }

    private void setBoundAngle(float angle){
        boundAngle = angle;
    }

    public int getChildCount(){
        return optCount;
    }

    public OuterOptionView getOptionView(int id){
        System.out.println(id+" Call");

        return (optViews != null && optViews.length != 0 && optViews.length > id) ? optViews[id] : null;
    }

    public void setOptionButton(OptionObject[] objs){
        if(objs.length > 4){
            Log.e(TAG, "Its size can't put over 4 items");
            return;
        }

        if(objs.length % 2 == 1){
            Log.e(TAG, "Its size can't be an odd");
            return;
        }

        optViews = new OuterOptionView[objs.length];
        optCount = objs.length;

        float degreeOfEach = 360 / optCount;
        int outerRadius = (int)(DEFAULT_RADIUS * 0.9);
        for(int i=0; i<optCount; i++){
            float degreePos = i*degreeOfEach;

            OptionObject obj = objs[i];

            OuterOptionView optView = new OuterOptionView(i, obj, degreePos, outerRadius);
            optViews[i] = optView;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = measureDimension(widthMeasureSpec);
        int measureHeight = measureDimension(heightMeasureSpec);

        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measureDimension(int measureSpec){
        int dimen = 0;

        switch(MeasureSpec.getMode(measureSpec)){
            case MeasureSpec.UNSPECIFIED:
                dimen = measureSpec;
                break;
            case MeasureSpec.AT_MOST:
                dimen = DEFAULT_BOUNDS;
                break;
            case MeasureSpec.EXACTLY:
                dimen = MeasureSpec.getSize(measureSpec);
                break;
        }

        return dimen;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = getMeasuredWidth();
        int height =getMeasuredHeight();

        int outerLeft = DEFAULT_PADDING;
        int outerTop = DEFAULT_PADDING;
        int outerRight = width - (DEFAULT_PADDING * 2);
        int outerBottom = height - (DEFAULT_PADDING * 2);

        bound.set(outerLeft, outerTop, outerRight, outerBottom);

        int bRad = (int)bound.width();
        int cLeft = outerLeft + ((bRad / 2) - (DEFAULT_RADIUS / 2));
        int cTop = outerTop + ((bRad / 2) - (DEFAULT_RADIUS / 2));
        int cBottom = cLeft + DEFAULT_RADIUS;
        int cRight = cTop + DEFAULT_RADIUS;
        centerObj.set(cLeft, cTop, cBottom, cRight);

        center.set(bound.centerX(), bound.centerY());

        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(bound, startAngle, boundAngle, true, mPaint);

        canvas.drawOval(centerObj, cPaint);

        if(innerDrawable != null){
            innerDrawable.setBounds((int)centerObj.left, (int)centerObj.top, (int)centerObj.right, (int)centerObj.bottom);
            innerDrawable.draw(canvas);
        }

        if(optViews != null && optViews.length != 0){

            for(OuterOptionView optView : optViews){
                int degree = (int)optView.getDegreePosition();
                optView.setPosition(center);

                if(degree < boundAngle) {
                    optView.draw(canvas);
                    optView.setHidden(false);
                }else{
                    optView.setHidden(true);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(centerObj.left < x && centerObj.right > x && centerObj.top < y && centerObj.bottom > y){
                    if(!isOn){
                        isOn = true;

                        anim.setIsOpenAnim(true);
                        anim.setStartAngle(startAngle);
                    }else{
                        isOn = false;

                        anim.setIsOpenAnim(false);
                        anim.setStartAngle(boundAngle);
                    }

                    startAnimation(anim);
                }else{
                    if(optViews != null && optViews.length != 0) {
                        for (OuterOptionView optView : optViews) {
                            optView.onTouch(event);
                        }
                    }else {
                        System.out.println("Not Center");
                    }
                }

                break;
        }


        return true;
    }

    private class BoundsAnimation extends Animation {
        private FloatingOptionButton btn;
        private boolean isOpenAnim = false;

        private float startAngle = 0.0f;

        public BoundsAnimation(FloatingOptionButton btn){
            this.btn = btn;
        }

        public void setIsOpenAnim(boolean isOpenAnim){
            this.isOpenAnim = isOpenAnim;
        }

        public void setStartAngle(float startAngle){
            this.startAngle = startAngle;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float endOfAngle = (isOpenAnim) ? 360.0f : 0.0f;
            float angle = startAngle + ((endOfAngle - startAngle) * interpolatedTime);

            btn.setBoundAngle(angle);
            btn.postInvalidate();
        }
    }

    protected class OuterOptionView{
        private static final String TAG = "OuterOptionView";
        private static final int DEFAULT_OPTION_VIEW_BOUNDS = 80;

        private OptionObject obj;

        private int id;
        private Drawable drawable;
        private OnClickListener listener;
        private float degreePosition;
        private float radianPosition;
        private int oLeft, oTop, oRight, oBottom;
        private Rect bounds;

        private int radius;

        private boolean isHidden = true;

        public OuterOptionView(int id, OptionObject obj, float degreePosition, int radius){
            this.id = id;
            this.obj = obj;
            this.drawable = obj.getDrawable();
            this.listener = obj.geOnClickListener();
            this.degreePosition = degreePosition;
            this.radianPosition = (float)(degreePosition * Math.PI / 180);
            this.radius = radius;
        }

        public void setPosition(PointF center){
            int x = (int)(center.x + (Math.cos(radianPosition) * radius));
            int y = (int)(center.y + (Math.sin(radianPosition) * radius));

            oLeft = x - (DEFAULT_OPTION_VIEW_BOUNDS / 2);
            oTop = y - (DEFAULT_OPTION_VIEW_BOUNDS / 2);
            oRight = x + (DEFAULT_OPTION_VIEW_BOUNDS / 2);
            oBottom = y + (DEFAULT_OPTION_VIEW_BOUNDS / 2);

            drawable.setBounds(oLeft, oTop, oRight, oBottom);
            bounds = drawable.getBounds();
        }

        private boolean isHidden(){
            return isHidden;
        }

        private void setHidden(boolean state){
            isHidden = state;
        }

        public void setOnClickListener(OnClickListener listener){
            this.listener = listener;
        }

        protected float getDegreePosition(){
            return degreePosition;
        }

        public void draw(Canvas canvas){
            drawable.draw(canvas);
        }

        public void onTouch(MotionEvent event){
            int x = (int)event.getX();
            int y = (int)event.getY();

            if(bounds.left < x && bounds.right > x && bounds.top < y && bounds.bottom > y){
                Log.d(TAG, "onClick ("+id+")");

                if(listener != null && !isHidden){
                    listener.onClick(id);
                }
            }
        }
    }

    public interface OnClickListener{
        public void onClick(int id);
    }
}