/*
    Cole Howell, Manoj Bompada
    ITCS 4180
    EditProfileFragment.java
 */

package example.com.homework9;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    private static String LOGGEDUSER ="" ;
    String oldEmail;
    String oldPassword;
    Button update, cancel;
    TextView fullName, emailtxt, phone, password, nameDescription;
    private Firebase mRef;
    User loggeduser;
    ImageView profpic;
    User usernew;
    String username;
    private static  int RESULT_LOAD_IMAGE = 1;
    User editLogUser;
    User changedLogUser = new User();

    private OnFragmentInteractionListener mListener;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bm = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] b = baos.toByteArray();

            String b64;

            b64 = Base64.encodeToString(b, Base64.DEFAULT);

            ImageView imageView = (ImageView) getView().findViewById(R.id.profileImage);
            changedLogUser.setPicture(b64);
            Log.d("demo", changedLogUser.getPicture());
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Edit Profile");

        mRef = new Firebase("https://homework-9.firebaseio.com/users");
        Log.d("demo", "login email = " + mRef.getAuth().getProviderData().get("email")) ;

        update = (Button) getView().findViewById(R.id.updateProfile);
        cancel = (Button) getView().findViewById(R.id.cancelUpdate);
        fullName = (TextView) getView().findViewById(R.id.fullNameEditProfile);
        emailtxt = (TextView) getView().findViewById(R.id.emailEditProfile);
        phone = (TextView) getView().findViewById(R.id.phoneNumbeEditProfile);
        password = (TextView) getView().findViewById(R.id.passwordEdit);
        nameDescription = (TextView) getView().findViewById(R.id.nameDescription);
        profpic = (ImageView) getView().findViewById(R.id.profileImage);

        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                editLogUser = dataSnapshot.getValue(User.class);
                Log.d("demo", "child event listener logusers = " + editLogUser.toString());

                Log.d("demo", "provider    = " + mRef.getAuth().getProviderData().get("email"));

                Log.d("demo", " oldEmail=" + oldEmail);

                if (editLogUser.getEmail().equals(mRef.getAuth().getProviderData().get("email"))) {
                    changedLogUser = editLogUser;

                    Log.d("demo", "Edit logged user " + editLogUser.toString());
                    fullName.setText(editLogUser.getFullname());
                    emailtxt.setText(editLogUser.getEmail());
                    phone.setText(editLogUser.getPhoneno().toString());
                    password.setText(editLogUser.getPassword().toString());
                    nameDescription.setText(editLogUser.getFullname());

                    byte[] decodedString = Base64.decode(editLogUser.getPicture(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    profpic.setImageBitmap(decodedByte);

                    Log.d("demo", "changed user befoer assig mail = " + changedLogUser);
                    oldEmail = changedLogUser.getEmail();
                    oldPassword = changedLogUser.getPassword();

                    String sm = oldEmail;
                    String[] split = sm.split("@");
                    username = split[0];
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        profpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRef.changeEmail(oldEmail, oldPassword, emailtxt.getText().toString(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {

                    }
                });

                mRef.changePassword(oldEmail, oldPassword, password.getText().toString(), new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {

                    }
                });
                Toast.makeText(getActivity(), "Saved successfully!", Toast.LENGTH_SHORT).show();

                String flname = fullName.getText().toString();
                String eml = emailtxt.getText().toString();
                String pwd = password.getText().toString();
                String ph = phone.getText().toString();
                usernew = new User(flname,eml,pwd,changedLogUser.getPicture(),ph);

                mListener.goToConversationFragmentfromEditProfile();

                mRef.child(username).setValue(usernew);
                Log.d("demo", " afetr click upate " + usernew.toString());

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String flname = fullName.getText().toString();
                String eml = emailtxt.getText().toString();
                String pwd = password.getText().toString();
                String ph = phone.getText().toString();
                usernew = new User(flname,eml,pwd,changedLogUser.getPicture(), ph);

                Log.d("demo", " oldEmail=" + oldEmail);
                Log.d("demo", " usernew.getPassword()=" + usernew.getPassword());
                Log.d("demo", "usernew.getEmail()= " + usernew.getEmail());

                mRef.child(username).setValue(changedLogUser);

                mListener.goToConversationFragmentfromEditProfile();
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    public interface OnFragmentInteractionListener{
        public void goToConversationFragmentfromEditProfile();
    }

}
