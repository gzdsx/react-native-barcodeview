import React from "react";
import {Animated, requireNativeComponent, StyleSheet, View, Dimensions, Image} from "react-native";

const lineWidth = Dimensions.get("window").width
const startY = Dimensions.get("window").height * 0.1;
const endY = Dimensions.get("window").height * 0.9;

export default class BarcodeView extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            top: new Animated.Value(startY),
            opacity: 1
        }
    }

    startAnimate = () => {
        this.animate = Animated.timing(this.state.top, {
            toValue: endY,
            duration: 3000,
            useNativeDriver: false
        });
        this.animate.start(({finished}) => {
            if (finished) {
                this.setState({top: new Animated.Value(startY), opacity: 1}, () => {
                    this.startAnimate();
                });
            }
        });
    }

    stopAnimate = () => {
        this.animate.stop();
        this.setState({opacity: 0});
    }

    componentDidMount() {
        this.startAnimate();
    }

    render() {
        let {onRead, ...props} = this.props;
        return (
            <View style={StyleSheet.absoluteFill}>
                <RNBarcodeView
                    style={{flex: 1}}
                    onBarcodeRead={e => {
                        this.stopAnimate();
                        onRead && onRead(e)
                    }}
                    {...props}
                />
                <Animated.View
                    style={{
                        top: this.state.top,
                        opacity: this.state.opacity,
                        position: 'absolute',
                        left: 0,
                        right: 0,
                        alignItems: 'center',
                        justifyContent: 'center'
                    }}
                >
                    <Image
                        source={require('./line.png')}
                        style={{
                            width: lineWidth,
                            height: 50,
                            tintColor: '#06D96B',
                            resizeMode: 'contain'
                        }}
                    />
                </Animated.View>
                {this.props.children}
            </View>
        )
    }
}

const RNBarcodeView = requireNativeComponent('RNBarcodeView', BarcodeView);
