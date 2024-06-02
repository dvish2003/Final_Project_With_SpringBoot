package lk.Ijse.Model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Data
public class ProductShowRoomJoin {
    public String showRoomId;
    private String productId;
    private String productDescription;
    private int productQuantityOnHand;
    private int productUnitPrice;
}
