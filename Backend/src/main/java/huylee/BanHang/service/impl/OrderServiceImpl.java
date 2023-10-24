package huylee.BanHang.service.impl;

import huylee.BanHang.dtos.OrderDTO;
import huylee.BanHang.entity.Order;
import huylee.BanHang.entity.User;
import huylee.BanHang.exception.AppException;
import huylee.BanHang.repository.OrderRepository;
import huylee.BanHang.repository.UserRepository;
import huylee.BanHang.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static huylee.BanHang.entity.OrderStatus.PENDING;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderRepository repository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) {
        User userId = userRepository.findById(orderDTO.getUserId()).orElseThrow(
                () -> new AppException(NOT_FOUND, "user not found")
        );
        // cập nhật các trường của đơn hàng orderDTO.
        Order order = convertToOrder(orderDTO);
        modelMapper.map(orderDTO,order); // convert orderDto => Order => true
        order.setUser(userId);
        order.setOrderDate(new Date());
        order.setStatus(PENDING);
        setShippingDate(order,orderDTO.getShippingDate());
        order.setActive(true);
        return repository.save(order);
    }

    @Override
    public Order getOrder(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new AppException(NOT_FOUND, "Order getOder with id =" + id + " not found")
        );
    }

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) {
        Order existingOrder = repository.findById(id)
                .orElseThrow(() -> new AppException(NOT_FOUND,"Category with id= "+ id + " not found"));
        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new AppException(NOT_FOUND,"User with id= "+ id + " not found"));
        convertToOrder(orderDTO);
        modelMapper.map(orderDTO,existingOrder);
        existingOrder.setUser(existingUser);
        return repository.save(existingOrder);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = repository.findById(id)
                .orElseThrow(() -> new AppException(NOT_FOUND, "Oder with id= " + id + " not found" ));
        order.setActive(false);
        repository.save(order);
    }

    @Override
    public List<Order> getAllOrders(Long id) {
        return repository.findByUserId(id);
    }

    @Override
    public Order convertToOrder(OrderDTO orderDTO) {
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        return modelMapper.map(orderDTO, Order.class);
    }

    private void setShippingDate(Order order, LocalDate shippingDate) {
        LocalDate effectiveDate = (shippingDate == null) ? LocalDate.now() : shippingDate;
        if (effectiveDate.isBefore(LocalDate.now())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "ngày ít nhất phải là hôm nay");
        }
        order.setShippingDate(effectiveDate);
    }
}
