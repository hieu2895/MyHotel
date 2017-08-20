package com.example.hieu.myhotel.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hieu.myhotel.Modules.book;
import com.example.hieu.myhotel.Modules.booking;
import com.example.hieu.myhotel.R;
import com.example.hieu.myhotel.Activity.myGoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Hieu on 26/07/2017.
 */

public class FragmentBooking extends Fragment {
    private TextView txtName, txtAddress, txtCost, txtDetail;
    private Button btnDateIn, btnDateOut, btnBooking, btnGoogleMap;
    private EditText edtquantty;
    private ImageView imgHotel;
    private book bookingInforEntity;
    private Dialog dialog;
    private DatabaseReference mDB;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_booking, container, false);
        mDB = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        Mapping(root);
        getDataFromFragment();
        btnDateIn.setOnClickListener(Date_In_Click);
        btnDateOut.setOnClickListener(Date_out_click);
        btnBooking.setOnClickListener(Booking_click);
        btnGoogleMap.setOnClickListener(GoogleMap_click);
        bookingInforEntity = new book();
        return root;

    }

    private void getDataFromFragment() {
        Bundle bundle = getArguments();
        Toast.makeText(getActivity(), bundle.getString("name"), Toast.LENGTH_SHORT).show();
        txtName.setText(bundle.getString("name"));
        txtAddress.setText(bundle.getString("address") + " " + bundle.getString("district") + " " + bundle.getString("city"));
        txtCost.setText(bundle.getInt("cost") + "");
        txtDetail.append(bundle.getString("detail"));
        Glide.with(this).load(bundle.getString("url")).into(imgHotel);
    }

    private void Mapping(View root) {
        imgHotel = (ImageView) root.findViewById(R.id.imgHotelB);
        txtName = (TextView) root.findViewById(R.id.tvNameB);
        txtAddress = (TextView) root.findViewById(R.id.tvAddressB);
        txtCost = (TextView) root.findViewById(R.id.tvCostB);
        txtDetail = (TextView) root.findViewById(R.id.tvdetailB);
        btnDateIn = (Button) root.findViewById(R.id.btnDateInBooking);
        btnDateOut = (Button) root.findViewById(R.id.btnDateOutBooking);
        edtquantty = (EditText) root.findViewById(R.id.edtQuantity);

        btnBooking = (Button) root.findViewById(R.id.book);
        btnGoogleMap = (Button) root.findViewById(R.id.btnGoogleMap);
    }

    private DatePickerDialog dialogDate;
    private View.OnClickListener Date_In_Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(getActivity(), onDateInSelected, cal
                    .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        }

    };

    private DatePickerDialog.OnDateSetListener onDateInSelected = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfMonth);

            bookingInforEntity.setDateIn(cal.getTime());
            SimpleDateFormat fm = new SimpleDateFormat("dd-MM-yyyy");
            btnDateIn.setText(fm.format(cal.getTime()));

        }
    };
    private View.OnClickListener Date_out_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar cal = Calendar.getInstance();
            if (bookingInforEntity.getDateOut() != null) {
                cal.setTime(bookingInforEntity.getDateOut());
            } else {
                cal.setTime(bookingInforEntity.getDateIn());
            }
            new DatePickerDialog(getActivity(), onDateOutSelected, cal
                    .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    private DatePickerDialog.OnDateSetListener onDateOutSelected = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            Calendar cal = Calendar.getInstance();
            cal.set(year, month, dayOfMonth);

            bookingInforEntity.setDateOut(cal.getTime());
            SimpleDateFormat fm = new SimpleDateFormat("dd-MM-yyyy");
            btnDateOut.setText(fm.format(cal.getTime()));

        }
    };

    private View.OnClickListener Booking_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mAuth.getCurrentUser() == null) {
                showDialogErr("Bạn cần đăng nhập để thực hiện chức năng này");
                return;
            }
           // String name = null, address = null, detail = null, dateIn = null, dateOut = null;
            int day = 0;
            int quantity = 0;
            int cost = 0;
            String name = txtName.getText().toString();
            String address = txtAddress.getText().toString();
            String detail = txtDetail.getText().toString();
                //SimpleDateFormat fm = new SimpleDateFormat("dd - MM - yyyy");
                //dateIn = btnDateIn.getText().toString();
                //dateOut = btnDateOut.getText().toString();
                if (bookingInforEntity.getDateIn() != null && bookingInforEntity.getDateOut() != null) {
                    day = (int) ((bookingInforEntity.getDateOut().getTime() - bookingInforEntity.getDateIn().getTime()) / (1000 * 60 * 60 * 24));
//
                }
                cost = Integer.parseInt(txtCost.getText().toString());
                try {
                    quantity = Integer.parseInt(edtquantty.getText().toString());
                } catch (Exception e) {
                    showDialogErr("Sai số phòng");
                }
            double total = cost * day * quantity;

            bookingInforEntity.setAddress(address);
            bookingInforEntity.setNameHotel(name);
            bookingInforEntity.setCost(cost);
            bookingInforEntity.setQuantity(quantity);
            bookingInforEntity.setTotal(total);
            showDialogDB("Tổng số tiền cần thanh toán là " + total + ". Bạn xác nhận đặt phòng?");
        }
    };

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

    private void saveInDB(){
        mDB.child("BookingInfor").push().setValue(bookingInforEntity);

        mDB.child("BookingInfor").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String idBook = (String) dataSnapshot.getKey();
                String id = mAuth.getCurrentUser().getUid();
                mDB.child("Booking").push().setValue(new booking(id, idBook));
                showDialogErr("Lưu đơn hàng thành công");
                FragmentBookingInfor frg = new FragmentBookingInfor();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.myFrame, frg).commit();
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

    private void showDialogErr(String s){
        dialog = new Dialog(getActivity());
        dialog.setTitle("Messeger");
        dialog.setContentView(R.layout.dialogerror);
        TextView tvDialog = (TextView) dialog.findViewById(R.id.err);
        tvDialog.setText(s);
        Button btnDialog = (Button) dialog.findViewById(R.id.btnDialog);
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showDialogDB(String s) {
        dialog = new Dialog(getActivity());
        dialog.setTitle("Messeger");
        dialog.setContentView(R.layout.dialoger_booking);
        TextView tvDialog = (TextView) dialog.findViewById(R.id.err);
        tvDialog.setText(s);
        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInDB();
                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
