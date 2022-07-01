package com.example.recetapp.Listeners;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.example.recetapp.R;

public class LoadingDialog {

    Context context;
    Dialog dialog;

    public LoadingDialog(Context context) {
        this.context = context;
    }

    public void ShowDialog(String title){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView titleTextView = dialog.findViewById(R.id.dialog_title);
        titleTextView.setText(title);

        dialog.create();
        dialog.show();
    }

    public void HideDialog(){
        dialog.dismiss();
    }
}
