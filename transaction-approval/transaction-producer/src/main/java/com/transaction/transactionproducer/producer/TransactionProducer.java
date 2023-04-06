package com.transaction.transactionproducer.producer;

import com.training.springcloud.kafkastreams.Request;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Supplier;

@Service
public class TransactionProducer {

    @Bean
    public Supplier<Request.TransactionApproval> transactionSupplier() {
        Request.TransactionApproval transactions = createTransactions();
        return () -> transactions;
    }

    public Request.TransactionApproval createTransactions() {
        String id = UUID.randomUUID().toString();
        long amount = RandomUtils.nextLong(0, 1000000000);
        String accountId = UUID.randomUUID().toString();
        int transactionDirectionIndex = RandomUtils.nextInt(0, 2);
        Request.TransactionDirection transactionDirection = Request.TransactionDirection.values()[transactionDirectionIndex];

        Request.TransactionApproval transactionApproval = Request.TransactionApproval
                .newBuilder()
                .setId(id)
                .setAmount(amount)
                .setAccountId(accountId)
                .setTransactionDirection(transactionDirection)
                .build();

        return transactionApproval;
    }
}
