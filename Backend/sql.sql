insert into _role (id, name) VALUES (1,'admin'), (2,'user'),(3,'broker');

select * from _role;

select * from _user;
insert into _user(id, active,full_name,phone_number, address,password, date_of_birth, facebook_account_id , google_account_id, role_id)
VALUES (1,true,'John Doe', '0329123456', '123 Main St, Hanoi', 'password1', '1990-05-15', 1, 1, 2),
       (2,true,'Jane Smith', '0329765432', '456 Elm St, Ho Chi Minh City', 'password2', '1985-07-20', 2, 2, 3),
       (3,true,'Alice Johnson', '0329333333', '789 Oak St, Danang', 'password3', '1995-03-10', 3, 3, 1),
       (4,true,'Bob Brown', '0329444444', '101 Pine St, Hue', 'password4', '1980-12-05', 4, 4, 2),
       (5,true,'Charlie Wilson', '0329555555', '202 Maple St, Nha Trang', 'password5', '1998-02-25', 5, 5, 3);

select * from _product;

select  * from _order;