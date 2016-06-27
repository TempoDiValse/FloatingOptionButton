package lavalse.kr.floatingoptionbutton;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * @author LaValse
 * @date 2016-06-22
 */
public class OptionObject {
    private Drawable drawable;
    private FloatingOptionButton.OnClickListener listener;

    public OptionObject(Drawable drawable, FloatingOptionButton.OnClickListener listener){
        this.drawable = drawable;
        this.listener = listener;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public FloatingOptionButton.OnClickListener geOnClickListener() {
        return listener;
    }
}
