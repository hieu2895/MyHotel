package com.example.hieu.myhotel.Fragment;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.hieu.myhotel.Adapter.HotelAdapterMN;
import com.example.hieu.myhotel.Modules.hotel;
import com.example.hieu.myhotel.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Hieu on 26/07/2017.
 */

public class FragmentHotelManager extends ListFragment {
    private ArrayList<hotel> listHotel;
    private  ArrayList<hotel> listAdapter;
    HotelAdapterMN adapter;

    private Button btnAdd,btnSearchAdmin;
    private EditText etSerachHotelAdmin;
    //private ListView lvHotel;

    DatabaseReference mDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_hotel_manager,container,false);
        mDB = FirebaseDatabase.getInstance().getReference();
        listHotel = new ArrayList<hotel>();
        listAdapter = new ArrayList<hotel>();
        Mapping(root);
        adapter = new HotelAdapterMN(getActivity(), R.layout.hotel_item, listAdapter);
        setListAdapter(adapter);
        loadDataFromDB();
        searchAdmin();
        addHotel();
        return root;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        hotel x = listAdapter.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("id", x.getId());
        bundle.putString("name", x.getName());
        bundle.putString("city", x.getCity());
        bundle.putString("district", x.getDistrict());
        bundle.putString("address", x.getAddress());
        bundle.putInt("cost", x.getCost());
        bundle.putString("detail",x.getDetail());
        bundle.putString("url",x.getImgHotel());
        FragmentAddHotel frg = new FragmentAddHotel();
        frg.setArguments(bundle);
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.myFrame, frg).commit();
    }
    private void searchAdmin() {
        btnSearchAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search_text = etSerachHotelAdmin.getText().toString();
                ArrayList<hotel> listUpdate = new ArrayList<>();
                for(hotel i : listHotel){
                    if(i.getName().toUpperCase().contains(search_text.toUpperCase()))
                        listUpdate.add(i);
                }
                listAdapter.clear();
                for(hotel i : listUpdate){
                    listAdapter.add(i);
                }
                adapter.notifyDataSetChanged();
                // adapter = new HotelAdapterMN(getActivity(), R.layout.hotel_item, listUpdate);
                // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arrCity);
                //setListAdapter(adapter);
            }
        });

    }

    private void loadDataFromDB() {
        mDB.child("Hotel").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                hotel hotelEntity = (hotel)dataSnapshot.getValue(hotel.class);
                listHotel.add(hotelEntity);
                listAdapter.add(hotelEntity);
//                Toast.makeText(getActivity(), hotelEntity.getName(), Toast.LENGTH_SHORT).show();
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //adapter = new HotelAdapterMN(getActivity(), R.layout.hotel_item, listHotel);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arrCity);
        //setListAdapter(adapter);

    }

    public void Mapping(View root){
        etSerachHotelAdmin = (EditText) root.findViewById(R.id.et_search_hotel_admin);
        btnSearchAdmin = (Button) root.findViewById(R.id.btnSearchAdmin);
        btnAdd = (Button) root.findViewById(R.id.btnAddAdmin);
    }

    public void addHotel(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentAddHotel frg = new FragmentAddHotel();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.myFrame, frg).commit();
            }
        });

    }
}
