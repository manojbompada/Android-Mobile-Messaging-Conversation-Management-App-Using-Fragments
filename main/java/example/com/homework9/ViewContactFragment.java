/*
    Cole Howell, Manoj Bompada
    ITCS 4180
    ViewContactFragment.java
 */

package example.com.homework9;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewContactFragment extends Fragment {

    private User loguser;
    private User contactobj;
    TextView name, username, email, phoneNumber;
    ImageView contactPhoto;
    private Firebase mRef;

    private OnFragmentInteractionListener mListener;

    public ViewContactFragment() {
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

    public void setDetails(User user){
        name = (TextView) getView().findViewById(R.id.vcFullName);
        username = (TextView) getView().findViewById(R.id.vcContactName);
        email = (TextView) getView().findViewById(R.id.vcEmail);
        phoneNumber = (TextView) getView().findViewById(R.id.vcPhoneNum);
        contactPhoto = (ImageView) getView().findViewById(R.id.vcPhoto);
        byte[] decodedString = Base64.decode(user.getPicture(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        name.setText(user.getFullname());
        username.setText(user.getFullname());
        email.setText(user.getEmail());
        phoneNumber.setText(String.valueOf(user.getPhoneno()));
        contactPhoto.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 150, 150, false));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRef = new Firebase("https://homework-9.firebaseio.com/users");


        AuthData authData = mRef.getAuth();

        if (authData == null) {
            getActivity().finish();
        }

        name = (TextView) getView().findViewById(R.id.vcFullName);
        username = (TextView) getView().findViewById(R.id.vcContactName);
        email = (TextView) getView().findViewById(R.id.vcEmail);
        phoneNumber = (TextView) getView().findViewById(R.id.vcPhoneNum);
        contactPhoto = (ImageView) getView().findViewById(R.id.vcPhoto);
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
        return inflater.inflate(R.layout.fragment_view_contact, container, false);
    }

    public interface OnFragmentInteractionListener{
//        public void goToViewContactFragment();
    }
}
