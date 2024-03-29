package com.mehrgan.Fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mehrgan.Adapter.recyclerAdapter;
import com.mehrgan.Classes.ShowMessage;
import com.mehrgan.Classes.getDate;
import com.mehrgan.DataBase.DataSource.tb_BillsDataSource;
import com.mehrgan.DataBase.Structure.tb_BillsStructure;
import com.mehrgan.DataBase.Tables.tb_Bills;
import com.mehrgan.R;

import java.util.ArrayList;
import java.util.List;


public class fr_Search extends Fragment {

    private EditText edtFrSearch_PK;
    private EditText edtFrSearch_FullName;
    private EditText edtFrSearch_Phone;
    private RadioGroup radioGroupFrSearch;
    private LinearLayout linearFrSearch_DateMulti;
    private TextView txtFrSearch_DateSMulti_Start;
    private TextView txtFrSearch_DateSMulti_Start2;
    private TextView txtFrSearch_DateSMulti_End;
    private TextView txtFrSearch_DateSMulti_End2;
    private LinearLayout linearFrSearch_DateSingle;
    private RelativeLayout relatFrSearch;
    private TextView txtFrSearch_DateSingle;
    private TextView txtFrSearch_Date2Single;
    private ScrollView scrollViewSearch;
    private FloatingActionButton btnSearch;
    private RecyclerView recyclFr_Search;
    private RelativeLayout layoutMain;
    private LinearLayout linearSearch;
    private TextView txtFrSearch_NoData;
    private ImageView imgDoSearch_v4_1;
    private ImageView imgDoSearch_v4_2;

    private String dateMiladi = "";
    private String dateMiladiStart = "";
    private String dateMiladiEnd = "";

    private List<tb_Bills> list;

    public static fr_Search newInstance() {

        Bundle args = new Bundle();
        fr_Search fragment = new fr_Search();
        fragment.setArguments(args);
        args.putString("data", "data");
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_search, container, false);
        findViews(view);
        clicks();

        return view;
    }

    private void clicks() {
        clickDates();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scrollViewSearch.getVisibility() == View.VISIBLE)
                    doSearch();
                else {
                    scrollViewSearch.setVisibility(View.VISIBLE);
                    linearSearch.setVisibility(View.GONE);
                    circularRevealActivity(scrollViewSearch);
                }
            }
        });

        imgDoSearch_v4_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scrollViewSearch.getVisibility() == View.VISIBLE)
                    doSearch();
                else {
                    scrollViewSearch.setVisibility(View.VISIBLE);
                    linearSearch.setVisibility(View.GONE);
                    circularRevealActivity(scrollViewSearch);
                }
            }
        });
        imgDoSearch_v4_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scrollViewSearch.getVisibility() == View.VISIBLE)
                    doSearch();
                else {
                    scrollViewSearch.setVisibility(View.VISIBLE);
                    linearSearch.setVisibility(View.GONE);
                    circularRevealActivity(scrollViewSearch);
                }
            }
        });

        radioGroupFrSearch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                switch (selectedId) {
                    case R.id.radioFrSearch_DateMulti:
                        linearFrSearch_DateMulti.setVisibility(View.VISIBLE);
                        linearFrSearch_DateSingle.setVisibility(View.GONE);
                        dateMiladi = "";
                        txtFrSearch_DateSingle.setText("انتخاب تاریخ");
                        txtFrSearch_Date2Single.setText("");

                        break;
                    case R.id.radioFrSearch_DateSingle:
                        linearFrSearch_DateMulti.setVisibility(View.GONE);
                        linearFrSearch_DateSingle.setVisibility(View.VISIBLE);
                        dateMiladiStart = "";
                        dateMiladiEnd = "";
                        txtFrSearch_DateSMulti_Start.setText("تاریخ شروع");
                        txtFrSearch_DateSMulti_Start2.setText("");
                        txtFrSearch_DateSMulti_End.setText("تاریخ پایان");
                        txtFrSearch_DateSMulti_End2.setText("");
                        break;
                }
            }
        });


    }

    private void doSearch() {

        if (TextUtils.isEmpty(edtFrSearch_PK.getText()) && TextUtils.isEmpty(edtFrSearch_FullName.getText()) &&
                TextUtils.isEmpty(edtFrSearch_Phone.getText()) && dateMiladi.equals("") &&
                dateMiladiStart.equals("") && dateMiladiEnd.equals("") && dateMiladiStart.equals("") && !dateMiladiEnd.equals("")) {
            new ShowMessage(getContext()).ShowMessage_SnackBar(layoutMain, "لطفا یک فیلد را پر کنید");
            return;
        }

        if (edtFrSearch_Phone.getText().toString().length() != 11 && !edtFrSearch_Phone.getText().toString().equals("")) {
            new ShowMessage(getContext()).ShowMessage_SnackBar(layoutMain, "فیلد شماره تلفن باید 11 رقم باشد");
            return;
        }

        ArrayList arrayList = new ArrayList();

        if (!(TextUtils.isEmpty(edtFrSearch_PK.getText()))) {
            arrayList.add(tb_BillsStructure.PK_Bill + " = '" + edtFrSearch_PK.getText().toString() + "'");
        }
        if (!(TextUtils.isEmpty(edtFrSearch_FullName.getText()))) {
            arrayList.add(tb_BillsStructure.fullName + " LIKE '%" + edtFrSearch_FullName.getText().toString() + "%'");
        }
        if (!(TextUtils.isEmpty(edtFrSearch_Phone.getText()))) {
            arrayList.add(tb_BillsStructure.phoneNum + " = '" + edtFrSearch_Phone.getText().toString() + "'");
        }
        if (!dateMiladi.equals("")) {
            arrayList.add(tb_BillsStructure.dateMiladi + " = '" + dateMiladi + "'");
        }
        if (!dateMiladiStart.equals("") && !dateMiladiEnd.equals("")) {
            arrayList.add(tb_BillsStructure.dateMiladi + " BETWEEN '" + dateMiladiStart + "' AND '" + dateMiladiEnd + "'");
        }

        if (arrayList.size() == 0) {
            new ShowMessage(getContext()).ShowMessage_SnackBar(layoutMain, "لطفا یک فیلد را پر کنید");
            return;
        }

        list = new tb_BillsDataSource(getContext()).Search(arrayList);
        recyclerAdapter adapter = new recyclerAdapter(getContext(), list);
        recyclFr_Search.setAdapter(adapter);
        scrollViewSearch.setVisibility(View.GONE);
        linearSearch.setVisibility(View.VISIBLE);
        if (list.size() == 0) {
            txtFrSearch_NoData.setVisibility(View.VISIBLE);
            recyclFr_Search.setVisibility(View.GONE);
        } else {
            txtFrSearch_NoData.setVisibility(View.GONE);
            recyclFr_Search.setVisibility(View.VISIBLE);
        }
        circularRevealActivity(linearSearch);
    }

    private void clickDates() {
        txtFrSearch_DateSMulti_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getDate(getActivity().getFragmentManager()).getDate(new getDate.OnResponse() {
                    @Override
                    public void OnResponse(String persian, String miladi) {
                        dateMiladiStart = miladi;
                        txtFrSearch_DateSMulti_Start.setText("تاریخ انتخاب شده:");
                        txtFrSearch_DateSMulti_Start2.setText(persian);
                    }
                });
            }
        });

        txtFrSearch_DateSMulti_Start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getDate(getActivity().getFragmentManager()).getDate(new getDate.OnResponse() {
                    @Override
                    public void OnResponse(String persian, String miladi) {
                        dateMiladiStart = miladi;
                        txtFrSearch_DateSMulti_Start.setText("تاریخ انتخاب شده:");
                        txtFrSearch_DateSMulti_Start2.setText(persian);
                    }
                });
            }
        });


        txtFrSearch_DateSMulti_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getDate(getActivity().getFragmentManager()).getDate(new getDate.OnResponse() {
                    @Override
                    public void OnResponse(String persian, String miladi) {
                        dateMiladiEnd = miladi;
                        txtFrSearch_DateSMulti_End.setText("تاریخ انتخاب شده:");
                        txtFrSearch_DateSMulti_End2.setText(persian);
                    }
                });
            }
        });
        txtFrSearch_DateSMulti_End2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getDate(getActivity().getFragmentManager()).getDate(new getDate.OnResponse() {
                    @Override
                    public void OnResponse(String persian, String miladi) {
                        dateMiladiEnd = miladi;
                        txtFrSearch_DateSMulti_End.setText("تاریخ انتخاب شده:");
                        txtFrSearch_DateSMulti_End2.setText(persian);
                    }
                });
            }
        });

        txtFrSearch_DateSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getDate(getActivity().getFragmentManager()).getDate(new getDate.OnResponse() {
                    @Override
                    public void OnResponse(String persian, String miladi) {
                        dateMiladi = miladi;
                        txtFrSearch_DateSingle.setText("تاریخ انتخاب شده:");
                        txtFrSearch_Date2Single.setText(persian);
                    }
                });
            }
        });
        txtFrSearch_Date2Single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getDate(getActivity().getFragmentManager()).getDate(new getDate.OnResponse() {
                    @Override
                    public void OnResponse(String persian, String miladi) {
                        dateMiladi = miladi;
                        txtFrSearch_DateSingle.setText("تاریخ انتخاب شده:");
                        txtFrSearch_Date2Single.setText(persian);
                    }
                });
            }
        });

    }

    private void findViews(View view) {

        edtFrSearch_PK = view.findViewById(R.id.edtFrSearch_PK);
        edtFrSearch_FullName = view.findViewById(R.id.edtFrSearch_FullName);
        edtFrSearch_Phone = view.findViewById(R.id.edtFrSearch_Phone);
        radioGroupFrSearch = view.findViewById(R.id.radioGroupFrSearch);
        linearFrSearch_DateMulti = view.findViewById(R.id.linearFrSearch_DateMulti);
        txtFrSearch_DateSMulti_Start = view.findViewById(R.id.txtFrSearch_DateSMulti_Start);
        txtFrSearch_DateSMulti_Start2 = view.findViewById(R.id.txtFrSearch_DateSMulti_Start2);
        txtFrSearch_DateSMulti_End = view.findViewById(R.id.txtFrSearch_DateSMulti_End);
        txtFrSearch_DateSMulti_End2 = view.findViewById(R.id.txtFrSearch_DateSMulti_End2);
        linearFrSearch_DateSingle = view.findViewById(R.id.linearFrSearch_DateSingle);
        txtFrSearch_DateSingle = view.findViewById(R.id.txtFrSearch_DateSingle);
        txtFrSearch_Date2Single = view.findViewById(R.id.txtFrSearch_Date2Single);
        scrollViewSearch = view.findViewById(R.id.scrollViewSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        recyclFr_Search = view.findViewById(R.id.recyclFr_Search);
        layoutMain = view.findViewById(R.id.layoutMain);
        linearSearch = view.findViewById(R.id.linearSearch);
        txtFrSearch_NoData = view.findViewById(R.id.txtFrSearch_NoData);
        relatFrSearch = view.findViewById(R.id.relatFrSearch);
        imgDoSearch_v4_1 = view.findViewById(R.id.imgDoSearch_v4_1);
        imgDoSearch_v4_2 = view.findViewById(R.id.imgDoSearch_v4_2);

        scrollViewSearch.setVisibility(View.VISIBLE);
        linearSearch.setVisibility(View.GONE);

        linearFrSearch_DateMulti.setVisibility(View.VISIBLE);
        linearFrSearch_DateSingle.setVisibility(View.GONE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            relatFrSearch.setVisibility(View.GONE);
            btnSearch.setVisibility(View.VISIBLE);
        } else {
            relatFrSearch.setVisibility(View.VISIBLE);
            btnSearch.setVisibility(View.GONE);
        }

        list = new ArrayList<>();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void circularRevealActivity(View view) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int cx = view.getRight();
            int cy = view.getBottom();
            float finalRadius = Math.max(view.getWidth(), view.getHeight());
            Animator circularReveal = null;
            circularReveal = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
            circularReveal.setDuration(500);
            circularReveal.start();
        }

        view.setVisibility(View.VISIBLE);
    }

}