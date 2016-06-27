package lavalse.kr.floatingoptionbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingOptionButton fob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fob = (FloatingOptionButton)findViewById(R.id.fob);
        FloatingOptionButton.OuterOptionView obj1 = fob.getOptionView(0);

        obj1.setOnClickListener(new FloatingOptionButton.OnClickListener() {
            @Override
            public void onClick(int id) {

            }
        });

        /*
        OptionObject obj1 = new OptionObject(getResources().getDrawable(R.drawable.option_view_forward), new FloatingOptionButton.OnClickListener() {
            @Override
            public void onClick(int id) {
                Toast.makeText(MainActivity.this, "Object"+id, Toast.LENGTH_SHORT).show();
            }
        });

        OptionObject obj2 = new OptionObject(getResources().getDrawable(R.drawable.option_view_bottom), new FloatingOptionButton.OnClickListener() {
            @Override
            public void onClick(int id) {
                Toast.makeText(MainActivity.this, "Object"+id, Toast.LENGTH_SHORT).show();
            }
        });

        OptionObject obj3 = new OptionObject(getResources().getDrawable(R.drawable.option_view_back),new FloatingOptionButton.OnClickListener() {
            @Override
            public void onClick(int id) {
                Toast.makeText(MainActivity.this, "Object"+id, Toast.LENGTH_SHORT).show();
            }
        });

        OptionObject obj4 = new OptionObject(getResources().getDrawable(R.drawable.option_view_top), new FloatingOptionButton.OnClickListener() {
            @Override
            public void onClick(int id) {
                Toast.makeText(MainActivity.this, "Object"+id, Toast.LENGTH_SHORT).show();
            }
        });

        ArrayList<OptionObject> objs = new ArrayList<OptionObject>();
        objs.add(obj1);
        objs.add(obj2);
        objs.add(obj3);
        objs.add(obj4);

        fob.setOptionButton(objs.toArray(new OptionObject[]{}));
        */
    }
}
