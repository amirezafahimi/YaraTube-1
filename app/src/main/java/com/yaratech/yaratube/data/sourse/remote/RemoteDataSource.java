package com.yaratech.yaratube.data.sourse.remote;

import android.content.Context;
import android.util.Log;

import com.yaratech.yaratube.data.sourse.remote.DataSource;
import com.yaratech.yaratube.data.model.Category_list;
import com.yaratech.yaratube.utils.Util;
import com.yaratech.yaratube.dagger.component.DaggerGetServices;
import com.yaratech.yaratube.dagger.component.GetServices;
import com.yaratech.yaratube.dagger.module.RetrofitModule;
import com.yaratech.yaratube.data.model.Store;

import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vah on 8/8/2018.
 */

public class RemoteDataSource implements DataSource {

    private Context context;
    private Store store;
    private List<Collection> collections;
    private List<Category_list> category_list;
    public RemoteDataSource (Context context){
        this.context = context;
    }

    @Override
    public void getHome(final LoadStoreCallback callback) {
        if (Util.isOnline(context)) {
            GetServices getServices = DaggerGetServices
                    .builder()
                    .retrofitModule(new RetrofitModule())
                    .build();
            Call<Store> storeCall = getServices.getStoreService().getStore();
            storeCall.enqueue(new Callback<Store>() {
                @Override
                public void onResponse(Call<Store> call, Response<Store> response) {

                    if (response.isSuccessful()) {
                        store = response.body();
                        Log.e("Tag", store.getName());
                        callback.onStoreLoaded(store);
                    } else
                        callback.onError("عملیات با خطا مواجه شد!");
                }

                @Override
                public void onFailure(Call<Store> call, Throwable t) {

                }
            });
        } else
            callback.onError("ارتباط اینترنت شما قطع است");
    }

    @Override
    public void getCategory(final LoadCatetoryCallback callback) {
        if (Util.isOnline(context)) {
            GetServices getServices = DaggerGetServices
                    .builder()
                    .retrofitModule(new RetrofitModule())
                    .build();
            final Call<List<Category_list>> categoryListCall = getServices.getStoreService().getCategory();
            categoryListCall.enqueue(new Callback<List<Category_list>>() {
                @Override
                public void onResponse(Call<List<Category_list>> call, Response<List<Category_list>> response) {

                    if (response.isSuccessful()) {
                        category_list = response.body();
                        callback.onCategoryLoaded(category_list);
                    } else
                        callback.onError("عملیات با خطا مواجه شد!");
                }

                @Override
                public void onFailure(Call<List<Category_list>> call, Throwable t) {

                }
            });
        } else
            callback.onError("ارتباط اینترنت شما قطع است");
    }
}
