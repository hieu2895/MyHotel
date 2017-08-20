package com.example.hieu.myhotel.Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hieu.myhotel.Modules.hotel;
import com.example.hieu.myhotel.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Hieu on 24/07/2017.
 */

public class FragmentAddHotel extends Fragment {

    private ImageView imgHotel;
    private EditText edtID;
    private EditText edtName;
    private EditText edtAddress;
    private EditText edtCost;
    private EditText edtDetail;
    private Button btnAdd , btnChooseHotel, btnUpdate, btnDelete;
    private Spinner spinnerCity, spinnerDisstrict;
    private Dialog dialog;

    private hotel myHotel;
    private String id;
    private String urlImg;
    private String name;
    private String district;
    private String city;
    private String address;
    private int cost;
    private String detail;

    String arrCity[] =new String[]{"Hà Nội", "Hồ Chí Minh", "Khánh Hòa", "Quảng Ninh"};
    String HaNoi[] = new String[]{"Hoàn Kiếm", "Cầu Giấy", "Đống Đa", "Thanh Xuân", "Hai Bà Trưng", "Long Biên", "Hà Đông"};
    String HCM[] = new String[]{"Quận 1","Quận 2","Quận 3","Quận 4","Quận 5","Quận 6","Quận 7","Quận 8","Quận 9","Quận 10"};
    String QuangNinh[]=new String[]{"Hạ Long", "Móng Cái", "Quảng Yên","Cẩm Phả", "Cô Tô", "Hải Hà"};
    String KhanhHoa[]=new String[]{"Nha Trang", "Cam Ranh", "Trường Sa", "Ninh Hòa"};
    int REQUEST_CODE_IMG_CHOOSE=5;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference mDB= FirebaseDatabase.getInstance().getReference();
    final StorageReference storageRef = storage.getReferenceFromUrl("gs://myhotel-874af.appspot.com");
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_add_hotel,container,false);
        Mapping(root);
        getData();
        chooseImg();
        saveDB();
        UpdateDB();
        DeleteDB();
        return root;
    }

    private void getData(){
        Bundle bundle = getArguments();
        if(bundle == null){
            btnAdd.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.INVISIBLE);
        }else {
            btnAdd.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.VISIBLE);
            id = bundle.getString("id");
            name = bundle.getString("name");
            city = bundle.getString("city");
            district = bundle.getString("district");
            address = bundle.getString("address");
            cost = bundle.getInt("cost");
            detail = bundle.getString("detail");
            urlImg = bundle.getString("url");
            Glide.with(this).load(urlImg).into(imgHotel);
            edtID.setText(id);
            edtName.setText(name);
            edtCost.setText(cost+"");
            edtAddress.setText(address);
            edtDetail.setText(detail);
            edtID.setEnabled(false);
        }
    }

    private void UpdateDB(){
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("Sửa thành công");
                FragmentHotelManager frg = new FragmentHotelManager();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.myFrame, frg).commit();
            }
        });
    }

    private void DeleteDB(){
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("Xóa thành công");
                FragmentHotelManager frg = new FragmentHotelManager();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.myFrame, frg).commit();
            }
        });
    }

    private void getDataFromInterface(){
        try{
            id = edtID.getText().toString();
            name = edtName.getText().toString();
            address = edtAddress.getText().toString();
            city = spinnerCity.getSelectedItem().toString();
            district = spinnerDisstrict.getSelectedItem().toString();
            try {
                cost = Integer.parseInt(edtCost.getText().toString());
            } catch (NumberFormatException e) {
                showDialog("Giá phòng là số tự nhiên");
            }
            detail = edtDetail.getText().toString();
        } catch (Exception e) {
            showDialog("Không bỏ trống dữ liệu");
        }
    }

    private void saveDB() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getDataFromInterface();
                // Get the data from an ImageView as bytes
                Calendar calendar = Calendar.getInstance();
                StorageReference mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");

                imgHotel.setDrawingCacheEnabled(true);
                imgHotel.buildDrawingCache();
                Bitmap bitmap = imgHotel.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = mountainsRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(getActivity(), "Lỗi up ảnh", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(getActivity(), "Upload Thành công", Toast.LENGTH_SHORT).show();
                        getDataFromInterface();
                        urlImg = String.valueOf(downloadUrl);
                        myHotel = new hotel(id,urlImg,name, district,city, address,cost, detail);
                        mDB.child("Hotel").push().setValue(myHotel, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if(databaseError == null){
                                    edtID.setText("");
                                    edtName.setText("");
                                    edtAddress.setText("");
                                    edtCost.setText("");
                                    edtDetail.setText("");
                                    imgHotel.setImageResource(R.drawable.ksluxtery);
                                    Toast.makeText(getActivity(), "Lưu dữ liệu thành công", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getActivity(), "Lưu thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public void Mapping(View root){
        imgHotel = (ImageView) root.findViewById(R.id.imgHotel);
        edtID = (EditText) root.findViewById(R.id.edtID);
        edtName = (EditText) root.findViewById(R.id.edtNameHotel);
        edtAddress = (EditText) root.findViewById(R.id.edtAddress);
        edtCost = (EditText) root.findViewById(R.id.edtCost);
        edtDetail = (EditText) root.findViewById(R.id.edtDetail);
        btnAdd = (Button) root.findViewById(R.id.btnAdd);
        btnDelete = (Button) root.findViewById(R.id.btnDelete);
        btnUpdate = (Button) root.findViewById(R.id.btnUpdate);
        btnChooseHotel = (Button) root.findViewById(R.id.btnChooseHotel);
        spinnerCity = (Spinner) root.findViewById(R.id.spinnerCity);
        spinnerDisstrict = (Spinner) root.findViewById(R.id.spinnerDistrict);

        // Spinner
        ArrayAdapter<String> adapter= null;
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrCity);
        //phải gọi lệnh này để hiển thị danh sách cho Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        //Thiết lập adapter cho Spinner
        spinnerCity.setAdapter(adapter);
        //thiết lập sự kiện chọn phần tử cho Spinner
        spinnerCity.setOnItemSelectedListener(new MyProcessEvent());

        Glide.with(this)
                .load(R.drawable.ksluxtery)
                .into(imgHotel);
    }

    public void chooseImg(){
        btnChooseHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(intent, 0);
                startActivityForResult(intent, REQUEST_CODE_IMG_CHOOSE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_IMG_CHOOSE && resultCode == RESULT_OK && data!=null){
            Uri targetUri = data.getData();
            Glide.with(this)
                    .load(targetUri)
                    .into(imgHotel);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class MyProcessEvent implements AdapterView.OnItemSelectedListener {
        //Khi có chọn lựa thì vào hàm này
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            city = spinnerCity.getSelectedItem().toString();
            ArrayAdapter<String> adapter= null;
            String arr[] = null;
            if(city.equals("Hà Nội")) arr = HaNoi;
            else if(city.equals("Hồ Chí Minh")) arr=HCM;
            else if(city.equals("Khánh Hòa")) arr=KhanhHoa;
            else if(city.equals("Quảng Ninh")) arr=QuangNinh;
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, arr);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            spinnerDisstrict.setAdapter(adapter);
            spinnerDisstrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    district = spinnerDisstrict.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(getActivity(), "Chọn một huyện", Toast.LENGTH_SHORT).show();
                }
            });
        }
        //Nếu không chọn gì cả
        public void onNothingSelected(AdapterView<?> arg0) {
            Toast.makeText(getActivity(), "Chọn một quận", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(String s) {
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
}

