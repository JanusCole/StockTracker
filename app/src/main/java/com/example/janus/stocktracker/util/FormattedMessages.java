package com.example.janus.stocktracker.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.janus.stocktracker.R;
import com.example.janus.stocktracker.data.stockquotes.StockQuote;
import com.example.janus.stocktracker.stockquote.StockQuoteActivity;
import com.example.janus.stocktracker.stocksearch.StockSearchContract;

import java.io.Serializable;

// This is a utility class for making nicely formatted messages

public class FormattedMessages {

    public static void displayErrorMessageAlertDialog(String alertMessage, LayoutInflater layoutInflater, Context context) {

        View dialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(dialogView);

        TextView alertDialogMessage = (TextView) dialogView.findViewById(R.id.messageTextView_AlertDialog);
        alertDialogMessage.setText(alertMessage);

        final AlertDialog errorMessageAlertDialog = alertDialogBuilder.create();
        errorMessageAlertDialog.setCanceledOnTouchOutside(true);

        Button dialogButton = (Button) dialogView.findViewById(R.id.okButton_AlertDialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorMessageAlertDialog.dismiss();
            }
        });

        errorMessageAlertDialog.show();
    }


    // Method for setting up the network busy message
    public static AlertDialog getNetworkActivityAlert(LayoutInflater inflater, Context context) {

        View dialogView = inflater.inflate(R.layout.busy_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setView(dialogView);

        return alertDialogBuilder.create();

    }

}
