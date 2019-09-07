package com.mehrgan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mehrgan.Classes.CalendarTool;
import com.mehrgan.Classes.ShowMessage;
import com.mehrgan.Classes.getDate;
import com.mehrgan.DataBase.DataSource.tb_BillsDataSource;
import com.mehrgan.DataBase.Tables.tb_Bills;
import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.TimeZone;

public class Activity_Details_Mehrgan extends AppCompatActivity {

    private EditText edtAcDe_PK;
    private EditText edtAcDe_FullName;
    private EditText edtAcDe_Phone;
    private EditText edtAcDe_DocName;
    private EditText edtAcDe_RightDor;
    private EditText edtAcDe_RightNazdik;
    private EditText edtAcDe_LeftDoor;
    private EditText edtAcDe_LeftNazdik;
    private EditText edtAcDe_Dec;
    private Button btnAcDe_Add;
    private LinearLayout linearAcDe_Date;
    private LinearLayout layoutMain;
    private LinearLayout linearAcDe_Add;
    private TextView txtAcDe_Date;
    private TextView txtAcDe_Date2;
    private String dateJalali = "";
    private String dateMiladi = "";

    private Context context = this;
    private String status;
    private String PK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_mehrgan);
        findViews();
        status = getIntent().getStringExtra("status");
        PK = getIntent().getStringExtra("PK");

        if (status.equals("show"))
            setEnabled(false);
        else
            setEnabled(true);
        setDefault(PK);
        clicks();

// toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView txtAppbar = findViewById(R.id.txtAppbar);
        String temp01 = "مـهـرگـان";
        txtAppbar.setTextColor(Color.WHITE);
        txtAppbar.setText(temp01);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endActivity();
            }
        });


    }

    private void findViews() {

        edtAcDe_PK = findViewById(R.id.edtAcDe_PK);
        edtAcDe_FullName = findViewById(R.id.edtAcDe_FullName);
        edtAcDe_Phone = findViewById(R.id.edtAcDe_Phone);
        edtAcDe_DocName = findViewById(R.id.edtAcDe_DocName);
        edtAcDe_RightDor = findViewById(R.id.edtAcDe_RightDor);
        edtAcDe_RightNazdik = findViewById(R.id.edtAcDe_RightNazdik);
        edtAcDe_LeftDoor = findViewById(R.id.edtAcDe_LeftDoor);
        edtAcDe_LeftNazdik = findViewById(R.id.edtAcDe_LeftNazdik);
        edtAcDe_Dec = findViewById(R.id.edtAcDe_Dec);
        btnAcDe_Add = findViewById(R.id.btnAcDe_Add);
        linearAcDe_Date = findViewById(R.id.linearAcDe_Date);
        layoutMain = findViewById(R.id.layoutMain);
        txtAcDe_Date = findViewById(R.id.txtAcDe_Date);
        txtAcDe_Date2 = findViewById(R.id.txtAcDe_Date2);
        linearAcDe_Add = findViewById(R.id.linearAcDe_Add);

        btnAcDe_Add.setTextColor(context.getResources().getColor(R.color.colorLogo));
        linearAcDe_Add.setVisibility(View.GONE);

    }

    private void setEnabled(boolean set) {
        edtAcDe_PK.setEnabled(false);
        edtAcDe_FullName.setEnabled(set);
        edtAcDe_Phone.setEnabled(set);
        edtAcDe_DocName.setEnabled(set);
        edtAcDe_RightDor.setEnabled(set);
        edtAcDe_RightNazdik.setEnabled(set);
        edtAcDe_LeftDoor.setEnabled(set);
        edtAcDe_LeftNazdik.setEnabled(set);
        edtAcDe_Dec.setEnabled(set);
        btnAcDe_Add.setEnabled(set);
        linearAcDe_Date.setEnabled(set);
        layoutMain.setEnabled(set);
        txtAcDe_Date.setEnabled(set);
        txtAcDe_Date2.setEnabled(set);
        if (set)
            btnAcDe_Add.setVisibility(View.VISIBLE);
        else
            btnAcDe_Add.setVisibility(View.GONE);

    }

    private void setDefault(String pk) {

        tb_BillsDataSource tb_billsDataSource = new tb_BillsDataSource(context);
        tb_billsDataSource.Open();
        tb_Bills data = tb_billsDataSource.GetRecord(pk);
        tb_billsDataSource.Close();
        edtAcDe_PK.setText(data.PK_Bill);
        edtAcDe_PK.setEnabled(false);
        edtAcDe_FullName.setText(data.fullName);
        edtAcDe_Phone.setText(data.phoneNum);
        edtAcDe_DocName.setText(data.docName);
        edtAcDe_RightNazdik.setText(data.rightNazdik);
        edtAcDe_RightDor.setText(data.rightDoor);
        edtAcDe_LeftNazdik.setText(data.leftNazdik);
        edtAcDe_LeftDoor.setText(data.leftDoor);
        edtAcDe_Dec.setText(data.description);
        txtAcDe_Date2.setText(data.dateJalali);
        dateJalali = data.dateJalali;
        dateMiladi = data.dateMiladi;

    }

    private void clicks() {
        linearAcDe_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtAcDe_Date.isEnabled())
                    getDateClass();
            }
        });
        txtAcDe_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtAcDe_Date.isEnabled())
                    getDateClass();
            }
        });
        txtAcDe_Date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtAcDe_Date.isEnabled())
                    getDateClass();
            }
        });

        btnAcDe_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (linearAcDe_Add.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(edtAcDe_PK.getText()) || TextUtils.isEmpty(edtAcDe_FullName.getText()) ||
                            TextUtils.isEmpty(edtAcDe_Phone.getText()) || dateJalali.equals("") || dateMiladi.equals("")) {
                        new ShowMessage(context).ShowMessage_SnackBar(layoutMain, "لطفا تمام موردنظر را پر کنید");
                        return;
                    }
                    if (edtAcDe_Phone.getText().toString().length() != 11) {
                        new ShowMessage(context).ShowMessage_SnackBar(layoutMain, "فیلد شماره تلفن باید 11 رقم باشد");
                        return;
                    }
                    editData();
                }

            }
        });

    }

    private void editData() {

        tb_Bills data = new tb_Bills();
        data.PK_Bill = edtAcDe_PK.getText().toString();
        data.fullName = edtAcDe_FullName.getText().toString();
        data.phoneNum = edtAcDe_Phone.getText().toString();
        data.docName = edtAcDe_DocName.getText().toString();
        data.rightNazdik = edtAcDe_RightNazdik.getText().toString();
        data.rightDoor = edtAcDe_RightDor.getText().toString();
        data.leftNazdik = edtAcDe_LeftNazdik.getText().toString();
        data.leftDoor = edtAcDe_LeftDoor.getText().toString();
        data.description = edtAcDe_Dec.getText().toString();
        data.dateJalali = dateJalali;
        data.dateMiladi = dateMiladi;

        if (new tb_BillsDataSource(context).EditItems(data) == -1)
            new ShowMessage(context).ShowMessage_SnackBar(layoutMain, "ویرایش اطلاعات با مشکل مواجه شد!");
        else {
            new ShowMessage(context).ShowMessage_SnackBar(layoutMain, "اطلاعات شما با موفقیت ویرایش شد");
            SharedPreferences sharedPreferences = context.getSharedPreferences("Mehrgan", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("onRefresh", true);
            editor.commit();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);

        }

    }

    private void getDateClass() {
        new getDate(getFragmentManager()).getDate(new getDate.OnResponse() {
            @Override
            public void OnResponse(String persian, String miladi) {
                dateJalali = persian;
                dateMiladi = miladi;
                txtAcDe_Date2.setText(dateJalali);
                txtAcDe_Date.setText("تاریخ انتخاب شده:");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nvgEdit:
                status = "edit";
                setEnabled(true);
                linearAcDe_Add.setVisibility(View.VISIBLE);
                break;
            case R.id.nvgDelete:
                deleteItem(PK);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void deleteItem(final String id) {
        new AlertDialog.Builder(context)
                .setTitle("حذف این قبض")
                .setMessage("آیا مطمئن هستید؟")
                .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tb_BillsDataSource tb_billsDataSource = new tb_BillsDataSource(context);
                        tb_billsDataSource.Open();
                        tb_billsDataSource.DeleteById(id);
                        tb_billsDataSource.Close();
                        SharedPreferences sharedPreferences = getSharedPreferences("Mehrgan", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("onRefresh", true);
                        editor.commit();
                        finish();
                    }
                })
                .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        endActivity();
        super.onBackPressed();
    }

    private void endActivity() {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        anim.reset();
        LinearLayout layoutMain = findViewById(R.id.layoutMain);
        layoutMain.clearAnimation();
        layoutMain.startAnimation(anim);
        finish();

    }


}
