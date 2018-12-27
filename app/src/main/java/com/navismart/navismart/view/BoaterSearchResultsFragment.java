package com.navismart.navismart.view;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.navismart.navismart.R;
import com.navismart.navismart.adapters.ExpandableListAdapter;
import com.navismart.navismart.adapters.MarinaListAdapter;
import com.navismart.navismart.model.MarinaModel;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class BoaterSearchResultsFragment extends Fragment {

    public static String fromDate, toDate;
    private static float minRange, maxRange;
    private static boolean freeCancellationNeeded = false;
    List<String> starRating;
    ArrayList<String> facilities;
    private ExpandableListAdapter expandableListAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private RangeSeekBar<Float> priceRangeSeekBar;
    private Button showResults, setDateButton;
    private Switch freeCancellationSwitch;
    private List<MarinaModel> marinaList, filteredMarinaList;
    private RecyclerView marinaListRecyclerView;
    private MarinaListAdapter marinaListAdapter;
    private TextView closestSortTextView, cheapestSortTextView;
    private ImageView filterImageView, changeDateImageView;
    private Dialog filterDialog, dateChangeDialog;
    private DatePicker fromDatePicker, toDatePicker;
    private TextView rangeDisplay;
    private boolean noStarFilter, noFacilityFilter, sortByClosest = false, sortByCheapest = false;
    private boolean starBool[], facilitiesBool[], filtered = false;

    public BoaterSearchResultsFragment() {
        // Required empty public constructor
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boater_search_results, container, false);

        fromDate = getArguments().getString("fromDate");
        toDate = getArguments().getString("toDate");

        prepareListData();

        starBool = new boolean[starRating.size()];
        facilitiesBool = new boolean[facilities.size()];

        prepareMarinaList();

        if (sortByClosest) {
            sortByDist();
        }
        if (sortByCheapest) {
            sortByPrice();
        }
        if (filtered) {
            filteredMarinaList = filterMarinaList();
        } else {
            minRange = getMinPrice();
            maxRange = getMaxPrice();
        }

        marinaListAdapter = new MarinaListAdapter(getActivity(), filteredMarinaList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        marinaListRecyclerView = view.findViewById(R.id.marina_search_result_recycler_view);
        marinaListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        marinaListRecyclerView.setLayoutManager(mLayoutManager);
        marinaListRecyclerView.setAdapter(marinaListAdapter);

        closestSortTextView = view.findViewById(R.id.closest_sort_textView);
        closestSortTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByDist();
                marinaListAdapter.notifyDataSetChanged();
            }
        });
        cheapestSortTextView = view.findViewById(R.id.cheapest_sort_textView);
        cheapestSortTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByPrice();
                marinaListAdapter.notifyDataSetChanged();
            }
        });

        filterDialog = new Dialog(getContext());
        filterDialog.setContentView(R.layout.filter_dialog);
        filterDialog.setTitle("Filters");

        expandableListAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        ((ExpandableListView) filterDialog.findViewById(R.id.filter_expandableList)).setAdapter(expandableListAdapter);

        freeCancellationSwitch = filterDialog.findViewById(R.id.free_cancellation_switch);
        freeCancellationSwitch.setChecked(freeCancellationNeeded);
        freeCancellationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                freeCancellationNeeded = isChecked;
            }
        });

        rangeDisplay = filterDialog.findViewById(R.id.range_display);
        rangeDisplay.setText("From " + minRange + " to " + maxRange);
        priceRangeSeekBar = filterDialog.findViewById(R.id.price_range_seekbar);
        priceRangeSeekBar.setRangeValues(getMinPrice(), getMaxPrice());
        priceRangeSeekBar.setSelectedMaxValue(maxRange);
        priceRangeSeekBar.setSelectedMinValue(minRange);
        priceRangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Float>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Float minValue, Float maxValue) {
                minRange = minValue;
                maxRange = maxValue;
                rangeDisplay.setText("From " + minValue + " to " + maxValue);
            }
        });

        showResults = filterDialog.findViewById(R.id.show_result_button);
        showResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredMarinaList = filterMarinaList();
                marinaListAdapter = new MarinaListAdapter(getActivity(), filteredMarinaList);
                marinaListRecyclerView.setAdapter(marinaListAdapter);
                filterDialog.dismiss();
                filtered = true;
            }
        });

        filterImageView = view.findViewById(R.id.change_filter_icon);
        filterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDialog.show();
            }
        });

        dateChangeDialog = new Dialog(getContext());
        dateChangeDialog.setContentView(R.layout.date_change_dialog);
        dateChangeDialog.setTitle("Change Date");

        fromDatePicker = dateChangeDialog.findViewById(R.id.from_date_pick);
        toDatePicker = dateChangeDialog.findViewById(R.id.to_date_pick);

        setDateButton = dateChangeDialog.findViewById(R.id.set_date_button);
        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateChangeDialog.dismiss();
                fromDate = fromDatePicker.getDayOfMonth() + "/" + fromDatePicker.getMonth() + "/" + fromDatePicker.getYear();
                toDate = fromDatePicker.getDayOfMonth() + "/" + toDatePicker.getMonth() + "/" + toDatePicker.getYear();
            }
        });

        changeDateImageView = view.findViewById(R.id.change_date_icon);
        changeDateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateChangeDialog.show();
            }
        });

        return view;
    }

    private ArrayList<MarinaModel> filterMarinaList() {

        getFilterBoolean();
        checkNoStarFilter();
        checkNoFacilityFilter();

        ArrayList<MarinaModel> temp = new ArrayList<>();

        for (MarinaModel m : marinaList) {

            if (between(Float.parseFloat(m.getPrice()), minRange, maxRange) && freeCancellation(m) && (noStarFilter || starBool[m.getRating()]) && facilitiesMatch(m)) {
                temp.add(m);
            }

        }

        return temp;

    }

    private boolean facilitiesMatch(MarinaModel m) {

        int array[] = m.getFacilitiesAvlbl();
        for (int i = 0; i < facilitiesBool.length; i++) {

            if (facilitiesBool[i]) {
                if (!contains(i, array)) return false;
            }

        }
        return true;

    }

    private boolean contains(int x, int y[]) {

        for (int i = 0; i < y.length; i++) {
            if (y[i] == x) return true;
        }
        return false;
    }

    private void checkNoStarFilter() {

        noStarFilter = true;
        for (boolean a : starBool) {
            if (a) {
                noStarFilter = false;
                break;
            }
        }

    }

    private void checkNoFacilityFilter() {

        noFacilityFilter = false;
        for (boolean a : facilitiesBool) {
            if (a) {
                noFacilityFilter = false;
                break;
            }
        }

    }

    private void getFilterBoolean() {

        for (int i = 0; i < 6; i++) {
            starBool[i] = false;
        }
        for (int i = 0; i < facilitiesBool.length; i++) {
            facilitiesBool[i] = false;
        }

        final Set<Pair<Long, Long>> checkedItems = expandableListAdapter.getCheckedItems();

        for (Pair<Long, Long> pair : checkedItems) {

            switch (pair.first.intValue()) {

                case 0: {

                    switch (pair.second.intValue()) {

                        case 0:
                            starBool[0] = true;
                            break;
                        case 1:
                            starBool[1] = true;
                            break;
                        case 2:
                            starBool[2] = true;
                            break;
                        case 3:
                            starBool[3] = true;
                            break;
                        case 4:
                            starBool[4] = true;
                            break;
                        case 5:
                            starBool[5] = true;
                            break;
                        case 6:
                            starBool[6] = true;
                            break;

                    }

                    break;
                }

                case 1: {

                    switch (pair.second.intValue()) {

                        case 0:
                            facilitiesBool[0] = true;
                            break;
                        case 1:
                            facilitiesBool[1] = true;
                            break;
                        case 2:
                            facilitiesBool[2] = true;
                            break;
                        case 3:
                            facilitiesBool[3] = true;
                            break;
                        case 4:
                            facilitiesBool[4] = true;
                            break;
                        case 5:
                            facilitiesBool[5] = true;
                            break;
                        case 6:
                            facilitiesBool[6] = true;
                            break;
                        case 7:
                            facilitiesBool[7] = true;
                            break;
                        case 8:
                            facilitiesBool[8] = true;
                            break;
                        case 9:
                            facilitiesBool[9] = true;
                            break;

                    }

                    break;
                }

            }

        }
    }

    private boolean freeCancellation(MarinaModel m) {

        if (!freeCancellationNeeded || (freeCancellationNeeded && m.isFreeCancellation())) {

            return true;

        } else {

            return false;

        }

    }

    private boolean between(Float a, Float minRange, Float maxRange) {

        if (a >= minRange && a <= maxRange) return true;
        else return false;

    }

    private void sortByPrice() {

        sortByCheapest = true;
        sortByClosest = false;

        Comparator<MarinaModel> marinaModelComparator = new Comparator<MarinaModel>() {
            @Override
            public int compare(MarinaModel o1, MarinaModel o2) {

                float p1 = Float.parseFloat(o1.getPrice());
                float p2 = Float.parseFloat(o2.getPrice());

                return (int) (p1 - p2);
            }
        };

        Collections.sort(filteredMarinaList, marinaModelComparator);

        cheapestSortTextView.setTypeface(null, Typeface.BOLD);
        closestSortTextView.setTypeface(null, Typeface.NORMAL);
    }

    private void sortByDist() {

        sortByCheapest = false;
        sortByClosest = true;

        Comparator<MarinaModel> marinaModelComparator = new Comparator<MarinaModel>() {
            @Override
            public int compare(MarinaModel o1, MarinaModel o2) {

                float d1 = o1.getDistFromCity();
                float d2 = o2.getDistFromCity();

                return (int) (d1 - d2);
            }
        };

        Collections.sort(filteredMarinaList, marinaModelComparator);

        cheapestSortTextView.setTypeface(null, Typeface.NORMAL);
        closestSortTextView.setTypeface(null, Typeface.BOLD);
    }


    private float getMaxPrice() {

        float max = 0;

        for (MarinaModel m : marinaList) {
            if (Float.parseFloat(m.getPrice()) > max) {
                max = Float.parseFloat(m.getPrice());
            }
        }
        return max;
    }

    private float getMinPrice() {

        float min = Float.parseFloat(marinaList.get(0).getPrice());

        for (MarinaModel m : marinaList) {
            if (Float.parseFloat(m.getPrice()) < min) {
                min = Float.parseFloat(m.getPrice());
            }
        }
        return min;
    }

    private void prepareMarinaList() {

        Bitmap image = Bitmap.createBitmap(150, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawColor(Color.GRAY);


        String t = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer consequat, mi a blandit auctor, massa dui sollicitudin lectus, id vestibulum sapien nisl at mi. Pellentesque laoreet dapibus ipsum vel fermentum. ";
        String d = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer consequat, mi a blandit auctor, massa dui sollicitudin lectus, id vestibulum sapien nisl at mi. Pellentesque laoreet dapibus ipsum vel fermentum. ";

        marinaList = new ArrayList<>();
        marinaList.add(new MarinaModel("Hello", image, "2.0", "default", 5.0f, 1, true, d, t, new int[]{1, 2, 3}));
        marinaList.add(new MarinaModel("Hello", image, "5.0", "default", 2.0f, 2, false, d, t, new int[]{0, 1, 2}));
        marinaList.add(new MarinaModel("Hello", image, "3.0", "default", 1.0f, 3, false, d, t, new int[]{1, 3}));
        marinaList.add(new MarinaModel("Hello", image, "1.0", "default", 4.0f, 4, true, d, t, new int[]{7, 1, 0}));
        marinaList.add(new MarinaModel("Hello", image, "4.0", "default", 3.0f, 5, true, d, t, new int[]{1, 8, 6, 0, 4}));
        filteredMarinaList = marinaList;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        starRating = new ArrayList<>();
        facilities = new ArrayList<>();

        // Adding child data
        listDataHeader.add("Star rating");
        listDataHeader.add("Facilities");

        // Adding child data

        starRating.add("Unrated");
        starRating.add("1 star");
        starRating.add("2 stars");
        starRating.add("3 stars");
        starRating.add("4 stars");
        starRating.add("5 stars");


        facilities.add("Drinking Water");
        facilities.add("Electricity");
        facilities.add("Fuel Station");
        facilities.add("24/7 Access");
        facilities.add("Travel Lift");
        facilities.add("Security");
        facilities.add("Residual Water Collection");
        facilities.add("Restaurant");
        facilities.add("Dry Port");
        facilities.add("Maintenance");


        listDataChild.put(listDataHeader.get(0), starRating); // Header, Child data
        listDataChild.put(listDataHeader.get(1), facilities);

    }

}
