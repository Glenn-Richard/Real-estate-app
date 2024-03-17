package com.example.realestatemanager.viewmodel;


import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SimulatorViewModel extends ViewModel {
    private final MutableLiveData<String> monthlyPayment = new MutableLiveData<>();

    @SuppressLint("DefaultLocale")
    public void calculateMonthlyPayment(double loanAmount, double interestRate, int loanTerm, double downPayment) {
        loanAmount -= downPayment;
        double monthlyInterestRate = interestRate / 1200;
        int totalMonths = loanTerm * 12;

        double monthlyPaymentValue = (loanAmount * monthlyInterestRate) /
                (1 - Math.pow(1 + monthlyInterestRate, -totalMonths));

        monthlyPayment.setValue(String.format("%.2f", monthlyPaymentValue));
    }


    public LiveData<String> getMonthlyPayment() {
        return monthlyPayment;
    }
}