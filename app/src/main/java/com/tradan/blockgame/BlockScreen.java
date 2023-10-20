package com.tradan.blockgame;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BlockScreen extends View
{
    private ArrayList<Block> array=new ArrayList<Block>();
    private Paint fill=new Paint();
    private Paint border=new Paint();
    private Paint text=new Paint();
    private Ball ball;
    private Paddle paddle;
    private boolean lose=false;
    //private boolean
    private int score=0;
    private int walls=0;
    public BlockScreen(Context context, @Nullable AttributeSet attrs)
    {
        super(context,attrs);
        int width=500/5;
        int height=500/4;
        System.out.println(getMeasuredWidth());
        Bitmap b=BitmapFactory.decodeResource(getResources(),R.drawable.bird);
        ball=new Ball(325,475,375,525,5,5,b);
        paddle=new Paddle(350, 800, 450, 900,BitmapFactory.decodeResource(getResources(),R.drawable.cheese));
        /*for(int x=0;x<5;x++)
        {
            for(int y=0;y<4;y++)
            {
                Block b=new Block(x*width,y*height,(x+1)*width,(y+1)*height);
                array.add(b);
            }
        }*/
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //float x = motionEvent.getX();
                //paddle.left = x - paddle.halfWidth;
                // paddle.right = x + paddle.halfWidth;
                if(paddle.contains(motionEvent.getX(),motionEvent.getY()))
                {
                    lose=true;
                }
                else if(motionEvent.getAction()==MotionEvent.ACTION_DOWN) {
                    if (!lose) {
                        ball.dy = -25;

                    }
                    if (lose) {
                        //lose = false;
                        reset();
                    }

                }
                return true;
            }

        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!lose) {
            text.setColor(Color.BLACK);
            text.setStyle(Paint.Style.FILL);
            text.setTextSize(60);
            fill.setColor(Color.GREEN);
            fill.setStyle(Paint.Style.FILL);
            border.setColor(Color.BLACK);
            border.setStyle(Paint.Style.STROKE);
            border.setStrokeWidth(10);
            //checkCollision();
           /* for(int x=0;x<array.size();x++)
            {
                canvas.drawRect(array.get(x),fill);
                canvas.drawRect(array.get(x),border);
            }*/
            paddle.update(canvas);
            ball.update(canvas,walls);
            checkCollision(canvas);
            canvas.drawText(""+score,1000,80,text);
            if(walls>=2)
                lose=true;
        }
        else
        {
            text.setColor(Color.BLACK);
            text.setStyle(Paint.Style.FILL);
            text.setTextSize(60);
            canvas.drawText("You lose!",400,1000,text);
            canvas.drawText("Tap to Restart",350,1100,text);
            canvas.drawText("Score: "+score,400,1200,text);
        }
            invalidate();

    }
    public void reset()
    {
        ball=new Ball(325,475,375,525,5,5,BitmapFactory.decodeResource(getResources(),R.drawable.bird));
        paddle=new Paddle(350, 800, 450, 900,BitmapFactory.decodeResource(getResources(),R.drawable.cheese));
        lose=false;
        score=0;
        walls=0;
    }
    private void checkCollision(Canvas canvas)
    {
        /*ArrayList<Block> toRemove=new ArrayList<Block>();
        for(Block b:array)
        {
            //if(RectF.intersects(b,new Ball(ball.left+ball.dx,ball.top+ball.dy,ball.right+ball.dx,ball.bottom+ball.dy)))
            if(RectF.intersects(b,ball))
            {
                float centerX=(ball.left+ball.right)/2;
                float centerY=(ball.top+ball.bottom)/2;
                if(ball.top<=b.bottom||ball.bottom>=b.top) {
                    ball.dy = ball.dy * -1;
                }
                else if(ball.left<=b.right||ball.right>=b.left)
                {
                    ball.dx=ball.dx*-1;
                }
                //if(ball.left+dx)
                toRemove.add(b);
            }

        }
        for(Block b:toRemove)
        {
            array.remove(b);
        }*/
        int height=canvas.getHeight();
        int width=canvas.getWidth();
        if(RectF.intersects(ball,paddle))
        {
            float left=(float)Math.random()*(width-100);
            float top=(float)Math.random()*(height-100);
            paddle.left=left;
            paddle.top=top;
            paddle.right=left+100;
            paddle.bottom=top+100;
            score=score+1;
            walls=0;
        }
        if(ball.top<0||ball.bottom>canvas.getHeight())
        {
            lose=true;
        }

    }


}
class Block extends RectF
{
    public Block(float left, float top, float right, float bottom) { super(left,top,right,bottom);}

}
class Ball extends RectF
{
    public float dx=50;
    public float dy=50;
    private Paint p=new Paint();
    private Bitmap mBitmap;

    public Ball(float left, float top, float right, float bottom)
    {
        super(left,top,right,bottom);
    }
    public Ball(float left, float top, float right, float bottom, float dx, float dy,Bitmap b)
    {
        super(left,top,right,bottom);
        this.dx=dx;
        this.dy=dy;
        mBitmap= b;
    }
    public void update(Canvas canvas,int walls)
    {
        p.setColor(Color.BLUE);
        p.setStyle(Paint.Style.FILL);
        dy=dy+2;
        offset(dx,dy);
        if(left<0 || right >canvas.getWidth())
        {
            dx=dx*-1;
            left=left+dx;
            right=right+dx;
            walls=walls+1;
        }
        /*if(top<0||bottom>canvas.getHeight())
        {
            dy=dy*-1;
            top=top+dy;
            bottom=bottom+dy;
        }*/
        //canvas.drawRect(this,p);
        canvas.drawBitmap(mBitmap,null,this,null);
    }
}
class Paddle extends RectF
{
    public float halfWidth;
    private Paint p=new Paint();
    private Bitmap bitmap;
    public Paddle(float left, float top, float right, float bottom,Bitmap b)
    {
        super(left,top,right,bottom);
        halfWidth=(right-left)/2;
        bitmap=b;
    }
    public void update(Canvas canvas)
    {
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);
        //canvas.drawRect(this,p);
        canvas.drawBitmap(bitmap,null,this,null);
    }

}
