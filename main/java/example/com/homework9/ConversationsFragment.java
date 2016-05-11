/*
    Cole Howell, Manoj Bompada
    ITCS 4180
    ConversationsFragment.java
 */

package example.com.homework9;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
public class ConversationsFragment extends Fragment {

    String username;
    private Firebase mRef;
    ListView listView;
    ArrayList<User> userList = new ArrayList<User>();
    ConversationsListAdapter adapter;
    User userObj = new User();
    User luser;
    User user;
    static Boolean archieved = false;

    User loguser= new User();

    ArrayList<Conversations> convsList = new ArrayList<Conversations>();

    private OnFragmentInteractionListener mListener;

    public ConversationsFragment() {
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
        getActivity().setTitle("Conversations");

        mRef = new Firebase("https://homework-9.firebaseio.com/");

        AuthData authData = mRef.child("users").getAuth();
        Log.d("demo", "conacts frag " + authData.getProviderData().get("email"));


        if (authData == null) {
            getActivity().finish();
        }

        authData = mRef.getAuth();
        if (authData != null) {
            final Firebase loggedInUserUrl = new Firebase("https://homework-9.firebaseio.com/"+"/users/" + authData.getProviderData().get("email").toString().split("@")[0]);
            loggedInUserUrl.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    loguser = snapshot.getValue(User.class);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });
        }
//------------------------------------------------------------------------------------------------------------------------

        mRef.child("Conversations").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Conversations con = dataSnapshot.getValue(Conversations.class);

                        convsList.add(con);


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

//        ----------------------------------------------------------------------------------------------------------

        listView = (ListView) getView().findViewById(R.id.ConverList);
        userList.clear();
        mRef.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                user = dataSnapshot.getValue(User.class);

                if (user.getEmail().equals(mRef.getAuth().getProviderData().get("email"))) {
                    userObj = user;
                }

                if (!user.getEmail().equals(mRef.getAuth().getProviderData().get("email"))) {

                    if(archieved){

                    }

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
        adapter = new ConversationsListAdapter(getActivity(),R.layout.conv_row_layout,userList);
        adapter.setNotifyOnChange(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("demo", "Position : " + position + " value: " + userList.get(position).toString());
                getFragmentManager().beginTransaction().replace(R.id.container, new ViewMessagesFragment(), "viewmessage").addToBackStack(null).commit();
                Firebase mUserRef = new Firebase(mRef + "/users");
                mUserRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot expenseSnapshot : dataSnapshot.getChildren()) {
                            User user = expenseSnapshot.getValue(User.class);
                            if (user.getFullname().equals(userList.get(position).getFullname()) && user.getEmail().equals(userList.get(position).getEmail())) {
                                try {
                                    ViewMessagesFragment m = (ViewMessagesFragment) getFragmentManager().findFragmentByTag("viewmessage");
                                    m.setDetails(user);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked

                                final User u = userList.get(position);

                                mRef.child("Conversations").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                        Conversations con = dataSnapshot.getValue(Conversations.class);
                                        con.setKey(dataSnapshot.getKey());

                                        if(u.getFullname().equals(con.getParticipant1())&& loguser.getFullname().equals(con.getParticipant2())||
                                          u.getFullname().equals(con.getParticipant2())&& loguser.getFullname().equals(con.getParticipant1()) ){


                                            Firebase updateConv = new Firebase("https://homework-9.firebaseio.com/" + "/Conversations/" + con.getKey());
                                            if(con.getParticipant1().equals(loguser.getFullname())){
                                                con.setIsArchived_by_participant1(true);
                                                updateConv.setValue(con);
                                            }
                                            else{
                                                con.setIsArchived_by_participant2(true);
                                                updateConv.setValue(con);

                                            }
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



                                Toast.makeText(getActivity(),"Archieved",Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked

                                final User us = userList.get(position);

                                mRef.child("Conversations").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                        Conversations con = dataSnapshot.getValue(Conversations.class);
                                        con.setKey(dataSnapshot.getKey());

                                        if(us.getFullname().equals(con.getParticipant1())&& loguser.getFullname().equals(con.getParticipant2())||
                                                us.getFullname().equals(con.getParticipant2())&& loguser.getFullname().equals(con.getParticipant1()) ){


                                            Firebase updateConv = new Firebase("https://homework-9.firebaseio.com/" + "/Conversations/" + con.getKey());

                                                    if(con.getDeletedBy().equals(" ")){
                                                        con.setDeletedBy(loguser.getEmail());
                                                        updateConv.setValue(con);
                                                    }
                                                    else{
                                                        updateConv.getParent().setValue("");
                                                    }


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

                                Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Custom Options").setPositiveButton("Archive Conversations", dialogClickListener)
                        .setNegativeButton("DeleteConversations", dialogClickListener).show();
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
        return inflater.inflate(R.layout.fragment_conversations, container, false);
    }

    public interface OnFragmentInteractionListener{
//        public void goToConversationsFragment();
    }
}
