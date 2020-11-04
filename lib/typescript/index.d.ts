declare type PaymentRequest = {
    request3DS: 'Y' | 'N';
    paymentUI: boolean;
    storeCard: false;
    payCategoryID: string;
    userDefined1: string;
    userDefined2: string;
    userDefined3: string;
    userDefined4: string;
    userDefined5: string;
};
declare type PaymentRequestFunction = (params: PaymentRequest) => void;
declare type RNMy2c2pSdkType = {
    init: (privateKey: string, production: boolean) => void;
    requestPayment: PaymentRequestFunction;
    requestRecurringPayment: PaymentRequestFunction;
    requestInstallmentPayment: PaymentRequestFunction;
    requestAlternativePayment: PaymentRequestFunction;
    requestPaymentChannel: PaymentRequestFunction;
};
declare const _default: RNMy2c2pSdkType;
export default _default;
