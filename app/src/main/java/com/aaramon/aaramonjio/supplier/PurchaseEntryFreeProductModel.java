package com.aaramon.aaramonjio.supplier;

/**
 * Created by Aaramshop on 5/20/2017.
 */

public class PurchaseEntryFreeProductModel
{
    private long PurchaseFreeProductId;
    private long PurchaseDetailId;
    private long ProductBatchId;
    private Double Qty;

    public long getPurchaseFreeProductId() {
        return PurchaseFreeProductId;
    }

    public void setPurchaseFreeProductId(long purchaseFreeProductId) {
        PurchaseFreeProductId = purchaseFreeProductId;
    }

    public long getPurchaseDetailId() {
        return PurchaseDetailId;
    }

    public void setPurchaseDetailId(long purchaseDetailId) {
        PurchaseDetailId = purchaseDetailId;
    }

    public long getProductBatchId() {
        return ProductBatchId;
    }

    public void setProductBatchId(long productBatchId) {
        ProductBatchId = productBatchId;
    }

    public Double getQty() {
        return Qty;
    }

    public void setQty(Double qty) {
        Qty = qty;
    }
}
