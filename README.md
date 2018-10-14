# react-native-android-picker
[![npm version](http://img.shields.io/npm/v/react-native-android-picker.svg?style=flat-square)](https://npmjs.org/package/react-native-android-picker "View this project on npm")
[![npm version](http://img.shields.io/npm/dm/react-native-android-picker.svg?style=flat-square)](https://npmjs.org/package/react-native-android-picker "View this project on npm")

## Introduction
Android Picker component based on React-native.

Since picker is originally supported by ios while Android only supports a ugly Spinner component. If you want to have the same user behaviour, you can use this.

The android component is based on https://github.com/AigeStudio/WheelPicker which runs super fast and smoothly. It also supports curved effect which make it exactly the same looking and feel as the ios picker.
![](https://i.imgur.com/r6hykCY.png) ![](https://i.imgur.com/xgpTMfo.png)

## How to use

Install component

```
npm i react-native-android-picker --save
```

Add in settings.gradle
```
include ':react-native-android-picker'
project(':react-native-android-picker').projectDir = new File(settingsDir, '../node_modules/react-native-android-picker/android')
```
Add in app/build.gradle
```
compile project(':react-native-android-picker')
```
Modify MainApplication
```
    import com.nclong87.ReactNativeWheelPickerPackage;
    ......

    protected List<ReactPackage> getPackages() {
        return Arrays.<ReactPackage>asList(
            new MainReactPackage(), new ReactNativeWheelPickerPackage()
        );
    }
```

## Example code
```
import React, { Component } from 'react';
import {
	Platform,
	StyleSheet,
	Text,
	View,
} from 'react-native';


// import { WheelStraightPicker as PickerAndroid } from 'react-straight-picker-android';
import { WheelCurvedPicker as PickerAndroid } from 'react-straight-picker-android';
var PickerItem = PickerAndroid.Item;

export default class App extends Component<{}> {

	constructor(props) {
		super(props);
		this.state = {
			selectedItem : 2,
			itemList: ['1', '2', '3', '4', '5', '6', '7', '8', '9']
		};
	}

	onPickerSelect (index) {
		this.setState({
			selectedItem: index,
		})
	}

	render () {
		return (
			<View style={styles.container}>
				<Text style={styles.welcome}>
					WheelCurvedPicker
				</Text>
				<PickerAndroid
					style={{width: 70, height: 180}}
					selectedValue={this.state.selectedItem}
					itemStyle={{color:"white", fontSize:26}}
					lineStrokeWidth={5}
					onValueChange={(index) => this.onPickerSelect(index)}>
						{this.state.itemList.map((value, i) => (
							<PickerItem label={value} value={i} key={"money"+value}/>
						))}
				</PickerAndroid>
			</View>
		);
	}
}

const styles = StyleSheet.create({
	container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#1962dd',
  },
  welcome: {
		fontSize: 40,
		textAlign: 'center',
		margin: 10,
		color: '#ffffff',
	},
});
```
