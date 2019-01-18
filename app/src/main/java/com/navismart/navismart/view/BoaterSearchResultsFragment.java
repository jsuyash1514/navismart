package com.navismart.navismart.view;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.maps.android.SphericalUtil;
import com.navismart.navismart.R;
import com.navismart.navismart.adapters.ExpandableListAdapter;
import com.navismart.navismart.adapters.MarinaListAdapter;
import com.navismart.navismart.model.MarinaModel;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import static android.app.Activity.RESULT_OK;
import static com.navismart.navismart.MainActivity.getCountOfDays;
import static com.navismart.navismart.MainActivity.getDateFromString;
import static com.navismart.navismart.MainActivity.toBounds;

public class BoaterSearchResultsFragment extends Fragment {

    public static String fromDate, toDate;
    public static int noOfDocks = 0;
    private static float minRange, maxRange;
    private static boolean freeCancellationNeeded = false;
    int PLACE_PICKER_REQUEST = 1;
    List<String> starRating;
    ArrayList<String> facilities;
    String t = "", d = "";
    private float rating;
    private EditText locationEditText;
    private String locationAddress, marinaAddress, name;
    private LatLng locationLatLng;
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
    private ImageView locationChangeIcon, closeDialogIcon;
    private Dialog filterDialog, dateChangeDialog;
    private DatePicker fromDatePicker, toDatePicker;
    private TextView rangeDisplay;
    private TextView noResultsDisplay;
    private boolean noStarFilter, noFacilityFilter, sortByClosest = false, sortByCheapest = false;
    private boolean starBool[], facilitiesBool[], filtered = false;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ArrayList<String> marinaUIDList;
    private ProgressDialog fetchMarinaProgress;
    private long receptionCapacity;

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

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "No user found", Toast.LENGTH_SHORT).show();
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.boaterLandingFragment, true)
                    .build();
            Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(R.id.boaterLogoutAction, null, navOptions);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        fetchMarinaProgress = new ProgressDialog(getContext());

        fromDate = getArguments().getString("fromDate");
        toDate = getArguments().getString("toDate");

        noOfDocks = getArguments().getInt("noOfDocks");

        noResultsDisplay = view.findViewById(R.id.no_results_display);

        locationEditText = view.findViewById(R.id.location_editText_searchResult);
        locationChangeIcon = view.findViewById(R.id.change_location_icon);

        locationAddress = getArguments().getString("location_address");
        locationLatLng = getArguments().getParcelable("locationLatLng");

        prepareListData();

        starBool = new boolean[starRating.size()];
        facilitiesBool = new boolean[facilities.size()];

//        prepareMarinaList();
//        prepareMarinaList(locationLatLng);

        prepareMarinaList();
        marinaListRecyclerView = view.findViewById(R.id.marina_search_result_recycler_view);


        closestSortTextView = view.findViewById(R.id.closest_sort_textView);
        closestSortTextView.setOnClickListener((View v) -> {
            sortByDist();
            marinaListAdapter.notifyDataSetChanged();
        });
        cheapestSortTextView = view.findViewById(R.id.cheapest_sort_textView);
        cheapestSortTextView.setOnClickListener((View v) -> {
            sortByPrice();
            marinaListAdapter.notifyDataSetChanged();
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
        showResults.setOnClickListener((View v) -> {
            filteredMarinaList = filterMarinaList();
            marinaListAdapter = new MarinaListAdapter(getActivity(), filteredMarinaList);
            marinaListRecyclerView.setAdapter(marinaListAdapter);
            filterDialog.dismiss();
            filtered = true;
        });

        filterImageView = view.findViewById(R.id.change_filter_icon);
        filterImageView.setOnClickListener((View v) -> filterDialog.show());

        dateChangeDialog = new Dialog(getContext());
        dateChangeDialog.setContentView(R.layout.date_change_dialog);
        dateChangeDialog.setTitle("Change Date");

        closeDialogIcon = dateChangeDialog.findViewById(R.id.close_dialog);
        closeDialogIcon.setOnClickListener((View v) -> dateChangeDialog.dismiss());

        fromDatePicker = dateChangeDialog.findViewById(R.id.from_date_pick);
        toDatePicker = dateChangeDialog.findViewById(R.id.to_date_pick);
        Calendar c = Calendar.getInstance();
        fromDatePicker.setMinDate(c.getTimeInMillis());
        c.add(Calendar.DATE, 1);
        toDatePicker.setMinDate(c.getTimeInMillis());

        Date from = getDateFromString(fromDate);
        Date to = getDateFromString(toDate);

        fromDatePicker.updateDate(from.getYear() + 1900, from.getMonth(), from.getDate());
        toDatePicker.updateDate(to.getYear() + 1900, to.getMonth(), to.getDate());

        setDateButton = dateChangeDialog.findViewById(R.id.set_date_button);
        setDateButton.setOnClickListener((View v) -> {

            if (getCountOfDays(fromDatePicker.getDayOfMonth() + "/" + fromDatePicker.getMonth() + "/" + fromDatePicker.getYear(), toDatePicker.getDayOfMonth() + "/" + toDatePicker.getMonth() + "/" + toDatePicker.getYear()) <= 0) {
                Toast.makeText(getContext(), "Departure Date cannot be earlier or the same as Arrival Date!", Toast.LENGTH_SHORT).show();
            } else {
                fromDate = fromDatePicker.getDayOfMonth() + "/" + (fromDatePicker.getMonth() + 1) + "/" + fromDatePicker.getYear();
                toDate = toDatePicker.getDayOfMonth() + "/" + (toDatePicker.getMonth() + 1) + "/" + toDatePicker.getYear();
                dateChangeDialog.dismiss();
            }

        });

        changeDateImageView = view.findViewById(R.id.change_date_icon);
        changeDateImageView.setOnClickListener((View v) -> dateChangeDialog.show());
        locationEditText.setText(locationAddress);
        Log.d("Address", locationAddress);
        locationEditText.setOnClickListener((View v) -> {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            builder.setLatLngBounds(toBounds(locationLatLng, 1000));
            try {
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        locationChangeIcon.setOnClickListener((View v) -> {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            builder.setLatLngBounds(toBounds(locationLatLng, 1000));
            try {
                startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
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

            if (between(Float.parseFloat(m.getPrice()), minRange, maxRange) && freeCancellation(m) && (noStarFilter || starBool[(int) m.getRating()]) && facilitiesMatch(m)) {
                temp.add(m);
            }

        }

        return temp;

    }

    private boolean facilitiesMatch(MarinaModel m) {

        ArrayList<Integer> array = m.getFacilitiesAvlbl();
        for (int i = 0; i < facilitiesBool.length; i++) {

            if (facilitiesBool[i]) {
                if (!contains(i, array)) return false;
            }

        }
        return true;

    }

    private boolean contains(Integer x, ArrayList<Integer> y) {

        for (Integer a : y) {
            if (x.equals(a)) return true;
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

//                float d1 = o1.getDistFromCity();
//                float d2 = o2.getDistFromCity();
//
                double d1 = SphericalUtil.computeDistanceBetween(locationLatLng, new LatLng(o1.getLat(), o1.getLng()));
                double d2 = SphericalUtil.computeDistanceBetween(locationLatLng, new LatLng(o2.getLat(), o2.getLng()));

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

        float min;
        if (marinaList.size() > 0) min = Float.parseFloat(marinaList.get(0).getPrice());
        else min = 0;
        for (MarinaModel m : marinaList) {
            if (Float.parseFloat(m.getPrice()) < min) {
                min = Float.parseFloat(m.getPrice());
            }
        }
        return min;
    }

    private void prepareMarinaList() {
        /////////////////////////////////////////PREPARE MARINA LIST//////////////////////////////////////////////////////////

        marinaUIDList = new ArrayList<>();
        marinaList = new ArrayList<>();

        double latitude = locationLatLng.latitude;
        double longitude = locationLatLng.longitude;

        int i = (int) (latitude / 10);
        int temp = ((int) latitude) % 10;
        if (temp < 5) i = i * 10;
        else i = (i * 10) + 5;

        int j = (int) (longitude / 10);
        temp = ((int) longitude) % 10;
        if (temp < 5) j = j * 10;
        else j = (j * 10) + 5;

        Log.d("Firestore: ", "i: " + i + " j: " + j);
        fetchMarinaProgress.setMessage("Fetching marina list...");
        fetchMarinaProgress.show();
        DocumentReference location = firestore.collection("Location").document(i + "," + j);
        location.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.isComplete()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    marinaUIDList.addAll((ArrayList<String>) documentSnapshot.get("Marina List"));
                    Log.d("Firestore: ", "Recieved marina list at i,j with size: " + marinaUIDList.size());

                    if (marinaUIDList.size() == 0) {
                        marinaListRecyclerView.setVisibility(View.GONE);
                        noResultsDisplay.setVisibility(View.VISIBLE);
                    } else {
                        marinaListRecyclerView.setVisibility(View.VISIBLE);
                        noResultsDisplay.setVisibility(View.GONE);
                    }
                    for (String uid : marinaUIDList) {

                        DatabaseReference marinaDesc = databaseReference.child("users").child(uid);
                        MarinaModel model = new MarinaModel();
                        marinaDesc.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                name = (String) dataSnapshot.child("marina-description").child("marinaName").getValue();
                                model.setName(name);
                                t = (String) dataSnapshot.child("marina-description").child("terms-and-condition").getValue();
                                model.setTnc(t);
                                marinaAddress = (String) dataSnapshot.child("marina-description").child("locationAddress").getValue();
                                model.setLocation(marinaAddress);
                                d = (String) dataSnapshot.child("marina-description").child("description").getValue();
                                model.setDescription(d);
                                rating = Float.parseFloat(dataSnapshot.child("marina-description").child("starRating").getValue(String.class));
                                model.setRating(rating);
                                receptionCapacity = Long.parseLong(dataSnapshot.child("marina-description").child("capacity").getValue(String.class));
                                model.setReceptionCapacity(receptionCapacity);
                                ArrayList<Integer> f = new ArrayList<>();
                                for (DataSnapshot snapshot : dataSnapshot.child("marina-description").child("facilities").getChildren()) {
                                    f.add(((Long) snapshot.getValue()).intValue());
                                }
                                model.setFacilities(f);
                                model.setMarinaUID(uid);
                                Log.d("LAT", dataSnapshot.child("marina-description").child("latitude").getValue() + "");
                                model.setLat((double) dataSnapshot.child("marina-description").child("latitude").getValue());
                                model.setLng((double) dataSnapshot.child("marina-description").child("longitude").getValue());
                                model.setDistFromSearch((float) SphericalUtil.computeDistanceBetween(locationLatLng, new LatLng(model.getLat(), model.getLng())) / 1000.0f);
                                marinaList.add(model);
                                filteredMarinaList = marinaList;
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
                                Log.d("Firestore: ", "Size of filtered marina list: " + filteredMarinaList.size());
                                marinaListAdapter = new MarinaListAdapter(getActivity(), filteredMarinaList);
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

                                marinaListRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                marinaListRecyclerView.setLayoutManager(mLayoutManager);
                                marinaListRecyclerView.setAdapter(marinaListAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
                fetchMarinaProgress.dismiss();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Firestore", "Failed to recieve marina list.");
                        fetchMarinaProgress.dismiss();
                    }
                });


//////////////////////////////////////////////////////////////////////////////////////////////////////////
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, getContext());
            locationAddress = place.getAddress().toString();
            locationEditText.setText(locationAddress);
            locationLatLng = place.getLatLng();
            prepareMarinaList();
        }
    }

}
