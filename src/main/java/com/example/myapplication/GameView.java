package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {

    private int viewWidth;
    private int viewHeight;
    private int points = 3;
    private Sprite playerBird;
    private Sprite po;
    private Sprite enemyBird;
    private final int timerInterval = 30;

    public GameView(Context context) {

        super(context);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.samm);

        int w = b.getWidth();
        int h = b.getHeight();

        Rect firstFrame = new Rect(0, 0, w, h);

        playerBird = new Sprite(10, 0, 0, 100, firstFrame, b);


        b = BitmapFactory.decodeResource(getResources(), R.drawable.ptc);
        w = b.getWidth()/5;
        h = b.getHeight()/3;

        firstFrame = new Rect(4*w, 0, 5*w, h);

        enemyBird = new Sprite(2000, 250, -300, 0, firstFrame, b);

        for (int i = 0; i < 3; i++) {
            for (int j = 4; j >= 0; j--) {
                if (i ==0 && j == 4) {
                    continue;
                }
                if (i ==2 && j == 0) {
                    continue;
                }
                enemyBird.addFrame(new Rect(j*w, i*h, j*w+w, i*w+w));
            }
        }

        b = BitmapFactory.decodeResource(getResources(), R.drawable.po);
        w = b.getWidth();
        h = b.getHeight();



       po = new Sprite(2000, 250, -300, 0, firstFrame, b);

        Timer t = new Timer();
        t.start();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        viewHeight = h;
    }

    protected void update() {
        playerBird.update(timerInterval);
        po.update(timerInterval);
        enemyBird.update(timerInterval);


        if (playerBird.getY() + playerBird.getFrameHeight() > viewHeight) {
            playerBird.setY(viewHeight - playerBird.getFrameHeight());
            playerBird.setVy(-playerBird.getVy());
            points--;
        }
        else if (playerBird.getY() < 0) {
            playerBird.setY(0);
            playerBird.setVy(-playerBird.getVy());
            points--;
        }

        if (enemyBird.getX() < - enemyBird.getFrameWidth()) {
            teleportOblako ();
            points += 5;
        }

        if (enemyBird.intersect(playerBird)) {
            teleportOblako ();
            points -= 20;
        }

        if (po.getX() < - po.getFrameWidth()) {
            teleportEnemy ();
            points += 5;
        }

        if (po.intersect(playerBird)) {
            teleportEnemy ();
            points -= 20;
        }


        invalidate();
    }



    class Timer extends CountDownTimer {
        public Timer() {
            super(Integer.MAX_VALUE, timerInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            update();
        }

        @Override
        public void onFinish() {
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        canvas.drawARGB(250, 127, 199, 255); // заливаем цветом
        playerBird.draw(canvas);
        enemyBird.draw(canvas);
        po.draw(canvas);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setTextSize(55.0f);
        p.setColor(Color.WHITE);
        canvas.drawText(points+"", viewWidth - 100, 70, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int eventAction = event.getAction();

        if (eventAction == MotionEvent.ACTION_DOWN)  {
            // Движение вверх
            if (event.getY() < playerBird.getBoundingBoxRect().top) {
                playerBird.setVy(-100);
                points--;
            }
            else if (event.getY() > (playerBird.getBoundingBoxRect().bottom)) {
                playerBird.setVy(100);
                points--;
            }
        }
        return true;
    }

    private void teleportEnemy () {
        enemyBird.setX(viewWidth + Math.random() * 500);
        enemyBird.setY(Math.random() * (viewHeight - enemyBird.getFrameHeight()));
    }

    private void teleportOblako () {
        po.setX(viewWidth + Math.random() * 400);
        po.setY(Math.random() * (viewHeight - po.getFrameHeight()));
    }

}