
# react-native-barcodeview

## Getting started

`$ npm install react-native-barcodeview --save`

### Mostly automatic installation

`$ react-native link react-native-barcodeview`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-barcodeview` and add `RNBarcodeView.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNBarcodeView.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNBarcodeViewPackage;` to the imports at the top of the file
  - Add `new RNBarcodeViewPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-barcode-view'
  	project(':react-native-barcode-view').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-barcode-view/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-barcode-view')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNBarcodeView.sln` in `node_modules/react-native-barcode-view/windows/RNBarcodeView.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Barcode.View.RNBarcodeView;` to the usings at the top of the file
  - Add `new RNBarcodeViewPackage()` to the `List<IReactPackage>` returned by the `Packages` method


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
  