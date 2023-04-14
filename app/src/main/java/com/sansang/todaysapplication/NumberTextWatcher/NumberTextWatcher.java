package com.sansang.todaysapplication.NumberTextWatcher;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.core.net.ParseException;

import java.text.DecimalFormat;

public class NumberTextWatcher implements TextWatcher {
    private final DecimalFormat df;
    private final DecimalFormat dfnd;
    private boolean hasFractionalPart;

    private final EditText et;

    public NumberTextWatcher( EditText et ) {
        df = new DecimalFormat("#,###.##");
        df.setDecimalSeparatorAlwaysShown(true);
        dfnd = new DecimalFormat("#,###");
        this.et = et;
        hasFractionalPart = false;
    }

    private static final String TAG = "NumberTextWatcher";

    @Override
    public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

    }

    @Override
    public void onTextChanged( CharSequence s, int start, int before, int count ) {
        if (s.toString().contains(String.valueOf(df.getDecimalFormatSymbols().getDecimalSeparator()))) {
            hasFractionalPart = true;
        } else {
            hasFractionalPart = false;
        }
    }

    @Override
    public void afterTextChanged( Editable s ) {
        et.removeTextChangedListener(this);

        try {
            int inilen, endlen;
            inilen = et.getText().length();

            String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "");
            Number n = null;
            try {
                n = df.parse(v);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

            int cp = et.getSelectionStart();
            if (hasFractionalPart) {
                et.setText(df.format(n));
            } else {
                et.setText(dfnd.format(n));
            }
            endlen = et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                // place cursor at the end?
                et.setSelection(et.getText().length() - 1);
            }

        } catch (NumberFormatException nfe) {
            // do nothing?
        } catch (ParseException e) {
            // do nothing?
        }

        et.addTextChangedListener(this);

    }
}
