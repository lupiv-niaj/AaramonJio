package com.aaramon.aaramonjio.order;

public class ProductMultipleBatchModel {
    private String ProductName;
    private String ProductImage;
    private String ProductId;
    double sgst;
    double igst;
    double cgst;


    private String BatchNo;
    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getIgst() {
        return igst;
    }

    public void setIgst(double igst) {
        this.igst = igst;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public double getCess() {
        return cess;
    }

    public void setCess(double cess) {
        this.cess = cess;
    }

    double cess;
    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    Boolean check;

    public String getBatchNo() {
        return BatchNo;
    }

    public void setBatchNo(String batchNo) {
        BatchNo = batchNo;
    }

    public Long getProductBatchId() {
        return ProductBatchId;
    }

    public void setProductBatchId(Long productBatchId) {
        ProductBatchId = productBatchId;
    }

    private Long ProductBatchId;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public Double getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(Double productPrice) {
        ProductPrice = productPrice;
    }

    public Double getOfferPrice() {
        return OfferPrice;
    }

    public void setOfferPrice(Double offerPrice) {
        OfferPrice = offerPrice;
    }

    public String getOfferStartDate() {
        return OfferStartDate;
    }

    public void setOfferStartDate(String offerStartDate) {
        OfferStartDate = offerStartDate;
    }

    public String getOfferEndDate() {
        return OfferEndDate;
    }

    public void setOfferEndDate(String offerEndDate) {
        OfferEndDate = offerEndDate;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public Integer getItem() {
        return Item;
    }

    public void setItem(Integer item) {
        Item = item;
    }

    public Double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        TotalAmount = totalAmount;
    }

    public Integer getTotalItems() {
        return TotalItems;
    }

    public void setTotalItems(Integer totalItems) {
        TotalItems = totalItems;
    }

    private Double ProductPrice;
    private Double OfferPrice;
    private String OfferStartDate;
    private String OfferEndDate;
    private Double Amount;
    private Integer Item;
    private Double TotalAmount;
    private Integer TotalItems;

}
