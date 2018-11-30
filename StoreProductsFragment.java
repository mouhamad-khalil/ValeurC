package com.isae.mohamad.mahallat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.isae.mohamad.mahallat.Classes.Product;
import com.isae.mohamad.mahallat.Classes.utilities.APIClient;
import com.isae.mohamad.mahallat.Classes.utilities.APIInterface;
import com.isae.mohamad.mahallat.Classes.utilities.MyApplication;
import com.isae.mohamad.mahallat.Classes.utilities.ProductAdapter;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoreProductsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoreProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreProductsFragment extends Fragment {

    private static final String ARG_PARAM = "StoreId";

    ListView listView;
    private int storeId;
    APIInterface apiInterface;

    private OnFragmentInteractionListener mListener;

    public StoreProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param storeId Parameter 1.
     * @return A new instance of fragment StoreCategoriesFragment.
     */
    public static StoreProductsFragment newInstance(int storeId) {
        StoreProductsFragment fragment = new StoreProductsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, storeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            storeId = getArguments().getInt(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context context = this.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_products, container, false);

        listView = (ListView)view.findViewById(R.id.lvItems);

        getProducts(context);

        // When the user clicks on the ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                try {
                    Product product = (Product) listView.getItemAtPosition(position);
                    Intent ProductIntent = new Intent(v.getContext(), ProductDetailsActivity.class);
                    ProductIntent.putExtra("Product", (Serializable) product);
                    startActivity(ProductIntent);
                }
                catch(Exception e){}
            }
        });
        return view;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getProducts(final Context context) {
        final Context c = context;

        String credentials =  MyApplication.GetSavedCredentials();
        if(credentials == null)
            apiInterface = APIClient.getClient().create(APIInterface.class);
        else
            apiInterface = APIClient.getClient(credentials).create(APIInterface.class);

        /*Call the method in the interface to get the products list*/
        Call<List<Product>> call  = apiInterface.doGetProductList(Integer.toString(storeId));

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, retrofit2.Response<List<Product>> response) {
                if (response.isSuccessful() ) {
                    ProductAdapter productAdapter = new ProductAdapter(c, response.body());
                    listView.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(context, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
