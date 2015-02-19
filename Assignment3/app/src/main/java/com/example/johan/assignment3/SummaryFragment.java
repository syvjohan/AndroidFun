package com.example.johan.assignment3;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SummaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SummaryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EconomicDB db;

    private static ArrayList<Data> storeIncome = new ArrayList<>();
    private static ArrayList<Data> storeExpense = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    //Attributes for pie chart.
    CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    public static final String TYPE = "type";
    private GraphicalView mChartView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SummaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SummaryFragment newInstance(String param1, String param2) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SummaryFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        //Sets the title in actionbar
        getActivity().setTitle(R.string.fragment_summary_title);

        if (db == null) {
            //Create a new database object.
            db = new EconomicDB(view.getContext());
        }

        LoadAllExpenseContentFromDB();
        LoadAllIncomeContentFromDB();

        CalcExpenseSum();
        CalcIncomeSum();

        Summary sum = CalculateSum();
        ShowSummary(sum, view);
        CreatePieChart(sum, view);

        return view;
    }

    public void ShowSummary(Summary sum, View view) {
        //Sets the format for the output data.
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

        TextView txtIncome = (TextView) view.findViewById(R.id.txt_summary_income);
        TextView txtExpense = (TextView) view.findViewById(R.id.txt_summary_expense);
        TextView txtSum = (TextView) view.findViewById(R.id.txt_summary_sum);

        String formatIncome = decimalFormat.format(sum.GetIncome());
        String formatExpense = decimalFormat.format(sum.GetExpense());
        String formatSum = decimalFormat.format(sum.GetSummary());

        txtIncome.setText(formatIncome);
        txtExpense.setText(formatExpense);
        txtSum.setText(formatSum);
    }

    //Calculates the total sum
    public Summary CalculateSum() {
        Summary sum = new Summary();

        sum.SetIncome(CalcIncomeSum());
        sum.SetExpense(CalcExpenseSum());
        sum.SetSummary(sum.GetIncome() - sum.GetExpense());

        return sum;
    }

    public void LoadAllExpenseContentFromDB() {
        ArrayList<Data> temp = new ArrayList<>();

        //Load all contents from db.
        temp.addAll(db.getAllExpenseContent());
        if (!temp.isEmpty()) {
            storeExpense = temp;
        }
        //Erase reference, GC will delete object.
        temp = null;
    }

    public void LoadAllIncomeContentFromDB() {
        ArrayList<Data> temp = new ArrayList<>();

        //Load all contents from db.
        temp.addAll(db.getAllIncomeContent());
        if (!temp.isEmpty()) {
            storeIncome = temp;
        }
        //Erase reference, GC will delete object.
        temp = null;
    }

    public double CalcIncomeSum() {
        Double sumAmount = 0.0;
        if (storeIncome != null) {
            for(int i = 0; i != storeIncome.size(); i++) {
                double value = Double.parseDouble(storeIncome.get(i).GetAmount());
                sumAmount = sumAmount + value;
            }
        }

        return sumAmount;
    }

    public double CalcExpenseSum() {
        Double sumAmount = 0.0;
        if (storeExpense != null) {
            for(int i = 0; i != storeExpense.size(); i++) {
                double value = Double.parseDouble(storeExpense.get(i).GetAmount());
                sumAmount = sumAmount + value;
            }
        }

        return sumAmount;
    }

    public void FillPieChart(Summary sum){
        Resources res = getResources();
        int[] COLORS = new int[] {res.getColor(R.color.chart_pie_color_expense), res.getColor(R.color.chart_pie_color_incom)};

        //Calculate values in pie chart. Observe that calculation is not returning percent!
        double valueIncome = (sum.GetSummary() / sum.GetIncome());
        double valueExpense = (sum.GetSummary() / sum.GetExpense());
        double[] pieChartValues = {valueIncome, valueExpense};

        String[] pieChartTitles = {"Expenses", "Income"};

        for(int i=0;i<pieChartValues.length;i++)
        {
            mSeries.add(pieChartTitles[i], pieChartValues[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
            if (mChartView != null)
                mChartView.repaint();
        }
    }

    public void CreatePieChart(Summary sum ,View view) {
        mRenderer.setChartTitleTextSize(40);
        mRenderer.setLabelsTextSize(25);
        mRenderer.setLegendTextSize(25);
        mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setStartAngle(90);

        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.chart_pie);
            mChartView = ChartFactory.getPieChartView(getActivity(), mSeries, mRenderer);
            mRenderer.setClickEnabled(true);
            mRenderer.setSelectableBuffer(10);
            layout.addView(mChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.FILL_PARENT));
        } else {
            mChartView.repaint();
        }
        FillPieChart(sum);
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
}

