package io.hasura.shivam.chitchat.frags;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.hasura.shivam.chitchat.R;
import io.hasura.shivam.chitchat.activities.ChatActivity;
import io.hasura.shivam.chitchat.canvasview.CanvasView;
import io.hasura.shivam.chitchat.canvasview.SerialzablePaint;
import io.hasura.shivam.chitchat.canvasview.SerialzablePath;
import io.hasura.shivam.chitchat.database.Conversation;
import io.hasura.shivam.chitchat.database.Person;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrawFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrawFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrawFrag extends Fragment implements CanvasView.OnDrawingChangeListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public long me,with;

    LinearLayout toolbarBar;

    private CanvasView canvas = null;

    ImageButton undoBtn,redoBtn,clearBtn,moreBtn,saveBtn,colorPicker;
    RadioButton drawBtn,textBtn;

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

        with= ((ChatActivity)this.getActivity()).with;
        me=((ChatActivity)this.getActivity()).me;
    }

    //int toolbarTopMargin=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_draw, container, false);

        this.canvas = (CanvasView)view.findViewById(R.id.canvas);

        this.canvas.setOnDrawingChangeListener(this);

        this.canvas.setup();

      //  this.canvas.setOnDrawingChangeListener(this);

        undoBtn=(ImageButton)view.findViewById(R.id.undobtn);
        redoBtn=(ImageButton)view.findViewById(R.id.redobtn);
        clearBtn=(ImageButton)view.findViewById(R.id.clearbtn);
        //eraserBtn=(RadioButton)view.findViewById(R.id.eraserBtn);
        moreBtn=(ImageButton)view.findViewById(R.id.morebtn);
        textBtn=(RadioButton)view.findViewById(R.id.textMode);
        drawBtn=(RadioButton)view.findViewById(R.id.drawBtn);
        saveBtn=(ImageButton)view.findViewById(R.id.savebtn);
        colorPicker=(ImageButton)view.findViewById(R.id.color_btn_frt) ;

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
                canvas.undoToStart();
            }
        });

        toolbarBar=(LinearLayout) view.findViewById(R.id.toolbar_fnt);

        drawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canvas.setMode(CanvasView.Mode.DRAW);
            }
        });


        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText textip = new EditText(DrawFrag.this.getContext());

                textip.setText(canvas.getText());

// Set the default text to a link of the Queen
                textip.setHint("Enter Text to draw");

                new AlertDialog.Builder(DrawFrag.this.getContext())
                        .setTitle("Text")
                        .setMessage("Enter Text")
                        .setView(textip)
                        .setPositiveButton("done", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String text = textip.getText().toString();

                                canvas.setText(text);

                                canvas.setMode(CanvasView.Mode.TEXT);

                            }
                        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        textBtn.toggle();
                    }
                })
                        .show();
            }
        });
        //   dragBtn=(ImageButton)view.findViewById(R.id.dragButton);

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



   /*     eraserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

canvas.setMode(CanvasView.Mode.ERASER);
            }
        });*/


        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());
//                View sheetView = getActivity().getLayoutInflater().inflate(R.layout.drawing_option, null);
//                mBottomSheetDialog.setContentView(sheetView);
//                mBottomSheetDialog.show();

                OptionMenu bottomSheetDialogFragment = new OptionMenu();

                bottomSheetDialogFragment.setCanvasView(canvas);

                bottomSheetDialogFragment.show(DrawFrag.this.getFragmentManager(), bottomSheetDialogFragment.getTag());

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeImage(canvas.getBitmap());
            }
        });

//        paintBucket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                canvas.setMode(CanvasView.Mode.PAINT_BUCKET);
//            }
//        });

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
    public void onStart() {

        super.onStart();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public long onDrawingAdded(SerialzablePaint paint, SerialzablePath path) {
        Toast.makeText(this.getContext(),"onAdd",Toast.LENGTH_SHORT).show();

        Conversation conversation=new Conversation();

        conversation.isMe=true;

        conversation.isSent=false;

        conversation.isDraw=true;

        conversation.isDelivered=false;

        conversation.with= Person.load(Person.class,with);

        conversation.date= Calendar.getInstance().getTime();
      //TODO
            JSONObject msg = new JSONObject();
        try {
            msg.put("path", serialize(path));
            msg.put("paint", serialize(paint));
            conversation.message=msg.toString();


           return conversation.save();

        }catch (Exception e)
        {
            Log.e(TAG,e.toString());
        }

        return 0;
    }

    String TAG="DRAWFRAG";

    @Override
    public void onDrawingRemoved(long id) {

        Conversation conversation=Conversation.load(Conversation.class,id);

        new Delete().from(Conversation.class).where("Id=?",id);

        Toast.makeText(this.getContext(),"onRemov",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDrawingUpdated(long id,SerialzablePath path,SerialzablePaint paint) {
        Toast.makeText(this.getContext(),"onUp",Toast.LENGTH_SHORT).show();

        Conversation conversation=Conversation.load(Conversation.class,id);

        conversation.date= Calendar.getInstance().getTime();
        //TODO
        JSONObject msg = new JSONObject();
        try {
            msg.put("path", serialize(path));
            msg.put("paint", serialize(paint));
            conversation.message=msg.toString();
             conversation.save();

        }catch (Exception e)
        {
            Log.e(TAG,e.toString());
        }


    }

    @Override
    public void initializeHistory(CanvasView.History history) {

        List<Conversation> list=new Select().from(Conversation.class).where("isDraw=?",1).and("with=?",with).execute();

       Cursor cur= ActiveAndroid.getDatabase().query(false,"conversation",null,"isDraw=1",null,null,null,null,null);


        Toast.makeText(this.getContext(),"cursor size= "+cur.getCount(),Toast.LENGTH_SHORT).show();

        try {

        while(cur.moveToNext())
        {
           String msg= cur.getString(cur.getColumnIndex("message"));
            JSONObject jsonObject = new JSONObject(msg);

            SerialzablePaint paint = (SerialzablePaint) deserialize(jsonObject.get("paint").toString());

            paint.initialize();

            Toast.makeText(this.getContext(),"drawing saved",Toast.LENGTH_SHORT).show();

            SerialzablePath path = (SerialzablePath) deserialize(jsonObject.get("path").toString());

            path.initialize();


            Toast.makeText(this.getActivity(),"drawing saved",Toast.LENGTH_SHORT).show();


            history.pathLists.add(path);
            history.paintLists.add(paint);
            history.idList.add(cur.getLong(cur.getColumnIndex("Id")));

        }

            for (Conversation conversation : list) {

                JSONObject jsonObject = new JSONObject(conversation.message);

                SerialzablePaint paint = (SerialzablePaint) deserialize(jsonObject.get("paint").toString());

                paint.initialize();

                Toast.makeText(this.getContext(),"drawing saved",Toast.LENGTH_SHORT).show();

                SerialzablePath path = (SerialzablePath) deserialize(jsonObject.get("path").toString());

                path.initialize();

                history.pathLists.add(path);
                history.paintLists.add(paint);
                history.idList.add(conversation.getId());

            }
        }catch (Exception e3)
        {
            Log.e(TAG,e3.toString());
        }


        Toast.makeText(this.getActivity(),"executed",Toast.LENGTH_SHORT).show();
    }

    public static String serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        String str=Base64.encodeToString(out.toByteArray(),Base64.URL_SAFE);
        os.close();
        return str;
    }
    public static Object deserialize(String str) throws IOException, ClassNotFoundException {
        byte[] data=Base64.decode(str,Base64.URL_SAFE);
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
       Object ob=  is.readObject();
        is.close();
        return ob;
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

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d("",
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();

            Toast.makeText(getContext(),"image saved to "+pictureFile.getAbsolutePath(),Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            Log.d("", "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("", "Error accessing file: " + e.getMessage());
        }




    }
    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
//                + "/Android/data/"
//                + getContext().getApplicationContext().getPackageName()
                + "/Pictures/ChitChat");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="CHITCHAT"+ timeStamp +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
}
