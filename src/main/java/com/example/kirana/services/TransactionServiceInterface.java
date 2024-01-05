package com.example.kirana.services;

import com.example.kirana.entities.Transaction;
import com.example.kirana.schemas.RecordTransactionRequest;

public interface TransactionServiceInterface {
    Transaction recordTransaction(RecordTransactionRequest transaction);
}