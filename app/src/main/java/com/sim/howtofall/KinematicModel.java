package com.sim.howtofall;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;

/**
 * @author Sim
 */
@SuppressWarnings("deprecation")
public class KinematicModel extends BaseObservable {
    private final double METRIC_A = 9.80665;
    private final double IMPERIAL_A = 32.174;
    private final String METRIC_D_UNITS = "m";
    private final String IMPERIAL_D_UNITS = "ft";
    private final String METRIC_V_UNITS = "m/s";
    private final String IMPERIAL_V_UNITS = "ft/s";
    private final String ACCELERATION_SQUARE = "<sup><small>2</small></sup>";
    private final double METRIC_2_IMPERIAL = 3.2808398950131;
    private final double IMPERIAL_2_METRIC = 0.3048;

    private Component lastUpdated = Component.DISPLACEMENT;

    private double d = 0;
    private double t = 0;
    private double a = METRIC_A;
    private double vi = 0;
    private double vf = 0;
    private String v_units = METRIC_V_UNITS;
    private String d_units = METRIC_D_UNITS;
    private Spanned accelerationUnits = createAccelerationUnits(METRIC_V_UNITS);

    @Bindable
    public double getD() {
        return d;
    }

    public void setD(double value) {
        if (value != d) {
            d = value;
            lastUpdated = Component.DISPLACEMENT;
            calculateVFfromD();
            calculateT();
        }
    }

    @Bindable
    public double getT() {
        return t;
    }

    public void setT(double value) {
        if (value != t) {
            t = value;
            lastUpdated = Component.TIME;
            calculateVFfromT();
            calculateD();
        }
    }

    @Bindable
    public double getA() {
        return a;
    }

    private void setA(double value) {
        a = value;
        notifyPropertyChanged(BR.a);
        updateFromChange();
    }

    @Bindable
    public double getVi() {
        return vi;
    }

    public void setVi(double value) {
        if (value != vi) {
            vi = value;

            if (lastUpdated == Component.DISPLACEMENT) {
                calculateVFfromD();
                calculateT();
            } else {
                calculateVFfromT();
                calculateD();
            }
        }
    }

    @Bindable
    public double getVf() {
        return vf;
    }

    @Bindable
    public String getD_units() {
        return d_units;
    }

    private void setD_units(String value) {
        d_units = value;
        notifyPropertyChanged(BR.d_units);
    }

    @Bindable
    public String getV_units() {
        return v_units;
    }

    @Bindable
    public Spanned getAccelerationUnits() {
        return accelerationUnits;
    }

    private Spanned createAccelerationUnits(String value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(value+ACCELERATION_SQUARE, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(value+ACCELERATION_SQUARE);
        }
    }

    @BindingAdapter("android:text")
    public static void setText(EditText view, double oldValue, double newValue) {

        if (oldValue != newValue) {
            view.setText(String.valueOf(newValue));
        } else if (view.getText().length() == 0 && oldValue == 0.0) {
            view.setText(String.valueOf(newValue));
        }
    }

    @InverseBindingAdapter(attribute = "android:text")
    public static double getValue(EditText view) {
        try {
            return Double.parseDouble(view.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public void setMetric(View view) {
        updateFromUnitChange(METRIC_D_UNITS, METRIC_V_UNITS, IMPERIAL_2_METRIC);
        setA(METRIC_A);
    }

    public void setImperial(View view) {
        updateFromUnitChange(IMPERIAL_D_UNITS, IMPERIAL_V_UNITS, METRIC_2_IMPERIAL);
        setA(IMPERIAL_A);
    }

    private void updateFromUnitChange(String d_units, String v_units, double conversion) {
        setV_units(v_units);
        setVi(vi * conversion);
        notifyPropertyChanged(BR.vi);
        setD_units(d_units);

        if (lastUpdated == Component.DISPLACEMENT) {
            d = d * conversion;
            notifyPropertyChanged(BR.d);
        }
    }

    private void updateFromChange() {
        if (lastUpdated == Component.DISPLACEMENT) {
            calculateVFfromD();
        } else {
            calculateVFfromT();
            calculateD();
        }
    }

    private void setV_units(String value) {
        v_units = value;
        accelerationUnits = createAccelerationUnits(value);
        notifyPropertyChanged(BR.v_units);
        notifyPropertyChanged(BR.accelerationUnits);
    }

    private void calculateVFfromT() {
        vf = vi + a * t;
        notifyPropertyChanged(BR.vf);
    }

    private void calculateVFfromD() {
        vf = Math.sqrt(Math.pow(vi, 2) + 2 * a * d);
        notifyPropertyChanged(BR.vf);
    }

    private void calculateD() {
        d = ((vi + vf) / 2) * t;
        notifyPropertyChanged(BR.d);
    }

    private void calculateT() {
        t = (2 / (vi + vf)) * d;
        notifyPropertyChanged(BR.t);
    }
}
