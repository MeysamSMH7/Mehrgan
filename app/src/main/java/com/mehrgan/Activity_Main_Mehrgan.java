package com.mehrgan;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mehrgan.Adapter.ViewPagerAdapter;
import com.mehrgan.Classes.ExcelToSQLite;
import com.mehrgan.Classes.SQLiteToExcel;
import com.mehrgan.Classes.ShowMessage;
import com.mehrgan.DataBase.DataSource.tb_BillsDataSource;
import com.mehrgan.DataBase.DatabaseManagement;
import com.mehrgan.DataBase.Structure.tb_BillsStructure;
import com.mehrgan.Fragment.fr_Add;
import com.mehrgan.Fragment.fr_Main;
import com.mehrgan.Fragment.fr_Search;

import java.util.ArrayList;
import java.util.List;

public class Activity_Main_Mehrgan extends AppCompatActivity {

    private Context context = this;
    private Toolbar toolbar;
    private TabLayout tl_tabLayout;
    private android.support.v4.view.ViewPager vp_viewPager;
    private List<Fragment> fragments;
    private String[] titles;
    private static final int Time_Between_Two_Back = 2000;
    private long TimeBackPressed;
    private LinearLayout layoutMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mehrgan);
        findViews();
        setSupportActionBar(toolbar);
        setDefault();
        startActivity();
        initViewPager();
        changeTabsFont(tl_tabLayout);

        if (Build.VERSION.SDK_INT > 23)
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

    }

    protected void changeTabsFont(TabLayout tabLayout) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof AppCompatTextView) {
                    Typeface type = Typeface.createFromAsset(this.getAssets(), "font/vazir.ttf");
                    TextView viewChild = (TextView) tabViewChild;
                    viewChild.setTypeface(type);
                    viewChild.setAllCaps(false);
                }
            }
        }
    }

    private void setDefault() {

//      toolbar
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView txtAppbar = findViewById(R.id.txtAppbar);
        String temp01 = "مـهـرگـان";
        txtAppbar.setTextColor(Color.WHITE);
        txtAppbar.setText(temp01);

    }

    private void startActivity() {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        anim.reset();
        layoutMain.clearAnimation();
        layoutMain.startAnimation(anim);

    }

    private void findViews() {

        toolbar = findViewById(R.id.toolbar);
        layoutMain = findViewById(R.id.layoutMain);
        tl_tabLayout = findViewById(R.id.tl_tabLayout);
        vp_viewPager = findViewById(R.id.vp_viewPager);

    }

    private void initViewPager() {

        fragments = new ArrayList<>();

        fragments.add(fr_Search.newInstance());
        fragments.add(fr_Main.newInstance());
        fragments.add(fr_Add.newInstance());

        titles = new String[]{"جستجو", "اصلی", "جدید"};

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        vp_viewPager.setAdapter(adapter);
        tl_tabLayout.setupWithViewPager(vp_viewPager);
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        vp_viewPager.setOffscreenPageLimit(limit);
        vp_viewPager.setCurrentItem(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bottom_nav_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nvgImport:
                importExToDB();
                break;
            case R.id.nvgExport:
                exportDBToEx();

                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void exportDBToEx() {
        SQLiteToExcel sqLiteToExcel = new SQLiteToExcel(this, DatabaseManagement.databaseName);
        sqLiteToExcel.exportSingleTable(tb_BillsStructure.tableName, "merhrgan.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {
                new ShowMessage(context).ShowMessage_SnackBar(layoutMain,"درحال اجرا");
            }

            @Override
            public void onCompleted(String filePath) {
                new ShowMessage(context).ShowMessage_SnackBar(layoutMain,"فایل شما با موفقیت ذخیره شد\n" + filePath);
            }

            @Override
            public void onError(Exception e) {
                new ShowMessage(context).ShowMessage_SnackBar(layoutMain,"اضافه کردن فایل با شکست روبه رو شد!");
                e.printStackTrace();
            }
        });
    }

    private void importExToDB() {
        ExcelToSQLite excelToSQLite = new ExcelToSQLite(context, DatabaseManagement.databaseName, true);
        excelToSQLite.importFromFile(Environment.getExternalStorageDirectory().toString() + "/merhrgan.xls", new ExcelToSQLite.ImportListener() {
            @Override
            public void onStart() {
                new ShowMessage(context).ShowMessage_SnackBar(layoutMain,"درحال اجرا");
            }

            @Override
            public void onCompleted(String dbName) {
                tb_BillsDataSource tb_billsDataSource = new tb_BillsDataSource(context);
                tb_billsDataSource.Open();
                tb_billsDataSource.IraniToGery();
                tb_billsDataSource.Close();
                new ShowMessage(context).ShowMessage_SnackBar(layoutMain,"فایل مشا با موفقیت اضافه شد");
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(context, e+"", Toast.LENGTH_SHORT).show();
                new ShowMessage(context).ShowMessage_SnackBar(layoutMain,"اضافه کردن فایل با مشکل مواجه شد");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (TimeBackPressed + Time_Between_Two_Back > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else
            new ShowMessage(context).ShowMessage_SnackBar(layoutMain, "برای خروج دوباره کلیک کنید");

        TimeBackPressed = System.currentTimeMillis();
    }

}
