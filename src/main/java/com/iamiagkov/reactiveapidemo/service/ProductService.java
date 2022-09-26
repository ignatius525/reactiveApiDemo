package com.iamiagkov.reactiveapidemo.service;

import com.iamiagkov.reactiveapidemo.entity.ProductEntity;
import com.iamiagkov.reactiveapidemo.model.Product;
import com.iamiagkov.reactiveapidemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Flux<Product> getAllProducts() {
        return productRepository.findAll().map(p -> {
            Product product = new Product();
            product.setId(p.getId());
            product.setName(p.getName());
            product.setQuantity(p.getQuantity());
            product.setPrice(p.getPrice());
            return product;
        });
    }

    public Mono<Product> saveProduct(Product product){
        ProductEntity p = new ProductEntity();
        p.setId(product.getId());
        p.setName(product.getName());
        p.setQuantity(product.getQuantity());
        p.setPrice(product.getPrice());
        return productRepository.save(p).map(p1 -> {
            Product product1 = new Product();
            product1.setId(p1.getId());
            product1.setName(p1.getName());
            product1.setQuantity(p1.getQuantity());
            product1.setPrice(p1.getPrice());
            return product1;
        });
    }

    public Mono<Void> deleteProduct(Integer id){
        return productRepository.deleteById(id);
    }

    public Mono<Void> clearProducts(){
        return productRepository.deleteAll();
    }

    public Mono<Product> update(Mono<Product> product) {

        return product
                .flatMap((p) -> productRepository.findById(p.getId())
                        .flatMap(product1 -> {
                            product1.setName(p.getName());
                            product1.setQuantity(p.getQuantity());
                            product1.setPrice(p.getPrice());
                            return productRepository.save(product1);

                        }).map(updatedItem -> {
                            Product product1 = new Product();
                            product1.setId(updatedItem.getId());
                            product1.setName(updatedItem.getName());
                            product1.setQuantity(updatedItem.getQuantity());
                            product1.setPrice(updatedItem.getPrice());
                            return product1;
                        }));
    }
}
