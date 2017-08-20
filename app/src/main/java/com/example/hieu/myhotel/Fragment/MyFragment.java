package com.example.hieu.myhotel.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hieu.myhotel.R;

/**
 * Created by Hieu on 23/7/2017.
 */
public class MyFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_layout,container,false);
        TextView frgName=(TextView)root.findViewById(R.id.frgName);
        String name=getArguments().getString("FragmentName");
        frgName.setText(name);
        return root;
    }
}
