/*
    Cole Howell, Manoj Bompada
    ITCS 4180
    ViewMessagesFragment.java
 */

package example.com.homework9;


import android.app.Activity;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMessagesFragment extends Fragment {

    public static final String CONTACTUSER = "contact" ;
    public static final String LOGUSER = "user" ;
    public static User msgloguser;
    private User msgcontactobj= new User();
    ListView msglistView;
    Button sndBtn;
    EditText sendmsg;
    static MessageListAdapter adapter;
    List<Message> mList = new ArrayList<Message>();
    public static Firebase mRefmsg;
    Message msgobj = new Message();
    Message msg;
    AuthData authData;
    String currentLogggedInUserFullName, userId;
    Conversations conversations;
    ArrayList<Conversations> covList = new ArrayList<Conversations>();
    static Boolean conv = false;


    private OnFragmentInteractionListener mListener;

    public ViewMessagesFragment() {
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
        msgcontactobj.setEmail(user.getEmail());
        msgcontactobj.setFullname(user.getFullname());
        msgcontactobj.setHasUnreadMsg(user.getHasUnreadMsg());
        msgcontactobj.setPassword(user.getPassword());
        msgcontactobj.setPhoneno(user.getPhoneno());
        msgcontactobj.setPicture(user.getPicture());
        getActivity().setTitle(msgcontactobj.fullname);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRefmsg = new Firebase("https://homework-9.firebaseio.com/Messages/");


        msglistView = (ListView) getView().findViewById(R.id.msglistView);
        sndBtn = (Button) getView().findViewById(R.id.sendbutton);
        sendmsg = (EditText) getView().findViewById(R.id.sendmsgtextView);

        authData = mRefmsg.getAuth();
        if (authData != null) {
            final Firebase loggedInUserUrl = new Firebase("https://homework-9.firebaseio.com/"+"/users/" + authData.getProviderData().get("email").toString().split("@")[0]);
            loggedInUserUrl.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    msgloguser = snapshot.getValue(User.class);
                    Log.d("demo","View msg loggeduser url ="+msgloguser  );

                    currentLogggedInUserFullName = msgloguser.getFullname();

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });
            userId = authData.getUid();
        }
        covList.clear();

        final Firebase cMessage = new Firebase("https://homework-9.firebaseio.com/Conversations/");

        cMessage.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Conversations convs = dataSnapshot.getValue(Conversations.class);

//                if(!convs.equals(" ")){
                covList.add(convs);
//                }

                Log.d("demo", "covs list = " + covList);

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

        mList.clear();
        mRefmsg.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                adapter.setNotifyOnChange(true);
                adapter.notifyDataSetChanged();
                msg = dataSnapshot.getValue(Message.class);
                msg.setKey(dataSnapshot.getKey());


//
                if ((msg.getSender().equals(msgloguser.getFullname()) && msg.getReceiver().equals(msgcontactobj.getFullname())) ||
                        (msg.getSender().equals(msgcontactobj.getFullname()) && msg.getReceiver().equals(msgloguser.getFullname()))) {
                    if (msg.getReceiver().equals(currentLogggedInUserFullName) && msg.isMessage_read() == false) {
                        Firebase updateMessage = new Firebase("https://homework-9.firebaseio.com/" + "/Messages/" + msg.getKey());
                        msg.setMessage_read(true);
                        updateMessage.setValue(msg);
                    }

                    mList.add(msg);
                    adapter.notifyDataSetChanged();
//                    --------------------------------------------------------------------------------------------



                    conversations = new Conversations(" ", msg.getSender(), msg.getReceiver(), false, false);
                    for (Conversations x : covList) {
                        if ((x.getParticipant1().equals(conversations.getParticipant1()) && x.getParticipant2().equals(conversations.getParticipant2())) ||
                                x.getParticipant1().equals(conversations.getParticipant2()) && x.getParticipant2().equals(conversations.participant1)) {
                            conv = true;
                        }
                    }

                    Log.d("demo", "boolena conv = MUST BE FLASE" + conv);

                    if (!conv) {
                        Log.d("demo", "bool conv = " + conv);
                        cMessage.push().setValue(conversations);
                        covList.add(conversations);
                    }
//                    -------------------------------------------------------------------------------------------------------


                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                adapter.setNotifyOnChange(true);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Toast.makeText(getActivity(), "Message removed from firebase", Toast.LENGTH_SHORT).show();
                msg = dataSnapshot.getValue(Message.class);
                mList.remove(msg);
                adapter.notifyDataSetChanged();

                adapter.remove(dataSnapshot.getValue(Message.class));

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        sndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String timeStamp = DateFormat.getDateTimeInstance().format(new Date());
                if (sendmsg.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter some text", Toast.LENGTH_SHORT).show();

                }
                else {
                    msgobj = new Message(timeStamp," ",false, sendmsg.getText().toString(), msgloguser.getFullname(), msgcontactobj.getFullname());

                    mRefmsg.push().setValue(msgobj, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            System.out.println("done");
                        }
                    });

                    sendmsg.setText("");


                }



            }
        });
        adapter = new MessageListAdapter(getActivity(), R.layout.message_row_layout,mList);
        msglistView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        adapter.notifyDataSetChanged();


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
        return inflater.inflate(R.layout.fragment_view_messages, container, false);
    }

    public interface OnFragmentInteractionListener{
//        public void goToViewMessagesFragments();
    }
}
