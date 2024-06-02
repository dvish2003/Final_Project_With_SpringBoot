package lk.Ijse.Model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Data
public class OrderDetail {
    private String productId;
    private String orderId;
    private int quantity;
    private int UnitPrice;

}
