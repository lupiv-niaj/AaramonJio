package com.aaramon.aaramonjio.order;

public interface StaticVariable {

    //    TABLE_AS_PRODUCT
    int DATABASE_VERSION = 3;
    String DATABASE_AaramShop = "db_aaramon_jiopos";

    //Table Tax Schedule
    String TABLE_TAX = "as_merchant_store_tax_schedule";
    String TAX_SCHEDULE_ID = "tax_schedule_id";
    String TAX_MERCHANT_STORE_ID = "merchant_store_id";
    String TAX_SCHEDULE_NAME = "tax_schedule_name";
    String TAX_SCHEDULE_PERCENTAGE = "tax_schedule_percent";
    String TAX_ACTIVE = "active";
    String TAX_LAST_UPDATE = "last_updated_date";

    String TABLE_AS_PRODUCT = "as_product";
    String ASPROD_PRODUCT_ID = "product_id";
    String ASPROD_SERVER_PRODUCT_ID = "server_product_id";
    String ASPROD_BRAND_ID = "brand_id";
    String ASPROD_CATEGORY_ID = "category_id";
    String ASPROD_PRODUCT_NAME = "product_name";
    String ASPROD_SEO_NAME = "seo_name";
    String ASPROD_PRODUCT_DESCRIPTION = "product_description";
    String ASPROD_PRODUCT_SHORT_DESCRIPTION = "product_short_description";
    String ASPROD_HIGHLIGHT_CHECKOUT = "hightlight_checkout";
    String ASPROD_DISPLAY_AS_CATEGORY = "display_as_category";
    String ASPROD_RECOMMENDED_PRODUCT = "recommended_product";
    String ASPROD_PRODUCT_PRICE = "product_price";
    String ASPROD_EAN_CODE = "ean_code";
    String ASPROD_AARAMSHOP_CODE = "aaramshop_code";
    String ASPROD_IS_FEATURED = "is_featured";
    String ASPROD_FEATURED_START_DATE = "featured_start_date";
    String ASPROD_FEATURED_END_DATE = "featured_end_date";
    String ASPROD_META_TITLE = "meta_title";
    String ASPROD_META_KEYWORD = "meta_keyword";
    String ASPROD_META_DESCRIPTION = "meta_description";
    String ASPROD_SORT_ORDER = "sort_order";
    String ASPROD_ADDED_BY = "added_by";
    String ASPROD_EMPLOYEE_ID = "employee_id";
    String ASPROD_STATUS = "status";
    String ASPROD_EDIT_DELETE = "edit_delete";
    String ASPROD_SHOW_ON = "show_on";
    String ASPROD_DATE_ADDED = "date_added";
    String ASPROD_DATE = "date";
    String ASPROD_PK_UPDATE = "pk_update";
    String ASPROD_TAX_SCHEDULE_ID = "tax_schedule_id";
    String ASPROD_RETAILER_PRODUCT_ID = "retailer_product_id";
    String ASPROD_SYNC = "sync";
    String ASPROD_IMAGE_URL = "image_url";
    String ASPROD_CESS_RATE = "cess_rate";

    //Table AS_Product_Batches
    String TABLE_AS_PRODUCT_BATCHES = "as_product_batches";
    String ASPB_PRODUCT_BATCH_ID = "product_batch_id";
    String ASPB_SERVER_PRODUCT_BATCH_ID = "server_product_batch_id";
    String ASPB_MERCHANT_ID = "merchant_store_id";
    String ASPB_SEQUENCE = "sequence";
    String ASPB_BATCH_NO = "batchno";
    String ASPB_EXPIRY = "expiry";
    String ASPB_COST_PRICE = "cost_price";
    String ASPB_PRODUCT_PRICE = "product_price";
    String ASPB_OFFER_PRICE = "offer_price";
    String ASPB_OFFER_TYPE = "offer_type";
    String ASPB_FREE_PRODUCT = "free_product";
    String ASPB_OFFER_START_DATE = "offer_start_date";
    String ASPB_OFFER_END_DATE = "offer_end_date";
    String ASPB_STOCK = "stock";
    String ASPB_PRODUCT_DESCRIPTION = "product_description";
    String ASPB_PRODUCT_BARCODE_ID = "product_barcode_id";
    String ASPB_PRODUCT_ID = "product_id";
    String ASPB_IS_ACTIVE = "is_active";
    String ASPB_DATE_TIME = "datetime";
    String ASPB_ADDED_BY = "AddedBy";
    String ASPB_UPDATE_BY = "UpdatedBy";
    String ASPB_PK_UPDATE = "pk_update";
    String ASPB_SOURCE = "source";
    String ASPB_SYNC = "sync";


    //Table AS_Product_Barcodes
    String TABLE_AS_PRODUCT_BARCODES = "as_product_barcodes";
    String ASPBAR_PRODUCT_BARCODE_ID = "product_barcode_id";
    String ASPBAR_EAN_CODE = "ean_code";
    String ASPBAR_RETAILER_CODE = "retailer_code";
    String ASPBAR_PRODUCT_ID = "product_id";
    String ASPBAR_DATETIME = "datetime";


    //Table AS_Brand
    String TABLE_AS_BRAND = "as_brand";
    String ASBRAND_BRAND_ID = "brand_id";
    String ASBRAND_SERVER_BRAND_ID = "server_brand_id";
    String ASBRAND_COMPANY_ID = "company_id";
    String ASBRAND_BRAND_NAME = "brand_name";
    String ASBRAND_SEO_NAME = "seo_name";
    String ASBRAND_CONTACT_NAME = "contact_name";
    String ASBRAND_PHONE = "phone";
    String ASBRAND_MOBILE = "mobile";
    String ASBRAND_FAX = "fax";
    String ASBRAND_EMAIL_ID = "email_id";
    String ASBRAND_WEBSITE = "website";
    String ASBRAND_BRAND_DESCRIPTION = "brand_description";
    String ASBRAND_IMAGE = "image";
    String ASBRAND_IS_PREMIUM = "is_premium";
    String ASBRAND_HOME_PAGE_ICON = "home_page_icon";
    String ASBRAND_PAGE_IMAGE = "page_image";
    String ASBRAND_BRAND_LOGO = "brand_logo";
    String ASBRAND_HEADING_BG_COLOR = "heading_bg_color";
    String ASBRAND_HEADING_COLOR = "heading_color";
    String ASBRAND_SUBHEADING_COLOR = "subheading_color";
    String ASBRAND_DOCUMENTORIES_LINK = "documentaries_link";
    String ASBRAND_AUDIO_LINK = "audio_link";
    String ASBRAND_TVC_LINK = "tvc_link";
    String ASBRAND_FACEBOOK_LINK = "facebook_link";
    String ASBRAND_WEBSITE_LINK = "website_link";
    String ASBRAND_PREMIUM_BRAND_BANNER = "premium_brand_banner";
    String ASBRAND_PREMIUM_BRAND_LINK = "premium_brand_link";
    String ASBRAND_PREMIUM_BRAND_BANNER_TYPE = "premium_brand_banner_type";
    String ASBRAND_PREMIUM_BRAND_PRODUCT = "premium_brand_product";
    String ASBRAND_PREMIUM_BRAND_STORE = "premium_brand_store";
    String ASBRAND_PREMIUM_BRAND_GALLERY_IMAGE = "premium_brand_gallery_image";
    String ASBRAND_META_TITLE = "meta_title";
    String ASBRAND_META_KEYWORD = "meta_keyword";
    String ASBRAND_META_DESCRIPTION = "meta_description";
    String ASBRAND_SORT_ORDER = "sort_order";
    String ASBRAND_ADDED_BY = "added_by";
    String ASBRAND_EMPLOYEE_ID = "employee_id";
    String ASBRAND_TOP_BANNER = "top_banner";
    String ASBRAND_TOP_BANNER_LINK = "top_banner_link";
    String ASBRAND_LEFT_BANNER = "left_banner";
    String ASBRAND_LEFT_BANNER_LINK = "left_banner_link";
    String ASBRAND_MERCHANT_STORE_ID = "merchant_store_id";
    String ASBRAND_STATUS = "status";
    String ASBRAND_EDITDELETE = "edit_delete";
    String ASBRAND_SHOW_ON = "show_on";
    String ASBRAND_DATE_ADDED = "date_added";
    String ASBRAND_DATE = "date";
    String ASBRAND_PK_UPDATE = "pk_update";
    String ASBRAND_SYNC = "sync";


    //Table AS_Company
    String TABLE_AS_COMPANY = "as_company";
    String ASCOMPANY_COMPANY_ID = "company_id";
    String ASCOMPANY_SERVER_COMPANY_ID = "server_company_id";
    String ASCOMPANY_COMPANY_NAME = "company_name";
    String ASCOMPANY_SEO_NAME = "seo_name";
    String ASCOMPANY_CONTACT_NAME = "contact_name";
    String ASCOMPANY_PHONE = "phone";
    String ASCOMPANY_MOBILE = "mobile";
    String ASCOMPANY_FAX = "fax";
    String ASCOMPANY_EMAIL_ID = "email_id";
    String ASCOMPANY_WEBSITE = "website";
    String ASCOMPANY_COMPANY_DESCRIPTION = "company_description";
    String ASCOMPANY_IMAGE = "image";
    String ASCOMPANY_META_TITLE = "meta_title";
    String ASCOMPANY_META_KEYWORD = "meta_keyword";
    String ASCOMPANY_META_DESCRIPTION = "meta_description";
    String ASCOMPANY_COUNTRY_ID = "country_id";
    String ASCOMPANY_SORT_ORDER = "sort_order";
    String ASCOMPANY_ADDED_BY = "added_by";
    String ASCOMPANY_EMPLOYEE_ID = "employee_id";
    String ASCOMPANY_STATUS = "status";
    String ASCOMPANY_SHOW_ON = "show_on";
    String ASCOMPANY_DATE_ADDED = "date_added";
    String ASCOMPANY_DATE = "date";
    String ASCOMPANY_PK_UPDATE = "pk_update";
    String ASCOMPANY_SOURCE = "Source";
    String ASCOMPANY_SYNC = "sync";

    //Table AS_CATEGORY
    String TABLE_AS_CATEGORY = "as_category";
    String ASCATEGORY_CATEGORY_ID = "category_id";
    String ASCATEGORY_SERVER_CATEGORY_ID = "server_category_id";
    String ASCATEGORY_CATEGORY_NAME = "category_name";
    String ASCATEGORY_SEO_NAME = "seo_name";
    String ASCATEGORY_CATEGORY_DESCRIPTION = "category_description";
    String ASCATEGORY_PARENT_ID = "parent_id";
    String ASCATEGORY_CATEGORY_LEVEL = "category_level";
    String ASCATEGORY_CATEGORY_IMAGE = "category_image";
    String ASCATEGORY_CATEGORY_WEB_ICON = "category_web_icon";
    String ASCATEGORY_CATEGORY_ICON = "category_icon";
    String ASCATEGORY_CATEGORY_BANNER = "category_banner";
    String ASCATEGORY_SHOW_ON_TOP = "show_on_top";
    String ASCATEGORY_CATEGORY_TYPE = "category_type";
    String ASCATEGORY_META_TITLE = "meta_title";
    String ASCATEGORY_META_KEYWORD = "meta_keyword";
    String ASCATEGORY_META_DESCRIPTION = "meta_description";
    String ASCATEGORY_SORT_ORDER = "sort_order";
    String ASCATEGORY_EMPLOYEE_ID = "employee_id";
    String ASCATEGORY_SHOW_ON = "show_on";
    String ASCATEGORY_STATUS = "status";
    String ASCATEGORY_DATE_ADDED = "date_added";
    String ASCATEGORY_DATE = "date";
    String ASCATEGORY_SOURCE = "source";
    String ASCATEGORY_PK_UPDATE = "pk_update";
    String ASCATEGORY_SYNC = "sync";
    String ASCATEGORY_CGST_RATE = "cgst_rate";
    String ASCATEGORY_SGST_RATE = "sgst_rate";
    String ASCATEGORY_IGST_RATE = "igst_rate";


    //Table AS_PRODUCT_BARCODE_LIST
    String TABLE_AS_PRODUCT_BARCODE_LIST = "as_product_barcode_list";
    String ASPBL_PRODUCT_BARCODE_LIST_ID = "product_barcode_list_id";
    String ASPBL_LIST_NAME = "list_name";
    String ASPBL_DATETIME = "datetime";


    //Table AS_PRODUCT_BARCODE_LIST_PRODUCT_BATCHES
    String TABLE_AS_PRODUCT_BARCODE_LIST_PRODUCTBATCH = "as_product_barcode_list_product_batches";
    String ASPBLPB_PRODUCT_BARCODE_LIST_PRODUCT_BATCH_ID = "product_barcode_list_product_batch_id";
    String ASPBLPB_PRODUCT_BARCODE_LIST_ID = "product_barcode_list_id";
    String ASPBLPB_PRODUCT_BATCH_ID = "product_batch_id";
    String ASPBLPB_DATETIME = "datetime";

    //Table AS_ORDER
    String TABLE_AS_ORDER = "as_order";
    String ASORDER_ORDER_ID = "order_id";
    String ASORDER_ORDER_CODE = "order_code";
    String ASORDER_SERVER_ORDER_ID = "server_order_id";
    String ASORDER_RETAILER_ORDER_ID = "retailer_order_id";
    String ASORDER_SHOPPER_ADDRESS_ID = "shopper_address_id";
    String ASORDER_MERCHANT_STORE_ID = "merchant_store_id";
    String ASORDER_ORDER_SOURCE = "order_source";
    String ASORDER_DELIVERY_DATE = "delivery_date";
    String ASORDER_DELIVERY_TIMESLOT = "delivery_time_slot";
    String ASORDER_DELIVERY_TIMING = "delivery_timing";
    String ASORDER_ORDER_TIMINIG = "order_timing";
    String ASORDER_ORDER_STATUS = "order_status";
    String ASORDER_PARTIAL_RETURNED = "partial_returned";
    String ASORDER_TOTAL_AMOUNT = "total_amount";
    String ASORDER_TAX_AMOUNT = "tax_amount";
    String ASORDER_TAX_TYPE = "tax_type";
    String ASORDER_OFFER_TYPE = "offer_type";
    String ASORDER_TOTAL_DISCOUNT = "total_discount";
    String ASORDER_SPECAIL_DISCOUNT = "special_discount";
    String ASORDER_DELIVERY_CHARGES = "delivery_charge";
    String ASORDER_COUPON_CODE = "coupon_code";
    String ASORDER_SHOPPER_PAYMENT_TYPEID = "shopper_payment_type_id";
    String ASORDER_MERCHANT_PAYMENT_TYPE_ID = "merchant_payment_type_id";
    String ASORDER_IPADDRESS = "ip_address";
    String ASORDER_MERCHANT_STORE_PEOPLE_ID = "merchant_store_people_id";
    String ASORDER_DELIVERY_BOY_TIMING = "delivery_boy_timing";
    String ASORDER_PACKED_TIMING = "packed_timing";
    String ASORDER_DISPATCHED_TIMING = "dispatched_timing";
    String ASORDER_DELIVERED_TIMING = "delivered_timing";
    String ASORDER_CUSTOMER_DUE = "customer_due";
    String ASORDER_IS_READ = "is_read";
    String ASORDER_SPECIAL_REQUEST = "special_request";
    String ASORDER_ORDER_COMPLETED = "order_completed";
    String ASORDER_UNIQUE_ORDER_ID = "unique_order_id";
    String ASORDER_STATUS = "Status";
    String ASORDER_JIO_TXN_REF = "JioTxnRefNum";
    String ASORDER_ERROR_CODE = "ErrorCode";
    String ASORDER_RESPONSE_MSG = "ResponseMsg";
    String ASORDER_TXT_TIME = "TxnTimeStamp";
    String ASORDER_CARD_NUMBER = "CardNumber";
    String ASORDER_TXN_TYPE = "TxnType";
    String ASORDER_CARD_TYPE = "CardType";
    String ASORDER_ORDER_COMPLETED_PAYMENT_MODE = "order_completed_payment_mode";
    String ASORDER_ORDER_COMPLETED_PAYMENT_MODE_AMOUNT = "order_completed_payment_mode_amount";
    String ASORDER_ORDER_COMPLETED_PAYMENT_MODE_VOUCHERNO = "order_completed_payment_mode_voucherno";
    String ASORDER_CALL_CENTER_EMP_ID = "call_center_employee_id";
    String ASORDER_ORDER_CANCELLATION = "order_cancellation";
    String ASORDER_CANCELLATION_REASON = "cancellation_reason";
    String ASORDER_ORDER_CANCELLATION_TIMING = "order_cancellation_timimg";
    String ASORDER_ORDER_CANCELLATION_STATUS_CHANGE_TIMING = "order_cancellation_status_change_timing";
    String ASORDER_STORE_PICKUP = "store_pickup";
    String ASORDER_ORDER_TYPE_ID = "order_type_id";
    String ASORDER_USER_ID = "user_id";
    String ASORDER_COUNTER_ID = "counter_id";
    String ASORDER_BILL_FOOTER = "BillFooter";
    String ASORDER_FEEDBACK_RATING = "feedback_rating";
    String ASORDER_PK_UPDATE = "pk_update";
    String ASORDER_SYNC = "sync";


    //as_order_detail
    String TABLE_AS_ORDER_DETAIL = "as_order_detail";
    String ASORDERDETAIL_ORDER_DETAIL_ID = "order_detail_id";
    String ASORDERDETAIL_ORDER_ID = "order_id";
    String ASORDERDETAIL_PRODUCT_TYPE = "product_type";
    String ASORDERDETAIL_PRODUCT_BATCH_ID = "product_batch_id";
    String ASORDERDETAIL_MERCHANT_PRODUCT_ID = "merchant_product_id";
    String ASORDERDETAIL_PRODUCT_NAME = "product_name";
    String ASORDERDETAIL_QUANTITY = "quantity";
    String ASORDERDETAIL_PRODUCT_PRICE = "product_price";
    String ASORDERDETAIL_OFFER_TYPE = "offer_type";
    String ASORDERDETAIL_OFFER_PRICE = "offer_price";
    String ASORDERDETAIL_FREE_PRODUCT = "free_product";
    String ASORDERDETAIL_COMBO_DETAIL_ITEM = "combo_detail_items";
    String ASORDERDETAIL_IS_AVAILABLE = "is_available";
    String ASORDERDETAIL_IMAGE_NAME = "image_name";
    String ASORDERDETAIL_OFFER_DESCRIPTION = "offer_description";
    String ASORDERDETAIL_DATE_TIME = "datetime";
    String ASORDERDETAIL_TAX_PERCENTAGE = "tax_percentage";


    String TABLE_AS_TEMP_CART = "as_temp_cart";
    String COLUMN_TEMP_CART_ID = "temp_cart_id";
    String COLUMN_CART_NAME = "cart_name";
    String COLUMN_CART_VALUE = "cart_value";
    String COLUMN_CART_ITEM = "cart_item";
    String COLUMN_USER_ID = "user_id";
    String COLUMN_ADDED_DATE = "added_date";


    String TABLE_AS_TEMP_CART_ITEM = "as_temp_cart_item";
    String COLUMN_TEMP_CART_ITEM_ID = "temp_cart_item_id";
    String COLUMN_PRODUCT_ID = "product_id";
    String COLUMN_PRODUCT_NAME = "product_name";
    String COLUMN_PRODUCT_PRICE = "product_price";
    String COLUMN_OFFER_PRICE = "offer_price";
    String COLUMN_QUANTITY = "quantity";
    String COLUMN_IMAGE_URL = "image_url";
    String COLUMN_CGST_RATE = "cgst_rate";
    String COLUMN_SGST_RATE = "sgst_rate";
    String COLUMN_IGST_RATE = "igst_rate";
    String COLUMN_CESS_RATE = "cess_rate";
    String COLUMN_TOTAL_AMOUNT = "total_amt";
    String COLUMN_TOTAL_TAX_AMOUNT = "total_tax_amt";
    String COLUMN_BATCH_NO = "batch_no";
    String COLUMN_BATCH_ID = "batch_id";


    // Added by Vijay Kumar on 20/05/2017
    // Temporary Tables to store Purchase Entry Details for a Supplier until it is saved & sent successfully to API
    // PurchaseEntry Header Details
    String TABLE_AS_TEMP_PURCHASE = "as_temp_purchase";
    String COLUMN_PURCHASE_ID = "purchase_id";
    String COLUMN_PURCHASE_BILL_DATE = "ret_bill_date";
    String COLUMN_PURCHASE_BILL_NO = "ret_bill_number";
    String COLUMN_PURCHASE_SUPPLIER_ID = "supplier_id";
    String COLUMN_PURCHASE_MERCHANT_STORE_ID = "merchant_store_id";
    String COLUMN_PURCHASE_PAYMENT_TYPE = "payment_type";
    String COLUMN_PURCHASE_PAYMENT_TERM = "payment_term";
    String COLUMN_PURCHASE_DATE = "purchase_date";
    String COLUMN_PURCHASE_TOTAL_AMOUNT = "total_amount";
    String COLUMN_PURCHASE_TAX = "tax";
    String COLUMN_PURCHASE_AMOUNT_PAID = "amount_paid";
    String COLUMN_PURCHASE_STATUS = "status";
    String COLUMN_PURCHASE_DATE_ADDED = "date_added";
    String COLUMN_PURCHASE_DATETIME = "datetime";
    String COLUMN_BILL_IMAGE_NAME = "image_name";

    // Purchase Products Details
    String TABLE_AS_TEMP_PURCHASE_DETAIL = "as_temp_purchase_detail";
    String COLUMN_PURCHASE_DETAIL_ID = "purchase_detail_id";
    String COLUMN_PURCHASE_DETAIL_PRODUCT_BATCH_ID = "product_batch_id";
    String COLUMN_PURCHASE_DETAIL_RATE = "purchase_rate";
    String COLUMN_PURCHASE_DETAIL_MRP = "mrp";
    String COLUMN_PURCHASE_DETAIL_SP = "sp";
    String COLUMN_PURCHASE_DETAIL_QUANTITY = "quantity";
    String COLUMN_PURCHASE_DETAIL_TAX_PERCENTAGE = "tax_percentage";
    String COLUMN_PURCHASE_DETAIL_CREATED_AT = "created_at";
    String COLUMN_PURCHASE_DETAIL_CGST_RATE = "cgst_rate";
    String COLUMN_PURCHASE_DETAIL_SGST_RATE = "sgst_rate";
    String COLUMN_PURCHASE_DETAIL_IGST_RATE = "igst_rate";
    String COLUMN_PURCHASE_DETAIL_CESS_RATE = "cess_rate";
    String COLUMN_PURCHASE_DETAIL_TOTAL_AMOUNT = "total_amt";
    String COLUMN_PURCHASE_DETAIL_TOTAL_TAX_AMOUNT = "total_tax_amt";
    String COLUMN_PURCHASE_DETAIL_BATCH_NO = "batch_no";
    String COLUMN_PURCHASE_DETAIL_NEWPURCHASE_PRICE = "new_purchase";

    // String COLUMN_PURCHASE_DETAIL_IMAGE_URL = "image_url";

    // Purchase Entry Bill Image Details
    String TABLE_AS_TEMP_PURCHASE_BILL_IMAGE = "as_purchase_bill_image";
    String COLUMN_PURCHASE_BILL_IMAGE_ID = "purchase_bill_image_id";
    String COLUMN_PURCHASE_IMAGE_NAME = "image_name";
    String COLUMN_PURCHASE_IMAGE_SIZE = "image_size";
    String COLUMN_PURCHASE_IMAGE = "image";

    // Purchase Entry Product Free Products Detail
    String TABLE_AS_TEMP_PURCHASE_DETAIL_FREE_PRODUCTS = "as_purchase_detail_free_products";
    String COLUMN_PURCHASE_DETAIL_FREE_PRODUCT_ID = "purchase_detail_free_product_id";
    String COLUMN_FREE_PRODUCT_BATCH_ID = "product_batch_id";
    String COLUMN_Free_PRODUCT_QTY = "qty";

    // Tax Schedules
    String TABLE_AS_TAX_SCHEDULES = "as_merchant_store_tax_schedule";
    String COLUMN_TAX_SCHEDULE_ID = "tax_schedule_id";
    String COLUMN_TAX_SCHEDULE_MERCHANT_STORE_ID = "merchant_store_id";
    String COLUMN_TAX_SCHEDULE_TITLE = "tax_schedule_title";
    String COLUMN_TAX_SCHEDULE_PERCENTAGE = "tax_schedule_percentage";
    String COLUMN_TAX_SCHEDULE_ACTIVE = "active";
    String COLUMN_TAX_SCHEDULE_UPDATED_DATE = "last_updated_date";

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
