package vn.ClothingStore.domain.payment;

import lombok.Getter;
import lombok.Setter;
import vn.ClothingStore.domain.request.order.ReqOrderDTO;

@Getter
@Setter
public class reqVnpayDTO {
    private String amount;
    private ReqOrderDTO orderRequest;
}
