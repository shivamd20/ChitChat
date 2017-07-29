package io.hasura.shivam.chitchat.canvasview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class defines fields and methods for drawing.
 */
public class CanvasView extends View {
    // Enumeration for Mode
    public   enum Mode {
        DRAW,
        TEXT,
        ERASER
    }

    // Enumeration for Drawer
    public enum Drawer {
        PEN,
        LINE,
        RECTANGLE,
        CIRCLE,
        ELLIPSE,
        QUADRATIC_BEZIER,
        QUBIC_BEZIER;
    }

    private Canvas canvas   = null;
    private Bitmap bitmap   = null;

    private List<SerialzablePath>  pathLists  = new ArrayList<>();
    private List<SerialzablePaint> paintLists = new ArrayList<>();

    private final SerialzablePaint emptyPaint = new SerialzablePaint();

    // for Eraser
    private int baseColor = Color.WHITE;

    // for Undo, Redo
    private int historyPointer = 0;

    // Flags
    private Mode mode      = Mode.DRAW;
    private Drawer drawer  = Drawer.PEN;
    private boolean isDown = false;

    // for SerialzablePaint
    private SerialzablePaint.Style paintStyle    = SerialzablePaint.Style.STROKE;
    private int paintStrokeColor      = Color.BLACK;
    private int paintFillColor        = Color.BLACK;
    private float paintStrokeWidth    = 3F;
    private int opacity               = 255;
    private float blur                = 0F;
    private SerialzablePaint.Cap lineCap         = SerialzablePaint.Cap.ROUND;
   // private PathEffect drawPathEffect = null;

    // for Text
    private String text           = "";
    private Typeface fontFamily   = Typeface.DEFAULT;
    private float fontSize        = 32F;
    private SerialzablePaint.Align textAlign = SerialzablePaint.Align.RIGHT;  // fixed
    private SerialzablePaint textPaint       = new SerialzablePaint();
    private float textX           = 0F;
    private float textY           = 0F;

    // for Drawer
    private float startX   = 0F;
    private float startY   = 0F;
    private float controlX = 0F;
    private float controlY = 0F;

    /**
     * Copy Constructor
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CanvasView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setup();
    }

    /**
     * Copy Constructor
     *
     * @param context
     * @param attrs
     */
    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setup();
    }

    /**
     * Copy Constructor
     *
     * @param context
     */
    public CanvasView(Context context) {
        super(context);
        this.setup();
    }

    /**
     * Common initialization.
     *
     */
    private void setup() {

        this.pathLists.add(new SerialzablePath());
        this.paintLists.add(this.createPaint());
        this.historyPointer++;

        this.textPaint.setARGB(0, 255, 255, 255);
    }

    /**
     * This method creates the instance of SerialzablePaint.
     * In addition, this method sets styles for SerialzablePaint.
     *
     * @return SerialzablePaint This is returned as the instance of SerialzablePaint
     */
    private SerialzablePaint createPaint() {
        SerialzablePaint SerialzablePaint = new SerialzablePaint();

        SerialzablePaint.setAntiAlias(true);
        SerialzablePaint.setStyle(this.paintStyle);
        SerialzablePaint.setStrokeWidth(this.paintStrokeWidth);
        SerialzablePaint.setStrokeCap(this.lineCap);
        SerialzablePaint.setStrokeJoin(Paint.Join.MITER);  // fixed

        // for Text
        if (this.mode == Mode.TEXT) {
            SerialzablePaint.setTypeface(this.fontFamily);
            SerialzablePaint.setTextSize(this.fontSize);
            SerialzablePaint.setTextAlign(this.textAlign);
            SerialzablePaint.setStrokeWidth(0F);
        }

        if (this.mode == Mode.ERASER) {
            // Eraser
//            SerialzablePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//            SerialzablePaint.setARGB(0, 0, 0, 0);

            SerialzablePaint.setColor(this.baseColor);
            SerialzablePaint.setShadowLayer(this.blur, 0F, 0F, this.baseColor);
        } else {
            // Otherwise
            SerialzablePaint.setColor(this.paintStrokeColor);
            SerialzablePaint.setShadowLayer(this.blur, 0F, 0F, this.paintStrokeColor);
            SerialzablePaint.setAlpha(this.opacity);
           // SerialzablePaint.setPathEffect(this.drawPathEffect);
        }

        return SerialzablePaint;
    }

    /**
     * This method initialize SerialzablePath.
     * Namely, this method creates the instance of SerialzablePath,
     * and moves current position.
     *
     * @param event This is argument of onTouchEvent method
     * @return SerialzablePath This is returned as the instance of SerialzablePath
     */
    private SerialzablePath createPath(MotionEvent event) {
        SerialzablePath SerialzablePath = new SerialzablePath();

        // Save for ACTION_MOVE
        this.startX = event.getX();
        this.startY = event.getY();

        SerialzablePath.moveTo(this.startX, this.startY);

        return SerialzablePath;
    }

    /**
     * This method updates the lists for the instance of SerialzablePath and SerialzablePaint.
     * "Undo" and "Redo" are enabled by this method.
     *
     * @param SerialzablePath the instance of SerialzablePath
     */
    private void updateHistory(SerialzablePath SerialzablePath) {
        if (this.historyPointer == this.pathLists.size()) {
            this.pathLists.add(SerialzablePath);
            this.paintLists.add(this.createPaint());
            this.historyPointer++;
        } else {
            // On the way of Undo or Redo
            this.pathLists.set(this.historyPointer, SerialzablePath);
            this.paintLists.set(this.historyPointer, this.createPaint());
            this.historyPointer++;

            for (int i = this.historyPointer, size = this.paintLists.size(); i < size; i++) {
                this.pathLists.remove(this.historyPointer);
                this.paintLists.remove(this.historyPointer);
            }
        }
    }

    /**
     * This method gets the instance of SerialzablePath that pointer indicates.
     *
     * @return the instance of SerialzablePath
     */
    private SerialzablePath getCurrentPath() {
        return this.pathLists.get(this.historyPointer - 1);
    }

    /**
     * This method draws text.
     *
     * @param canvas the instance of Canvas
     */
    private void drawText(Canvas canvas) {
        if (this.text.length() <= 0) {
            return;
        }

        if (this.mode == Mode.TEXT) {
            this.textX = this.startX;
            this.textY = this.startY;

            this.textPaint = this.createPaint();
        }

        float textX = this.textX;
        float textY = this.textY;

        SerialzablePaint paintForMeasureText = new SerialzablePaint();

        // Line break automatically
        float textLength   = paintForMeasureText.measureText(this.text);
        float lengthOfChar = textLength / (float)this.text.length();
        float restWidth    = this.canvas.getWidth() - textX;  // text-align : right
        int numChars       = (lengthOfChar <= 0) ? 1 : (int)Math.floor((double)(restWidth / lengthOfChar));  // The number of characters at 1 line
        int modNumChars    = (numChars < 1) ? 1 : numChars;
        float y            = textY;

        for (int i = 0, len = this.text.length(); i < len; i += modNumChars) {
            String substring = "";

            if ((i + modNumChars) < len) {
                substring = this.text.substring(i, (i + modNumChars));
            } else {
                substring = this.text.substring(i, len);
            }

            y += this.fontSize;

            canvas.drawText(substring, textX, y, this.textPaint);
        }
    }

    /**
     * This method defines processes on MotionEvent.ACTION_DOWN
     *
     * @param event This is argument of onTouchEvent method
     */
    private void onActionDown(MotionEvent event) {
        switch (this.mode) {
            case DRAW   :
            case ERASER :
                if ((this.drawer != Drawer.QUADRATIC_BEZIER) && (this.drawer != Drawer.QUBIC_BEZIER)) {
                    // Oherwise
                    this.updateHistory(this.createPath(event));
                    this.isDown = true;
                } else {
                    // Bezier
                    if ((this.startX == 0F) && (this.startY == 0F)) {
                        // The 1st tap
                        this.updateHistory(this.createPath(event));
                    } else {
                        // The 2nd tap
                        this.controlX = event.getX();
                        this.controlY = event.getY();

                        this.isDown = true;
                    }
                }

                break;
            case TEXT   :
                this.startX = event.getX();
                this.startY = event.getY();

                break;
            default :
                break;
        }
    }

    /**
     * This method defines processes on MotionEvent.ACTION_MOVE
     *
     * @param event This is argument of onTouchEvent method
     */

    private void onActionMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (this.mode) {
            case DRAW   :
            case ERASER :

                if ((this.drawer != Drawer.QUADRATIC_BEZIER) && (this.drawer != Drawer.QUBIC_BEZIER)) {
                    if (!isDown) {
                        return;
                    }

                    SerialzablePath SerialzablePath = this.getCurrentPath();

                    switch (this.drawer) {
                        case PEN :
                            SerialzablePath.lineTo(x, y);
                            break;
                        case LINE :
                            SerialzablePath.reset();
                            SerialzablePath.moveTo(this.startX, this.startY);
                            SerialzablePath.lineTo(x, y);
                            break;
                        case RECTANGLE :
                            SerialzablePath.reset();

                            float left   = Math.min(this.startX, x);
                            float right  = Math.max(this.startX, x);
                            float top    = Math.min(this.startY, y);
                            float bottom = Math.max(this.startY, y);

                            SerialzablePath.addRect(left, top, right, bottom, Path.Direction.CCW);
                            break;
                        case CIRCLE :
                            double distanceX = Math.abs((double)(this.startX - x));
                            double distanceY = Math.abs((double)(this.startX - y));
                            double radius    = Math.sqrt(Math.pow(distanceX, 2.0) + Math.pow(distanceY, 2.0));

                            SerialzablePath.reset();
                            SerialzablePath.addCircle(this.startX, this.startY, (float)radius-50, Path.Direction.CCW);
                            break;
                        case ELLIPSE :
                            RectF rect = new RectF(this.startX, this.startY, x, y);

                            SerialzablePath.reset();
                            SerialzablePath.addOval(rect, Path.Direction.CCW);
                            break;
                        default :
                            break;
                    }
                } else {
                    if (!isDown) {
                        return;
                    }

                    SerialzablePath SerialzablePath = this.getCurrentPath();

                    SerialzablePath.reset();
                    SerialzablePath.moveTo(this.startX, this.startY);
                    SerialzablePath.quadTo(this.controlX, this.controlY, x, y);
                }

                break;
            case TEXT :
                this.startX = x;
                this.startY = y;

                break;
            default :
                break;
        }
    }

    /**
     * This method defines processes on MotionEvent.ACTION_DOWN
     *
     * @param event This is argument of onTouchEvent method
     */
    private void onActionUp(MotionEvent event) {
        if (isDown) {
            this.startX = 0F;
            this.startY = 0F;
            this.isDown = false;
        }
    }

    /**
     * This method updates the instance of Canvas (View)
     *
     * @param canvas the new instance of Canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Before "drawPath"
        canvas.drawColor(this.baseColor);

        if (this.bitmap != null) {
            canvas.drawBitmap(this.bitmap, 0F, 0F, emptyPaint);
        }


        for (int i = 0; i < this.historyPointer; i++) {
            SerialzablePath SerialzablePath   = this.pathLists.get(i);
            SerialzablePaint SerialzablePaint = this.paintLists.get(i);

            canvas.drawPath(SerialzablePath, SerialzablePaint);
        }

        this.drawText(canvas);

        this.canvas = canvas;
    }

    /**
     * This method set event listener for drawing.
     *
     * @param event the instance of MotionEvent
     * @return
     */

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.onActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE :
                this.onActionMove(event);
                break;
            case MotionEvent.ACTION_UP :
                this.onActionUp(event);
                break;
            default :
                break;
        }

        // Re draw
        this.invalidate();

        return true;
    }

    /**
     * This method is getter for mode.
     *
     * @return
     */
    public Mode getMode() {
        return this.mode;
    }

    /**
     * This method is setter for mode.
     *
     * @param mode
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * This method is getter for drawer.
     *
     * @return
     */
    public Drawer getDrawer() {
        return this.drawer;
    }

    /**
     * This method is setter for drawer.
     *
     * @param drawer
     */
    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    /**
     * This method checks if Undo is available
     *
     * @return If Undo is available, this is returned as true. Otherwise, this is returned as false.
     */
    public boolean canUndo() {
        return this.historyPointer > 1;
    }

    /**
     * This method checks if Redo is available
     *
     * @return If Redo is available, this is returned as true. Otherwise, this is returned as false.
     */
    public boolean canRedo() {
        return this.historyPointer < this.pathLists.size();
    }

    /**
     * This method draws canvas again for Undo.
     *
     * @return If Undo is enabled, this is returned as true. Otherwise, this is returned as false.
     */
    public boolean undo() {
        if (canUndo()) {
            this.historyPointer--;
            this.invalidate();

            return true;
        } else {
            return false;
        }
    }

    /**
     * This method draws canvas again for Redo.
     *
     * @return If Redo is enabled, this is returned as true. Otherwise, this is returned as false.
     */
    public boolean redo() {
        if (canRedo()) {
            this.historyPointer++;
            this.invalidate();

            return true;
        } else {
            return false;
        }
    }

    /**
     * This method initializes canvas.
     *
     * @return
     */

    public   void undoToStart()
    {
        while (canUndo())
            undo();
    }


    public void clear() {
        SerialzablePath SerialzablePath = new SerialzablePath();
        SerialzablePath.moveTo(0F, 0F);
        SerialzablePath.addRect(0F, 0F, super.getWidth(), super.getHeight(), Path.Direction.CCW);
        SerialzablePath.close();


        SerialzablePaint SerialzablePaint = new SerialzablePaint();
        SerialzablePaint.setColor(Color.WHITE);
        SerialzablePaint.setStyle(Paint.Style.FILL);



        if (this.historyPointer == this.pathLists.size()) {
            this.pathLists.add(SerialzablePath);
            this.paintLists.add(SerialzablePaint);
            this.historyPointer++;
        } else {
            // On the way of Undo or Redo
            this.pathLists.set(this.historyPointer, SerialzablePath);
            this.paintLists.set(this.historyPointer, SerialzablePaint);
            this.historyPointer++;

            for (int i = this.historyPointer, size = this.paintLists.size(); i < size; i++) {
                this.pathLists.remove(this.historyPointer);
                this.paintLists.remove(this.historyPointer);
            }
        }

        this.text = "";

        // Clear
        this.invalidate();
    }

    /**
     * This method is getter for canvas background color
     *
     * @return
     */
    public int getBaseColor() {
        return this.baseColor;
    }

    /**
     * This method is setter for canvas background color
     *
     * @param color
     */
    public void setBaseColor(int color) {
        this.baseColor = color;
    }

    /**
     * This method is getter for drawn text.
     *
     * @return
     */
    public String getText() {
        return this.text;
    }

    /**
     * This method is setter for drawn text.
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * This method is getter for stroke or fill.
     *
     * @return
     */
    public SerialzablePaint.Style getPaintStyle() {
        return this.paintStyle;
    }

    /**
     * This method is setter for stroke or fill.
     *
     * @param style
     */
    public void setPaintStyle(SerialzablePaint.Style style) {
        this.paintStyle = style;
    }

    /**
     * This method is getter for stroke color.
     *
     * @return
     */
    public int getPaintStrokeColor() {
        return this.paintStrokeColor;
    }

    /**
     * This method is setter for stroke color.
     *
     * @param color
     */
    public void setPaintStrokeColor(int color) {
        this.paintStrokeColor = color;
    }

    /**
     * This method is getter for fill color.
     * But, current Android API cannot set fill color (?).
     *
     * @return
     */
    public int getPaintFillColor() {
        return this.paintFillColor;
    };

    /**
     * This method is setter for fill color.
     * But, current Android API cannot set fill color (?).
     *
     * @param color
     */
    public void setPaintFillColor(int color) {
        this.paintFillColor = color;
    }

    /**
     * This method is getter for stroke width.
     *
     * @return
     */
    public float getPaintStrokeWidth() {
        return this.paintStrokeWidth;
    }

    /**
     * This method is setter for stroke width.
     *
     * @param width
     */
    public void setPaintStrokeWidth(float width) {
        if (width >= 0) {
            this.paintStrokeWidth = width;
        } else {
            this.paintStrokeWidth = 3F;
        }
    }

    /**
     * This method is getter for alpha.
     *
     * @return
     */
    public int getOpacity() {
        return this.opacity;
    }

    /**
     * This method is setter for alpha.
     * The 1st argument must be between 0 and 255.
     *
     * @param opacity
     */
    public void setOpacity(int opacity) {
        if ((opacity >= 0) && (opacity <= 255)) {
            this.opacity = opacity;
        } else {
            this.opacity= 255;
        }
    }

    /**
     * This method is getter for amount of blur.
     *
     * @return
     */
    public float getBlur() {
        return this.blur;
    }

    /**
     * This method is setter for amount of blur.
     * The 1st argument is greater than or equal to 0.0.
     *
     * @param blur
     */
    public void setBlur(float blur) {
        if (blur >= 0) {
            this.blur = blur;
        } else {
            this.blur = 0F;
        }
    }

    /**
     * This method is getter for line cap.
     *
     * @return
     */
    public SerialzablePaint.Cap getLineCap() {
        return this.lineCap;
    }

    /**
     * This method is setter for line cap.
     *
     * @param cap
     */
    public void setLineCap(SerialzablePaint.Cap cap) {
        this.lineCap = cap;
    }

    /**
     * This method is getter for SerialzablePath effect of drawing.
     *
     * @return drawPathEffect
//     */
//    public PathEffect getDrawPathEffect() {
//        return drawPathEffect;
//    }
//
//    /**
//     * This method is setter for SerialzablePath effect of drawing.
//     *
//     * @param drawPathEffect
//     */
//    public void setDrawPathEffect(PathEffect drawPathEffect) {
//        this.drawPathEffect = drawPathEffect;
//    }
    /**
     * This method is getter for font size,
     *
     * @return
     */
    public float getFontSize() {
        return this.fontSize;
    }

    /**
     * This method is setter for font size.
     * The 1st argument is greater than or equal to 0.0.
     *
     * @param size
     */
    public void setFontSize(float size) {
        if (size >= 0F) {
            this.fontSize = size;
        } else {
            this.fontSize = 32F;
        }
    }

    /**
     * This method is getter for font-family.
     *
     * @return
     */
    public Typeface getFontFamily() {
        return this.fontFamily;
    }

    /**
     * This method is setter for font-family.
     *
     * @param face
     */
    public void setFontFamily(Typeface face) {
        this.fontFamily = face;
    }

    /**
     * This method gets current canvas as bitmap.
     *
     * @return This is returned as bitmap.
     */
    public Bitmap getBitmap() {
        this.setDrawingCacheEnabled(false);
        this.setDrawingCacheEnabled(true);

        return Bitmap.createBitmap(this.getDrawingCache());
    }

    /**
     * This method gets current canvas as scaled bitmap.
     *
     * @return This is returned as scaled bitmap.
     */
    public Bitmap getScaleBitmap(int w, int h) {
        this.setDrawingCacheEnabled(false);
        this.setDrawingCacheEnabled(true);

        return Bitmap.createScaledBitmap(this.getDrawingCache(), w, h, true);
    }

    /**
     * This method draws the designated bitmap to canvas.
     *
     * @param bitmap
     */
    public void drawBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.invalidate();
    }

    /**
     * This method draws the designated byte array of bitmap to canvas.
     *
     * @param byteArray This is returned as byte array of bitmap.
     */
    public void drawBitmap(byte[] byteArray) {
        this.drawBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
    }

    /**
     * This static method gets the designated bitmap as byte array.
     *
     * @param bitmap
     * @param format
     * @param quality
     * @return This is returned as byte array of bitmap.
     */
    public static byte[] getBitmapAsByteArray(Bitmap bitmap, CompressFormat format, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(format, quality, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * This method gets the bitmap as byte array.
     *
     * @param format
     * @param quality
     * @return This is returned as byte array of bitmap.
     */
    public byte[] getBitmapAsByteArray(CompressFormat format, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        this.getBitmap().compress(format, quality, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * This method gets the bitmap as byte array.
     * Bitmap format is PNG, and quality is 100.
     *
     * @return This is returned as byte array of bitmap.
     */
    public byte[] getBitmapAsByteArray() {
        return this.getBitmapAsByteArray(CompressFormat.PNG, 100);
    }

   /* public void paintBucket( Point pt,  int replacementColor)
    {
        Bitmap bmp=this.getBitmap();

        int targetColor=bmp.getPixel(pt.x,pt.y);

        FloodFill(bmp,pt,targetColor,replacementColor);

        SerialzablePath p=new SerialzablePath();

    }*/

/*    private Queue<Point> FloodFill(Bitmap bmp, Point pt, int targetColor, int replacementColor){
        Queue<Point> q = new LinkedList<>();
        q.add(pt);
        while (q.size() > 0) {
            Point n = q.poll();
            if (bmp.getPixel(n.x, n.y) != targetColor)
                continue;

            Point w = n, e = new Point(n.x + 1, n.y);
            while ((w.x > 0) && (bmp.getPixel(w.x, w.y) == targetColor)) {
                bmp.setPixel(w.x, w.y, replacementColor);
                if ((w.y > 0) && (bmp.getPixel(w.x, w.y - 1) == targetColor))
                    q.add(new Point(w.x, w.y - 1));
                if ((w.y < bmp.getHeight() - 1)
                        && (bmp.getPixel(w.x, w.y + 1) == targetColor))
                    q.add(new Point(w.x, w.y + 1));
                w.x--;
            }
            while ((e.x < bmp.getWidth() - 1)
                    && (bmp.getPixel(e.x, e.y) == targetColor)) {
                bmp.setPixel(e.x, e.y, replacementColor);

                if ((e.y > 0) && (bmp.getPixel(e.x, e.y - 1) == targetColor))
                    q.add(new Point(e.x, e.y - 1));
                if ((e.y < bmp.getHeight() - 1)
                        && (bmp.getPixel(e.x, e.y + 1) == targetColor))
                    q.add(new Point(e.x, e.y + 1));
                e.x++;
            }
        }

        return  q;
    }*/
}