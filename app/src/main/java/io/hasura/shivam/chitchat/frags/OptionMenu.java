package io.hasura.shivam.chitchat.frags;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.RadioButton;


import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.io.IOException;

import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.canvasview.CanvasView;

/**
 * Created by shivam on 10/6/17.
 */

public class OptionMenu extends BottomSheetDialogFragment {

    CanvasView canvasView;
   public  static final int SELECT_PHOTO=100;

    public static final byte BG_OPICITY=1,FONT_SIZE=2,BRUSH_WIDTH=3,TEXT_SIZE=4,BG_BLUR=5;

    static int SEEK_OPTION=3;

    ImageButton brushColorBtn,
           shareBtn,forwardBtn;
    Button  bgImageBtn,
            fontFamilyBtn;


    RadioButton brushWidthBtn,bgBlurBtn,bgOpacityBtn,penBtn,strokeBtn,fillBtn,fill_strokeBtn,lineButtButton,lineRoundBtn,lineSquareBtn,lineBtn,circleBtn,rectangleBtn,elipseBtn,curveBtn,textSizeBtn,font_size_btn;

    SeekBar seekBar;

   public OptionMenu()
   {

        super();
    }


    public void setCanvasView(CanvasView canvasView) {

        this.canvasView = canvasView;
    }



    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = View.inflate(getContext(), R.layout.drawing_option, null);

        seekBar=(SeekBar)view.findViewById(R.id.seekBar);

        penBtn=(RadioButton) view.findViewById(R.id.penbtn);

//        seekBar.setProgress((int)canvasView.getPaintStrokeWidth());

        lineBtn=(RadioButton) view.findViewById(R.id.draw_line_btn);

        lineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setDrawer(CanvasView.Drawer.LINE);
            }
        });

        penBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setDrawer(CanvasView.Drawer.PEN);
            }
        });

        circleBtn=(RadioButton) view.findViewById(R.id.draw_circle_btn);
        circleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setDrawer(CanvasView.Drawer.CIRCLE);
            }
        });
        rectangleBtn=(RadioButton) view.findViewById(R.id.draw_rect_btn);
        rectangleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setDrawer(CanvasView.Drawer.RECTANGLE);
            }
        });
        elipseBtn=(RadioButton) view.findViewById(R.id.draw_oval_btn);
        elipseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setDrawer(CanvasView.Drawer.ELLIPSE);
            }
        });
        curveBtn=(RadioButton) view.findViewById(R.id.draw_curve_btn);
        curveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setDrawer(CanvasView.Drawer.QUADRATIC_BEZIER);
            }
        });
        textSizeBtn=(RadioButton) view.findViewById(R.id.font_sz_btn);

        strokeBtn=(RadioButton) view.findViewById(R.id.brush_stroke_btn);
        strokeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setPaintStyle(Paint.Style.STROKE);
            }
        });
        fillBtn=(RadioButton) view.findViewById(R.id.brush_fill_btn);
        fillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setPaintStyle(Paint.Style.FILL);
            }
        });
        fill_strokeBtn=(RadioButton) view.findViewById(R.id.fill_n_str_btn);
        fill_strokeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setPaintStyle(Paint.Style.FILL_AND_STROKE);
            }
        });
        brushColorBtn=(ImageButton) view.findViewById(R.id.brush_color_btn);

        brushColorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(OptionMenu.this.getContext())
                        .setTitle("Choose color")
                        .initialColor(canvasView.getBaseColor())
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {

                               // toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                canvasView.setBaseColor(selectedColor);

                                brushColorBtn.setColorFilter(selectedColor);

                                canvasView.invalidate();
                               // changeBackgroundColor(selectedColor);
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .build()
                        .show();
            }
        });

        brushWidthBtn=(RadioButton) view.findViewById(R.id.brush_width_btn);
        bgOpacityBtn=(RadioButton) view.findViewById(R.id.bg_opicity_btn);
        bgBlurBtn=(RadioButton) view.findViewById(R.id.bg_blur_btn);
        lineButtButton=(RadioButton) view.findViewById(R.id.cap_butt_btn);

        lineButtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setLineCap(Paint.Cap.BUTT);
            }
        });
        lineRoundBtn=(RadioButton) view.findViewById(R.id.round_cap_btn);

        lineRoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setLineCap(Paint.Cap.ROUND);
            }
        });


        lineSquareBtn=(RadioButton) view.findViewById(R.id.sqre_cap_btn);

        lineSquareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setLineCap(Paint.Cap.SQUARE);
            }
        });

        fontFamilyBtn=(Button) view.findViewById(R.id.text_font_btn);
        font_size_btn=(RadioButton) view.findViewById(R.id.font_sz_btn);
        // enterTextBtn=(ImageButton) view.findViewById(R.id.te);
        shareBtn=(ImageButton) view.findViewById(R.id.share_btn);
        forwardBtn=(ImageButton) view.findViewById(R.id.forward_btn);


        bgImageBtn=(Button) view.findViewById(R.id.bg_img_btn);
        bgImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        brushWidthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SEEK_OPTION=BRUSH_WIDTH;
                optionMenuFill();

            //    seekBar.setThumb(Dr);
            }
        });

        bgOpacityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SEEK_OPTION=BG_OPICITY;
                optionMenuFill();
            }
        });

        bgBlurBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SEEK_OPTION=BG_BLUR;
                optionMenuFill();
            }
        });
        textSizeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SEEK_OPTION=TEXT_SIZE;
                optionMenuFill();

            }
        });





        dialog.setContentView(view);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                switch (SEEK_OPTION)
                {
                    case BG_OPICITY:

                        canvasView.setOpacity(progress);
                        break;

                    case BRUSH_WIDTH:

                        canvasView.setPaintStrokeWidth(progress);
                        break;

                    case BG_BLUR:
                        canvasView.setBlur( progress);
                        break;

                    case TEXT_SIZE:

                        canvasView.setFontSize(progress);
                        break;

                    default:
                }

                // canvasView.setPaintStrokeWidth(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        optionMenuFill();

    }






    void optionMenuFill()
    {
        // DRAW

        if(canvasView.getDrawer()== CanvasView.Drawer.CIRCLE)
        {
            circleBtn.toggle();

        } else  if(canvasView.getDrawer()== CanvasView.Drawer.RECTANGLE)
        {
            rectangleBtn.toggle();

        }else  if(canvasView.getDrawer()== CanvasView.Drawer.QUADRATIC_BEZIER)
        {
            curveBtn.toggle();

        }
        else   if(canvasView.getDrawer()== CanvasView.Drawer.PEN)
        {

            penBtn.toggle();
        }
        else   if(canvasView.getDrawer()== CanvasView.Drawer.ELLIPSE)
        {

            elipseBtn.toggle();

        } else  if(canvasView.getDrawer()== CanvasView.Drawer.LINE)
        {
            lineBtn.toggle();
        }

       if(canvasView.getPaintStyle()==Paint.Style.FILL)
       {
           fillBtn.toggle();
       }
       else  if(canvasView.getPaintStyle()==Paint.Style.FILL_AND_STROKE)
       {
           canvasView.setPaintFillColor(Color.RED);
           fill_strokeBtn.toggle();
       }
       else  if(canvasView.getPaintStyle()==Paint.Style.STROKE)
       {
           strokeBtn.toggle();
       }

       //LINE CAP
       if(canvasView.getLineCap()== Paint.Cap.BUTT)
       {
           lineButtButton.toggle();

       } else if(canvasView.getLineCap()== Paint.Cap.ROUND)
       {
           lineRoundBtn.toggle();

       }else if(canvasView.getLineCap()== Paint.Cap.SQUARE)
       {
            lineSquareBtn.toggle();
       }


       //SEEK OPTION
       switch (SEEK_OPTION)
       {
           case BG_OPICITY:

               bgOpacityBtn.toggle();
               seekBar.setProgress(canvasView.getOpacity());
               break;

           case BRUSH_WIDTH:

               brushWidthBtn.toggle();
               seekBar.setProgress((int) canvasView.getPaintStrokeWidth());
               break;

           case BG_BLUR:
               bgBlurBtn.toggle();
               seekBar.setProgress((int)canvasView.getBlur());
               break;

           case TEXT_SIZE:

               font_size_btn.toggle();
               seekBar.setProgress((int)canvasView.getFontSize());
               break;

           default:
       }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view= super.onCreateView(inflater, container, savedInstanceState);

//        brushColorBtn.setColorFilter(canvasView.getBaseColor());

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK && data != null) {

            Uri pickedImage = data.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), pickedImage);

                bitmap=Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),false);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;



                canvasView.drawBitmap(bitmap);


            }catch (IOException ioe)
            {
                Log.e("error getting image",ioe.toString());
            }

        }
    }
}
