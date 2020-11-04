"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = void 0;

var _reactNative = require("react-native");

const {
  RNMy2c2pSdk
} = _reactNative.NativeModules;
const defaultPaymentRequest = {
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
var _default = {
  init: function (privateKey, production) {
    RNMy2c2pSdk.setup(privateKey, production);
  },
  requestPayment: function (params) {
    var payment = Object.assign({}, defaultPaymentRequest, params);
    return RNMy2c2pSdk.requestPayment(payment);
  },
  requestRecurringPayment: function (params) {
    var payment = Object.assign({}, defaultPaymentRequest, params);
    return RNMy2c2pSdk.requestRecurringPayment(payment);
  },
  requestInstallmentPayment: function (params) {
    var payment = Object.assign({}, defaultPaymentRequest, params);
    return RNMy2c2pSdk.requestInstallmentPayment(payment);
  },
  requestAlternativePayment: function (params) {
    var payment = Object.assign({}, defaultPaymentRequest, params);
    return RNMy2c2pSdk.requestAlternativePayment(payment);
  },
  requestPaymentChannel: function (params) {
    var payment = Object.assign({}, defaultPaymentRequest, params);
    return RNMy2c2pSdk.requestPaymentChannel(payment);
  }
};
exports.default = _default;
//# sourceMappingURL=index.js.map