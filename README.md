# Exchange Rate Android App

Applications for viewing the exchange rate, designed using the components of the Material You.

## Tech stack & Open source libraries
- Minimum SDK level 26.
- [Kotlin](https://kotlinlang.org/) based + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- Hilt for dependency injection.
- JetPack
  - Lifecycle - dispose observing data when lifecycle state changes.
  - ViewModel - UI related data holder, lifecycle aware.
  - Flow - asynchronous cold stream of elements.
  - Room - construct database.
- Architecture
  - Clean Architecture
  - MVVM
- [Material Design 3](https://m3.material.io/) - The latest version of Material Design includes personalization and accessibility features that put people at the center.
- [Retrofit & OkHttp](https://github.com/square/retrofit) - Construct the REST APIs and paging network data.
- [ViewBindingPropertyDelegate](https://github.com/androidbroadcast/ViewBindingPropertyDelegate) - Make work with Android View Binding simpler.
