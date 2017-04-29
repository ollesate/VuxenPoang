package com.sjoholm.olof.vuxenpoang.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.model.Expense;
import com.sjoholm.olof.vuxenpoang.utils.NumberUtils;

public class EditDialog extends AppCompatDialogFragment implements View.OnClickListener{
    private static final String ARG_EXPENSE = "EXPENSE";
    private CreateDialog.Listener listener;
    private EditText editTextName;
    private EditText editTextCost;

    public static EditDialog newInstance(Expense expense) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXPENSE, expense);
        EditDialog fragment = new EditDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.create_new_expense, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Expense expense = (Expense) getArguments().getSerializable(ARG_EXPENSE);

        if (expense == null) {
            throw new IllegalStateException("Need to have args " + ARG_EXPENSE);
        }

        editTextName = (EditText) view.findViewById(R.id.edt_name);
        editTextName.setText(expense.name);
        editTextCost = (EditText) view.findViewById(R.id.edt_cost);
        editTextCost.setText(String.valueOf(expense.cost));

        view.findViewById(R.id.btn_ok).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (listener == null) {
            throw new IllegalStateException("No listener");
        }
        String name = editTextName.getText().toString();
        String costStr = editTextCost.getText().toString();
        int cost = NumberUtils.valueOfInt(costStr, -1);

        if (name.length() == 0) {
            editTextName.setError("Name missing");
        } else if (cost == -1) {
            editTextCost.setError("Cost not valid");
        } else {
            listener.onAccept(new Expense(name, cost));
            dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fill the layout in the window
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        }
    }

    public void setOnAcceptListener(CreateDialog.Listener listener) {
        this.listener = listener;
    }
}
