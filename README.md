
# react-native-gzdsx-barcodeview

## Getting started

`$ npm install react-native-gzdsx-barcodeview --save`
or
`$ yarn add react-native-gzdsx-barcodeview`

### Mostly automatic installation

`$ npx pod-install`


## Usage
```javascript
import BarcodeView from 'react-native-barcode-view';
const App = () => {
    return (
        <BarcodeView
            onRead={e => {
                console.log(e.nativeEvent);
            }}
        />
    )
}
// TODO: What to do with the module?
BarcodeView;
```
  