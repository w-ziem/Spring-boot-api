package com.wziem.store.mappers;

import com.wziem.store.dtos.*;
import com.wziem.store.entities.Order;
import com.wziem.store.entities.OrderItem;
import com.wziem.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    //checkout endpoint
    OrderCheckoutDto toDto(Long orderId);


    //orders list
    default OrdersListDto toOrderListDto(List<Order> orders) {
        if (orders == null) {
            OrdersListDto result = new OrdersListDto();
            result.setOrders(Collections.emptyList());
            return result;
        }

        List<OrderSummaryDto> summaries = toOrderSummaryDto(orders);
        OrdersListDto result = new OrdersListDto();
        result.setOrders(summaries);
        return result;
    }


    List<OrderSummaryDto> toOrderSummaryDto(List<Order> orders);

    //mapping order to summary
    @Mapping(target = "items", source = "orderItems")
    OrderSummaryDto toOrderSummaryDto(Order order);

    //mapping individual items
    @Mapping(target = "product", source = "product")
    OrderItemResponseDto toOrderItemResponseDto(OrderItem orderItem);

    //mapping product to it's summary in orederItems
    ProductSummaryDto toProductSummaryDto(Product product);

}
