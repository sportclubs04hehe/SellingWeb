package huylee.BanHang.service;


import huylee.BanHang.dtos.OrderDetailDTO;
import huylee.BanHang.entity.Order;
import huylee.BanHang.entity.OrderDetail;
import huylee.BanHang.entity.Product;

import java.util.List;

public interface OrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDTO);
    OrderDetail getOrderDetail (Long id);
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDTO);
    void deleteOrderDetail(Long id);
    List<OrderDetail> getAllOrderDetails(Long id);
    List<OrderDetail> findByOrderId(Long orderId);
    OrderDetail convertToEntity(OrderDetailDTO orderDTO, Order order, Product product);
}
