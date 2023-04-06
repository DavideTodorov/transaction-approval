package com.example.transactionapprovalsvc.service;

import com.training.springcloud.kafkastreams.Transaction;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TransactionProcessor {

    private final StreamBridge streamBridge;
    private final Map<String, Long> accountsAmount;

    public TransactionProcessor(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
        accountsAmount = new HashMap<>();
    }

    public boolean processTransaction(Transaction.TransactionApproval transaction) {
        Transaction.TransactionDirection descriptor = transaction.getTransactionDirection();

        if (Transaction.TransactionDirection.DEBIT.getNumber() == descriptor.getNumber()) {
            processDebitTransaction(transaction);
        } else if (Transaction.TransactionDirection.CREDIT.getNumber() == descriptor.getNumber()) {
            processCreditTransaction(transaction);
        }
        return false;
    }

    private void processDebitTransaction(Transaction.TransactionApproval transaction) {
        String accountId = transaction.getAccountId();

        accountsAmount.putIfAbsent(accountId, 0l);
        Long oldAmount = accountsAmount.get(accountId);
        accountsAmount.put(accountId, oldAmount + transaction.getAmount());
    }

    private void processCreditTransaction(Transaction.TransactionApproval transaction) {
        String accountId = transaction.getAccountId();

        if (!accountsAmount.containsKey(accountId)) {
            return;
        }

        Long accountAmount = accountsAmount.get(accountId);

        long transactionAmount = transaction.getAmount();
        if (accountAmount < transactionAmount) {
            streamBridge.send("invalidTransactionProducer-out-0", transaction);
            return;
        }

        Long newAmount = accountAmount - transactionAmount;
        accountsAmount.put(accountId, newAmount);
        streamBridge.send("approvedTransactionProducer-out-0", transaction);
    }
}
