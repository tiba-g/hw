package tbag.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tbag.homework.model.dto.CashDTO;

@Slf4j
@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    @PostMapping(value = "/deposit")
    public double deposit(@RequestBody CashDTO cashDTO)  {
        return -21;
    }

}
