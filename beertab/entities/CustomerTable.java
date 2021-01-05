package beertab.entities;


public class CustomerTable  {

    private int customerId;
    private int table;
    private String Customer;
    private String Beverage;
    private int quantity;
    private int cost;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getTable() {
        return table;
    }

    public void setTable(int table) {
        this.table = table;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String customer) {
        this.Customer = customer;
    }

    public String getBeverage() {
        return Beverage;
    }

    public void setBeverage(String beverage) {
        this.Beverage = beverage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }


    public CustomerTable(){
        this.customerId = 0;
        this.Customer = "";
        this.Beverage = "";
        this.quantity = 0;
        this.cost = 0;
    }

    public CustomerTable(int id, int tb, String cust, String bev, int qty, int cost){

        this.customerId = id;
        this.table = tb;
        this.Customer = cust;
        this.Beverage = bev;
        this.quantity = qty;
        this.cost = cost;
    }

}
