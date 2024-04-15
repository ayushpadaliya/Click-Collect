package com.example.ecommerce.network;

import com.example.ecommerce.Responces.OrderResponse.AddConfirmationResponse;
import com.example.ecommerce.Responces.OrderResponse.AddOrderResponse;
import com.example.ecommerce.Responces.AllDataResponse;
import com.example.ecommerce.Responces.LauncherRespose.CartResponse.CartDetailsResponse;
import com.example.ecommerce.Responces.CategoryResponse.AddCategoryResponse;
import com.example.ecommerce.Responces.OrderResponse.OrderResponse;
import com.example.ecommerce.Responces.OrderResponse.VendorOrderResponse;
import com.example.ecommerce.Responces.ProductResponse.AddProductResponse;
import com.example.ecommerce.Responces.ProductResponse.DeleteProductResponse;
import com.example.ecommerce.Responces.ProductResponse.EditProductResponse;
import com.example.ecommerce.Responces.ProductResponse.GetProductResponse;
import com.example.ecommerce.Responces.SubCategoryResponse.AddSubCategoryResponse;
import com.example.ecommerce.Responces.CategoryResponse.DeleteCategoryResponse;
import com.example.ecommerce.Responces.CategoryResponse.EditCategoryResponse;
import com.example.ecommerce.Responces.CategoryResponse.GetCategoryResponse;
import com.example.ecommerce.Responces.SubCategoryResponse.DeleteSubCategoryResponse;
import com.example.ecommerce.Responces.SubCategoryResponse.EditSubCategoryResponse;
import com.example.ecommerce.Responces.SubCategoryResponse.GetSubCategoryResponse;
import com.example.ecommerce.Responces.LauncherRespose.CartResponse.UserAddCartItemResponse;
import com.example.ecommerce.Responces.LauncherRespose.CartResponse.UserGetCartItemResponse;
import com.example.ecommerce.Responces.LauncherRespose.UserLoginResponse;
import com.example.ecommerce.Responces.LauncherRespose.UserRegisterResponse;
import com.example.ecommerce.Responces.LauncherRespose.VendorGetData;
import com.example.ecommerce.Responces.LauncherRespose.VendorLoginResponse;
import com.example.ecommerce.Responces.LauncherRespose.VendorRegisterResponse;
import com.example.ecommerce.Responces.LauncherRespose.CartResponse.ViewCartResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Single;

public interface RestCall {

    @FormUrlEncoded
    @POST("userRegisterController.php")
    Single<UserRegisterResponse> UserRegister(
            @Field("user_register") String user_register,
            @Field("user_name") String name,
            @Field("user_email") String email,
            @Field("user_password") String password,
            @Field("user_phone") String phone
    );
    @FormUrlEncoded
    @POST("userRegisterController.php")
    Single<UserLoginResponse> UserLogin(
            @Field("user_login") String user_login,
            @Field("user_email") String user_email,
            @Field("user_password") String user_password
    );
    @FormUrlEncoded
    @POST("vendorRegisterController.php")
    Single<VendorRegisterResponse> VendorRegister(
            @Field("vendor_register") String vendor_register,
            @Field("vendor_name") String name,
            @Field("vendor_email") String email,
            @Field("vendor_password") String password,
            @Field("vendor_phone") String phone
    );
    @FormUrlEncoded
    @POST("vendorRegisterController.php")
    Single<VendorLoginResponse> VendorLogin(
            @Field("vendor_login") String vendor_login,
            @Field("vendor_email") String vendor_email,
            @Field("vendor_password") String vendor_password
    );
    @FormUrlEncoded
    @POST("vendorRegisterController.php")
    Single<VendorGetData> VendorGetData(

        @Field("get_vendor_data") String get_vendor_data
    );
    @FormUrlEncoded
    @POST("addcategory.php")
    Single<AddCategoryResponse> AddCategory(
            @Field("add_category") String add_category,
            @Field("category_name") String category_name,
            @Field("vendor_id") String vendor_id
     );
    @FormUrlEncoded
    @POST("addcategory.php")
    Single<GetCategoryResponse> getCategory(
            @Field("get_category") String get_category,
            @Field("vendor_id") String vendor_id
    );
    @FormUrlEncoded
    @POST("addcategory.php")
    Single<EditCategoryResponse> editCategory(
            @Field("edit_category") String edit_category,
            @Field("vendor_id") String vendor_id,
            @Field("category_id") String category_id,
            @Field("category_edit_name") String category_edit_name
    );
    @FormUrlEncoded
    @POST("addcategory.php")
    Single<DeleteCategoryResponse> deleteCategory(
            @Field("delete_category") String delete_category,
            @Field("vendor_id") String vendor_id,
            @Field("category_id") String category_id
    );
    @FormUrlEncoded
    @POST("subcategory.php")
    Single<AddSubCategoryResponse> addSubCategory(
            @Field("add_sub_category") String add_sub_category,
            @Field("vendor_id") String vendor_id,
            @Field("category_id") String category_id,
            @Field("sub_category_name") String sub_category_name
    );
    @FormUrlEncoded
    @POST("subcategory.php")
    Single<GetSubCategoryResponse> getSubCategory(
            @Field("get_sub_category") String get_sub_category,
            @Field("vendor_id") String vendor_id,
            @Field("category_id") String category_id
    );
    @FormUrlEncoded
    @POST("subcategory.php")
    Single<EditSubCategoryResponse> editSubCategory(
            @Field("edit_sub_category") String edit_sub_category,
            @Field("vendor_id") String vendor_id,
            @Field("category_id") String category_id,
            @Field("sub_category_id") String sub_category_id,
            @Field("sub_category_edit_name") String sub_category_name
    );
    @FormUrlEncoded
    @POST("subcategory.php")
    Single<DeleteSubCategoryResponse> deleteSubCategory(
                    @Field("delete_sub_category") String delete_sub_category,
                    @Field("vendor_id") String vendor_id,
                    @Field("category_id") String category_id,
                    @Field("sub_category_id") String sub_category_id

    );
    @Multipart
    @POST("product.php")
    Single<AddProductResponse> addProduct(
            @Part("add_product") RequestBody add_product,
            @Part("vendor_id") RequestBody vendor_id,
            @Part("category_id") RequestBody category_id,
            @Part("sub_category_id")RequestBody sub_category_id,
            @Part("product_name") RequestBody product_name,
            @Part("product_price")RequestBody  product_price,
            @Part MultipartBody.Part file_path

    );
    @FormUrlEncoded
    @POST("product.php")
    Single<GetProductResponse> getProduct(
            @Field("get_product") String get_product,
            @Field("vendor_id") String vendor_id,
            @Field("category_id") String category_id,
            @Field("sub_category_id") String sub_category_id
    );
    @FormUrlEncoded
    @POST("product.php")
    Single<EditProductResponse> editProduct(
            @Field("edit_product") String edit_product,
            @Field("vendor_id") String vendor_id,
            @Field("category_id") String category_id,
            @Field("sub_category_id") String sub_category_id,
            @Field("product_id") String product_id,
            @Field("product_edit_name") String product_edit_name,
            @Field("product_price_edit") String product_price_edit
    );
    @FormUrlEncoded
    @POST("product.php")
    Single<DeleteProductResponse> deleteProduct(
            @Field("delete_product") String delete_product,
            @Field("vendor_id") String vendor_id,
            @Field("category_id") String category_id,
            @Field("sub_category_id") String sub_category_id,
            @Field("product_id") String product_id
    );
    @FormUrlEncoded
    @POST("cartDetailsController.php")
    Single<UserAddCartItemResponse> addCartItem(
            @Field("add_cart_item") String add_cart_item,
            @Field("cart_id") String cart_id,
            @Field("vendor_id") String vendor_id,
            @Field("user_id") String user_id,
            @Field("product_id") String product_id,
            @Field("product_quantity") String product_quantity,
            @Field("product_name") String product_name,
            @Field("product_price") String product_price

    );
    @FormUrlEncoded
    @POST("cartDetailsController.php")
    Single<UserGetCartItemResponse> getCartItem(
            @Field("get_cart_item") String get_cart_item,
            @Field("cart_id") String cart_id,
            @Field("vendor_id") String vendor_id,
            @Field("user_id") String user_id,
            @Field("product_id") String product_id
    );
    @FormUrlEncoded
    @POST("cart.php")
    Single<CartDetailsResponse> CartItem(
            @Field("add_cart") String add_cart,
            @Field("vendor_id") String vendor_id,
            @Field("user_id") String user_id
    );
    @FormUrlEncoded
    @POST("cartDetailsController.php")
    Single<ViewCartResponse> showCart(
            @Field("get_cart") String get_cart,
            @Field("cart_id") String cart_id
    );
    @FormUrlEncoded
    @POST("cartDetailsController.php")
    Single<OrderResponse> orderPage(
            @Field("get_order") String get_order,
            @Field("user_id") String user_id
    );
    @FormUrlEncoded
    @POST("cartDetailsController.php")
    Single<AddOrderResponse> AddOrder(
            @Field("add_order") String add_order,
            @Field("cart_id") String cart_id


    );
    @FormUrlEncoded
    @POST("alldataController.php")
    Single<AllDataResponse> allData(
            @Field("all_data_fetch") String all_data_fetch,
            @Field("vendor_id") String vendor_id,
            @Field("user_id") String user_id
    );
    @FormUrlEncoded
    @POST("cartDetailsController.php")
    Single<VendorOrderResponse> getOrder(
            @Field("view_order") String view_order,
            @Field("vendor_id") String vendor_id
    );
    @FormUrlEncoded
    @POST("cartDetailsController.php")
    Single<AddConfirmationResponse> getConfirm(
            @Field("add_confirmation") String add_confirmation,
            @Field("vendor_id") String vendor_id,
            @Field("order_id") String order_id,
            @Field("confirmation_status") String status


    );
}
