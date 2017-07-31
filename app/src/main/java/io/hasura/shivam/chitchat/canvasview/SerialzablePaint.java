package io.hasura.shivam.chitchat.canvasview;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;

import java.io.Serializable;

/**
 * Created by shivam on 30/7/17.
 */

//TODO default ARGB
    //StokeJoin
    //textsize
    //shadowlayer
    //

public class SerialzablePaint extends Paint implements Serializable{

    private Join join=getStrokeJoin();
    private float textSize=getTextSize();
    private int shadowColor;
    private float blur;
    boolean antiAlias=super.isAntiAlias();
    private Style style=super.getStyle();
    private int color=getColor();
    private int alpha=getAlpha();
    private float strokeWidth=getStrokeWidth();
    private float mitter=getStrokeMiter();
    private Cap strokeCap=getStrokeCap();
    private Align textAlign=getTextAlign();
    private Typeface typeFace=getTypeface();

  public   void initialize()
    {
        setStrokeJoin(join);
        setTextSize(textSize);
        setShadowLayer(blur,0f,0f,shadowColor);
        setAntiAlias(antiAlias);
        setStyle(style);
        setColor(color);
        setAlpha(alpha);
        setStrokeCap(strokeCap);
        setStrokeMiter(mitter);
        setTextAlign(textAlign);
    }


    @Override
    public void setShadowLayer(float radius, float dx, float dy, int shadowColor) {
        this.shadowColor=shadowColor;
        this.blur=radius;
        super.setShadowLayer(radius, dx, dy, shadowColor);
    }

    @Override
    public void setTextSize(float textSize) {
        this.textSize=textSize;
        super.setTextSize(textSize);
    }

    @Override
    public void setStrokeJoin(Join join) {
        this.join=join;
        super.setStrokeJoin(join);
    }


    @Override
    public void setAntiAlias(boolean aa) {
        antiAlias=aa;


        super.setAntiAlias(aa);
    }

    @Override
    public void setStyle(Style style) {
        this.style=style;
        super.setStyle(style);
    }

    @Override
    public void setColor(@ColorInt int color) {
        this.color=color;
        super.setColor(color);
    }

    @Override
    public void setAlpha(int a) {
        this.alpha=a;
        super.setAlpha(a);
    }

    @Override
    public void setStrokeWidth(float width) {
        this.strokeWidth=width;
        super.setStrokeWidth(width);
    }

    @Override
    public void setStrokeCap(Cap cap) {
        this.strokeCap=cap;
        super.setStrokeCap(cap);
    }

    @Override
    public Typeface setTypeface(Typeface typeface) {

        this.typeFace=typeface;

        return super.setTypeface(typeface);
    }

    @Override
    public void setTextAlign(Align align) {
        this.textAlign=align;
        super.setTextAlign(align);
    }
}
