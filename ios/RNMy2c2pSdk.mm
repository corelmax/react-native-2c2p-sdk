#import "RNMy2c2pSdk.h"

@implementation RNMy2c2pSdk

- (dispatch_queue_t) methodQueue {
    return dispatch_get_main_queue();
}

- (void)setMandatoryFields: (NSDictionary *)params
{
    _my2c2pSDK.merchantID = params[@"merchantID"];
    _my2c2pSDK.uniqueTransactionCode = params[@"uniqueTransactionCode"];
    _my2c2pSDK.desc = params[@"desc"];
    _my2c2pSDK.amount = [params[@"amount"] doubleValue];
    _my2c2pSDK.currencyCode = params[@"currencyCode"];
    _my2c2pSDK.secretKey = params[@"secretKey"];
    [self nilSafeSetBoolPropertyValue: params[@"paymentUI"] forKey: @"paymentUI"];
}

- (void)setCardInfoFields: (NSDictionary *)params
{
    _my2c2pSDK.cardHolderName = params[@"cardHolderName"];
    _my2c2pSDK.cardHolderEmail = params[@"cardHolderEmail"];
    _my2c2pSDK.storeCardUniqueID = params[@"storeCardUniqueID"];
    _my2c2pSDK.pan = params[@"pan"];
    _my2c2pSDK.cardExpireMonth = [params[@"cardExpireMonth"] intValue];
    _my2c2pSDK.cardExpireYear = [params[@"cardExpireYear"] intValue];
    _my2c2pSDK.securityCode = params[@"securityCode"];
    _my2c2pSDK.panCountry = params[@"panCountry"];
    _my2c2pSDK.panBank = params[@"panBank"];
    _my2c2pSDK.request3DS = params[@"request3DS"];
    _my2c2pSDK.storeCard = params[@"storeCard"];
}

- (void)setRecurringFields: (NSDictionary *)params
{
    _my2c2pSDK.recurring = YES;
    _my2c2pSDK.invoicePrefix = params[@"invoicePrefix"];
    _my2c2pSDK.recurringInterval = [params[@"recurringInterval"] intValue];
    _my2c2pSDK.recurringAmount = [params[@"recurringAmount"] doubleValue];
    _my2c2pSDK.recurringCount = [params[@"recurringCount"] intValue];
    [self nilSafeSetBoolPropertyValue: params[@"allowAccumulate"] forKey: @"allowAccumulate"];
    [self nilSafeSetPropertyValue:params[@"maxAccumulateAmt"] forKey:@"maxAccumulateAmt"];
    [self nilSafeSetPropertyValue:params[@"chargeNextDate"] forKey:@"chargeNextDate"];
    [self nilSafeSetPropertyValue:params[@"promotion"] forKey:@"promotion"];
}

- (void)setInstallmentFields: (NSDictionary *)params
{
    _my2c2pSDK.ippTransaction = YES;
    _my2c2pSDK.installmentPeriod = [params[@"installmentPeriod"] intValue];
    _my2c2pSDK.interestType = params[@"interestType"];
}

- (void)setAlternativePaymentFields: (NSDictionary *)params
{
    _my2c2pSDK.paymentChannel = [self paymentChannelFromString:params[@"paymentChannel"]];
    _my2c2pSDK.cardHolderName = params[@"cardHolderName"];
    _my2c2pSDK.cardHolderEmail = params[@"cardHolderEmail"];
    _my2c2pSDK.agentCode = params[@"agentCode"];
    [self nilSafeSetPropertyValue:params[@"channelCode"] forKey:@"channelCode"];
    [self nilSafeSetPropertyValue:params[@"paymentExpiry"] forKey:@"paymentExpiry"];
    [self nilSafeSetPropertyValue:params[@"mobileNo"] forKey:@"mobileNo"];
}

- (void)setPaymentChannelFields: (NSDictionary *)params
{
    _my2c2pSDK.paymentChannel = [self paymentChannelFromString:params[@"paymentChannel"]];
}

- (My2c2pPaymentChannel)paymentChannelFromString: (NSString *)string
{
    if([string isEqualToString:@"MPU"]) {
        return MPU;
    } else if([string isEqualToString:@"UPOP"]) {
        return UPOP;
    } else if([string isEqualToString:@"ALIPAY"]) {
        return ALIPAY;
    } else if([string isEqualToString:@"ONE_TWO_THREE"]) {
        return ONE_TWO_THREE;
    } else if([string isEqualToString:@"MASTER_PASS"]) {
        return MASTER_PASS;
    } else if([string isEqualToString:@"APPLE_PAY"]) {
        return APPLE_PAY;
    } else if([string isEqualToString:@"QWIK"]) {
        return QWIK;
    } else if([string isEqualToString:@"LINE_PAY"]) {
        return LINE_PAY;
    } else {
        return CREDIT_CARD;
    }
}

- (void)setOptionalFields: (NSDictionary *)params
{
    [self nilSafeSetPropertyValue: params[@"payCategoryID"] forKey:@"payCategoryID"];
    [self nilSafeSetPropertyValue: params[@"userDefined1"] forKey:@"userDefined1"];
    [self nilSafeSetPropertyValue: params[@"userDefined2"] forKey:@"userDefined2"];
    [self nilSafeSetPropertyValue: params[@"userDefined3"] forKey:@"userDefined3"];
    [self nilSafeSetPropertyValue: params[@"userDefined4"] forKey:@"userDefined4"];
    [self nilSafeSetPropertyValue: params[@"userDefined5"] forKey:@"userDefined5"];
    [self nilSafeSetPropertyValue: params[@"statementDescriptor"] forKey:@"statementDescriptor"];
}

- (void)sendRequestWithResolver: (RCTPromiseResolveBlock)resolve rejecter: (RCTPromiseRejectBlock)reject
{
    // Determine what controller is in the front based on if the app has a navigation controller or a tab bar controller
    UIWindow *window = [[[UIApplication sharedApplication] delegate] window];
    UIViewController* showingController;
    if([window.rootViewController isKindOfClass:[UINavigationController class]]){

        showingController = ((UINavigationController*)window.rootViewController).visibleViewController;
    } else if ([window.rootViewController isKindOfClass:[UITabBarController class]]) {

        showingController = ((UITabBarController*)window.rootViewController).selectedViewController;
    } else {

        showingController = (UIViewController*)window.rootViewController;
    }
    _my2c2pSDK.delegate = (id<My2c2pSDKDelegate>) showingController;

    dispatch_async(dispatch_get_main_queue(), ^{
        [_my2c2pSDK requestWithTarget: showingController onResponse:^(NSDictionary *response)
         {
             RCTLogInfo(@"%@",response);
             resolve(response);

         } onFail:^(NSError *error) {

             if (error) {
                 RCTLogInfo(@"%@", error);
                 reject(@"payment error", error.localizedDescription, error);
             } else {
                 reject(@"TRANSACTION_CANCELED", @"Transaction is canceled", error);
             }
         }];
    });
}

- (void)nilSafeSetPropertyValue: (NSObject *)value forKey: (NSString *)key
{
    if (value && ![value isEqual:[NSNull null]]) {
        [_my2c2pSDK setValue: value forKey:key];
    }
}

- (void)nilSafeSetBoolPropertyValue: (NSObject *)value forKey: (NSString *)key
{
    if (value && ![value isEqual:[NSNull null]]) {
        [_my2c2pSDK setValue: (NSNumber *)value forKey:key];
    }
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(setup: (NSString *)privateKey
                  withProductionMode: (BOOL)productionMode) {
    RCTLogInfo(@"Initialize with params: production=%d", productionMode);
    _my2c2pSDK = [[My2c2pSDK alloc] initWithPrivateKey:privateKey];
    _my2c2pSDK.version = 9.0;
    _my2c2pSDK.productionMode = productionMode;
}

RCT_EXPORT_METHOD(requestPayment: (NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    RCTLogInfo(@"Request payment params: %@", params);

    [self setMandatoryFields: params];
    [self setCardInfoFields: params];
    [self setOptionalFields: params];

    [self sendRequestWithResolver: resolve rejecter: reject];
}

RCT_EXPORT_METHOD(requestRecurringPayment: (NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    RCTLogInfo(@"Request recurring payment params: %@", params);

    [self setMandatoryFields: params];
    [self setCardInfoFields: params];
    [self setRecurringFields: params];
    [self setOptionalFields: params];

    [self sendRequestWithResolver: resolve rejecter: reject];
}

RCT_EXPORT_METHOD(requestInstallmentPayment: (NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    RCTLogInfo(@"Request installment payment params: %@", params);

    [self setMandatoryFields: params];
    [self setCardInfoFields: params];
    [self setInstallmentFields: params];
    [self setOptionalFields: params];

    [self sendRequestWithResolver: resolve rejecter: reject];
}

RCT_EXPORT_METHOD(requestAlternativePayment: (NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    RCTLogInfo(@"Request alternative payment params: %@", params);

    [self setMandatoryFields: params];
    [self setAlternativePaymentFields: params];
    [self setOptionalFields: params];

    [self sendRequestWithResolver: resolve rejecter: reject];
}

RCT_EXPORT_METHOD(requestPaymentChannel: (NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
    RCTLogInfo(@"Request payment channel payment params: %@", params);

    [self setMandatoryFields: params];
    [self setPaymentChannelFields: params];
    [self setOptionalFields: params];

    [self sendRequestWithResolver: resolve rejecter: reject];
}


@end
