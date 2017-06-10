package io.hasura.shivam.chitchat.frags;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.canvasview.CanvasView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrawFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrawFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout toolbarBar;


    private CanvasView canvas = null;

    ImageButton undoBtn,redoBtn,clearBtn,penBtn,moreBtn,eraserBtn,saveBtn,colorPicker;

    private OnFragmentInteractionListener mListener;

    public DrawFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrawFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static DrawFrag newInstance(String param1, String param2) {
        DrawFrag fragment = new DrawFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_draw, container, false);

        this.canvas = (CanvasView)view.findViewById(R.id.canvas);

        undoBtn=(ImageButton)view.findViewById(R.id.undobtn);
        redoBtn=(ImageButton)view.findViewById(R.id.redobtn);
        clearBtn=(ImageButton)view.findViewById(R.id.clearbtn);
        eraserBtn=(ImageButton)view.findViewById(R.id.eraserBtn);
        moreBtn=(ImageButton)view.findViewById(R.id.morebtn);
        penBtn=(ImageButton)view.findViewById(R.id.penbtn);
        saveBtn=(ImageButton)view.findViewById(R.id.savebtn);
        colorPicker=(ImageButton)view.findViewById(R.id.color_btn_frt) ;

        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialogBuilder
                        .with(getContext())
                        .setTitle("Choose color")
                        .initialColor(canvas.getBaseColor())
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
                                canvas.setPaintStrokeColor(selectedColor);

                                colorPicker.setColorFilter(selectedColor);
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


        undoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.undo();
            }
        });
        redoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.redo();
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.clear();
            }
        });

        toolbarBar=(LinearLayout) view.findViewById(R.id.toolbar_fnt);

        toolbarBar.setOnTouchListener(new View.OnTouchListener() {

                                          float dX, dY;

                                          @Override
                                          public boolean onTouch(View view, MotionEvent event) {


                                              switch (event.getAction()) {

                                                  case MotionEvent.ACTION_DOWN:

                                                      dX = view.getX() - event.getRawX();
                                                      dY = view.getY() - event.getRawY();
                                                      break;

                                                  case MotionEvent.ACTION_MOVE:

                                                      view.animate()
                                                              .x(event.getRawX() + dX)
                                                              .y(event.getRawY() + dY)
                                                              .setDuration(0)
                                                              .start();
                                                      break;
                                                  default:
                                                      return false;
                                              }
                                              return true;
                                          }
        });

        eraserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

canvas.setMode(CanvasView.Mode.ERASER);
            }
        });

        penBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.setMode(CanvasView.Mode.DRAW);
                canvas.setDrawer(CanvasView.Drawer.PEN);
            }
        });

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());
//                View sheetView = getActivity().getLayoutInflater().inflate(R.layout.drawing_option, null);
//                mBottomSheetDialog.setContentView(sheetView);
//                mBottomSheetDialog.show();

                BottomSheetDialogFragment bottomSheetDialogFragment = new OptionMenu(canvas);
                bottomSheetDialogFragment.show(DrawFrag.this.getFragmentManager(), bottomSheetDialogFragment.getTag());

            }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
