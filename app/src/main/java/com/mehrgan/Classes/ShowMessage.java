package com.mehrgan.Classes;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.mehrgan.R;


public class ShowMessage {

    private Context context;

    public ShowMessage(Context context) {
        this.context = context;
    }


    public void ShowMessage_SnackBar(Object layout, String text) {

        Snackbar snackbar = Snackbar.make((View) layout, text, Snackbar.LENGTH_LONG).setAction("بستن", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View sb = snackbar.getView();
        TextView tv = sb.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        sb.setBackgroundColor(context.getResources().getColor(R.color.colorLogo));
        snackbar.show();
    }



}
