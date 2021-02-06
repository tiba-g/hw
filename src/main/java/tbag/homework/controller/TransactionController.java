package tbag.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tbag.homework.exception.InvalidActionException;
import tbag.homework.model.dto.CashDTO;
import tbag.homework.service.TransactionService;

import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(value = "/deposit")
    public double deposit(@RequestBody CashDTO cashDTO) throws InvalidActionException {
        return transactionService.deposit(cashDTO);
    }

    @PostMapping(value = "/withdraw")
    public double withdraw(@RequestBody CashDTO cashDTO) throws InvalidActionException {
        return transactionService.withdraw(cashDTO);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public void onNoSuchElementException(NoSuchElementException e) {
        log.error("Object does not exist. Message: {}, Exception: {}", e.getMessage(), e);
        throw e;
    }

    @ExceptionHandler(InvalidActionException.class)
    public void onNoSuchElementException(InvalidActionException e) throws InvalidActionException {
        log.error("Requested action cannot be performed. Message: {}, Exception: {}", e.getMessage(), e);
        throw e;
    }

}
