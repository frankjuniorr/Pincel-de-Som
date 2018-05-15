<img alt="Icon" src="app/app/src/main/res/mipmap-xxhdpi/ic_launcher.png?raw=true" align="left" hspace="1" vspace="1">

# Pincel de Som

[![dependence](https://img.shields.io/badge/dependence-libpd-0D47A1.svg?style=true)](https://github.com/libpd/pd-for-android)
![minSdkVersion 24](https://img.shields.io/badge/minSdkVersion-24-F44336.svg?style=true)
![targetSdkVersion 27](https://img.shields.io/badge/targetSdkVersion-27-F9A825.svg?style=true)

</br>

## Concept

The 'Pincel de Som' is a experiment with the proposal to merge art, technology and music. An prototype to visual music concept. Inspired by an album of a brazilian artist Daniel Finizola with the same name ['Pincel de Som'](https://soundcloud.com/daniel-finizola).

## Design Concept

The application colors are the same colors in the chorus of the song "Sonho Cego".

* TODO:
- concept icon

## Funcionality

The app works like a guitar pedal, where the user plug an eletric musical instrument in smartphone and each color represents a guitar pedal effect. Each X-Y point of screen of each color represent a modulation of there effect.

* TODO:
- screenshot
- google play link

## Dependence

To plug an electric musical instrument in a smartphone is necessary an adapter called 'iRig'.

## Libraries and tools used in the project

* [Pure Data](https://puredata.info/) - An open source visual programming language for sound synthesis.
* [pd-for-android](https://github.com/libpd/pd-for-android) - to link Pure Data code with Android.

## Structure

This project have 3 parts

### [app]

- Contais a Android code.

### [audio]

- Contais a Pure Data code.

### [scripts]

- Contais a shell script to build PD code into Android path.

[app]: app "App module"
[audio]: audio "Audio module"
[scripts]: scripts "Scripts module"
