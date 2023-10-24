package huylee.BanHang.controller;

import huylee.BanHang.dtos.OrderDetailDTO;
import huylee.BanHang.entity.OrderDetail;
import huylee.BanHang.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("${api.prefix}/order-detail")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService service;

    @PostMapping
    public ResponseEntity<?> insertOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
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
            OrderDetail orderDetail = service.createOrderDetail(orderDetailDTO);
            return new ResponseEntity<>(orderDetail, CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable Long id) {
        try {
            OrderDetail orderDetail = service.getOrderDetail(id);
            return new ResponseEntity<>(orderDetail,OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable Long orderId) {
        try {
            List<OrderDetail> orderDetails = service.findByOrderId(orderId);
            return new ResponseEntity<>(orderDetails,OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable Long id,
            @Valid @RequestBody OrderDetailDTO newOrderDetailDTO,
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
           OrderDetail orderDetail = service.updateOrderDetail(id, newOrderDetailDTO);
           return new ResponseEntity<>(orderDetail,OK);
       }catch (Exception e){
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable Long id) {
        service.deleteOrderDetail(id);
        return ResponseEntity.ok("Delete a Order Detail Success id= " + id);
    }
}
