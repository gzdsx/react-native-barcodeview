Pod::Spec.new do |s|
  s.name         = "RNBarcodeView"
  s.version      = "1.0.0"
  s.summary      = "react-native 二维码组件"
  s.description  = "react-native 二维码组件"
  s.homepage     = "https://github.com/gzdsx/react-native-barcodeview.git"
  s.license      = "MIT"
  s.author       = { "gzdsx" => "307718818@qq.com" }
  s.platform     = :ios, "12.0"
  s.source       = { :git => "https://github.com/gzdsx/react-native-barcodeview.git", :tag => "master" }
  s.source_files = "ios/*.{h,m}"
  s.resources    = "ios/*.wav"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end

  