package io.hasura.shivam.chitchat.frags;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


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

    ImageButton lineBtn,circleBtn,rectangleBtn,elipseBtn,curveBtn,textBtn,brushColorBtn,brushWidthBtn,
            enterTextBtn,saveBtn,shareBtn,forwardBtn;
    Button strokeBtn,fillBtn,fill_strokeBtn,
            bgImageBtn,bgOpacityBtn,bgBlurBtn,lineButtButton,lineRoundBtn,lineSquareBtn,
            fontFamilyBtn,fontSizeBtn;

   public OptionMenu()
    {
        super();
    }
   public OptionMenu(CanvasView canvasView)
    {
        super();

        this.canvasView=canvasView;

    }

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view = View.inflate(getContext(), R.layout.drawing_option, null);

        lineBtn=(ImageButton) view.findViewById(R.id.draw_line_btn);

        lineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setDrawer(CanvasView.Drawer.LINE);
            }
        });

        circleBtn=(ImageButton) view.findViewById(R.id.draw_circle_btn);
        circleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setDrawer(CanvasView.Drawer.CIRCLE);
            }
        });
        rectangleBtn=(ImageButton) view.findViewById(R.id.draw_rect_btn);
        rectangleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setDrawer(CanvasView.Drawer.RECTANGLE);
            }
        });
        elipseBtn=(ImageButton) view.findViewById(R.id.draw_oval_btn);
        elipseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setDrawer(CanvasView.Drawer.ELLIPSE);
            }
        });
        curveBtn=(ImageButton) view.findViewById(R.id.draw_curve_btn);
        curveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setDrawer(CanvasView.Drawer.QUADRATIC_BEZIER);
            }
        });
        textBtn=(ImageButton) view.findViewById(R.id.text_mode_btn);
        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setText("SAMPLE TEXT");
                canvasView.setMode(CanvasView.Mode.TEXT);
            }
        });
        strokeBtn=(Button) view.findViewById(R.id.brush_stroke_btn);
        strokeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setPaintStyle(Paint.Style.STROKE);
            }
        });
        fillBtn=(Button) view.findViewById(R.id.brush_fill_btn);
        fillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvasView.setPaintStyle(Paint.Style.FILL);
            }
        });
        fill_strokeBtn=(Button) view.findViewById(R.id.fill_n_str_btn);
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
//                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        })
                        .build()
                        .show();
            }
        });

        brushWidthBtn=(ImageButton) view.findViewById(R.id.brush_width_btn);
        bgImageBtn=(Button) view.findViewById(R.id.bg_img_btn);
        bgOpacityBtn=(Button) view.findViewById(R.id.bg_opicity_btn);
        bgBlurBtn=(Button) view.findViewById(R.id.bg_blur_btn);
        lineButtButton=(Button) view.findViewById(R.id.cap_butt_btn);
        lineRoundBtn=(Button) view.findViewById(R.id.round_cap_btn);
        lineSquareBtn=(Button) view.findViewById(R.id.sqre_cap_btn);
        fontFamilyBtn=(Button) view.findViewById(R.id.text_font_btn);
        fontSizeBtn=(Button) view.findViewById(R.id.font_sz_btn);
        // enterTextBtn=(ImageButton) view.findViewById(R.id.te);
        saveBtn=(ImageButton) view.findViewById(R.id.savebtn);
        shareBtn=(ImageButton) view.findViewById(R.id.share_btn);
        forwardBtn=(ImageButton) view.findViewById(R.id.forward_btn);

        bgImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });




        dialog.setContentView(view);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view= super.onCreateView(inflater, container, savedInstanceState);

        brushColorBtn.setColorFilter(canvasView.getBaseColor());

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

                bitmap=Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2,false);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;



                canvasView.drawBitmap(bitmap);


            }catch (IOException ioe)
            {
                Log.e("error image",ioe.toString());
            }


            //Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);


            // Do something with the bitmap


            // At the end remember to close the cursor or you will end with the RuntimeException!
           // cursor.close();
        }
    }
}
