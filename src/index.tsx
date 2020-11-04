import { NativeModules } from 'react-native';


const { RNMy2c2pSdk } = NativeModules;

type PaymentRequest = {
  request3DS: 'Y' | 'N'
  paymentUI: boolean
  storeCard: false
  payCategoryID: string
  userDefined1: string
  userDefined2: string
  userDefined3: string
  userDefined4: string
  userDefined5: string
}

type PaymentRequestFunction = (params: PaymentRequest) => void

type RNMy2c2pSdkType = {
  init: (privateKey: string, production: boolean) => void
  requestPayment: PaymentRequestFunction
  requestRecurringPayment: PaymentRequestFunction
  requestInstallmentPayment: PaymentRequestFunction
  requestAlternativePayment: PaymentRequestFunction
  requestPaymentChannel: PaymentRequestFunction
};

const defaultPaymentRequest: PaymentRequest = {
  request3DS: 'Y',
  paymentUI: false,
  storeCard: false,
  payCategoryID: '',
  userDefined1: '',
  userDefined2: '',
  userDefined3: '',
  userDefined4: '',
  userDefined5: ''
};

export default {
  init: function(privateKey: string, production: boolean) {
    RNMy2c2pSdk.setup(privateKey, production);
  },

  requestPayment: function(params: PaymentRequest) {
    var payment = Object.assign({}, defaultPaymentRequest, params);
    return RNMy2c2pSdk.requestPayment(payment);
  },

  requestRecurringPayment: function(params: PaymentRequest) {
    var payment = Object.assign({}, defaultPaymentRequest, params);
    return RNMy2c2pSdk.requestRecurringPayment(payment);
  },

  requestInstallmentPayment: function(params: PaymentRequest) {
    var payment = Object.assign({}, defaultPaymentRequest, params);
    return RNMy2c2pSdk.requestInstallmentPayment(payment);
  },

  requestAlternativePayment: function(params: PaymentRequest) {
    var payment = Object.assign({}, defaultPaymentRequest, params);
    return RNMy2c2pSdk.requestAlternativePayment(payment);
  },

  requestPaymentChannel: function(params: PaymentRequest) {
    var payment = Object.assign({}, defaultPaymentRequest, params);
    return RNMy2c2pSdk.requestPaymentChannel(payment);
  }
} as RNMy2c2pSdkType;