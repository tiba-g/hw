package tbag.homework.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferDTO {

    private int issuerAccountId;
    private int beneficiaryAccountId;
    private double amount;

}
