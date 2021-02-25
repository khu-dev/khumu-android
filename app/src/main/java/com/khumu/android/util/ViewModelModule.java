//package com.khumu.android.util;
//
//import androidx.lifecycle.ViewModel;
//
//import com.google.firebase.inject.Provider;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.util.Map;
//
//import dagger.MapKey;
//import dagger.Module;
//import dagger.Provides;
//
//@Module
//public class ViewModelModule {
//    @Target(ElementType.METHOD)
//    @Retention(RetentionPolicy.RUNTIME)
//    @MapKey
//    @interface ViewModelKey {
//        Class<? extends ViewModel> value();
//    }
//
//    @PerFragment
//    @Provides
//    ViewModelFactory viewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>>) {
//        return new ViewModelFactory(providerMap);
//    }
//}
