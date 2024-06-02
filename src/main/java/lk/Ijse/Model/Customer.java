package lk.Ijse.Model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Data
public class Customer {
    private String id;
    private String Name;
    private String Address;
    private String Contact;
    private String email;


    public Customer(String cuId) {
        this.id = cuId;
    }
}
