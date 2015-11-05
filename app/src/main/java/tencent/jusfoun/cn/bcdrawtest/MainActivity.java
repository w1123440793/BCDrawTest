package tencent.jusfoun.cn.bcdrawtest;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private BCMoreView bcMoreView;
    private LineDrawView lineDrawView;
    private RelativeLayout relativeLayout;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(new ImageScaleView(getApplicationContext()));
        setContentView(R.layout.activity_main);
        lineDrawView = (LineDrawView) findViewById(R.id.bc);
        relativeLayout = (RelativeLayout) findViewById(R.id.info);
        lineDrawView.setClickPot(new LineDrawView.OnClickPotListener() {
            @Override
            public void onClickPot() {
//                relativeLayout.setVisibility(View.VISIBLE);
            }
        });

        checkBox = (CheckBox) findViewById(R.id.checkbox);
//        BCView bcView=new BCView(this);
//        BCTwoView bcTwoView=new BCTwoView(this);
//        BCMoreView bcMoreView=new BCMoreView(this);
//        setContentView(bcTwoView);
//        setContentView(bcMoreView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
