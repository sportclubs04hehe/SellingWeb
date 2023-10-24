package huylee.BanHang.controller;

import huylee.BanHang.dtos.OrderDTO;
import huylee.BanHang.entity.Order;
import huylee.BanHang.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<?> insertOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream().map(
                                FieldError::getDefaultMessage
                        ).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Order order = service.createOrder(orderDTO);
            return new ResponseEntity<>(order, CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrders(@PathVariable Long user_id) {
        try {
            List<Order> orders = service.getAllOrders(user_id);
            return ResponseEntity.ok().body(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        try {
            Order order = service.getOrder(id);
            return new ResponseEntity<>(order, OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ) {
        try{
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                        .stream().map(
                                FieldError::getDefaultMessage
                        ).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Order order = service.updateOrder(id,orderDTO);
            return new ResponseEntity<>(order,OK);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id) {
        service.deleteOrder(id);
        return ResponseEntity.ok("Successfully canceled order id= " + id);
    }
}
