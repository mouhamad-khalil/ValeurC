package com.isae.mohamad.mahallat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.isae.mohamad.mahallat.Classes.Category;
import com.isae.mohamad.mahallat.Classes.utilities.CategoryAdapter;

import java.util.ArrayList;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.isae.mohamad.mahallat.Classes.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoreCategoriesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoreCategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreCategoriesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RequestQueue requestQueue;
    GridView gridView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StoreCategoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreCategoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreCategoriesFragment newInstance(String param1, String param2) {
        StoreCategoriesFragment fragment = new StoreCategoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context context = this.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_categories, container, false);

        gridView = (GridView)view.findViewById(R.id.gridView);

        requestQueue = Volley.newRequestQueue(context);
        getCategories(context);

        // When the user clicks on the GridItem
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                try {
                    Category category = (Category) gridView.getItemAtPosition(position);
                    Intent ProductsIntent = new Intent(v.getContext(), ProductsActivity.class);
                    ProductsIntent.putExtra("CategoryId", category.getId());
                    startActivity(ProductsIntent);
                }
                catch(Exception e){}
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

/*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

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

    private void getCategories(Context context) {
        final Context c = context;
        JsonArrayRequest arrReq = new JsonArrayRequest(Request.Method.GET, Constants.Get_Categories_API,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response.length() > 0) {
                            ArrayList<Category> categories = new ArrayList<Category>();
                            categories = Category.fromJson(response);
                            CategoryAdapter categoryAdapter = new CategoryAdapter(c, categories);
                            gridView.setAdapter(categoryAdapter);
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // If there a HTTP error then add a note to our repo list.
                        //setRepoListText("Error while calling REST API");
                        Log.e("Volley", error.toString());
                    }
                }
        );
        // Add the request we just defined to our request queue.
        // The request queue will automatically handle the request as soon as it can.
        requestQueue.add(arrReq);
    }
}
