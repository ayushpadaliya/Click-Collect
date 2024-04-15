package com.example.ecommerce.Responces.LauncherRespose.CartResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserGetCartItemResponse
{
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("message")
        @Expose
        private List<Message> message;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<Message> getMessage() {
            return message;
        }

        public void setMessage(List<Message> message) {
            this.message = message;
        }
    public class Message {

        @SerializedName("product_quantity")
        @Expose
        private String productQuantity;
        @SerializedName("cart_item_id")
        @Expose
        private String cartItemId;

        public String getProductQuantity() {
            return productQuantity;
        }

        public void setProductQuantity(String productQuantity) {
            this.productQuantity = productQuantity;
        }

        public String getCartItemId() {
            return cartItemId;
        }

        public void setCartItemId(String cartItemId) {
            this.cartItemId = cartItemId;
        }
}
}
