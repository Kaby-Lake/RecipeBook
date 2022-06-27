# COMP3059MDP Coursework 2 - RecipeBook Report

### Thrid-Party Libraries

-   me.tatarka.bindingcollectionadapter2:bindingcollectionadapter
-   me.tatarka.bindingcollectionadapter2:bindingcollectionadapter-recyclerview

Those two libiaries (under Apache License, can be found at https://github.com/evant/binding-collection-adapter) were used to support the `DataBinding` from `ViewModel` with `ObservableField` to `RecyclerView` with collection of views, therefore possible to apply `MVVM` pattern in this coursework.

### Attentions

It seems that there's a bug in the Emulator running API 28 (arm64-v8) which causes the whole system to freeze while playing any video file, and API 29 (arm64-v8) is fixes this issue.
