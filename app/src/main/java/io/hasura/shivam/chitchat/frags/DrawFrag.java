package io.hasura.shivam.chitchat.frags;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.graphics.CanvasView;

import io.hasura.shivam.chitchat.R;

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


    private CanvasView canvas = null;

    ImageButton undoBtn,redoBtn,clearBtn,penBtn,moreBtn,eraserBtn,saveBtn;

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

        eraserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
