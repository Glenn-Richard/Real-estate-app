package com.example.realestatemanager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.realestatemanager.viewmodel.SimulatorViewModel;

public class SimulatorActivity extends AppCompatActivity {

    private SimulatorViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulator);

        initToolBar();

        viewModel = new ViewModelProvider(this).get(SimulatorViewModel.class);

        EditText editTextDownPayment = findViewById(R.id.editTextDownPayment);
        EditText editTextLoanAmount = findViewById(R.id.editTextLoanAmount);
        EditText editTextInterestRate = findViewById(R.id.editTextInterestRate);
        EditText editTextLoanTerm = findViewById(R.id.editTextLoanTerm);
        TextView textViewMonthlyPayment = findViewById(R.id.textViewMonthlyPayment);
        Button buttonCalculate = findViewById(R.id.buttonCalculate);

        buttonCalculate.setOnClickListener(v -> {
            String loanAmountStr = editTextLoanAmount.getText().toString();
            String interestRateStr = editTextInterestRate.getText().toString();
            String loanTermStr = editTextLoanTerm.getText().toString();
            String downPaymentStr = editTextDownPayment.getText().toString();

            if (!loanAmountStr.isEmpty() && !interestRateStr.isEmpty() && !loanTermStr.isEmpty()) {
                try {
                    double loanAmount = Double.parseDouble(loanAmountStr);
                    double interestRate = Double.parseDouble(interestRateStr);
                    int loanTerm = Integer.parseInt(loanTermStr);
                    double downPayment = Double.parseDouble(downPaymentStr);

                    viewModel.calculateMonthlyPayment(loanAmount, interestRate, loanTerm,downPayment);
                } catch (NumberFormatException e) {
                    textViewMonthlyPayment.setText(getString(R.string.input_error));
                }
            } else {
                textViewMonthlyPayment.setText(getString(R.string.input_error));
            }
        });

        viewModel.getMonthlyPayment().observe(this, payment -> {
            String formattedPayment = getString(R.string.monthly_payment, payment);
            textViewMonthlyPayment.setText(formattedPayment);
        });

    }

    private void initToolBar() {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            getSupportActionBar().setTitle("Loan Simulator");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}