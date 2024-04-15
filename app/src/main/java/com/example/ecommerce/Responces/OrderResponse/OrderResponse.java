package com.example.ecommerce.Responces.OrderResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderResponse implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Data> data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
    public class Data implements Serializable {

        @SerializedName("vendor_name")
        @Expose
        private String vendorName;
        @SerializedName("product_name")
        @Expose
        private String productName;
        @SerializedName("product_quantity")
        @Expose
        private String productQuantity;
        @SerializedName("product_price")
        @Expose
        private String productPrice;
        @SerializedName("confirmation_status")
        @Expose
        private String confirmationStatus;
        @SerializedName("cart_date")
        @Expose
        private String cartDate;
        @SerializedName("order_id")
        @Expose
        private String orderId;




        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }
        public String getVendorName() {
            return vendorName;
        }

        public void setVendorName(String vendorName) {
            this.vendorName = vendorName;
        }


        public String getProductQuantity() {
            return productQuantity;
        }

        public void setProductQuantity(String productQuantity) {
            this.productQuantity = productQuantity;
        }

        public String getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        public String getConfirmationStatus() {
            return confirmationStatus;
        }

        public void setConfirmationStatus(String confirmationStatus) {
            this.confirmationStatus = confirmationStatus;
        }
        public String getCartDate() {
            return cartDate;
        }

        public void setCartDate(String cartDate) {
            this.cartDate = cartDate;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

    }
}
