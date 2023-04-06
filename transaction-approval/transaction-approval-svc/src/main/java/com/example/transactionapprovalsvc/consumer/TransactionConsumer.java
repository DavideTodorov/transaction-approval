package com.example.transactionapprovalsvc.consumer;

import com.example.transactionapprovalsvc.service.TransactionProcessor;
import com.training.springcloud.kafkastreams.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class TransactionConsumer {
    private final TransactionProcessor transactionProcessor;

    public TransactionConsumer(TransactionProcessor transactionProcessor) {
        this.transactionProcessor = transactionProcessor;
    }

    @Bean
    public Consumer<Transaction.TransactionApproval> transactionConsumer() {
        return transactionProcessor::processTransaction;
    }

}
