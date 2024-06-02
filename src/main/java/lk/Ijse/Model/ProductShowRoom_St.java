package lk.Ijse.Model;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Data
public class ProductShowRoom_St {
    private Products products;
    private List<Product_ShowRoom> psList;
}
