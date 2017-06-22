package com.aaramon.aaramonjio.supplier;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aaramon.aaramonjio.dataaccess.AppController;
import com.aaramon.aaramonjio.dataaccess.SharedPreference_Main;
import com.aaramon.aaramonjio.syncproduct.QueryClass;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aaramshop on 5/17/2017.
 */

public class SupplierService {

    android.widget.ListView ListView;
    SupplierBillDetailAdapter BaseAdapter;
    TextView[] HeaderArray;
    PurchaseEntryModel PurchaseEntryModel;

    // Business Methods
    public void DeletePurchaseEntryDetail(long PurchaseEntryDetailId) throws Exception {
        QueryClass Query = new QueryClass();
        Query.DeleteProductEntryDetail(PurchaseEntryDetailId);
    }

    public void PopulateSupplierBillsFromAPI(final int SupplierId,
                                             final Context context, android.widget.ListView SupplierBillDetailsListView,
                                             final SupplierBillDetailAdapter SupplierBillDetailAdapter,
                                             TextView[] SupplierHeaderArray, final String supplier_code
    ) {
        ListView = SupplierBillDetailsListView;
        BaseAdapter = SupplierBillDetailAdapter;
        HeaderArray = SupplierHeaderArray;

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please Wait...");
        pDialog.setCancelable(false);
        pDialog.show();
        String APIUrl = "http://www.aaramshop.co.in:80/api/index.php/Supplier/getPurchaseEntry";
        StringRequest Request = new StringRequest(
                com.android.volley.Request.Method.POST,
                APIUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("APIResponse ", response);
                            pDialog.cancel();
                            JSONObject ResponseObject = new JSONObject(response);
                            JSONObject Control = ResponseObject.getJSONObject("Control");

                            if (Control.getString("Status").trim().equals("1")) {
                                SupplierModel SupplierModel = new SupplierModel();
                                JSONObject Data = ResponseObject.getJSONObject("Data");
                                GenerateSupplier(SupplierModel, Data, supplier_code);
                                GenerateSupplierBills(SupplierModel, Data.getJSONArray("PurchaseEntries"));
                                BaseAdapter.setSupplierModel(SupplierModel);
                                ListView.setAdapter(BaseAdapter);
                                SetSupplierHeader(HeaderArray, SupplierModel);
                                if (Double.parseDouble(HeaderArray[4].getText().toString()) <= 0) {
                                    HeaderArray[5].setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(context, Control.getString("Message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("Exception API", e.getMessage());
                            //throw new Exception(e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // pDialog.cancel();
                pDialog.cancel();
                VolleyLog.e("TAG", "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreference_Main sharedPreference_Main = new SharedPreference_Main(context);
                Map<String, String> params = new HashMap<String, String>();
                params.put("AaramShopMagicKey", "AaramShop@Android$vipul#dinesh|||6364");
                params.put("DeviceId", sharedPreference_Main.getDeviceId());
                params.put("DeviceType", "2");
                params.put("MerchantStoreId", sharedPreference_Main.getStoreId());
                params.put("PageNo", "0");
                params.put("SupplierId", String.valueOf(SupplierId));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(Request);
    }

    public void GenerateSupplierBills(SupplierModel SupplierModel, JSONArray PurchaseEnteries) throws Exception {
        for (int i = 0; i < PurchaseEnteries.length(); i++) {
            SupplierBillModel SupplierBillModel = new SupplierBillModel();
            JSONObject PurchaseEntry = PurchaseEnteries.getJSONObject(i);

            // Get DateTime From TimeStamp
            SupplierBillModel.setPurchaseEntryId(PurchaseEntry.getInt("PurchaseEntryId"));
            Date BillDate = UtilityService.GetDateTimeFromTimeStamp(Long.parseLong(PurchaseEntry.getString("BillDate")));
            SupplierBillModel.setMonth((String) DateFormat.format("MMM", BillDate));
            SupplierBillModel.setDay(Integer.parseInt((String) DateFormat.format("dd", BillDate)));
            SupplierBillModel.setBillNo(PurchaseEntry.getString("BillNo"));
            SupplierBillModel.setPurchaseType(PurchaseEntry.getString("PurchaseType"));
            SupplierBillModel.setItemsCount(Integer.parseInt(PurchaseEntry.getString("TotalItems")));
            SupplierBillModel.setBillAmount(Double.parseDouble(PurchaseEntry.getString("OrderAmount")));
            SupplierModel.getSupplierBillList().add(SupplierBillModel);
        }
    }

    public void GenerateSupplier(SupplierModel SupplierModel, JSONObject SupplierObject, String supplier_Code) throws Exception {
        SupplierModel.setSupplierId(Integer.parseInt(SupplierObject.getString("SupplierId")));
        SupplierModel.setSuppplierName(SupplierObject.getString("SupplierName"));
        SupplierModel.setSupplierAddress(SupplierObject.getString("SupplierAddress"));
        SupplierModel.setSupplierMobile(SupplierObject.getString("SupplierMobile"));
        SupplierModel.setSupplierCode(supplier_Code);
        SupplierModel.setSupplierOutstanding(Double.parseDouble(SupplierObject.getString("SupplierOutstanding")));

    }

    public void SetSupplierHeader(TextView[] SupplierHeaderArray, SupplierModel SupplierModel) {
        SupplierHeaderArray[0].setText(SupplierModel.getSuppplierName());
        SupplierHeaderArray[1].setText(SupplierModel.getSuppplierName());
        SupplierHeaderArray[2].setText(SupplierModel.getSupplierAddress());
        SupplierHeaderArray[3].setText(SupplierModel.getSupplierMobile());
        SupplierHeaderArray[4].setText(String.format("%1$.2f", SupplierModel.getSupplierOutstanding()));

    }

    public int insertPurchaseEntry(PurchaseEntryModel PurchaseEntryModel, int MerchantStoreId) throws Exception {
        try {
            Log.d("POPO", "in service");
            QueryClass Query = new QueryClass();
            int value = Query.insertPurchaseEntry(PurchaseEntryModel, MerchantStoreId);
            ;
            Log.d("Valiueambdf", value + "");
            return value;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public int insertPurchaseEntryDetail(PurchaseEntryDetailModel PurchaseEntryDetailModel) throws Exception {
        try {
            Log.d("SCAN1975", "insertPurchaseEntryDetail Service");
            QueryClass Query = new QueryClass();
            return Query.insertPurchaseEntryDetail(PurchaseEntryDetailModel);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public ArrayList<TaxScheduleModel> getTaxScheduleList(int Status, int MerchantStoreId) throws Exception {
        QueryClass Query = new QueryClass();
        ArrayList<TaxScheduleModel> TaxScheduleList = new ArrayList<TaxScheduleModel>();
        try {
            if (Query.haveTaxSchedules() == false) {
                Query.insertTaxSchedules(MerchantStoreId);
            }
            TaxScheduleList = Query.getTaxSchedules(Status);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return TaxScheduleList;
    }

//    public void InsertDummyProducts(int MerchantStoreId) throws Exception
//    {
//        QueryClass Query = new QueryClass();
//        try
//        {
//                if (Query.haveProducts() == false)
//                {
//                    // Insert First Product
//                     long ProductId =   Query.InsertProduct("0",0,0,"Dabur Amla 200gm", "120", "8901157004205","8901157004205","");
//                     long  ProductBarCodeId =  Query.InsertProductBarcode("8901157004205", ProductId);
//                     long ProductBatchId = Query.InsertProductBatch(String.valueOf(MerchantStoreId),"1", "D001", "2017-06-23", "100", "120", "120", "0", "0001-01-01", "0001-01-01", "0", ProductBarCodeId, ProductId);
//                     Log.d("First", ProductId + "-" + ProductBatchId + "-" + ProductBarCodeId);
//
//                    // Insert Second Product
//                    ProductId =   Query.InsertProduct("0",0,0,"Pepsi 1 ltr", "25", "8901157000214","8901157000214","");
//                    ProductBarCodeId =  Query.InsertProductBarcode("8901157000214", ProductId);
//                    ProductBatchId = Query.InsertProductBatch(String.valueOf(MerchantStoreId),"1", "P001", "2017-06-23", "100", "120", "120", "0", "0001-01-01", "0001-01-01", "0", ProductBarCodeId, ProductId);
//                    Log.d("Second", ProductId + "-" + ProductBatchId + "-" + ProductBarCodeId);
//
//                    // Insert Third Product
//                    ProductId =   Query.InsertProduct("0",0,0,"Amul Butter 100 gm", "20", "8901023006647","8901023006647","");
//                    ProductBarCodeId =  Query.InsertProductBarcode("8901023006647", ProductId);
//                    ProductBatchId = Query.InsertProductBatch(String.valueOf(MerchantStoreId),"1", "A001", "2017-06-23", "18", "20", "18", "1", "2017-05-23", "2017-06-23", "0", ProductBarCodeId, ProductId);
//                    Log.d("Third", ProductId + "-" + ProductBatchId + "-" + ProductBarCodeId);
//
//                    // Insert Fourth Product
//                    ProductId =   Query.InsertProduct("0",0,0,"Dettol Handwash 200ml", "80", "8901314114464","8901314114464","");
//                    ProductBarCodeId =  Query.InsertProductBarcode("8901314114464", ProductId);
//                    ProductBatchId = Query.InsertProductBatch(String.valueOf(MerchantStoreId),"1", "D002", "2017-06-23", "75", "80", "79", "1", "2017-05-23", "2017-06-23", "0", ProductBarCodeId, ProductId);
//                    Log.d("Fourth", ProductId + "-" + ProductBatchId + "-" + ProductBarCodeId);
//
//                    // Insert Fifth Product
//                    ProductId =   Query.InsertProduct("0",0,0,"Pears Soap 75 gm", "36", "8901023003790","8901023003790","");
//                    ProductBarCodeId =  Query.InsertProductBarcode("8901023003790", ProductId);
//                    ProductBatchId = Query.InsertProductBatch(String.valueOf(MerchantStoreId),"1", "P002", "2017-06-23", "32", "36", "36", "0", "0001-01-01", "0001-01-01", "0", ProductBarCodeId, ProductId);
//                    Log.d("Fifth", ProductId + "-" + ProductBatchId + "-" + ProductBarCodeId);
//                }
//        }
//        catch (Exception e)
//        {
//            throw new Exception(e.getMessage());
//        }
//    }

    public String GetProductStringPurchase(String EanCode) throws Exception {
        QueryClass Query = new QueryClass();
        try {
            return Query.GetProductPurchase(EanCode);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String[] GetProductStringPurchaseSearch(String ProductName) throws Exception {
        QueryClass Query = new QueryClass();
        try {
            return Query.GetProductPurchaseSearch(ProductName);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String GetProductEanCode(String ProductName) throws Exception {
        QueryClass Query = new QueryClass();
        try {
            return Query.GetProductEanCode(ProductName);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String GetPurchaseEntryDetail(int PurchasEntryId) throws Exception {
        QueryClass Query = new QueryClass();
        try {
            return Query.GetPurchaseEntry(PurchasEntryId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Long ProductBatchExists(long ProductBatchId, long PurchaseEntryId) throws Exception {
        try {


            QueryClass Query = new QueryClass();
            return Query.ProductBatchExists(ProductBatchId, PurchaseEntryId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String GetPurchaseEntryProductDetail(int PurchaseEntryDetailId) throws Exception {
        QueryClass Query = new QueryClass();
        try {
            return Query.GetPurchaseEntryDetail(PurchaseEntryDetailId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void ChangeBatchStock(long ProductBatchId, int Stock, int Action, int ExistingStock) throws Exception {
        QueryClass Query = new QueryClass();
        try {
            Query.ChangeBatchStock(ProductBatchId, Stock, Action, ExistingStock);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String GeneratePurchaseEntryJson(PurchaseEntryModel PurchaseEntryModel) throws Exception {
        JSONObject PurchaseEntryObject = new JSONObject();
        PurchaseEntryObject.put("RetailerBillNo", String.valueOf(PurchaseEntryModel.getBillNumber()));
        PurchaseEntryObject.put("PurchaseType", PurchaseEntryModel.getPaymentMode().toLowerCase().equals("cash") ? 1 : 2);
        int PaymentTerm = 0;

        if (PurchaseEntryModel.getPaymentTerm().toLowerCase().equals("due in 7 days")) {
            PaymentTerm = 7;
        } else if (PurchaseEntryModel.getPaymentTerm().toLowerCase().equals("due in 15 days")) {
            PaymentTerm = 15;
        } else if (PurchaseEntryModel.getPaymentTerm().toLowerCase().equals("due in 1 month")) {
            PaymentTerm = 30;
        } else {
            PaymentTerm = 0;
        }
        PurchaseEntryObject.put("PurchaseTerm", String.valueOf(PaymentTerm));
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        PurchaseEntryObject.put("PurchaseDate", PurchaseEntryModel.getBillDate());
        PurchaseEntryObject.put("TotalAmount", String.valueOf(PurchaseEntryModel.getTotalAmount()));
        PurchaseEntryObject.put("TotalTax", (PurchaseEntryModel.getTax() == null) ? 0 : PurchaseEntryModel.getTax());

        JSONArray PurchaseDetails = new JSONArray();
        for (PurchaseEntryDetailModel PurchaseEntryDetail : PurchaseEntryModel.getProductEntryDetailList()) {
            JSONObject PurchaseDetailObject = new JSONObject();
            PurchaseDetailObject.put("ServerMerchantStoreProductId", String.valueOf(PurchaseEntryDetail.getProductBatchModel().getProductModel().getServerProductId()));
            PurchaseDetailObject.put("BatchNo", String.valueOf(PurchaseEntryDetail.getProductBatchModel().getBatchNo()));
            PurchaseDetailObject.put("ProductName", String.valueOf(PurchaseEntryDetail.getProductBatchModel().getProductModel().getProductName()));
            PurchaseDetailObject.put("ExpiryDate", PurchaseEntryDetail.getProductBatchModel().getExpiry());
            PurchaseDetailObject.put("PurchasePrice", String.valueOf(PurchaseEntryDetail.getPurchaseRate()));
            PurchaseDetailObject.put("ProductPrice", String.valueOf(PurchaseEntryDetail.getMRP()));
            PurchaseDetailObject.put("SellingPrice", String.valueOf(PurchaseEntryDetail.getSP()));
            PurchaseDetailObject.put("Quantity", String.valueOf(PurchaseEntryDetail.getQty()));
            PurchaseDetailObject.put("CESSRate",String.valueOf(PurchaseEntryDetail.getCess()));
            PurchaseDetailObject.put("CGSTRate",String.valueOf(PurchaseEntryDetail.getCgst()));
            PurchaseDetailObject.put("SGSTRate",String.valueOf(PurchaseEntryDetail.getSgst()));
            PurchaseDetailObject.put("IGSTRate",String.valueOf(PurchaseEntryDetail.getIgst()));
//            PurchaseDetailObject.put("BatchId",String.valueOf(PurchaseEntryDetail.getBatchNo()));
            PurchaseDetailObject.put("TaxPercentage", (PurchaseEntryDetail.getProductBatchModel().getProductModel().getTaxScheduleModel().getTaxPercentage() == null)
                    ? 0
                    : PurchaseEntryDetail.getProductBatchModel().getProductModel().getTaxScheduleModel().getTaxPercentage());

            JsonArray FreeItemObject = new JsonArray();
            PurchaseDetailObject.put("FreeItems", FreeItemObject);
            PurchaseDetails.put(PurchaseDetailObject);
        }
        PurchaseEntryObject.put("PurchaseDetails", PurchaseDetails);
        Log.d("DHOL", PurchaseEntryObject.toString());
        return PurchaseEntryObject.toString();
    }

    public void ChangeProductBatchesStock(ArrayList<PurchaseEntryDetailModel> PurchaseEntryDetailModelList) throws Exception {
        QueryClass Query = new QueryClass();
        for (PurchaseEntryDetailModel PurchaseEntryDetailModel : PurchaseEntryDetailModelList) {
            int ExistingStock = Query.GetProductBatchStock(PurchaseEntryDetailModel.getProductBatchModel().getProductBatchId());
            Query.ChangeBatchStock(PurchaseEntryDetailModel.getProductBatchModel().getProductBatchId(), PurchaseEntryDetailModel.getQty(), ProductBatchModel.ADDBATCH, ExistingStock);
            Log.d("VIJAY", "UPDATED BATCH");

            // Cross Check Batch Stock
            //Double Stock = Query.GetProductBatchStock(PurchaseEntryDetailModel.getProductBatchModel().getProductBatchId());
            //Log.d("VIJAY", Stock+"");
        }
    }


    public void ClearTableData(String TableName) throws Exception {
        QueryClass Query = new QueryClass();
        Query.ClearTableData(TableName);
    }

    public void UpdateProductBatch(ProductBatchModel ProductBatchModel) throws Exception {
        QueryClass Query = new QueryClass();
        Query.UpdateProductBatch(ProductBatchModel);
    }

    public void UpdateProductBarCode(ProductBatchModel ProductBatchModel) throws Exception {
        QueryClass Query = new QueryClass();
        Query.UpdateProductBarCode(ProductBatchModel);
    }

    public void UpdatePurchaseEntryDetail(PurchaseEntryDetailModel PurchaseEntryDetailModel) throws Exception {
        QueryClass Query = new QueryClass();
        Query.UpdateProductEntryDetail(PurchaseEntryDetailModel);
    }

    public String[] GetTotalItemsAndAmountPurchaseEntry(long PurchaseEntryId) throws Exception {
        QueryClass Query = new QueryClass();
        return Query.GetTotalItemsAndAmountPurchaseEntry(PurchaseEntryId);
    }

    public void UpdatePurchaseEntryQty(long PurchaseEntryDetailId, int Qty, double total_amt, double tax_amt) throws Exception {
        QueryClass Query = new QueryClass();
        Query.UpdatePurchaseEntryQty(PurchaseEntryDetailId, Qty,total_amt,tax_amt);
    }

}
