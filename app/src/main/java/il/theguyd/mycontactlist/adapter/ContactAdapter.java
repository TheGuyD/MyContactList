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

import java.util.ArrayList;

import il.theguyd.mycontactlist.ContactActivity;
import il.theguyd.mycontactlist.Models.Contact;
import il.theguyd.mycontactlist.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>  {
    private static final String TAG = "ContactAdapter";

    private Context context;
    private ArrayList<Contact> Contacts = new ArrayList<>();

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
        holder.txtName.setText(Contacts.get(position).getFullName());
        holder.txtTelephone.setText(Contacts.get(position).getTelephone());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: is it appropriate to navigate activities from adapter?
                Intent intent = new Intent(context, ContactActivity.class);
                intent.putExtra("contactID", Contacts.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Contacts.size();
    }
    public void setContacts(ArrayList<Contact> Contacts) {
        this.Contacts = Contacts;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder  {

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