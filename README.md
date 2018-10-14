# rn-straight-picker-android
[![npm version](http://img.shields.io/npm/v/rn-straight-picker-android.svg?style=flat-square)](https://npmjs.org/package/rn-straight-picker-android "View this project on npm")
[![npm version](http://img.shields.io/npm/dm/rn-straight-picker-android.svg?style=flat-square)](https://npmjs.org/package/rn-straight-picker-android "View this project on npm")

## Introduction
Android Picker component based on React-native.

Since picker is originally supported by ios while Android only supports a ugly Spinner component. If you want to have the same user behaviour, you can use this.

The android component is based on https://github.com/AigeStudio/WheelPicker which runs super fast and smoothly. It also supports curved effect which make it exactly the same looking and feel as the ios picker.
![](https://i.imgur.com/7ukJ6e1.png)

## How to use

Install component

```
npm i rn-straight-picker-android --save
```

Add in settings.gradle
```
include ':rn-straight-picker-android'
project(':rn-straight-picker-android').projectDir = new File(settingsDir, '../node_modules/rn-straight-picker-android/android')
```
Add in app/build.gradle
```
compile project(':rn-straight-picker-android')
```
Modify MainApplication
```
    import com.aigestudio.wheelpicker.view.ReactNativeWheelPickerPackage;
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


import Picker from 'rn-straight-picker-android'
var PickerItem = Picker.Item;

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
					Welcome to React Native!
				</Text>
				<Picker style={{width: 150, height: 180}}
					selectedValue={this.state.selectedItem}
					itemStyle={{color:"white", fontSize:26}}
					onValueChange={(index) => this.onPickerSelect(index)}>
						{this.state.itemList.map((value, i) => (
							<PickerItem label={value} value={i} key={"money"+value}/>
						))}
				</Picker>
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
});
```
