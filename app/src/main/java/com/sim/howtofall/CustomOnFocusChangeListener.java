package com.sim.howtofall;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Sim
 */

public abstract class CustomOnFocusChangeListener implements OnFocusChangeListener {

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            EditText editText = (EditText)v;

            if (textBoxIsEmpty(editText)) {
                editText.setText("0");
            }

            try {
                updateModel(editText);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean textBoxIsEmpty(EditText editText) {
        return editText.getText().length() == 0;
    }


    abstract void updateModel(EditText editText)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;
}
