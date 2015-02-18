package com.example.johan.assignment3;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IncomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button AddBtn;
    private ListView lstIncome;
    private EconomicDB db;
    private static ArrayList<Data> storeIncome = new ArrayList<>();
    private String rowId;
    private IncomeAdapter incomeAdapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IncomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomeFragment newInstance(String param1, String param2) {
        IncomeFragment fragment = new IncomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public IncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void SaveContentToDB() {
        EditText editAmount = (EditText) getView().findViewById(R.id.editxt_income_amount);
        EditText editTitle = (EditText) getView().findViewById(R.id.editxt_income_title);

        String amount = editAmount.getText().toString();
        String title = editTitle.getText().toString();

        String regex = "[0-9]+";
        boolean atleasOneAlpha = title.matches(".*[a-zA-Z]+.*");
        boolean onlyDigits = amount.matches(regex);

        if (!amount.isEmpty() && onlyDigits && atleasOneAlpha) {
            //Save data in database
            Data data = new Data();
            data.SetTitle(title);
            data.SetAmount(amount);
            data.SetId(TimeStamp());
            data.SetDate(TimeStamp());

            //Push data to DB.
            db.openWrite();
            db.saveIncome(data);

            //Store the newly inserted data id.
            rowId = data.GetId();

        } else {
            //Fail.
            TextView error = (TextView) getView().findViewById(R.id.txt_error);
            error.setText(R.string.wrong_input);
            rowId = "";
        }

        editAmount.setText("");
        editTitle.setText("");
    }

    public void LoadSpecificContentFromDB(View view) {
        if (!rowId.isEmpty()) {
            db.openRead();

           Data newData = new Data();
           newData = db.getSpecificIncomeContent(rowId);
           storeIncome.add(newData);

           // Add data from db into listView.
           AddToListView(view);
        }
    }

    public void AddToListView(View view) {

        if (incomeAdapter == null) {
            incomeAdapter = new IncomeAdapter(getActivity(), storeIncome);
        }

        lstIncome = (ListView) view.findViewById(R.id.listView_income);

        lstIncome.setAdapter(incomeAdapter);
        incomeAdapter.notifyDataSetChanged();

    }

    public void LoadAllIncomeContentFromDB(View view) {
        ArrayList<Data> temp = new ArrayList<>();

        //Load all contents from db.
        temp.addAll(db.getAllIncomeContent());
        if (!temp.isEmpty()) {
            storeIncome = temp;

            //Show objects in listview
            AddToListView(view);
        }
        //Erase reference, GC will delete object.
        temp = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view = inflater.inflate(R.layout.fragment_income, container, false);

        //Sets the title in actionbar
        getActivity().setTitle(R.string.fragment_income_title);

        if (db == null) {
            //Create a new database object.
            db = new EconomicDB(view.getContext());
        }

        LoadAllIncomeContentFromDB(view);

        AddBtn = (Button) view.findViewById(R.id.btn_add_income);
        AddBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                SaveContentToDB();
                LoadSpecificContentFromDB(view);
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public String TimeStamp() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        return timeStamp;
    }
}
