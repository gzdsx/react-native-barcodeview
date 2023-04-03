//
//  RNBarcodeManager.h
//
//  Created by songdewei on 2023/4/1.
//

#import <Foundation/Foundation.h>
#import <React/RCTViewManager.h>

@class RNBarcodeView;

NS_ASSUME_NONNULL_BEGIN

@interface RNBarcodeManager : RCTViewManager

@property RNBarcodeView *barcodeView;

@end

NS_ASSUME_NONNULL_END
