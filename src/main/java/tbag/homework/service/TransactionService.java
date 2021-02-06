package tbag.homework.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tbag.homework.exception.InvalidActionException;
import tbag.homework.model.Account;
import tbag.homework.model.History;
import tbag.homework.model.dto.CashDTO;
import tbag.homework.model.dto.TransactionHistoryDTO;
import tbag.homework.model.dto.TransactionHistoryWrapperDTO;
import tbag.homework.model.dto.TransferDTO;
import tbag.homework.model.enums.HistoryType;
import tbag.homework.model.enums.TransactionType;
import tbag.homework.repository.AccountRepository;
import tbag.homework.repository.HistoryRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.time.format.*;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

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

    @Transactional
    public TransactionHistoryWrapperDTO getHistory(int accountId, HistoryType historyType) throws InvalidActionException {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new InvalidActionException("Account not found."));
        List<HistoryType> historyTypes = createFilter(historyType);
        List<History> histories = historyRepository.selectByIssuerAccountIdAndHistoryType(accountId, historyTypes);
        List<TransactionHistoryDTO> historyDTOS = convertHistories(histories);

        return new TransactionHistoryWrapperDTO(account.getId(), account.getBalance(), historyDTOS);
    }

    private List<TransactionHistoryDTO> convertHistories(List<History> histories) {
        List<TransactionHistoryDTO> result = new ArrayList<>();
        for (History history : histories) {
            result.add(convertHistory(history));
        }

        return result;
    }

    private TransactionHistoryDTO convertHistory(History history) {
        return TransactionHistoryDTO.builder()
                .amount(history.getAmount())
                .date(history.getDate())
                .historyType(history.getHistoryType())
                .transactionType(history.getTransactionType())
                .issuerName(history.getIssuerAccount().getOwner().getName())
                .beneficiaryName(history.getBeneficiaryAccount() != null ? history.getBeneficiaryAccount().getOwner().getName() : null)
                .build();
    }

    private List<HistoryType> createFilter(HistoryType historyType) {
        List<HistoryType> historyTypes = new ArrayList<>();
        if (historyType == null) {
            historyTypes.add(HistoryType.DEPOSIT);
            historyTypes.add(HistoryType.WITHDRAWAL);
        } else {
            historyTypes.add(historyType);
        }

        return historyTypes;
    }

}
