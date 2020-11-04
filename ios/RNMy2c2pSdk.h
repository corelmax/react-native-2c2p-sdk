#import <React/RCTBridgeModule.h>
#import <React/RCTLog.h>
#import "my2c2pSDK/My2c2pSDK.h"

@interface RNMy2c2pSdk : NSObject <RCTBridgeModule>
@property (nonatomic,strong) My2c2pSDK *my2c2pSDK;
@end
