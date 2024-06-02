package lk.Ijse.Model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Data
public class Products {
      private String product_id;
      private String product_description;
      private int showRoom_qtyOnHand;
      private int product_unitPrice;

}
