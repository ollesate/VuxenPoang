package com.sjoholm.olof.vuxenpoang.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.model.Expense;

public class CreateDialog extends AppCompatDialogFragment {

    private Listener listener;

    public interface Listener {

        void onAccept(Expense item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_new_expense, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText edtName = (EditText) view.findViewById(R.id.edt_name);
        final EditText edtCost = (EditText) view.findViewById(R.id.edt_cost);

        view.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    String name = edtName.getText().toString();
                    if (name.length() == 0) {
                        edtName.setError("Name missing");
                        return;
                    }
                    int cost;
                    try {
                        cost = Integer.valueOf(edtCost.getText().toString());
                    } catch (NumberFormatException ignored) {
                        edtCost.setError("Cost not valid");
                        return;
                    }
                    listener.onAccept(new Expense(name, cost));
                }
                dismiss();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fill window
        getDialog().getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
    }

    public void setOnAcceptListener(Listener listener) {
        this.listener = listener;
    }
}
