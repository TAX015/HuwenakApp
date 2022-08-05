package com.itats.huwenakapp.ui.laporan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.itats.huwenakapp.R;
import com.itats.huwenakapp.koneksi.Servers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DayFragment extends Fragment {
    int year, month, day;
    TextView txtDayDate;
    ImageButton btnDayDate;

    public DayFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_daytab, container, false);
        txtDayDate = root.findViewById(R.id.txt_daydate);
        btnDayDate = root.findViewById(R.id.btn_daydate);

        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        txtDayDate.setText("Tgl : " + day + " " + Servers.MONTH_LIST[month] + " " + year);

        btnDayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        txtDayDate.setText("Tgl : " + day + " " + Servers.MONTH_LIST[month] + " " + year);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });

        return root;
    }
}
