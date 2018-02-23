package com.sim.howtofall;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sim.howtofall.databinding.ActivityMainBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    final KinematicModel kinematicModel = new KinematicModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setModel(kinematicModel);
        setEditTextFocusChangeListerners(binding.getRoot());
    }

    private void setEditTextFocusChangeListerners(View rootView) {
        setEditTextFocusChangeListener((EditText)rootView.findViewById(R.id.editText_d), "setD");
        setEditTextFocusChangeListener((EditText)rootView.findViewById(R.id.editText_t), "setT");
        setEditTextFocusChangeListener((EditText)rootView.findViewById(R.id.editText_vi), "setVi");
    }

    private void setEditTextFocusChangeListener(EditText editText, final String toSet) {
        editText.setOnFocusChangeListener(new CustomOnFocusChangeListener() {
            @Override
            void updateModel(EditText editText)
                    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
                Method m = KinematicModel.class.getDeclaredMethod(toSet, double.class);
                double value = Double.parseDouble(editText.getText().toString());
                m.invoke(kinematicModel, value);
            }
        });

    }
}
