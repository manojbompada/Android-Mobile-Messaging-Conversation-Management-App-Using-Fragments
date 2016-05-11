/*
    Cole Howell, Manoj Bompada
    ITCS 4180
    ContactsFragment.java
 */

package example.com.homework9;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {
    public static final String USEROBJ ="user" ;
    public static final String LOGGEDUSER = "logged user";
    public static final String CONTACTOBJ = "contact";
    String username;
    private Firebase mRef;
    ListView listView;
    ArrayList<User> userList = new ArrayList<User>();
    ContactslistAdapter adapter;
    User userObj = new User();
    User luser;
    User user;

    private OnFragmentInteractionListener mListener;

    public ContactsFragment() {
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
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRef = new Firebase("https://homework-9.firebaseio.com/");
        getActivity().setTitle("View Contacts");

        AuthData authData = mRef.child("users").getAuth();
        Log.d("demo","conacts frag "+ authData.getProviderData().get("email"));


        if (authData == null) {
            getActivity().finish();
        }

        listView = (ListView) getView().findViewById(R.id.contactsList);
        userList.clear();

        mRef.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                user = dataSnapshot.getValue(User.class);

                if (user.getEmail().equals(mRef.getAuth().getProviderData().get("email"))) {
                    userObj = user;
                }

                if (!user.getEmail().equals(mRef.getAuth().getProviderData().get("email"))) {

                    userList.add(user);


                }

                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

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

        adapter = new ContactslistAdapter(getActivity(),R.layout.contacts_row_layout,userList);
        adapter.setNotifyOnChange(true);

        Firebase messageFirebaseRoot = new Firebase("https://homework-9.firebaseio.com/Messages");
        messageFirebaseRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //messageArrayList.clear();

                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {

                    Message msg = messageSnapshot.getValue(Message.class);
                    msg.setKey(messageSnapshot.getKey());
                    int position = 0;
                    if(msg.isMessage_read()==false){
                        for (User u:userList) {
                            if(msg.getSender().equals(u.getFullname())){
                                if(u.getHasUnreadMsg().equals("false")){
                                    u.setHasUnreadMsg("true");
                                    userList.set(position,u);
                                    break;
                                }
                            }
                            position++;
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("demo", "Position : " + position + " value: " + userList.get(position).toString());
                getFragmentManager().beginTransaction().replace(R.id.container, new ViewMessagesFragment(), "viewmessage").addToBackStack(null).commit();
                Firebase mUserRef= new Firebase(mRef+"/users");
                mUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot expenseSnapshot: dataSnapshot.getChildren()){
                            User user= expenseSnapshot.getValue(User.class);
                            if(user.getFullname().equals(userList.get(position).getFullname()) && user.getEmail().equals(userList.get(position).getEmail()))
                            {   try{
                                ViewMessagesFragment m= (ViewMessagesFragment)getFragmentManager().findFragmentByTag("viewmessage");
                                m.setDetails(user);
                            }catch (Exception e){e.printStackTrace();}
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("demo", "Position : " + position + " value: " + userList.get(position).toString());
//                mListener.goToViewContact();
                getFragmentManager().beginTransaction().replace(R.id.container, new ViewContactFragment(), "viewContact").addToBackStack(null).commit();
                Firebase mUserRef= new Firebase(mRef+"/users");
                mUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot expenseSnapshot: dataSnapshot.getChildren()){
                            User user= expenseSnapshot.getValue(User.class);
                            if(user.getFullname().equals(userList.get(position).getFullname()) && user.getEmail().equals(userList.get(position).getEmail()))
                            {   try{
                                ViewContactFragment v= (ViewContactFragment)getFragmentManager().findFragmentByTag("viewContact");
                                v.setDetails(user);
                            }catch (Exception e){e.printStackTrace();}
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                return true;
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
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    public interface OnFragmentInteractionListener{
        public void goToViewMessage();
        public void goToViewContact();
    }

}
