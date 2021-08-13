package ru.netology.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentInfoModel {
    String id;
    String amount;
    String created;
    String status;
    String transaction_id;
}
