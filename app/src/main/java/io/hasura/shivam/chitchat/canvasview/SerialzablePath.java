package io.hasura.shivam.chitchat.canvasview;

import android.graphics.Path;
import android.graphics.RectF;

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

public class SerialzablePath extends Path  {
    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public void addCircle(float x, float y, float radius, Direction dir) {
        super.addCircle(x, y, radius, dir);
    }

    @Override
    public void moveTo(float x, float y) {
        super.moveTo(x, y);
    }

    @Override
    public void lineTo(float x, float y) {
        super.lineTo(x, y);
    }

    @Override
    public void quadTo(float x1, float y1, float x2, float y2) {
        super.quadTo(x1, y1, x2, y2);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void addRect(float left, float top, float right, float bottom, Direction dir) {
        super.addRect(left, top, right, bottom, dir);
    }

    @Override
    public void addOval(RectF oval, Direction dir) {
        super.addOval(oval, dir);
    }
}
