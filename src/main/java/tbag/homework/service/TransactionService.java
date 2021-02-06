package tbag.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tbag.homework.model.Account;
import tbag.homework.model.History;
import tbag.homework.model.User;
import tbag.homework.model.dto.CashDTO;
import tbag.homework.model.enums.HistoryType;
import tbag.homework.model.enums.TransactionType;
import tbag.homework.repository.AccountRepository;
import tbag.homework.repository.HistoryRepository;
import tbag.homework.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final HistoryRepository historyRepository;

    @Transactional
    public double deposit(CashDTO cashDTO) {
        Account account = accountRepository.findById(cashDTO.getAccountId()).orElseThrow();

        account.setBalance(account.getBalance() + cashDTO.getAmount());
        History history = History.builder()
                .amount(cashDTO.getAmount())
                .date(LocalDateTime.now())
                .historyType(HistoryType.DEPOSIT)
                .transactionType(TransactionType.EXTERNAL)
                .issuerAccount(account)
                .beneficiaryAccount(account)
                .build();

        accountRepository.saveAndFlush(account);
        historyRepository.saveAndFlush(history);

        return account.getBalance();
    }

}
