package com.unipi.reactor.p04HotColdPublishers;

import com.unipi.reactor.util.Util;
import lombok.Data;
import lombok.ToString;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class Lec06Assignment {

    public static void main(String[] args) {

        // Init services
        OrderService orderService = new OrderService();
        RevenueService revenueService = new RevenueService();
        InventoryService inventoryService = new InventoryService();

        // revenue and inventory - observe the order service
        orderService.orderStream().subscribe(revenueService.subscribeOrderStream());
        orderService.orderStream().subscribe(inventoryService.subscribeOrderStream());

        inventoryService.inventoryStream()
                .subscribe(Util.subscriber("Inventory"));

        inventoryService.inventoryStream()
                .subscribe(Util.subscriber("Revenue"));

        Util.sleepSeconds(60);
    }
}

class InventoryService {

    private Map<String, Integer> db = new HashMap<>();

    public InventoryService() {
        db.put("Kids", 100);
        db.put("Automotive", 100);
    }

    public Consumer<PurchaseOrder> subscribeOrderStream() {
        return p -> db.computeIfPresent(
                p.getCategory(),
                (k, v) -> v - p.getQuantity());
    }

    // Publisher
    public Flux<String> inventoryStream() {
        return Flux.interval(Duration.ofSeconds(2))
                .map(i -> db.toString());
    }
}

class RevenueService {

    private Map<String, Double> db = new HashMap<>();

    public RevenueService() {
        db.put("Kids", 0.0);
        db.put("Automotive", 0.0);
    }

    public Consumer<PurchaseOrder> subscribeOrderStream() {
        return p -> db.computeIfPresent(
                p.getCategory(),
                (k, v) -> v + p.getPrice());
    }

    // Publisher
    public Flux<String> revenueStream() {
        return Flux.interval(Duration.ofSeconds(2))
                .map(i -> db.toString());
    }
}

class OrderService {

    private Flux<PurchaseOrder> flux;

    // With this method we ensure that we have the pipeline once.
    public Flux<PurchaseOrder> orderStream() {
        if (Objects.isNull(this.flux))
           this.flux = getOrderStream();
        return this.flux;
    }

    // Publisher
    // If we make this public, everyone who invokes the getOrderStream(),
    // will get a new pipeline, we don't want this.
    private Flux<PurchaseOrder> getOrderStream() {
        return Flux.interval(Duration.ofMillis(100))
                .map(i -> new PurchaseOrder())
                .publish() // The order stream will be the same for everyone
                .refCount(2); // 2 subscribers to start emitting
    }
}

@Data
@ToString
class PurchaseOrder {

    private String item;
    private Double price;
    private String category;
    private int quantity;

    public PurchaseOrder() {
        this.item = Util.faker().commerce().productName();
        this.price = Util.faker().random().nextInt(1, 15).doubleValue();
        this.category = Util.faker().commerce().department();
        this.quantity = Util.faker().random().nextInt(1, 10);
    }
}
