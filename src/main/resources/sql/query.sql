CREATE DATABASE KD_Aircon;

USE KD_Aircon;

CREATE TABLE Register (
                          Register_id VARCHAR(10) PRIMARY KEY,
                          Register_Name VARCHAR(50) NOT NULL,
                          Register_post VARCHAR(50) NOT NULL,
                          Register_Password VARCHAR(30) NOT NULL
);

INSERT INTO Register VALUES('R001','Vishan','Manager','Vishan2003###');


CREATE TABLE Customer (
                          Customer_id VARCHAR(10) PRIMARY KEY,
                          Customer_Name VARCHAR(50) NOT NULL,
                          Customer_Address VARCHAR(50) NOT NULL,
                          Customer_Contact VARCHAR(15) NOT NULL,
                          Customer_Email VARCHAR(50) UNIQUE
);

CREATE TABLE Employee (
                          Employee_id VARCHAR(10) PRIMARY KEY,
                          Employee_Name VARCHAR(50) NOT NULL,
                          Employee_Age Varchar(3) NOT NULL,
                          Employee_Address VARCHAR(50) NOT NULL,
                          Employee_Contact Varchar(15) NOT NULL,
                          Employee_Email VARCHAR(50) UNIQUE
);

CREATE TABLE Location (
                          Customer_id VARCHAR(10),
                          Location_id VARCHAR(10) PRIMARY KEY,
                          Location_Province VARCHAR(50) NOT NULL,
                          Location_City VARCHAR(50) NOT NULL,
                          Location_Address VARCHAR(50) NOT NULL,
                          Location_ZipCode VARCHAR(50) NOT NULL,
                          FOREIGN KEY (Customer_id) REFERENCES Customer(Customer_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE Payment (
                         Payment_id VARCHAR(10) PRIMARY KEY,
                         Payment_Amount INT(50) NOT NULL,
                         Payment_Date DATE NOT NULL
);

CREATE TABLE Booking (
                         Booking_id VARCHAR(10) PRIMARY KEY,
                         Employee_id VARCHAR(10) NOT NULL,
                         Location_id VARCHAR(10) NOT NULL,
                         Payment_id VARCHAR(10) NOT NULL,
                         Booking_Date DATE NOT NULL,
                         Place_Date DATE NOT NULL,
                         Booking_Description VARCHAR(100),
                         FOREIGN KEY (Employee_id) REFERENCES Employee(Employee_id)ON UPDATE CASCADE ON DELETE CASCADE,
                         FOREIGN KEY (Location_id) REFERENCES Location(Location_id)ON UPDATE CASCADE ON DELETE CASCADE,
                         FOREIGN KEY (Payment_id) REFERENCES Payment(Payment_id)ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE ShowRoom (
                          ShowRoom_id VARCHAR(10) PRIMARY KEY,
                          ShowRoom_Location VARCHAR(50) NOT NULL

);

CREATE TABLE Product (
                         Product_id VARCHAR(10) PRIMARY KEY,
                         Product_Description VARCHAR(50) NOT NULL,
                         ShowRoom_QtyOnHand INT(50) NOT NULL,
                         Product_UnitPrice INT(50) NOT NULL
);

CREATE TABLE Product_ShowRoom (
                                  Product_id VARCHAR(10),
                                  ShowRoom_id VARCHAR(10),
                                  FOREIGN KEY (Product_id) REFERENCES Product(Product_id) ON UPDATE CASCADE ON DELETE CASCADE,
                                  FOREIGN KEY (ShowRoom_id) REFERENCES ShowRoom(ShowRoom_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE `Order` (
                         Order_id VARCHAR(10) PRIMARY KEY,
                         Customer_id VARCHAR(10) NOT NULL,
                         Payment_id VARCHAR(10) NOT NULL,
                         OrderPlaceDate_Date DATE NOT NULL,
                         FOREIGN KEY (Customer_id) REFERENCES Customer(Customer_id) ON UPDATE CASCADE ON DELETE CASCADE,
                         FOREIGN KEY (Payment_id) REFERENCES Payment(Payment_id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE OrderDetails (
                              Product_id VARCHAR(10),
                              Order_id VARCHAR(10),
                              Qty INT(50) NOT NULL,
                              Product_UnitPrice INT(50) NOT NULL,
                              FOREIGN KEY (Product_id) REFERENCES Product(Product_id) ON UPDATE CASCADE ON DELETE CASCADE,
                              FOREIGN KEY (Order_id) REFERENCES `Order`(Order_id) ON UPDATE CASCADE ON DELETE CASCADE
);


Join Query Booking

SELECT
    Booking.Booking_id,
    Employee.Employee_Name,
    Customer.Customer_Name,
    Location.Location_Address,
    Payment.Payment_Amount,
    Booking.Booking_Date
FROM
    Booking
        JOIN
    Employee ON Booking.Employee_id = Employee.Employee_id
        JOIN
    Location ON Booking.Location_id = Location.Location_id
        JOIN
    Payment ON Booking.Payment_id = Payment.Payment_id
        JOIN
    Customer ON Location.Customer_id = Customer.Customer_id;



SELECT o.Order_id, c.Customer_Name, p.Product_Description, od.Qty, od.Product_UnitPrice, py.Payment_Amount
FROM `Order` o
         JOIN Customer c ON o.Customer_id = c.Customer_id
         JOIN OrderDetails od ON o.Order_id = od.Order_id
         JOIN Product p ON od.Product_id = p.Product_id
         JOIN Payment py ON o.Payment_id = py.Payment_id;


SELECT o.Order_id, c.Customer_Name, p.Product_Description, od.Qty, od.Product_UnitPrice, py.Payment_Amount
FROM `Order` o
         JOIN Customer c ON o.Customer_id = c.Customer_id
         JOIN OrderDetails od ON o.Order_id = od.Order_id
         JOIN Product p ON od.Product_id = p.Product_id
         JOIN Payment py ON o.Payment_id = py.Payment_id
WHERE o.Order_id = 'your_order_id_here';


SELECT
    C.Customer_Name,
    L.Location_Address,
    B.Booking_Date
FROM
    Booking B
        INNER JOIN
    Location L ON B.Location_id = L.Location_id
        INNER JOIN
    Customer C ON L.Customer_id = C.Customer_id
WHERE
    B.Booking_Date =  ? ;





INSERT INTO Booking VALUES
                        ('B004', 'E004', 'L004', 'P007', '2024-05-04', '2024-04-28', 'Booking 4 Description'),
                        ('B005', 'E005', 'L005', 'P008', '2024-05-05', '2024-04-29', 'Booking 5 Description'),
                        ('B006', 'E006', 'L006', 'P009', '2024-05-06', '2024-04-30', 'Booking 6 Description'),
                        ('B007', 'E007', 'L007', 'P010', '2024-05-07', '2024-05-01', 'Booking 7 Description'),
                        ('B008', 'E008', 'L008', 'P011', '2024-05-08', '2024-05-02', 'Booking 8 Description'),
                        ('B009', 'E009', 'L009', 'P012', '2024-05-09', '2024-05-03', 'Booking 9 Description'),
                        ('B010', 'E010', 'L010', 'P013', '2024-05-10', '2024-05-04', 'Booking 10 Description');
