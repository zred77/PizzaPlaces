# Pizza App
An app displaying a map with pizza restaurants written kotlin language.

It starts with displaying a map with markers of the restaurants. By clicking a marker a horizontally scollable list displays. 
By clicking an item in the list a detail screen appears.

Libraries/solutions used in app:

- MVVM
- LiveData
- kotlin coroutines
- Dagger 2
- Retrofit
- Moshi
- Coil

Add your own google maps api key to local.properties with the following key:
`maps.api.key`


Projcect config contains two flavour:
- beeceptor
- mockable

to easily change between api endpoinds.
