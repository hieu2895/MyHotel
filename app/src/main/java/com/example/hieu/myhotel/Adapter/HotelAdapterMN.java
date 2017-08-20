package com.example.hieu.myhotel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hieu.myhotel.Modules.hotel;
import com.example.hieu.myhotel.R;

import java.util.ArrayList;

/**
 * Created by Hieu on 25/07/2017.
 */

public class HotelAdapterMN extends BaseAdapter {
    Context myContext;
    int myLayout;
    ArrayList<hotel> arrayHotel ;

    public HotelAdapterMN(Context myContext, int myLayout, ArrayList<hotel> arrayHotel) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.arrayHotel = arrayHotel;
    }

    public HotelAdapterMN() {
    }

    @Override
    public int getCount() {
        return arrayHotel.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayHotel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imgHotel ;
        TextView txtName,txtAddress, txtCost;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View rowView = convertView;
        ViewHolder holder = new ViewHolder();

        if(convertView == null){
            convertView = inflater.inflate(myLayout,null);
            holder.imgHotel = (ImageView) convertView.findViewById(R.id.imgHotelItem);
            holder.txtName = (TextView) convertView.findViewById(R.id.tvNameItem);
            holder.txtAddress = (TextView) convertView.findViewById(R.id.tvAddressItem);
            holder.txtCost = (TextView) convertView.findViewById(R.id.tvCostItem);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtName.setText(arrayHotel.get(position).getName());
        holder.txtAddress.setText(arrayHotel.get(position).getCity()+" - "+arrayHotel.get(position).getDistrict()+
        " - "+arrayHotel.get(position).getAddress());
        holder.txtCost.setText(arrayHotel.get(position).getCost()+"");
        Glide.with(myContext).load(arrayHotel.get(position).getImgHotel()).into(holder.imgHotel);
        return convertView;
    }
}
