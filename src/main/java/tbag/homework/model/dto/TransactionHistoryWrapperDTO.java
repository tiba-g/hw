package tbag.homework.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryWrapperDTO {

    private int accountId;
    private double currentBalance;
    private List<TransactionHistoryDTO> transactions;

}
