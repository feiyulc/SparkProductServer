import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;

public class Server {
    // 创建一个简单的内存数据库来存储商品
    private static Map<String, Product> products = new HashMap<>();

    // Getter 方法
    public static Map<String, Product> getProducts() {
        return products;
    }

    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello, World!");

        // 添加一个新的路由来处理带有参数的请求
        get("/hello/:name", (req, res) -> {
            String name = req.params(":name");
            return "Hello, " + name + "!";
        });

        // 添加一个新的路由来添加商品
        post("/products/:name", (req, res) -> {
            String name = req.params(":name");
            String description = req.queryParams("description");
            double price = Double.parseDouble(req.queryParams("price"));
            getProducts().put(name, new Product(name, description, price));
            return "Product added!";
        });

        // 添加一个新的路由来获取商品
        get("/products/:name", (req, res) -> {
            String name = req.params(":name");
            Product product = getProducts().get(name);
            if (product != null) {
                return product.getName() + ", " + product.getDescription() + ", " + product.getPrice();
            } else {
                return "Product not found";
            }
        });

        // 添加一个新的路由来删除商品
        delete("/products/:name", (req, res) -> {
            String name = req.params(":name");
            Product product = getProducts().remove(name);
            if (product != null) {
                return "Product removed!";
            } else {
                return "Product not found";
            }
        });

        // 添加一个新的路由来更新商品
        put("/products/:name", (req, res) -> {
            String name = req.params(":name");
            String description = req.queryParams("description");
            double price = Double.parseDouble(req.queryParams("price"));
            Product product = getProducts().get(name);
            if (product != null) {
                product.setDescription(description);
                product.setPrice(price);
                return "Product updated!";
            } else {
                return "Product not found";
            }
        });

        // 添加一个新的路由来获取所有商品
        get("/products", (req, res) -> {
            StringBuilder sb = new StringBuilder();
            for (Product product : getProducts().values()) {
                sb.append(product.getName()).append(", ").append(product.getDescription()).append(", ").append(product.getPrice()).append("\n");
            }
            return sb.toString();
        });

        // 添加一个新的路由来处理购物车
        post("/cart/:name", (req, res) -> {
            String name = req.params(":name");
            int quantity = Integer.parseInt(req.queryParams("quantity"));
            Cart cart = new Cart(name, quantity);
            return "Added to cart: " + cart.getProduct().getName() + ", quantity: " + cart.getQuantity();
        });
    }
}

class Product {
    private String name;
    private String description;
    private double price;

    public Product(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

class Cart {
    private Product product;
    private int quantity;

    public Cart(String name, int quantity) {
        this.product = Server.getProducts().get(name);
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
