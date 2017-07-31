package io.hasura.shivam.chitchat.canvasview;

import android.graphics.Path;
import android.graphics.RectF;

import java.io.Serializable;

/**
 * Created by shivam on 30/7/17.
 */

//moveTo
    //lineTo
    //reset
    //addRect l r b u dir
    //addCircle x y r dir
    //addOval RectF dir
    //quadTo a b x y
    //close

public class SerialzablePath extends Path  implements Serializable{
    
    
    // for circle

    float cirx,ciry,cirr;
    Direction cirDir;
    private float movX;
    private float movY;
    private float lineY;
    private float linex;
    private float quadX1;
    private float quadY1;
    private RectF RectFOval;
    private Direction ovalDir;
    private float quadX2;
    private float quadY2;
    private float leftRect;
    private float rightRect;
    private float topRect;
    private float bottomRect;
    private Direction dirRect;

   public void initialize()
    {
        addCircle(cirx,ciry,cirr,cirDir);
        moveTo(movX,movY);
        lineTo(linex,lineY);
        quadTo(quadX1,quadY1,quadX2,quadY2);
        addRect(leftRect,topRect,rightRect,bottomRect,dirRect);

        super.close();
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public void addCircle(float x, float y, float radius, Direction dir) {
        
       cirx=x;
        cirDir=dir;
        cirr=radius;
        ciry=y;


        
        super.addCircle(x, y, radius, dir);
    }

    @Override
    public void moveTo(float x, float y) {
        this.movX=x;
        this.movY=y;
        
        super.moveTo(x, y);
    }

    @Override
    public void lineTo(float x, float y) {
        
        this.linex=x;
        this.lineY=y;
        
        super.lineTo(x, y);
    }

    @Override
    public void quadTo(float x1, float y1, float x2, float y2) {
        
        this.quadX1=x1;
        this.quadX2=x2;
        this.quadY1=y1;
        this.quadY2=y2;
        super.quadTo(x1, y1, x2, y2);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void addRect(float left, float top, float right, float bottom, Direction dir) {
        this.leftRect=left;
        this.rightRect=right;
        this.topRect=top;
        this.bottomRect=bottom;
        this.dirRect=dir;
        super.addRect(left, top, right, bottom, dir);
    }

    @Override
    public void addOval(RectF oval, Direction dir) {
        this.RectFOval=oval;
        this.ovalDir=dir;
        super.addOval(oval, dir);
    }
}
