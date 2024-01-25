package il.theguyd.mycontactlist.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import il.theguyd.mycontactlist.ContactActivity;
import il.theguyd.mycontactlist.Models.Contact;
import il.theguyd.mycontactlist.R;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private static final String TAG = "ContactAdapter";

    private Context context;
    private ArrayList<Contact> contacts = new ArrayList<>();

    public ContactAdapter(Context context) {
        this.context = context;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_rec_view_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,  int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.txtName.setText(contacts.get(position).getFullName());
        holder.txtTelephone.setText(contacts.get(position).getTelephone());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: navigate to another activity
                Intent intent = new Intent(context, ContactActivity.class);
                //TODO: get the contact id os it will pas to the edit activity
                //intent.putExtra("contactID", contacts.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName;
        private TextView txtTelephone;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName =  itemView.findViewById(R.id.txtName);
            txtTelephone =  itemView.findViewById(R.id.txtTelephone);
            parent =  itemView.findViewById(R.id.parent);
        }
    }


}