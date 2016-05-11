/*
    Cole Howell, Manoj Bompada
    ITCS 4180
    ConversationListAdapter.java
 */

package example.com.homework9;
//
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import example.com.homework9.Message;
import example.com.homework9.R;
import example.com.homework9.User;

/**
 * Created by Manoj on 4/23/2016.
 */
public class ConversationsListAdapter extends ArrayAdapter<User> {
    List<User> mData;
    Context mContext;
    int mResourceId;
    ImageView imageViewIsUnread;
    ArrayList<Message> messagesList = new ArrayList<>();
    final Firebase myFirebaseRef = new Firebase("https://homework-9.firebaseio.com/Messages");
    final Firebase myUserRef= new Firebase("https://homework-9.firebaseio.com/users");



    public ConversationsListAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mData = objects;
        this.mResourceId = resource;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(mResourceId, parent, false);
            holder = new Holder();
            holder.contactPhoto = (ImageView) convertView.findViewById(R.id.profilepic);
            holder.contactName = (TextView) convertView.findViewById(R.id.fname);
            holder.read_status = (ImageView) convertView.findViewById(R.id.status);
            holder.phone = (ImageView) convertView.findViewById(R.id.phone);

            convertView.setTag(holder);

        }

        holder = (Holder) convertView.getTag();

        final User user = mData.get(position);
        ImageView contactPhoto = holder.contactPhoto;
        try{
            byte[] decodedString = Base64.decode(mData.get(position).getPicture(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            contactPhoto.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 100, 100, false));
        }
        catch (Exception e){
        }


        TextView contactName = holder.contactName;
        contactName.setText(mData.get(position).getFullname());

        ImageView phone = holder.phone;



        final ImageView status = holder.read_status;
        status.setImageResource(R.drawable.red1);

//        if(user.getHasUnreadMsg().equals("true"))
//            status.setVisibility(View.VISIBLE);
//        else
//            status.setVisibility(View.INVISIBLE);

        final int pos = position;



        final String sender =mData.get(position).getFullname();
        myFirebaseRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot d : dataSnapshot.getChildren()) {
                    if (d.getValue(Message.class).getSender().equals(sender)) {
                        Firebase userRef = myUserRef.child(myFirebaseRef.getAuth().getProviderData().get("email").toString().split("@")[0]);
                        userRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.getValue(User.class).getFullname().equals(d.getValue(Message.class).getReceiver())) {
                                    status.setVisibility(View.INVISIBLE);
                                    return;
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return convertView;
    }

    private class Holder{
        ImageView contactPhoto, read_status, phone;
        TextView contactName;
    }
}