package com.oa.task2do;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Avishay on 18/02/14.
 */
public class AlertDialog extends DialogFragment {

    boolean editMode =false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());

        // set title
        alertDialogBuilder.setTitle("ALERT");

        // set dialog message
        alertDialogBuilder
                .setMessage("do you want to delete?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        editMode=true;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        editMode=false;
                        dialog.cancel();
                    }
                });

        // create alert dialog
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        //alertDialog.show();
        //return choice;
        return alertDialog;
    }


}