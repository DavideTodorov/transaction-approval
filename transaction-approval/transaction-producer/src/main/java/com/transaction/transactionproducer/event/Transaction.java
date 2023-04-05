package com.transaction.transactionproducer.event;


public class Transaction {
    private String id;
    private long amount;
    private String accountId;

    private TransactionDirection transactionDirection;

    public Transaction(String id, long amount, String accountId, TransactionDirection transactionDirection) {
        this.id = id;
        this.amount = amount;
        this.accountId = accountId;
        this.transactionDirection = transactionDirection;
    }

    public String getId() {
        return id;
    }

    public long getAmount() {
        return amount;
    }

    public String getAccountId() {
        return accountId;
    }

    public TransactionDirection getTransactionDirection() {
        return transactionDirection;
    }
}
