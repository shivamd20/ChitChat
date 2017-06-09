package io.hasura.shivam.chitchat.frags;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.canvasview.CanvasView;

/**
 * Created by shivam on 10/6/17.
 */

public class OptionMenu extends BottomSheetDialogFragment {

    CanvasView canvasView;

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

        dialog.setContentView(view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  view= super.onCreateView(inflater, container, savedInstanceState);


        return view;

    }
}
