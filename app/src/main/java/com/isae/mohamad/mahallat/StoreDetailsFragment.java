package com.isae.mohamad.mahallat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.isae.mohamad.mahallat.Classes.Comment;
import com.isae.mohamad.mahallat.Classes.utilities.CommentAdapter;
import com.isae.mohamad.mahallat.R;

import java.util.ArrayList;

public class StoreDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public StoreDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StoreDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreDetailsFragment newInstance(String param1, String param2) {
        StoreDetailsFragment fragment = new StoreDetailsFragment();
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
        View view = inflater.inflate(R.layout.fragment_store_details, container, false);

        try {
            ArrayList<Comment> arrayOfComments = new ArrayList<Comment>();
            Comment newComment = new Comment("1", "1", "1", "1", "user 1", "New Comment 1", "21/09/2018 7:00 PM");
            Comment newComment2 = new Comment("2", "2", "2", "2", "user 2", "New Comemnt 2", "22/09/2018 6:00 PM");
            Comment newComment3 = new Comment("3", "3", "3", "3", "user 3", "New Comment 3", "20/09/2018 5:00 PM");

            arrayOfComments.add(newComment);
            arrayOfComments.add(newComment2);
            arrayOfComments.add(newComment3);
            CommentAdapter adapter = new CommentAdapter(this.getContext(), arrayOfComments);

            ListView listView = (ListView) view.findViewById(R.id.LvComments);

            listView.setAdapter(adapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Inflate the layout for this fragment
        return view;
    }

    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
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
    }*/

/*    @Override
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
