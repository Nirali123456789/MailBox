package com.aiemail.superemail.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aiemail.superemail.R;
import com.aiemail.superemail.Models.Email;
import com.aiemail.superemail.utilis.Helpers;

public class CustomListViewDialog extends Dialog implements View.OnClickListener {

    Email obj;

    public CustomListViewDialog(Activity context, Email type) {
        super(context);
        this.obj = type;
        this.activity=context;
    }


    public Activity activity;
    public Dialog dialog;
    public TextView yes;
    TextView title;
    RelativeLayout important;
    RelativeLayout spam;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_move);
        yes = (TextView) findViewById(R.id.yes);
        important = findViewById(R.id.folder_imp);
        spam = findViewById(R.id.folder_spam);
        title = findViewById(R.id.title);
        yes.setOnClickListener(this);
        important.setOnClickListener(this);
        spam.setOnClickListener(this);
        onClick(yes );


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes:
                dismiss();
                break;
            case R.id.folder_imp:
                dismiss();
                Helpers.MoveFolder(activity, obj.getMsgid(), "IMPORTANT");
                break;
            case R.id.folder_spam:
                dismiss();
                Helpers.MoveFolder(activity, obj.getMsgid(), "SPAM");
                break;

            default:
                break;
        }
        dismiss();
    }
}