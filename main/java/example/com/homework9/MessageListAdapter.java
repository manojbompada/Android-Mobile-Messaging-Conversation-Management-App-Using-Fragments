/*
    Cole Howell, Manoj Bompada
    ITCS 4180
    MessageListAdapter.java
 */

package example.com.homework9;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

/**
 * Created by colehowell on 4/23/16.
 */
public class MessageListAdapter extends ArrayAdapter<Message> {
    Context mContext;
    List<Message> mData;
    int mResource;

    public MessageListAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);

        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(mResource, parent, false);
            holder = new Holder();
            holder.hdelbtn = (ImageView) convertView.findViewById(R.id.delimageView);
            holder.hflname = (TextView) convertView.findViewById(R.id.flname);
            holder.hmsg = (TextView) convertView.findViewById(R.id.msg);
            holder.htimestmp = (TextView) convertView.findViewById(R.id.datetxt);

            convertView.setTag(holder);
        }

        holder = (Holder) convertView.getTag();

        TextView flname = holder.hflname;
        flname.setText(mData.get(position).getSender());

        TextView msg = holder.hmsg;
        msg.setText(mData.get(position).getMessage_text());

        TextView timestmp = holder.htimestmp;
        timestmp.setText(mData.get(position).getTimestamp());

        ImageView delbtn = holder.hdelbtn;

        if(ViewMessagesFragment.msgloguser.fullname.equals(mData.get(position).getSender())){
            convertView.setBackgroundColor(Color.parseColor("#D3D3D3"));
            delbtn.setVisibility(View.VISIBLE);
        }
        else{
            convertView.setBackgroundColor(Color.WHITE);
            delbtn.setVisibility(View.INVISIBLE);
        }

        delbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewMessagesFragment.mRefmsg
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    Message msg = postSnapshot.getValue(Message.class);
//                                    System.out.println(post.getAuthor() + " - " + post.getTitle());
                                    if (msg.toString().equals(mData.get(position).toString())) {
                                        postSnapshot.getRef().removeValue();

                                    }
                                }
                                ViewMessagesFragment.adapter.notifyDataSetChanged();
                            }
                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });


            }
        });

        return convertView;
    }

    private class Holder{
        ImageView hdelbtn;
        TextView hflname,hmsg,htimestmp;
    }
}
