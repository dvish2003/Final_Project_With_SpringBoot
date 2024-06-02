package lk.Ijse.Model;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString

public class Order {
     private String orderId;
     private String customerId;
     private String payment_Id;
     private Date OrderPlaceDate;



}
