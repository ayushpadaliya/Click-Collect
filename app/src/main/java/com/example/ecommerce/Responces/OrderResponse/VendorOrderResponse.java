package com.example.ecommerce.Responces.OrderResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import javax.annotation.processing.Generated;

public class VendorOrderResponse {


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

            @SerializedName("user_name")
            @Expose
            private String userName;
            @SerializedName("product_name")
            @Expose
            private String productName;
            @SerializedName("product_quantity")
            @Expose
            private String productQuantity;
            @SerializedName("product_price")
            @Expose
            private String productPrice;
            @SerializedName("order_id")
            @Expose
            private String orderId;
            @SerializedName("checkout_time")
            @Expose
            private String checkoutTime;

            @SerializedName("confirmation_status")
            @Expose
            private String confirmationStatus;
            @SerializedName("user_id")
            @Expose
            private String user_id;
            @SerializedName("product_id")
            @Expose
            private String product_id;

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
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

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getCheckoutTime() {
                return checkoutTime;
            }

            public void setCheckoutTime(String checkoutTime) {
                this.checkoutTime = checkoutTime;
            }

            public String getConfirmationStatus() {
                return confirmationStatus;
            }

            public void setConfirmationStatus(String confirmationStatus) {
                this.confirmationStatus = confirmationStatus;
            }
            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String productId) {
                this.product_id= productId;
            }
            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id= user_id;
            }

        }
    }

