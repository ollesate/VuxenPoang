package com.sjoholm.olof.vuxenpoang.ui.expense;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.sjoholm.olof.vuxenpoang.R;
import com.sjoholm.olof.vuxenpoang.model.Expense;
import com.sjoholm.olof.vuxenpoang.utils.NumberUtils;

public class ExpenseDialog extends AppCompatDialogFragment implements View.OnClickListener{
    private static final String ARG_INITIAL_COST = "ARG_INITIAL_COST";
    private static final String ARG_INITIAL_NAME = "ARG_INITIAL_NAME";
    private static final String ARG_TITLE = "ARG_TITLE";
    private ExpenseDialogListener listener;
    private EditText editTextName;
    private EditText editTextCost;

    public interface ExpenseDialogListener {

        void onAccept(@NonNull Expense expense);
    }

    public static ExpenseDialog newInstance(int title, @NonNull String name, int cost) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_INITIAL_NAME, name);
        args.putSerializable(ARG_INITIAL_COST, cost);
        args.putInt(ARG_TITLE, title);
        ExpenseDialog fragment = new ExpenseDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static ExpenseDialog newInstance(int title) {
        ExpenseDialog fragment = new ExpenseDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expense_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextName = (EditText) view.findViewById(R.id.edt_name);
        editTextCost = (EditText) view.findViewById(R.id.edt_cost);

        String name = getArguments().getString(ARG_INITIAL_NAME);
        int cost = getArguments().getInt(ARG_INITIAL_COST);
        editTextName.setText(name);
        editTextCost.setText(String.valueOf(cost));

        int title = getArguments().getInt(ARG_TITLE);
        ((TextView) view.findViewById(R.id.title)).setText(title);

        view.findViewById(R.id.btn_ok).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setOnAcceptListener(ExpenseDialogListener listener) {
        this.listener = listener;
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
        } else if (cost <= 0) {
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
}
