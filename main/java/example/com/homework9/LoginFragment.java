/*
    Cole Howell, Manoj Bompada
    ITCS 4180
    LoginFragment.java
 */

package example.com.homework9;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Firebase mRef;

    public LoginFragment() {
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
        getActivity().setTitle("Stay in Touch (Login)");
        final TextView email, password;

        mRef = new Firebase("https://homework-9.firebaseio.com/");

        email = (TextView) getView().findViewById(R.id.email);
        password = (TextView) getView().findViewById(R.id.password);

        getView().findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRef.authWithPassword(email.getText().toString(), password.getText().toString(), new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        mListener.goToConversationFragment();

                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Log.d("demo", "Email = "+email.getText().toString()+" passowrd = "+ password.getText().toString());
                        Toast.makeText(getActivity(), "Login Failed!!!! An error occurred.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        getView().findViewById(R.id.createAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToSignupFragment();
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public interface OnFragmentInteractionListener{
        public void goToSignupFragment();

        public void goToConversationFragment();
    }
}
