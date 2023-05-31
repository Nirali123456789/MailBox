package com.aiemail.superemail.Bottomsheet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.aiemail.superemail.R;

public class ActionBottomDialogFragment extends BottomSheetDialogFragment
    implements View.OnClickListener {
  public static final String TAG = "ActionBottomDialog";
  private ItemClickListener mListener;
  int layout=0;
  int style;

  public ActionBottomDialogFragment(int layout) {
    this.layout=layout;

  }

  public static ActionBottomDialogFragment newInstance(int layout) {
    return new ActionBottomDialogFragment(layout);
  }
  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(layout, container, false);
  }
  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    view.findViewById(R.id.textView).setOnClickListener(this);
    //view.findViewById(R.id.textView4).setOnClickListener(this);
    //view.findViewById(R.id.textView3).setOnClickListener(this);
//    view.findViewById(R.id.textView4).setOnClickListener(this);
  }
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ItemClickListener) {
      mListener = (ItemClickListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement ItemClickListener");
    }
  }

  @Override
  public int getTheme() {
    return R.style.BottomSheetDialogcustom;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);


    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override
      public void onShow(DialogInterface dialog) {
        Bottomsheetsetup(dialog);

      }
    });
    {

      return dialog;

    }
  }
  private BottomSheetBehavior sheetBehavior;
  public void Bottomsheetsetup(DialogInterface dialogInterface)
  {
    BottomSheetDialog bottomSheetDialog=(BottomSheetDialog) dialogInterface;

    View bottomsheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
    bottomsheet.setBackgroundColor(Color.TRANSPARENT);
    sheetBehavior = BottomSheetBehavior.from(bottomsheet);
    ViewGroup.LayoutParams layoutParams = bottomsheet.getLayoutParams();

    int windowHeight = getWindowHeight();
//    if (layoutParams != null) {
//      layoutParams.height = windowHeight;
//    }
    bottomsheet.setLayoutParams(layoutParams);
    sheetBehavior.setFitToContents(false);
    sheetBehavior.setPeekHeight(1950);
    //sheetBehavior.setf(5);

  }



  private int getWindowHeight() {
    // Calculate window height for fullscreen use
    DisplayMetrics displayMetrics = new DisplayMetrics();
    ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    return displayMetrics.heightPixels;
  }
  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }
  @Override public void onClick(View view) {
    TextView tvSelected = (TextView) view;
    mListener.onItemClick(tvSelected.getText().toString());
    dismiss();
  }
  public interface ItemClickListener {
    void onItemClick(String item);
  }
}