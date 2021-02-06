package tbag.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import tbag.homework.model.dto.CashDTO;
import tbag.homework.service.TransactionService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(value = "/deposit")
    public double deposit(@RequestBody CashDTO cashDTO)  {
        return transactionService.deposit(cashDTO);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public void onNoSuchElementException(NoSuchElementException e) {
        log.error("Object does not exist. Message: {}, Exception: {}", e.getMessage(), e);
        throw e;
    }

}
