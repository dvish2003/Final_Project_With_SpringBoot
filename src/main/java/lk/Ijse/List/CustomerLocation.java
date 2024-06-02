package lk.Ijse.List;

import lk.Ijse.Model.Customer;
import lk.Ijse.Model.Location;

public class CustomerLocation {
    private Customer customer;
    private Location location;

    public CustomerLocation() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public CustomerLocation(Customer customer, Location location) {
        this.customer = customer;
        this.location = location;

    }
}
