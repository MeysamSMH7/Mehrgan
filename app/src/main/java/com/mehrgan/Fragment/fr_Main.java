package com.mehrgan.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mehrgan.Adapter.recyclerAdapter;
import com.mehrgan.DataBase.DataSource.tb_BillsDataSource;
import com.mehrgan.DataBase.Tables.tb_Bills;
import com.mehrgan.R;

import java.util.ArrayList;
import java.util.List;


public class fr_Main extends Fragment {


    private SwipeRefreshLayout refreshFr_Main;
    private RecyclerView recyclFr_Main;
    private TextView txtFrMain_NoData;


    public static fr_Main newInstance() {

        Bundle args = new Bundle();
        fr_Main fragment = new fr_Main();
        fragment.setArguments(args);
        args.putString("data", "data");
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_main, container, false);
        checkForRefresh();
        findViews(view);
        setList();

        refreshFr_Main.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        return view;
    }

    private void setList() {
        List<tb_Bills> data = new ArrayList<>();
        tb_BillsDataSource tb_billsDataSource = new tb_BillsDataSource(getContext());
        tb_billsDataSource.Open();
        data = tb_billsDataSource.GetList();
        tb_billsDataSource.Close();

        if (data.size() == 0){
            txtFrMain_NoData.setVisibility(View.VISIBLE);
            recyclFr_Main.setVisibility(View.GONE);
        }else {
            txtFrMain_NoData.setVisibility(View.GONE);
            recyclFr_Main.setVisibility(View.VISIBLE);
            recyclerAdapter adapter = new recyclerAdapter(getContext(), data);
            recyclFr_Main.setAdapter(adapter);
        }
    }


    private void findViews(View view) {
        recyclFr_Main = view.findViewById(R.id.recyclFr_Main);
        refreshFr_Main = view.findViewById(R.id.refreshFr_Main);
        txtFrMain_NoData = view.findViewById(R.id.txtFrMain_NoData);
    }

    private void refreshData() {

        refreshFr_Main.setColorSchemeResources(
                R.color.colorPurple6,
                R.color.colorPink,
                R.color.colorLogo,
                R.color.colorIndigo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setList();
                refreshFr_Main.setRefreshing(false);
            }
        }, 2000);

    }


    private void checkForRefresh() {


        final SharedPreferences preferences = getContext().getSharedPreferences("Mehrgan", 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean restoredText = preferences.getBoolean("onRefresh", false);
                if (restoredText) {
                    refreshFr_Main.setRefreshing(true);
                    refreshData();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("onRefresh", false);
                    editor.commit();
                }
                checkForRefresh();
            }
        }, 1000);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


}