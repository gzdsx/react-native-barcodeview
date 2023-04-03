import React from "react";
import {requireNativeComponent,StyleSheet} from "react-native";

export default class BarcodeView extends React.Component {
    constructor(props) {
        super(props);
    }


    render() {
        let {onRead,...props} = this.props;
        return(
            <RNBarcodeView
                style={StyleSheet.absoluteFill}
                onBarcodeRead={e=>{
                    onRead && onRead(e)
                }}
                {...props}
            />
        )
    }
}

const RNBarcodeView = requireNativeComponent('RNBarcodeView', BarcodeView);
