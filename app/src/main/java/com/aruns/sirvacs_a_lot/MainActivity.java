package com.aruns.sirvacs_a_lot;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.aruns.sirvacs_a_lot.models.Center;
import com.aruns.sirvacs_a_lot.models.CentersRoot;
import com.aruns.sirvacs_a_lot.models.District;
import com.aruns.sirvacs_a_lot.models.DistrictsRoot;
import com.aruns.sirvacs_a_lot.models.Session;
import com.aruns.sirvacs_a_lot.models.SessionDetail;
import com.aruns.sirvacs_a_lot.models.State;
import com.aruns.sirvacs_a_lot.models.States;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {

private static List<State> stateList;
private static List<District> districtList;
private static boolean initialCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        getStateList();
        initialCall=false;
        List<CharSequence> list = new ArrayList<CharSequence>();
        for (State state:stateList){
            list.add(state.getStateName());
        }
        Spinner stateSpinner = findViewById(R.id.stateSpinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(adapter);
        stateSpinner.setSelection(0);
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               if(!initialCall){
                  populateDistrictSpinner();

               }
               else {
                   initialCall=false;
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        Switch mySwitch = findViewById(R.id.pincodeSwitch);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RecyclerView recyclerView=findViewById(R.id.idResultHolder);
                TextView detailsTextView=findViewById(R.id.detailsTextView);
                recyclerView.setVisibility(View.GONE);
                detailsTextView.setVisibility(View.GONE);
                LinearLayout pincodeLayout=findViewById(R.id.pincodeLayout);
                LinearLayout districtLayout=findViewById(R.id.districtLayout);
                LinearLayout stateLayout =findViewById(R.id.stateLayout);
               if(isChecked){
                   stateLayout.setVisibility(View.GONE);
                   districtLayout.setVisibility(View.GONE);
                   pincodeLayout.setVisibility(View.VISIBLE);
               }
               else {
                   pincodeLayout.setVisibility(View.GONE);
                   stateLayout.setVisibility(View.VISIBLE);
                   districtLayout.setVisibility(View.VISIBLE);
               }
            }
        });

        Button getOpenSlotsBtn= findViewById(R.id.openSlotsButton);
        getOpenSlotsBtn.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                getSlots(false);
            }
        });

        Button getallSlotsBtn= findViewById(R.id.allSlotsButton);
        getallSlotsBtn.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                getSlots(true);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void populateDistrictSpinner(){
        Spinner spinner = findViewById(R.id.stateSpinner);
        String currentState = spinner.getSelectedItem().toString();
        Integer stateId=stateList.stream().filter(x->x.getStateName().equals(currentState)).findFirst().get().getStateId();
        getDistrictList(stateId);
        List<CharSequence> list = new ArrayList<CharSequence>();
        for (District district:districtList){
            list.add(district.getDistrictName());
        }
        Spinner districtSpinner = findViewById(R.id.districtSpinner);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter);
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void getStateList(){
        States states = null;
        URL url = null;
        try {
            url = new URL("https://cdn-api.co-vin.in/api/v2/admin/location/states");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            states = mapper.readValue(responseStream, States.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        stateList=states.getStates();

    }

    private void getDistrictList(int stateId){
        DistrictsRoot districts = null;
        URL url = null;
        try {
            url = new URL("https://cdn-api.co-vin.in/api/v2/admin/location/districts/"+stateId);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            districts = mapper.readValue(responseStream, DistrictsRoot.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        districtList=districts.getDistricts();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getSlots(boolean allSlots){

        Switch pincodeSwitch = findViewById(R.id.pincodeSwitch);
        if(pincodeSwitch.isChecked()){
            EditText pinCodeText = findViewById(R.id.editTextNumber);
            String textFromUi=pinCodeText.getText().toString();
            if(textFromUi!=null && StringUtils.isNotBlank(textFromUi) && StringUtils.isNotEmpty(textFromUi)){
               getSlotsByPinCode(textFromUi,allSlots);
            }
            else {
                RecyclerView recyclerView=findViewById(R.id.idResultHolder);
                TextView detailsTextView=findViewById(R.id.detailsTextView);
                recyclerView.setVisibility(View.GONE);
                detailsTextView.setText("Enter a valid Pin Code!");
                detailsTextView.setVisibility(View.VISIBLE);
            }

        }
        else{
            Spinner spinner = findViewById(R.id.districtSpinner);
            String currentDistrict = spinner.getSelectedItem().toString();
            if(currentDistrict==null){
                RecyclerView recyclerView=findViewById(R.id.idResultHolder);
                TextView detailsTextView=findViewById(R.id.detailsTextView);
                recyclerView.setVisibility(View.GONE);
                detailsTextView.setText("Select a District!");
                detailsTextView.setVisibility(View.VISIBLE);
            }
            else {
                Integer districtId = districtList.stream().filter(x -> x.getDistrictName().equals(currentDistrict)).findFirst().get().getDistrictId();
                getSlotsByDistrict(districtId,allSlots);
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getSlotsByPinCode(String pincode, boolean noFiltering){
        List<Center> fullCenterList;
        String url = String.format(String.format("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=%s",pincode)) + "&date=%s";
        fullCenterList=getAllCenters(url);
        List<Session> openSessions=new ArrayList<>();
        if(noFiltering){
            fullCenterList.stream().forEach(x->openSessions.addAll(x.getSessions()));
        }
        else{
            List<Center> centersWithOpenSlots = fullCenterList.stream().filter(x->x.getSessions().stream().anyMatch(y->y.getAvailableCapacity()>0)).collect(Collectors.toList());
            centersWithOpenSlots.stream().forEach(x->openSessions.addAll(x.getSessions().stream().filter(y->y.getAvailableCapacity()>0).collect(Collectors.toList())));
        }
        ArrayList<SessionDetail> sessionDetails =new ArrayList<>();
        for(Session session:openSessions){
            SessionDetail detail=new SessionDetail();
            detail.sessionId=session.getSessionId();
            detail.vaccine=session.getVaccine();
            detail.minAgeLimit=session.getMinAgeLimit();
            detail.date=session.getDate();
            detail.availableSlots=session.getAvailableCapacity();
            detail.availableCapacityDose1=session.getAvailableCapacityDose1();
            detail.availableCapacityDose2=session.getAvailableCapacityDose2();
            Center center=fullCenterList.stream().filter(x->x.getSessions().stream().anyMatch(y->y.getSessionId().equals(session.getSessionId()))).findFirst().get();
            detail.centreName=center.getName();
            detail.pinCode=center.getPincode();
            detail.district=center.getDistrictName();
            detail.centreAddress=center.getAddress();
            detail.feeType=center.getFeeType();
            if(center.getVaccineFees()!=null && !center.getFeeType().equalsIgnoreCase("free")) {
                detail.fee = center.getVaccineFees().stream().filter(x -> x.getVaccine().equals(session.getVaccine())).findFirst().get().getFee();
            }
            else {
                detail.fee="0";
            }
            sessionDetails.add(detail);

        }
        if(noFiltering){
            setCenterCardListView(sessionDetails);
        }
        else {
            RadioGroup ageRadioGroup = findViewById(R.id.ageLimitRadio);
            int selectedRadioButtonId = ageRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
            String minAgeLimitFromUi = selectedRadioButton.getText().toString();
            int minAgeLimitFromUiInt = Integer.parseInt(minAgeLimitFromUi);

            RadioGroup doseRadioGroup = findViewById(R.id.doseRadio);
            int selectedDoseId = doseRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedDoseRadioButton = findViewById(selectedDoseId);
            String doseFromUi = selectedDoseRadioButton.getText().toString();
            ArrayList<SessionDetail> filteredList = new ArrayList<>();
            if (doseFromUi.equalsIgnoreCase("dose 1")) {
                filteredList = new ArrayList<>(sessionDetails.stream().filter(x -> x.availableCapacityDose1 > 0).collect(Collectors.toList()));
            } else {
                filteredList = new ArrayList<>(sessionDetails.stream().filter(x -> x.availableCapacityDose2 > 0).collect(Collectors.toList()));
            }
            filteredList = new ArrayList<>(filteredList.stream().filter(x -> x.minAgeLimit <= minAgeLimitFromUiInt).collect(Collectors.toList()));
            setCenterCardListView(filteredList);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getSlotsByDistrict(int districtId,boolean noFilter){
        List<Center> fullCenterList;
        String url = String.format("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=%d",districtId) + "&date=%s";
        fullCenterList=getAllCenters(url);
        List<Session> openSessions=new ArrayList<>();
        if(noFilter){
            fullCenterList.stream().forEach(x->openSessions.addAll(x.getSessions()));
        }
        else {
            List<Center> centersWithOpenSlots = fullCenterList.stream().filter(x -> x.getSessions().stream().anyMatch(y -> y.getAvailableCapacity() > 0)).collect(Collectors.toList());
            centersWithOpenSlots.stream().forEach(x -> openSessions.addAll(x.getSessions().stream().filter(y -> y.getAvailableCapacity() > 0).collect(Collectors.toList())));
        }
        ArrayList<SessionDetail> sessionDetails =new ArrayList<>();
        for(Session session:openSessions){
            SessionDetail detail=new SessionDetail();
            detail.sessionId=session.getSessionId();
            detail.vaccine=session.getVaccine();
            detail.minAgeLimit=session.getMinAgeLimit();
            detail.date=session.getDate();
            detail.availableSlots=session.getAvailableCapacity();
            detail.availableCapacityDose1=session.getAvailableCapacityDose1();
            detail.availableCapacityDose2=session.getAvailableCapacityDose2();
            Center center=fullCenterList.stream().filter(x->x.getSessions().stream().anyMatch(y->y.getSessionId().equals(session.getSessionId()))).findFirst().get();
            detail.centreName=center.getName();
            detail.pinCode=center.getPincode();
            detail.district=center.getDistrictName();
            detail.centreAddress=center.getAddress();
            detail.feeType=center.getFeeType();
            if(center.getVaccineFees()!=null && !center.getFeeType().equalsIgnoreCase("free")) {
                detail.fee = center.getVaccineFees().stream().filter(x -> x.getVaccine().equals(session.getVaccine())).findFirst().get().getFee();
            }
            else {
                detail.fee="0";
            }
            sessionDetails.add(detail);

        }

        if(noFilter){
            setCenterCardListView(sessionDetails);
        }
        else{
            RadioGroup ageRadioGroup=findViewById(R.id.ageLimitRadio);
            int selectedRadioButtonId = ageRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton =findViewById(selectedRadioButtonId);
            String minAgeLimitFromUi=selectedRadioButton.getText().toString();
            int minAgeLimitFromUiInt=Integer.parseInt(minAgeLimitFromUi);

            RadioGroup doseRadioGroup=findViewById(R.id.doseRadio);
            int selectedDoseId = doseRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedDoseRadioButton =findViewById(selectedDoseId);
            String doseFromUi=selectedDoseRadioButton.getText().toString();
            ArrayList<SessionDetail> filteredList=new ArrayList<>();
            if(doseFromUi.equalsIgnoreCase("dose 1")){
                filteredList= new ArrayList<>(sessionDetails.stream().filter(x -> x.availableCapacityDose1 > 0).collect(Collectors.toList()));
            }
            else {
                filteredList=new ArrayList<>(sessionDetails.stream().filter(x->x.availableCapacityDose2>0).collect(Collectors.toList()));
            }
            filteredList=new ArrayList<>(filteredList.stream().filter(x->x.minAgeLimit<=minAgeLimitFromUiInt).collect(Collectors.toList()));
            setCenterCardListView(filteredList);
        }


    }

    private List<Center> getAllCenters(String uri){
        CentersRoot centersRoot;
        String pattern = "dd-MM-yyyy";
        List<String> dateList=new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Using today's date
        dateList.add(sdf.format(c.getTime()));
        c.add(Calendar.DATE, 7); // Adding 7 days
        dateList.add(sdf.format(c.getTime()));
        List<Center> fullCenterList =new ArrayList<>();
        for (String dateInString:dateList){
            URL url = null;
            try {
                url = new URL(String.format(uri,dateInString));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                InputStream responseStream = connection.getInputStream();
                ObjectMapper mapper = new ObjectMapper();
                centersRoot = mapper.readValue(responseStream, CentersRoot.class);
                fullCenterList.addAll(centersRoot.getCenters());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return fullCenterList;
    }

    private void setCenterCardListView(ArrayList<SessionDetail> sessionDetails){
        StringBuilder detailBuilder=new StringBuilder();
        RecyclerView recyclerView=findViewById(R.id.idResultHolder);
        TextView detailsTextView=findViewById(R.id.detailsTextView);
        if(sessionDetails.size()<=0){
            recyclerView.setVisibility(View.GONE);
            detailsTextView.setVisibility(View.VISIBLE);
            detailBuilder.append("No slots available!\n\n");
            detailsTextView.setText(detailBuilder.toString());
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            detailsTextView.setVisibility(View.GONE);
            ResultAdapter resultAdapter=new ResultAdapter(this,sessionDetails);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(resultAdapter);
        }
    }
}