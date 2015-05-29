package com.example.cristian;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import com.example.cristian.groovytest.R;


public class MainActivity extends ActionBarActivity {
    CamView camView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        camView = new CamView(this);
        setContentView(camView);
        Log.v("ACA", "Hola");
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

class CamView extends SurfaceView implements Runnable, SurfaceHolder.Callback {

    Thread thread;
    SurfaceHolder holder;
    Boolean running = false;

    CamView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        running = false;
        while (true)
        {
            try {
                thread.join();
                break;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void run() {
        running = true;

        while (running)
        {

            if (! holder.getSurface().isValid())
                continue;


            Canvas canvas = holder.lockCanvas();
            canvas.drawARGB(255, 56, 255, 70);
            Paint paint = new Paint();

            paint.setARGB(255,255,255,255);
            canvas.drawCircle(200, 200, 50, paint);
            holder.unlockCanvasAndPost(canvas);
        }
    }
}