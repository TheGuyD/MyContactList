package il.theguyd.mycontactlist.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import il.theguyd.mycontactlist.R;

public class Utils {

    public static boolean isValidInput(@NonNull EditText e){
        int inputType = e.getInputType();
        boolean res = false;
        switch (inputType){
            case 1:
                res = isValidName(e.getText());
                break;
            case 3:
                res = isValidTelephoneNumber(e.getText());
                break;
            case 33:
                res = isValidEmail(e.getText());
                break;
            case 129:
                res = isValidPassword(e.getText());
                break;
            default:
                res = true;
        }
        return res;

    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidName(CharSequence target) {
        //TODO : implement name validation
        return true;
    }
    public static boolean isValidTelephoneNumber(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches());
    }

    public static boolean isValidPassword(CharSequence target) {
        //TODO : implement Password validation;
        return true;
    }

    public static TextWatcher getWatcherWithValidations(EditText edt, AppCompatActivity app) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isValidInput(edt)) {
                    edt.setTextColor(app.getResources().getColor(R.color.black));
                } else {
                    edt.setTextColor(app.getResources().getColor(R.color.error));
                }
            }
        };
    }


}
