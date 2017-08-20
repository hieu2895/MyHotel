package com.example.hieu.myhotel.Activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hieu.myhotel.Fragment.FragmentBookingInfor;
import com.example.hieu.myhotel.Fragment.FragmentHome;
import com.example.hieu.myhotel.Fragment.FragmentHotelManager;
import com.example.hieu.myhotel.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private TextView txtnameUser;
    private ImageView profilePictureView;

    private ListView myMenu;
    private Bundle bundle;
    private DrawerLayout myDrawer;

    private String[] myItems_Staff=new String[]{
            "Tìm kiếm khách sạn","Thông tin đặt phòng","Quản lý khách sạn", "Xác nhận đơn đặt phòng", "Thoát"
    };
    private String[] myItems_Client=new String[]{
            "Đăng nhập","Đăng ký","Tìm kiếm khách sạn","Liên hệ với chúng tôi"
    };
    private String[] myItems_User=new String[]{
            "Tìm kiếm khách sạn","Thông tin đặt phòng","Liên hệ với chúng tôi", "Thoát"
    };
    private String[] myItems = myItems_Client;

    private String id;
    private String email;
    private ArrayAdapter<String> myAdapter;
    private LinearLayout my_nav;


    DatabaseReference mDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Mapping();
        getDataOfIntent();
        boolean x = getDataOfIntent();
        if(x == true) {
            checkAdmin();
        }else action();
    }


    public void checkAdmin(){
        mDB = FirebaseDatabase.getInstance().getReference();
        mDB.child("Admin").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String emailDB = dataSnapshot.getValue().toString();
                Log.d("qpo",email);
                if(email.equals(emailDB)){
                    myItems = myItems_Staff;
                    Glide.with(HomeActivity.this).load(R.drawable.nv).into(profilePictureView);
                }
                action();
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


    public void action(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.my_tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Find My Hotel");
        toolbar.setTitleTextColor(Color.WHITE);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,myDrawer,toolbar,
                R.string.open,R.string.close);
        myDrawer.setDrawerListener(toggle);

        toggle.syncState();

        myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,myItems);
        myMenu.setAdapter(myAdapter);

        FragmentHome frg = new FragmentHome();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().add(R.id.myFrame, frg).commit();
        //thay doi noi dung- thay doi fragment
        myMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(myItems[position].equals("Thoát")){
                    Logout();
                    return;
                }else if(myItems[position].equals("Đăng nhập")){
                    Intent register = new Intent(HomeActivity.this, MainActivity.class);
                    startActivity(register);
                    return;
                }else if(myItems[position].equals("Đăng ký")){
                    Intent register = new Intent(HomeActivity.this, RegisterActivity.class);
                    startActivity(register);
                    return;
                }else if(myItems[position].equals("Quản lý khách sạn")){
                    FragmentHotelManager frg = new FragmentHotelManager();
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(R.id.myFrame, frg).commit();
                    myDrawer.closeDrawer(my_nav);
                    return;
                }else if(myItems[position].equals("Tìm kiếm khách sạn")){
                    FragmentHome frg = new FragmentHome();
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().add(R.id.myFrame, frg).commit();
                    myDrawer.closeDrawer(my_nav);
                }else if(myItems[position].equals("Thông tin đặt phòng")){
                    FragmentBookingInfor frg = new FragmentBookingInfor();
                    FragmentManager manager = getFragmentManager();
                    manager.beginTransaction().replace(R.id.myFrame, frg).commit();
                    myDrawer.closeDrawer(my_nav);
                }
                //dong menu navigator sau khi thay doi fragment
                myDrawer.closeDrawer(my_nav);
            }
        });

    }

    public void Logout(){
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
    }



    private void Mapping(){
        myMenu=(ListView)findViewById(R.id.myMenu);
        myDrawer=(DrawerLayout)findViewById(R.id.myDrawer);
        my_nav=(LinearLayout)findViewById(R.id.my_nav);
        profilePictureView = (ImageView) findViewById(R.id.imageProfilePictureView);
        txtnameUser = (TextView) findViewById(R.id.tvName);

    }

    public boolean getDataOfIntent() {
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("Packet");

        if(bundle != null) {
            String name = bundle.getString("name");
            email = bundle.getString("email");
            String uri = bundle.getString("uri");
            id = bundle.getString("id");
            if (name != null) {
                txtnameUser.setText(name);
                Glide.with(HomeActivity.this).load(uri).into(profilePictureView);
                myItems = myItems_User;
            } else if (email != null && name == null) {
                txtnameUser.setText(email);
                Glide.with(this).load(R.drawable.user_icon).into(profilePictureView);
                myItems = myItems_User;
            }
            return true;
        }
        return false;

    }


}
