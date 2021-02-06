package tbag.homework.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tbag.homework.model.enums.HistoryType;
import tbag.homework.model.enums.TransactionType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionHistoryDTO {

    private double amount;
    private LocalDateTime date;
    private HistoryType historyType;
    private TransactionType transactionType;
    private String issuerName;
    private String beneficiaryName;

}
