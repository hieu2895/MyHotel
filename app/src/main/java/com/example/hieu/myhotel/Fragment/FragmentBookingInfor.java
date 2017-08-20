package com.example.hieu.myhotel.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hieu.myhotel.Modules.book;
import com.example.hieu.myhotel.R;
import com.example.hieu.myhotel.Activity.myGoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 *
 */

public class FragmentBookingInfor extends Fragment {
    TextView txtName, txtAddress, txtDateIn, txtDateOut, txtQuantity, txtTotal, txtInfor;
    Button btnOk;
    private DatabaseReference mDB;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_booking_infor,container,false);
        mDB = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        Mapping(root);
        loadData();
        btnOk.setOnClickListener(GoogleMap_click);
        return root;

    }

    private void loadData(){
        mDB.child("BookingInfor").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                book b = dataSnapshot.getValue(book.class);
                txtName.setText(b.getNameHotel());
                txtAddress.setText(b.getAddress());
                txtDateIn.setText(b.getDateIn().toString());
                txtDateOut.setText(b.getDateOut().toString());
                txtQuantity.setText(b.getQuantity()+"");
                txtTotal.setText(b.getTotal().toString());

                txtName.setVisibility(View.VISIBLE);
                txtAddress.setVisibility(View.VISIBLE);
                txtDateIn.setVisibility(View.VISIBLE);
                txtDateOut.setVisibility(View.VISIBLE);
                txtQuantity.setVisibility(View.VISIBLE);
                txtTotal.setVisibility(View.VISIBLE);
                btnOk.setVisibility(View.VISIBLE);
                txtInfor.setVisibility(View.INVISIBLE);
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private View.OnClickListener GoogleMap_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String address = txtAddress.getText().toString();

            Bundle bundle = new Bundle();
            bundle.putString("address", address);
            Intent intent = new Intent(getActivity(), myGoogleMap.class);
            intent.putExtra("Packet", bundle);
            startActivity(intent);
        }
    };

    private void Mapping(View root) {
        txtName = (TextView) root.findViewById(R.id.nameInfor);
        txtAddress = (TextView) root.findViewById(R.id.addInfor);
        txtDateIn = (TextView) root.findViewById(R.id.DateInInfor);
        txtTotal = (TextView) root.findViewById(R.id.totalInfor);
        txtDateOut = (TextView) root.findViewById(R.id.DateOutInfor);
        txtQuantity = (TextView) root.findViewById(R.id.quantityInfor);
        txtInfor = (TextView) root.findViewById(R.id.tvInfor);
        btnOk = (Button) root.findViewById(R.id.btnGoogleMapInfor);

        txtName.setVisibility(View.INVISIBLE);
        txtAddress.setVisibility(View.INVISIBLE);
        txtDateIn.setVisibility(View.INVISIBLE);
        txtDateOut.setVisibility(View.INVISIBLE);
        txtQuantity.setVisibility(View.INVISIBLE);
        txtTotal.setVisibility(View.INVISIBLE);
        btnOk.setVisibility(View.INVISIBLE);
    }
}
