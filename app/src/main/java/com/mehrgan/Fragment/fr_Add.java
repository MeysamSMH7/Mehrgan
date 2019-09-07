package com.mehrgan.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mehrgan.Adapter.ViewPagerAdapter;
import com.mehrgan.Classes.ShowMessage;
import com.mehrgan.Classes.getDate;
import com.mehrgan.DataBase.DataSource.tb_BillsDataSource;
import com.mehrgan.DataBase.Tables.tb_Bills;
import com.mehrgan.R;

public class fr_Add extends Fragment {

    private EditText edtFrAdd_PK;
    private EditText edtFrAdd_FullName;
    private EditText edtFrAdd_Phone;
    private EditText edtFrAdd_DocName;
    private EditText edtFrAdd_RightDor;
    private EditText edtFrAdd_RightNazdik;
    private EditText edtFrAdd_LeftDoor;
    private EditText edtFrAdd_LeftNazdik;
    private EditText edtFrAdd_Dec;
    private Button btnFrAdd_Add;
    private LinearLayout linearFrAdd_Date;
    private LinearLayout layoutMain;
    private TextView txtFrAdd_Date;
    private TextView txtFrAdd_Date2;
    private String dateJalali = "";
    private String dateMiladi = "";

    public static fr_Add newInstance() {

        Bundle args = new Bundle();
        fr_Add fragment = new fr_Add();
        fragment.setArguments(args);
        args.putString("data", "data");
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_add, container, false);
        findViews(view);
        clicks();

        return view;
    }

    private void clicks() {
        linearFrAdd_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateClass();
            }
        });
        txtFrAdd_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateClass();
            }
        });
        txtFrAdd_Date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDateClass();
            }
        });

        btnFrAdd_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(edtFrAdd_PK.getText()) || TextUtils.isEmpty(edtFrAdd_FullName.getText()) ||
                        TextUtils.isEmpty(edtFrAdd_Phone.getText()) || dateJalali.equals("") || dateMiladi.equals("")) {
                    new ShowMessage(getContext()).ShowMessage_SnackBar(layoutMain, "لطفا تمام موردنظر را پر کنید");
                    return;
                }

                if (edtFrAdd_Phone.getText().toString().length() != 11){
                    new ShowMessage(getContext()).ShowMessage_SnackBar(layoutMain, "فیلد شماره تلفن باید 11 رقم باشد");
                    return;
                }

                addData();


            }
        });

    }

    private void clear() {

        edtFrAdd_PK.setText("");
        edtFrAdd_FullName.setText("");
        edtFrAdd_Phone.setText("");
        edtFrAdd_DocName.setText("");
        edtFrAdd_RightDor.setText("");
        edtFrAdd_RightNazdik.setText("");
        edtFrAdd_LeftDoor.setText("");
        edtFrAdd_LeftNazdik.setText("");
        edtFrAdd_Dec.setText("");
        txtFrAdd_Date.setText("انتخاب تاریخ");
        txtFrAdd_Date2.setText("");
        dateJalali = "";
        dateMiladi = "";
    }

    private void addData() {

        tb_Bills data = new tb_Bills();
        data.PK_Bill = edtFrAdd_PK.getText().toString();
        data.fullName = edtFrAdd_FullName.getText().toString();
        data.phoneNum = edtFrAdd_Phone.getText().toString();
        data.docName = edtFrAdd_DocName.getText().toString();
        data.rightNazdik = edtFrAdd_RightNazdik.getText().toString();
        data.rightDoor = edtFrAdd_RightDor.getText().toString();
        data.leftNazdik = edtFrAdd_LeftNazdik.getText().toString();
        data.leftDoor = edtFrAdd_LeftDoor.getText().toString();
        data.description = edtFrAdd_Dec.getText().toString();
        data.dateJalali = dateJalali;
        data.dateMiladi = dateMiladi;

        if (new tb_BillsDataSource(getContext()).Add(data) == -1)
            new ShowMessage(getContext()).ShowMessage_SnackBar(layoutMain, "اضافه کردن اطلاعات با مشکل مواجه شد!");
        else {
            new ShowMessage(getContext()).ShowMessage_SnackBar(layoutMain, "اطلاعات شما با موفقیت ثبت شد");
            clear();

            android.support.v4.view.ViewPager vp_viewPager = getActivity().findViewById(R.id.vp_viewPager);
            vp_viewPager.setCurrentItem(1);

            SharedPreferences sharedPreferences = getContext().getSharedPreferences("Mehrgan", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("onRefresh", true);
            editor.commit();

        }

    }


    private void getDateClass() {
        new getDate(getActivity().getFragmentManager()).getDate(new getDate.OnResponse() {
            @Override
            public void OnResponse(String persian, String miladi) {
                dateJalali = persian;
                dateMiladi = miladi;
                txtFrAdd_Date2.setText(dateJalali);
                txtFrAdd_Date.setText("تاریخ انتخاب شده:");
            }
        });

    }

    private void findViews(View view) {

        edtFrAdd_PK = view.findViewById(R.id.edtFrAdd_PK);
        edtFrAdd_FullName = view.findViewById(R.id.edtFrAdd_FullName);
        edtFrAdd_Phone = view.findViewById(R.id.edtFrAdd_Phone);
        edtFrAdd_DocName = view.findViewById(R.id.edtFrAdd_DocName);
        edtFrAdd_RightDor = view.findViewById(R.id.edtFrAdd_RightDor);
        edtFrAdd_RightNazdik = view.findViewById(R.id.edtFrAdd_RightNazdik);
        edtFrAdd_LeftDoor = view.findViewById(R.id.edtFrAdd_LeftDoor);
        edtFrAdd_LeftNazdik = view.findViewById(R.id.edtFrAdd_LeftNazdik);
        edtFrAdd_Dec = view.findViewById(R.id.edtFrAdd_Dec);
        btnFrAdd_Add = view.findViewById(R.id.btnFrAdd_Add);
        linearFrAdd_Date = view.findViewById(R.id.linearFrAdd_Date);
        layoutMain = view.findViewById(R.id.layoutMain);
        txtFrAdd_Date = view.findViewById(R.id.txtFrAdd_Date);
        txtFrAdd_Date2 = view.findViewById(R.id.txtFrAdd_Date2);

        btnFrAdd_Add.setTextColor(getContext().getResources().getColor(R.color.colorLogo));

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

}