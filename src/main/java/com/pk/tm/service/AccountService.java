package com.pk.tm.service;

import com.pk.tm.entity.Account;
import com.pk.tm.exception.AccountInactiveException;
import com.pk.tm.exception.InsufficientBalanceException;
import com.pk.tm.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Transactional
    public void createAccount(Account account) {
        accountRepository.save(account);
    }

    //transfer money declarative
    @Transactional
    public void transferMoneyDeclarative(long fromAccountId, long toAccountId, double amount) {
        logger.info("Initiating transfer of amount {} from account {} to account {}", amount, fromAccountId, toAccountId);
        Account fromAccount = accountRepository.findById(fromAccountId);
        Account toAccount = accountRepository.findById(toAccountId);

        if (!fromAccount.isActive() || !toAccount.isActive()) {
            logger.error("One or both accounts are inactive. From Account Active: {}, To Account Active: {}", fromAccount.isActive(), toAccount.isActive());
            throw new AccountInactiveException("One or both accounts are inactive.");
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            logger.error("Insufficient balance in from account {}: {} < {}", fromAccountId, fromAccount.getBalance(), amount);
            throw new InsufficientBalanceException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        accountRepository.save(fromAccount);

        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(toAccount);

        logger.info("Successfully transferred {} from account {} to account {}", amount, fromAccountId, toAccountId);
    }

    public void transferMoneyProgrammatic(long fromAccountId, long toAccountId, double amount) {
        logger.info("Initiating transfer of amount {} from account {} to account {}", amount, fromAccountId, toAccountId);
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = platformTransactionManager.getTransaction(definition);

        try {
            Account fromAccount = accountRepository.findById(fromAccountId);
            Account toAccount = accountRepository.findById(toAccountId);

            if (!fromAccount.isActive() || !toAccount.isActive()) {
                logger.error("One or both accounts are inactive. From Account Active: {}, To Account Active: {}", fromAccount.isActive(), toAccount.isActive());
                throw new AccountInactiveException("One or both accounts are inactive.");
            }

            if (fromAccount.getBalance().compareTo(amount) < 0) {
                logger.error("Insufficient balance in from account {}: {} < {}", fromAccountId, fromAccount.getBalance(), amount);
                throw new InsufficientBalanceException("Insufficient balance");
            }
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            accountRepository.save(fromAccount);

            toAccount.setBalance(toAccount.getBalance() + amount);
            accountRepository.save(toAccount);
            logger.info("Successfully transferred {} from account {} to account {}", amount, fromAccountId, toAccountId);
            platformTransactionManager.commit(status);
        } catch (Exception e) {
            platformTransactionManager.rollback(status);
            throw e;
        }
    }
}
