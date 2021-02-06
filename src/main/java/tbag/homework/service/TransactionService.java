package tbag.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tbag.homework.exception.InvalidActionException;
import tbag.homework.model.Account;
import tbag.homework.model.History;
import tbag.homework.model.User;
import tbag.homework.model.dto.CashDTO;
import tbag.homework.model.dto.TransferDTO;
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
    public double deposit(CashDTO cashDTO) throws InvalidActionException {
        Account account = accountRepository.findById(cashDTO.getAccountId()).orElseThrow(() -> new InvalidActionException("Account not found."));

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

    @Transactional
    public double withdraw(CashDTO cashDTO) throws InvalidActionException {
        Account account = accountRepository.findById(cashDTO.getAccountId()).orElseThrow(() -> new InvalidActionException("Account not found."));

        if (account.getBalance() < cashDTO.getAmount()) {
            throw new InvalidActionException("Insufficient funds");
        }

        account.setBalance(account.getBalance() - cashDTO.getAmount());

        History history = History.builder()
                .amount(cashDTO.getAmount())
                .date(LocalDateTime.now())
                .historyType(HistoryType.WITHDRAWAL)
                .transactionType(TransactionType.EXTERNAL)
                .issuerAccount(account)
                .build();

        accountRepository.saveAndFlush(account);
        historyRepository.saveAndFlush(history);

        return account.getBalance();
    }

    @Transactional
    public double transfer(TransferDTO transferDTO) throws InvalidActionException {
        Account issuerAccount = accountRepository.findById(transferDTO.getIssuerAccountId())
                .orElseThrow(() -> new InvalidActionException("Issuer account not found."));
        Account beneficiaryAccount = accountRepository.findById(transferDTO.getBeneficiaryAccountId())
                .orElseThrow(() -> new InvalidActionException("Beneficiary account not found."));

        if (issuerAccount.getBalance() < transferDTO.getAmount()) {
            throw new InvalidActionException("Insufficient funds on issuer account");
        }

        issuerAccount.setBalance(issuerAccount.getBalance() - transferDTO.getAmount());
        beneficiaryAccount.setBalance(beneficiaryAccount.getBalance() + transferDTO.getAmount());

        LocalDateTime now = LocalDateTime.now();

        History issuerHistory = History.builder()
                .amount(transferDTO.getAmount())
                .date(now)
                .historyType(HistoryType.WITHDRAWAL)
                .transactionType(TransactionType.INTERNAL)
                .issuerAccount(issuerAccount)
                .beneficiaryAccount(beneficiaryAccount)
                .build();

        historyRepository.saveAndFlush(issuerHistory);

        History beneficiaryHistory = History.builder()
                .amount(transferDTO.getAmount())
                .date(now)
                .historyType(HistoryType.DEPOSIT)
                .transactionType(TransactionType.INTERNAL)
                .issuerAccount(issuerAccount)
                .beneficiaryAccount(beneficiaryAccount)
                .build();

        historyRepository.saveAndFlush(issuerHistory);
        historyRepository.saveAndFlush(beneficiaryHistory);

        accountRepository.saveAndFlush(issuerAccount);
        accountRepository.saveAndFlush(beneficiaryAccount);

        return issuerAccount.getBalance();
    }

}
