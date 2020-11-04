package com.reactnativemy2c2psdk

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.ccpp.my2c2psdk.cores.My2c2pResponse
import com.ccpp.my2c2psdk.cores.My2c2pSDK
import com.ccpp.my2c2psdk.cores.My3DSActivity
import com.facebook.react.bridge.*


class RNMy2c2pSdkModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
      return "RNMy2c2pSdk"
  }

  private val TAG = "RNMy2c2pSdkModule"
  private val REQUEST_SDK = 1
  private val API_VERSION = "9.1"
  private val ACTIVITY_DOES_NOT_EXIST = "ACTIVITY_DOES_NOT_EXIST"
  private val PAYMENT_REQUEST_ERROR = "PAYMENT_REQUEST_ERROR"
  private val NO_RESPONSE = "NO_RESPONSE"
  private val TRANSACTION_CANCELED = "TRANSACTION_CANCELED"

  private lateinit var my2c2pSDK: My2c2pSDK
  private var promise: Promise? = null

  private val activityEventListener: ActivityEventListener = object : BaseActivityEventListener() {
    override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent) {
      if (requestCode == REQUEST_SDK) {
        Log.d(TAG, "result code: $resultCode")
        val response: My2c2pResponse = data.extras!!.getParcelable(My2c2pResponse.RESPONSE)!!
        if (response.respCode == "301") {
          Log.d(TAG, "transaction canceled$resultCode")
          promise!!.reject(TRANSACTION_CANCELED, "Transaction is canceled")
          return
        }
        Log.d(TAG, "response" + response.toString())
        val result = Arguments.createMap()
        result.putString("version", response.version)
        result.putString("timeStamp", response.timeStamp)
        result.putString("merchantID", response.merchantID)
        result.putString("respCode", response.respCode)
        result.putString("pan", response.pan)
        result.putString("amount", response.amount)
        result.putString("uniqueTransactionCode", response.uniqueTransactionCode)
        result.putString("tranRef", response.tranRef)
        result.putString("approvalCode", response.approvalCode)
        result.putString("refNumber", response.refNumber)
        result.putString("dateTime", response.dateTime)
        result.putString("eci", response.eci)
        result.putString("status", response.status)
        result.putString("failReason", response.failReason)
        result.putString("userDefined1", response.userDefined1)
        result.putString("userDefined2", response.userDefined2)
        result.putString("userDefined3", response.userDefined3)
        result.putString("userDefined4", response.userDefined4)
        result.putString("userDefined5", response.userDefined5)
        result.putString("storeCardUniqueID", response.storeCardUniqueID)
        result.putString("recurringUniqueID", response.recurringUniqueID)
        result.putString("hashValue", response.hashValue)
        result.putString("ippPeriod", response.ippPeriod)
        result.putString("ippInterestType", response.ippInterestType)
        result.putString("ippInterestRate", response.ippInterestRate)
        result.putString("ippMerchantAbsorbRate", response.ippMerchantAbsorbRate)
        result.putString("paidChannel", response.paidChannel)
        result.putString("paidAgent", response.paidAgent)
        result.putString("paymentChannel", response.paymentChannel)
        result.putString("backendInvoice", response.backendInvoice)
        result.putString("issuerCountry", response.issuerCountry)
        result.putString("bankName", response.bankName)
        result.putString("raw", response.raw)
        promise!!.resolve(result)
      }
    }
  }

  init {
    this.reactApplicationContext.addActivityEventListener(activityEventListener)
  }

  @ReactMethod
  fun setup(privateKey: String?, productionMode: Boolean = false) {
    my2c2pSDK = My2c2pSDK(privateKey).apply {
      version = API_VERSION
    }
    my2c2pSDK.productionMode = productionMode
  }

  @ReactMethod
  fun requestPayment(params: ReadableMap, promise: Promise) {
    try {
      this.promise = promise
      setMandatoryFields(params)
      setCardInfoFields(params)
      setOptionalFields(params)
      sendRequest()
    } catch (e: Exception) {
      Log.e(TAG, PAYMENT_REQUEST_ERROR, e)
      promise.reject(PAYMENT_REQUEST_ERROR, e.message, e)
    }
  }

  @ReactMethod
  fun requestRecurringPayment(params: ReadableMap, promise: Promise) {
    try {
      this.promise = promise
      setMandatoryFields(params)
      setCardInfoFields(params)
      setRecurringFields(params)
      setOptionalFields(params)
      sendRequest()
    } catch (e: Exception) {
      Log.e(TAG, PAYMENT_REQUEST_ERROR, e)
      promise.reject(PAYMENT_REQUEST_ERROR, e.message, e)
    }
  }

  @ReactMethod
  fun requestInstallmentPayment(params: ReadableMap, promise: Promise) {
    try {
      this.promise = promise
      setMandatoryFields(params)
      setCardInfoFields(params)
      setInstallmentFields(params)
      setOptionalFields(params)
      sendRequest()
    } catch (e: Exception) {
      Log.e(TAG, PAYMENT_REQUEST_ERROR, e)
      promise.reject(PAYMENT_REQUEST_ERROR, e.message, e)
    }
  }

  @ReactMethod
  fun requestAlternativePayment(params: ReadableMap, promise: Promise) {
    try {
      this.promise = promise
      setMandatoryFields(params)
      setAlternativePaymentFields(params)
      setOptionalFields(params)
      sendRequest()
    } catch (e: Exception) {
      Log.e(TAG, PAYMENT_REQUEST_ERROR, e)
      promise.reject(PAYMENT_REQUEST_ERROR, e.message, e)
    }
  }

  @ReactMethod
  fun requestPaymentChannel(params: ReadableMap, promise: Promise) {
    try {
      this.promise = promise
      setMandatoryFields(params)
      setPaymentChannelFields(params)
      setOptionalFields(params)
    } catch (e: Exception) {
      Log.e(TAG, PAYMENT_REQUEST_ERROR, e)
      promise.reject(PAYMENT_REQUEST_ERROR, e.message, e)
    }
  }

  private fun setMandatoryFields(params: ReadableMap) {
    my2c2pSDK.merchantID = params.getString("merchantID")
    my2c2pSDK.uniqueTransactionCode = params.getString("uniqueTransactionCode")
    my2c2pSDK.desc = params.getString("desc")
    my2c2pSDK.amount = params.getDouble("amount")
    my2c2pSDK.currencyCode = params.getString("currencyCode")
    my2c2pSDK.secretKey = params.getString("secretKey")
    my2c2pSDK.paymentUI = params.getBoolean("paymentUI")
  }

  private fun setCardInfoFields(params: ReadableMap) {
    val paymentUI = params.getBoolean("paymentUI")
    // credit card information (optional when payment with UI)
    if (!paymentUI) {
      my2c2pSDK.pan = ReadableMapUtil.getString(params, "pan")
      my2c2pSDK.cardHolderName = ReadableMapUtil.getString(params, "cardHolderName")
      my2c2pSDK.cardHolderEmail = ReadableMapUtil.getString(params, "cardHolderEmail")
      my2c2pSDK.panBank = ReadableMapUtil.getString(params, "panBank")
      my2c2pSDK.panCountry = ReadableMapUtil.getString(params, "panCountry")
      my2c2pSDK.securityCode = ReadableMapUtil.getString(params, "securityCode")
      my2c2pSDK.cardExpireMonth = String.format("%02d", ReadableMapUtil.getInt(params, "cardExpireMonth"))
      my2c2pSDK.cardExpireYear = String.format("%04d", ReadableMapUtil.getInt(params, "cardExpireYear"))
    }
    // store card
    my2c2pSDK.storeCard = ReadableMapUtil.getBoolean(params, "storeCard", false)
    my2c2pSDK.storedCardUniqueID = ReadableMapUtil.getString(params, "storedCardUniqueID")
    my2c2pSDK.request3DS = ReadableMapUtil.getString(params, "request3DS")
  }

  private fun setRecurringFields(params: ReadableMap) {
    // mandatory for recurring
    my2c2pSDK.recurring = true
    my2c2pSDK.invoicePrefix = params.getString("invoicePrefix")
    my2c2pSDK.recurringAmount = params.getDouble("recurringAmount")
    my2c2pSDK.recurringCount = params.getInt("recurringCount")
    my2c2pSDK.recurringInterval = params.getInt("recurringInterval")
    val allowAccumulate = ReadableMapUtil.getBoolean(params, "allowAccumulate", false)
    my2c2pSDK.allowAccumulate = allowAccumulate
    if (allowAccumulate) {
      my2c2pSDK.maxAccumulateAmt = params.getDouble("maxAccumulateAmt")
    }
    my2c2pSDK.chargeNextDate = ReadableMapUtil.getString(params, "chargeNextDate")
    my2c2pSDK.promotion = ReadableMapUtil.getString(params, "promotion")
    my2c2pSDK.statementDescriptor = ReadableMapUtil.getString(params, "statementDescriptor")
  }

  private fun setInstallmentFields(params: ReadableMap) {
    my2c2pSDK.ippTransaction = true
    my2c2pSDK.installmentPeriod = params.getInt("installmentPeriod")
    my2c2pSDK.interestType = params.getString("interestType")
  }

  private fun setAlternativePaymentFields(params: ReadableMap) {
    val paymentChannel: My2c2pSDK.PaymentChannel = My2c2pSDK.PaymentChannel.valueOf(params.getString("paymentChannel")!!)
    Log.d(TAG, "Payment channel=$paymentChannel")
    my2c2pSDK.paymentChannel = paymentChannel
    my2c2pSDK.cardHolderName = ReadableMapUtil.getString(params, "cardHolderName")
    my2c2pSDK.cardHolderEmail = ReadableMapUtil.getString(params, "cardHolderEmail")
    my2c2pSDK.agentCode = ReadableMapUtil.getString(params, "agentCode")
    my2c2pSDK.channelCode = ReadableMapUtil.getString(params, "channelCode")
    my2c2pSDK.paymentExpiry = ReadableMapUtil.getString(params, "paymentExpiry")
    my2c2pSDK.mobileNo = ReadableMapUtil.getString(params, "mobileNo")
  }

  private fun setPaymentChannelFields(params: ReadableMap) {
    val paymentChannel: My2c2pSDK.PaymentChannel = My2c2pSDK.PaymentChannel.valueOf(params.getString("paymentChannel")!!)
    Log.d(TAG, "Payment channel=$paymentChannel")
    my2c2pSDK.paymentChannel = paymentChannel
  }

  private fun setOptionalFields(params: ReadableMap) {
    my2c2pSDK.payCategoryID = ReadableMapUtil.getString(params, "payCategoryID")
    my2c2pSDK.userDefined1 = ReadableMapUtil.getString(params, "userDefined1")
    my2c2pSDK.userDefined2 = ReadableMapUtil.getString(params, "userDefined2")
    my2c2pSDK.userDefined3 = ReadableMapUtil.getString(params, "userDefined3")
    my2c2pSDK.userDefined4 = ReadableMapUtil.getString(params, "userDefined4")
    my2c2pSDK.userDefined5 = ReadableMapUtil.getString(params, "userDefined5")
    my2c2pSDK.statementDescriptor = ReadableMapUtil.getString(params, "statementDescriptor")
  }

  private fun sendRequest() {
    // execute the library using intent
    val currentActivity = currentActivity
    if (currentActivity == null) {
      promise!!.reject(ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist")
      return
    }
    val intent = Intent(currentActivity, My3DSActivity::class.java)
    my2c2pSDK.panBank = ""
    intent.putExtra(My2c2pSDK.PARAMS, my2c2pSDK)
    currentActivity.startActivityForResult(intent, REQUEST_SDK)
  }

}
