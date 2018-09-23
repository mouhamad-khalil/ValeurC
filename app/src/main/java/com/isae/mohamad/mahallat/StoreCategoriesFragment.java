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
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.isae.mohamad.mahallat.Classes.Category;
import com.isae.mohamad.mahallat.Classes.Comment;
import com.isae.mohamad.mahallat.Classes.utilities.CategoryAdapter;
import com.isae.mohamad.mahallat.Classes.utilities.CommentAdapter;
import com.isae.mohamad.mahallat.R;

import java.util.ArrayList;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_categories, container, false);

        final GridView gridView = (GridView)view.findViewById(R.id.gridView);

        Category category1 = new Category("1","Category 1", "Code 1", "Description 1");
        Category category2 = new Category("2","Category 2", "Code 2", "Description 2");
        Category category3 = new Category("3","Category 3", "Code 3", "Description 3");
        Category category4 = new Category("4","Category 4", "Code 4", "Description 4");
        Category category5 = new Category("5","Category 5", "Code 5", "Description 5");


        ArrayList<Category> categories = new ArrayList<Category>();

        categories.add(category1);
        categories.add(category2);
        categories.add(category3);
        categories.add(category4);
        categories.add(category5);

        CategoryAdapter categoryAdapter = new CategoryAdapter(this.getContext(), categories);


        gridView.setAdapter(categoryAdapter);

        // When the user clicks on the GridItem
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //Object o = gridView.getItemAtPosition(position);
                //Category category = (Category) o;
                Intent LoginIntent = new Intent(v.getContext(), ProductsActivity.class);
                startActivity(LoginIntent);
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
}
