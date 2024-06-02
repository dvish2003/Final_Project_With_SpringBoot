package lk.Ijse.Model;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartTm {
    private String P_ID;
    private String description;
    private int qty;
    private int unitPrice;
    private int total;
    private JFXButton btnRemove;
}
