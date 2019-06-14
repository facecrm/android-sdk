# Android-faceCRM-SDK

FaceCRM is an Android client library written in Java. It is used for register and detect faces.

## Features
* Find faces from live camera
* Detect faces from FaceCRM system
* Register faces with FaceCRM system

## Requirements
* Android API 19+

## Installation

### Add Library

Add this into your root build.gradle file:

```gradle
allprojects {
 repositories {
  ...
  url "https://facecrmsdk.bintray.com/facecrm"
 }
}
```

Add the dependency to your module build.gradle:

```gradle
implementation 'com.facecrm:facecrm-sdk:0.6'
```

### AndroidManifest.xml

The AZStack SDK requires some permissions and references from your app's AndroidManifest.xml file.
Below is an example with a com.example package; replace with your own package when merging with your own manifest.

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />

<uses-feature
        android:name="android.hardware.camera"
        android:required="false" />
    <uses-feature
        android:name="android.hardware.camera.autofocus"
        android:required="false" />
    <uses-feature
        android:name="android.hardware.camera.front"
        android:required="false" />
    <uses-feature
        android:name="android.hardware.camera.front.autofocus"
        android:required="false" />
```

### Add CameraView

Add UI CameraCustom in layout xml

```xml
<com.face.detect.CameraCustom
    android:id="@+id/camera_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_centerHorizontal="true" />
```


## Usage
### Instantiate SDK
```java
FaceCRMSDK.newInstance(Context mContext);
```

### Detect face 
Implement in 3 steps:
1. Add camera view into an layout and start to detect.
2. Listen the detected results via events.
3. Stop and remove camera view in layout if you do not want to continue detect.

#### 1. Add camera and start to detect
```java
FaceCRM.getsInstance().startDetectByCamera(Activity act, int containerView)
```
act: camera view's layout.

ContainerView: an CameraCustom in your layout xml, is used to embed camera view.


#### 2. Listen events
#### Found face from live camera
A face is found from live camera, prepare to send FaceCRM system for detect face.
```java
FaceCRMSDK.getsInstance().onFoundFace(new FoundFaceListener() {
    @Override
    public void onFoundFace(final Bitmap face, final Bitmap fullImage) {
        //face: UIImage contains a face is found in full image.
    }
});
```

#### Detect face from FaceCRM system
```java

FaceCRMSDK.getsInstance().onDetectFace(new DetectFaceListener() {
    @Override
    public void onDetectFaceSuccess(Bitmap face, Bitmap fullImage, String faceId, String metaData) {
          //face: UIImage contains a face is found in full image. 
          //Face ID: a face's indentifier, was detected successfully from FaceCRM system. 
          //Meta data: the embedded data of the detected face.
    }

    @Override
    public void onDetectFaceFail(Bitmap face, Bitmap fullImage, int errorCode, String errorMessage) {
          //face: UIImage contains a face is found in full image. 
          //errorCode: error code is returned from FaceCRM system.
          //errorMessage: error message is returned from FaceCRM system.
    }
});
```

#### 3. Stop and remove camera
```java
FaceCRM.getsInstance().stopCamera()
```

### Register faces
Implement in 5 steps:
1. Add camera view into an UIViewController and start to capture faces.
2. You will decide what face is captured for next step register
3. Send all captured faces or each face to FaceCRM system for register.
2. Listen the register results via events.
3. 
and remove camera view in UI CameraView if you do not want to continue register.

#### 1. Add camera and start to capture faces
```java
FaceCRM.getsInstance().startRegisterByCamera(Activity act, int containerView)
```
rect: camera view 's frame.

ContainerView: an CameraCustom in your layout xml, is used to embed camera view.

#### 2. Capture a face for register.
```java
FaceCRMSDK.getsInstance().captureFace(new CaptureFaceListener() {
    @Override
    public void onCaptureFace(Bitmap face, Bitmap fullImage) {
        //face: UIImage contains a face is found in full image.
    }
});
```

#### 3. Register faces with FaceCRM system
After captured faces, you can register all faces or register each face:

##### Register all faces:
```java
FaceCRM.getsInstance().registerFaces(List<Bitmap> faces)
```
faceArray: face array is registered

##### Register each face:
```java
FaceCRM.getsInstance().registerEachFace(Bitmap face)
```
face: face is registered

With register each face, you need to call the finish function:
```java
FaceCRM.getsInstance().finishRegister()
```

#### 4. Listen register events
```java
FaceCRMSDK.getsInstance().onRegisterFace(new RegisterFaceListener() {
    @Override
    public void onRegisterFace(Bitmap face, int code, String message) {
        //face: UIImage contains a face, was sent to FaceCRM system for register. 
        //status: register's result code.
        //message: register 's result message.
    }
});
```

#### 5. Stop and remove camera
```java
FaceCRM.getsInstance().stopCamera()
```

### Config
#### Set camera's scan frequency
Camera view will scan to found face each 1 second. Default ( also minimum) is 1 second.
```java
FaceCRM.getsInstance().setScanFrequency(1) // 1 seconds
```

#### Set whether show face's rectangle or not, when camera view scan and found a face.
Camera view will show a rectangle bounds face. Default is always show. if you do not want to show, you can turn off.
```java
FaceCRM.getsInstance().enableShowFaceResult(true)
```


## Sample

The sample app demonstrates the use of the FaceCRM Android client library. The sample shows scenarios face detection and face registration. [See SAMPLE](https://github.com/facecrm/facecrm-android-sample) for details.

## License
The FaceCRM is released under the BSD 2 license. [See LICENSE](https://github.com/facecrm/facecrm-android-sdk/blob/master/LICENSE) for details.

