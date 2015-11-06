package tencent.jusfoun.cn.bcdrawtest;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    private CheckBox checkBox;//一个改变了样式的Checkbox
    private LineDrawView lineDrawView;
    private BCOneLineView bcOneLineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        lineDrawView= (LineDrawView) findViewById(R.id.lineview);
        bcOneLineView= (BCOneLineView) findViewById(R.id.bcview);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox.setText("直线展开");
                    lineDrawView.setVisibility(View.VISIBLE);
                    bcOneLineView.setVisibility(View.GONE);
                } else {
                    checkBox.setText("曲线展开");
                    lineDrawView.setVisibility(View.GONE);
                    bcOneLineView.setVisibility(View.VISIBLE);
                }
            }
        });
        checkBox.setChecked(true);
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
