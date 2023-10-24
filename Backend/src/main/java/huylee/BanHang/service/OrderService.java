package huylee.BanHang.service;

import huylee.BanHang.dtos.OrderDTO;
import huylee.BanHang.entity.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderDTO orderDTO);
    Order getOrder (Long id);
    Order updateOrder(Long id, OrderDTO orderDTO);
    void deleteOrder(Long id);
    List<Order> getAllOrders(Long id);
    Order convertToOrder(OrderDTO orderDTO);
}
