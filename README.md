# Mixtape
Mixtape is an Android library which simplifies the task of displaying media to a user (e.g. books, movies, music etc.). The library provides high level caching mechanisms and is designed from the ground up to support asynchronous media loading. The interface driven design makes it easy to integrate the library into any existing app with any existing dataset.

<img src="https://raw.githubusercontent.com/MatthewTamlin/Mixtape/master/artwork/songs_scroll.gif" width="425"/> <img src="https://raw.githubusercontent.com/MatthewTamlin/Mixtape/master/artwork/albums_scroll.gif" width="425"/>
<img src="https://raw.githubusercontent.com/MatthewTamlin/Mixtape/master/artwork/songs_options.gif" width="425"/> <img src="https://raw.githubusercontent.com/MatthewTamlin/Mixtape/master/artwork/albums_options.gif" width="425"/>

## Download
Releases are made available through jCentre. Add `compile 'com.matthew-tamlin:mixtape:1.0.0’` to your gradle build file to use the latest version. Older versions are available in the [maven repo](https://bintray.com/matthewtamlin/maven/Mixtape).

## Usage
The components of the library can be divided into two categories:
- Data
- UI

### Data
There are five interfaces in the data category:
- `LibraryItem`
- `DisplayableDefaults`
- `DataBinder`
- `BaseDataSource`
- `ListDataSource`

The library is designed to display any media which can be represented as a LibraryItem. A libraryItem is a piece of media with a title, a subtitle and some artwork, but the exact meaning of each is left to the implementation. For example a film could use the film title, the studio, and a promotional image, whereas a song could use the track title, the artist, and the album cover. The UI is agnostic to the kind of media, and just displays these three pieces of information.

In some instances a LibraryItem will be unable to provide access to its data. This could be due to network unavailability, bad authentication or some other unpreventable event. Using a DisplayableDefaults object, default values can be displayed in the UI if a LibraryItem fails. The interface can be directly implemented, or one of the provided implementations can be used. The `ImmutableDisplayableDefaults` class and the `PojoDisplayableDefaults` class cover the majority of use cases.

The actual binding of data to the UI is handled by implementations of the DataBinder interface. For simplicity, three databinders have been provided: `TitleBinder`, `SubtitleBinder` and `ArtworkBinder`. These data binders load cache data in memory and load data asynchronously to increase performance and eliminate UI lag. The artwork binder uses a fade utility to gradually transition artwork, however this can be disabled if desired.

A BaseDataSource provides access to a single LibraryItem. The interface is minimal and only defines a few critical methods, which allows any existing database to be integrated with this library. The data source defines a few callbacks which notify listeners of important events such as changes to the data. The interface can be directly implemented, or the `BaseDataSourceHelper` class can be extended. This helper class handles callback registration, so that the subclass only needs to handle data operations.

A ListDataSource is simply a BaseDataSource which provides access to a List of items. The interface defines a few additional callbacks which notify listeners of changes to the list such as additions and removals. The interface can be directly implemented, or the `ListDataSourceHelper` class can be extended. This helper class handles callback registration, so that the subclass only needs to handle data operations.

### UI
The UI category contains views for presenting library items to the user. The main classes in the UI category are:
- `HeaderView`
- `BodyView`
- `ContainerView`
