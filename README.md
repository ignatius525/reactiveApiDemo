# reactiveApiDemo

## Set-up

### Cassandra + Docker

Back-end database for this demo is Reactive Cassandra NoSQL DB integrated with Spring Boot, running on a local docker container.

Installing Docker Engine: [Here](https://docs.docker.com/engine/install/)

Once docker is installed, pull latest Cassandra image in terminal window:
``` docker pull cassandra:latest```  
Next, navigate to project repo (or just copy the file [here](compose.yaml)) and run this command to instantiate container with cassandra image locally ```docker-compose up -d```  

If you have docker engine UI available, the container should appear within it. 

```docker exec -it cassandra-dev``` to start up the cql shell for Cassandra.

Once in the shell, run this command to make sure the table is instantiated properly.  
```CREATE KEYSPACE demo WITH REPLICATION = {'class':'SimpleStrategy','replication_factor':1}```
```use demo;```  
```create table if not exists productentity( id int , name text, price double, quantity int, PRIMARY KEY ((id));```

### Swagger-UI

Use your favorite Java IDE to start up the project (I used IDEA Ultimate). Run configs should already exist in the project.

Once started, go to ```localhost:8080/swagger-ui/index.html``` to test the different endpoints.

If swagger is not initialized, add this dependency to your pom:
```
<dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
            <version>3.0.0</version>
</dependency>
```

Also, take a look and add `/config/SpringFoxConfig.java` and add the `@EnableSwagger2` qualifier to the main application run file.

Postman should also work to test the varying endpoints.

### Creating From Scratch

Head over to [Spring Initializr](https://start.spring.io/)

Hit the `Add Dependencies` button near the top right and add Spring Reactive Web and Spring Data Reactive Cassandra to the new project:
![image](https://user-images.githubusercontent.com/55921708/193653803-4b53ca2f-2d7d-4173-937b-f365c8ab53c5.png)
If you would like to add lombok, it will make some of the model and entity code a little shorter.

Hit generate and extract the project. Then, using your favorite IDE, import the project. In IDEA Ultimate, traverse to `File -> Open` and find the master directory for your project wherever you saved it. 
Feel free to follow through the repository codebase while coding up the tutorial.

#### application.properties
```
spring.data.cassandra.keyspace-name=demo
spring.data.cassandra.port=9042
spring.data.cassandra.local-datacenter=datacenter1

spring.mvc.pathmatch.matching-strategy=ant-path-matcher
```

Make sure to add these lines to your `application.properties`. It contains the information for the Spring Cassandra Repository to connect to the local docker Cassandra DB that is running.

#### Model + Entity
The model package contains the base object definition for an object. With lombok and the `@data` qualifier, the code will look like this:
``` 
@Data
public class Product {

    private Integer id;

    private String name;
    private Integer quantity;
    private Double price;
} 
```

Without lombok, proceed to write `get` and `set` methods for each instance variable:

```
public Integer getId(){return this.id;)
public void setId(Integer id){this.id = id;}
```

The entity package will contain the exact same informaton as the model, but will serve as a wrapper class for database insertion. The only thing that will be different from the entity and the model is the addition of a `@Table` qualifier for the class and a `@PrimaryKey` qualifier for the Id:
```
@Table
@Data
public class ProductEntity {

    @PrimaryKey
    private Integer id;
    ...
```

#### Repository
The repository package will contain an interface as a layer of abstratction between the application and the Cassandra database. It will look something like this:
```
@Repository
public interface ProductRepository extends ReactiveCassandraRepository<ProductEntity, Integer> {
}
```

This product repository class will be created within the service class and will serve as a bridge between the clients requests and the database.

#### Service
The service class with begin with the `@Service` qualifier and thus will be instantiaed on app start-up. Each method in the product service will line up with a controller method for the exposed endpoints. Feel free to look at the service methods and how they line up with what the controller function should be doing. 

Example
```
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
```

A new product entity will be created based on the product that is passed along. That new entity will then be saved to the remote repository AND a product object will be mapped to the repository service in the application.

#### Controller (The Reactive Part)
```
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/product")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> createProduct(@RequestBody Product product){
        return productService.saveProduct(product);
    }

    @GetMapping("/listProducts")
    public Flux<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @DeleteMapping("/product/{id}")
    public Mono<Void> deleteProduct(@PathVariable Integer id){
        return productService.deleteProduct(id);
    }

    @DeleteMapping("/clearProducts")
    public Mono<Void> clear(){
        return productService.clearProducts();
    }

    @PutMapping("/product/{id}")
    public Mono<Product> updateProduct(@RequestBody Product product){
        return productService.update(Mono.just(product));
    }
}
```
This is it. It is that easy to create exposed endpoints in Spring. An autowired productService object will be attached to the controller class. Whenver a user hits a specified endpoint, it will then call the associated method with the service. Take note of the `Mono` and `Flux` data streams that are opened when hitting each of the endpoints. If you set up the Swagger UI earlier, feel free to use it to try out your new reactive API. Otherwise, I would recommend [Postman](https://www.postman.com/).

![image](https://user-images.githubusercontent.com/55921708/193660052-be52145b-3f45-44c1-941c-0ba8f338dc1c.png)
![image](https://user-images.githubusercontent.com/55921708/193660066-fb4a5c1c-7ae6-41b5-82fd-670d0c17e3b3.png)


## Exposed endpoints

POST `/product` - Pass product object to create new object in db  
GET `/listProducts` - Returns list of all products in db  
PUT `/product/{id}` - Pass product object to update product with associated id in header (you can pass different id in the object itself)  
DEL `/product/{id}` - Delete product object with associated id  
DEL `/clearProducts` - Clears the database

Based on the work of [Siva Prasad Rao Janapati](https://dzone.com/articles/build-reactive-rest-apis-with-spring-webflux)
