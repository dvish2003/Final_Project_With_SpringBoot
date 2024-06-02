package lk.Ijse.Model;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Data

public class Booking {
    private String bookingId;
    private String empId;
    private String LocId;
    private String paymentId;
    private Date bookingDate;
    private Date PlaceDate;
    private String bookingDescription;


}
