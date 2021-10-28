package um.si;

import java.util.Map;

public class Order {
    private String order_no;
    private Long date;
    private Integer rest_id;
    private Integer user_id;
    private Integer courier_id;
    private Map<String, Integer> items;

    public Order(){
        super();
    }
    public Order(String order_no, Long date, Integer rest_id, Integer user_id, Integer courier_id, Map<String, Integer> items) {
        this.order_no = order_no;
        this.date = date;
        this.rest_id = rest_id;
        this.user_id = user_id;
        this.courier_id = courier_id;
        this.items = items;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }



    public Integer getRest_id() {
        return rest_id;
    }

    public void setRest_id(Integer rest_id) {
        this.rest_id = rest_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getCourier_id() {
        return courier_id;
    }

    public void setCourier_id(Integer courier_id) {
        this.courier_id = courier_id;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order: (number: " + order_no + ", date: " + date + ", restaurant: " + rest_id+ ", no. of items: " + items.size()
                + ", user: " + user_id + ", courier: " + courier_id + ")";
    }
}
