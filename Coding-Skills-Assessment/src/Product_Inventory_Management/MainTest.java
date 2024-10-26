package Product_Inventory_Management;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class MainTest {

    // Tạo danh sách sản phẩm mẫu
    private ArrayList<Product> createSampleProductList() {
        ArrayList<Product> listProduct = new ArrayList<>();
        listProduct.add(new Product("Laptop", 999.99, 5));
        listProduct.add(new Product("Smartphone", 499.99, 10));
        listProduct.add(new Product("Tablet", 299.99, 0));
        listProduct.add(new Product("Smartwatch", 199.99, 3));
        return listProduct;
    }

    @Test
    public void testCalculateTotalInventoryValue() {
        ArrayList<Product> listProduct = createSampleProductList();
        double result = Main.calculateTotalInventoryValue(listProduct);
        assertEquals(999.99 * 5 + 499.99 * 10 + 299.99 * 0 + 199.99 * 3, result, 0.01);
    }

    @Test
    public void testFindTheMostExpensiveProduct() {
        ArrayList<Product> listProduct = createSampleProductList();
        String result = Main.findTheMostExpensiveProduct(listProduct);
        assertEquals("Laptop", result);
    }

    @Test
    public void testCheckNameProductInStock() {
        ArrayList<Product> listProduct = createSampleProductList();
        assertTrue(Main.checkNameProductInStock(listProduct, "Laptop"));
        assertFalse(Main.checkNameProductInStock(listProduct, "Tablet"));
    }

    @Test
    public void testSortProductListByPriceAsc() {
        ArrayList<Product> listProduct = createSampleProductList();
        ArrayList<Product> sortedProducts = Main.sortProductList(listProduct, "price", "ASC");
        assertEquals("Smartwatch", sortedProducts.get(0).getName());
        assertEquals("Tablet", sortedProducts.get(1).getName());
        assertEquals("Smartphone", sortedProducts.get(2).getName());
        assertEquals("Laptop", sortedProducts.get(3).getName());
    }

    @Test
    public void testSortProductListByPriceDesc() {
        ArrayList<Product> listProduct = createSampleProductList();
        ArrayList<Product> sortedProducts = Main.sortProductList(listProduct, "price", "DESC");
        assertEquals("Laptop", sortedProducts.get(0).getName());
        assertEquals("Smartphone", sortedProducts.get(1).getName());
        assertEquals("Tablet", sortedProducts.get(2).getName());
        assertEquals("Smartwatch", sortedProducts.get(3).getName());
    }
}
