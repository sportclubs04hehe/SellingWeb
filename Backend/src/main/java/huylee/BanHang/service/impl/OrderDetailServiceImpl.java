package huylee.BanHang.service.impl;

import huylee.BanHang.dtos.OrderDetailDTO;
import huylee.BanHang.entity.Order;
import huylee.BanHang.entity.OrderDetail;
import huylee.BanHang.entity.Product;
import huylee.BanHang.exception.AppException;
import huylee.BanHang.repository.OrderDetailRepository;
import huylee.BanHang.repository.OrderRepository;
import huylee.BanHang.repository.ProductRepository;
import huylee.BanHang.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository repository;
    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) {
        //order id có tồn tại không
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new AppException(NOT_FOUND, "Order with id= " + orderDetailDTO.getOrderId() + " not found"));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new AppException(NOT_FOUND, "Product with id= " + orderDetailDTO.getProductId() + " not found"));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .price(orderDetailDTO.getPrice())
                .numberOfProduct(orderDetailDTO.getNumberOfProduct())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();
        return repository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new AppException(NOT_FOUND, "getOrderDetail with id= " + id + " not found"));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = repository.findById(id)
                .orElseThrow(() -> new AppException(NOT_FOUND, "Order with id= " + id + " not found"));
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new AppException(NOT_FOUND, "Order with id= " + orderDetailDTO.getOrderId() + " not found"));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new AppException(NOT_FOUND, "Product with id= " + orderDetailDTO.getProductId() + " not found"));
        orderDetail.setProduct(product);
        orderDetail.setOrder(order);
        orderDetail.setColor(orderDetailDTO.getColor());
        orderDetail.setPrice(orderDetailDTO.getPrice());
        orderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        orderDetail.setNumberOfProduct(orderDetailDTO.getNumberOfProduct());
        return repository.save(orderDetail);
    }

    @Override
    public void deleteOrderDetail(Long id) {
        OrderDetail orderDetail = repository.findById(id)
                        .orElseThrow(() -> new AppException(NOT_FOUND, "orderDetail with id= " + id + " not found"));
        repository.delete(orderDetail);
    }

    @Override
    public List<OrderDetail> getAllOrderDetails(Long id) {
        return null;
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    @Override
    public OrderDetail convertToEntity(OrderDetailDTO orderDetailDTO, Order order, Product product) {
        return null;
    }
}
