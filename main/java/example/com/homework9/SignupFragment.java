/*
    Cole Howell, Manoj Bompada
    ITCS 4180
    SignupFragment.java
 */

package example.com.homework9;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignupFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private Firebase mRef;

    public SignupFragment() {
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Stay in Touch (SignUp)");

        mRef = new Firebase("https://homework-9.firebaseio.com/");

        final Button signUp, cancel;
        final TextView fullName, email, password, confirmPassword, phoneNumber;

        signUp = (Button) getView().findViewById(R.id.signUpButton);
        cancel = (Button) getView().findViewById(R.id.cancelButton);
        fullName = (TextView) getView().findViewById(R.id.fullName);
        email = (TextView) getView().findViewById(R.id.emailSignUp);
        password = (TextView) getView().findViewById(R.id.passwordSignup);
        phoneNumber = (TextView) getView().findViewById(R.id.phoneNumber);
        confirmPassword = (TextView) getView().findViewById(R.id.passwordConfirm);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.defaultcontact);
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        final byte[] image=stream.toByteArray();
        final String img_str = Base64.encodeToString(image, 0);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!email.getText().toString().isEmpty()&&!password.getText().toString().isEmpty()&&
                        !fullName.getText().toString().isEmpty()&&!confirmPassword.getText().toString().isEmpty()&&
                        !password.getText().toString().isEmpty() &&(
                        (password.getText().toString().equals(confirmPassword.getText().toString()))))
                mRef.createUser(email.getText().toString(), password.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> stringObjectMap) {

                        Firebase usersRef = mRef.child("users");

                        User user = new User(fullName.getText().toString(),email.getText().toString(),password.getText().toString(),
                                img_str,phoneNumber.getText().toString());

                        String s = user.getEmail();
                        String[] split = s.split("@");
                        String username = split[0];
                        usersRef.child(username).setValue(user);

                        Log.d("demo", "user created " + user.toString());

                        Toast.makeText(getActivity(), "User has been created successfully!", Toast.LENGTH_SHORT).show();

                        mListener.goToLoginFragment();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        switch (firebaseError.getCode()) {
                            case FirebaseError.EMAIL_TAKEN:
                                Toast.makeText(getActivity(), "This email address is already taken. Select another one", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getActivity(),firebaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToLoginFragment();
                getActivity().finish();

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
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    public interface OnFragmentInteractionListener{
        public void goToLoginFragment();
    }

}
