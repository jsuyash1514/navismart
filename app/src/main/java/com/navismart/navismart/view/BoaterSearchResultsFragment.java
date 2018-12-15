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

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    RangeSeekBar<Float> priceRangeSeekBar;
    private Button showResults, setDateButton;
    private Switch freeCancellationSwitch;
    private List<MarinaModel> marinaList, filteredMarinaList;
    private RecyclerView marinaListRecyclerView;
    private MarinaListAdapter marinaListAdapter;
    private TextView closestSortTextView, cheapestSortTextView;
    private ImageView filterImageView, changeDateImageView;
    private Dialog filterDialog, dateChangeDialog;
    private TextView rangeDisplay;
    private float minRange, maxRange;
    private boolean freeCancellationNeeded = false;
    private boolean noStarFilter;
    private boolean starBool[];

    public BoaterSearchResultsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_boater_search_results, container, false);

        starBool = new boolean[6];

        prepareMarinaList();

        marinaListAdapter = new MarinaListAdapter(filteredMarinaList);
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
        prepareListData();
        expandableListAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);
        ((ExpandableListView) filterDialog.findViewById(R.id.filter_expandableList)).setAdapter(expandableListAdapter);

        minRange = getMinPrice();
        maxRange = getMaxPrice();

        freeCancellationSwitch = filterDialog.findViewById(R.id.free_cancellation_switch);
        freeCancellationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                freeCancellationNeeded = isChecked;
            }
        });

        rangeDisplay = filterDialog.findViewById(R.id.range_display);
        rangeDisplay.setText("From " + minRange + " to " + maxRange);
        priceRangeSeekBar = filterDialog.findViewById(R.id.price_range_seekbar);
        priceRangeSeekBar.setRangeValues(minRange, maxRange);
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
                marinaListAdapter = new MarinaListAdapter(filteredMarinaList);
                marinaListRecyclerView.setAdapter(marinaListAdapter);
                filterDialog.dismiss();
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

        setDateButton = dateChangeDialog.findViewById(R.id.set_date_button);
        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateChangeDialog.dismiss();
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

        ArrayList<MarinaModel> temp = new ArrayList<>();

        for (MarinaModel m : marinaList) {

            if (between(Float.parseFloat(m.getPrice()), minRange, maxRange) && freeCancellation(m) && (noStarFilter || starBool[m.getRating()])) {
                temp.add(m);
            }

        }

        return temp;

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

    private void getFilterBoolean() {

        for (int i = 0; i < 6; i++) {
            starBool[i] = false;
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

        marinaList = new ArrayList<>();
        marinaList.add(new MarinaModel("Hello", image, "2.0", "default", 5.0f, 1, true));
        marinaList.add(new MarinaModel("Hello", image, "5.0", "default", 2.0f, 2, false));
        marinaList.add(new MarinaModel("Hello", image, "3.0", "default", 1.0f, 3, false));
        marinaList.add(new MarinaModel("Hello", image, "1.0", "default", 4.0f, 4, true));
        marinaList.add(new MarinaModel("Hello", image, "4.0", "default", 3.0f, 5, true));
        filteredMarinaList = marinaList;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Star rating");
        listDataHeader.add("Facilities");

        // Adding child data
        List<String> starRating = new ArrayList<String>();
        starRating.add("Unrated");
        starRating.add("1 star");
        starRating.add("2 stars");
        starRating.add("3 stars");
        starRating.add("4 stars");
        starRating.add("5 stars");

        ArrayList<String> facilities = new ArrayList<>();
        facilities.add("Facility 1");
        facilities.add("Facility 2");
        facilities.add("Facility 3");
        facilities.add("Facility 4");
        facilities.add("Facility 5");


        listDataChild.put(listDataHeader.get(0), starRating); // Header, Child data
        listDataChild.put(listDataHeader.get(1), facilities);

    }

}
