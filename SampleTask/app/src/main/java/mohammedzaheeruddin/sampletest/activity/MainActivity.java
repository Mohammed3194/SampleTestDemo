package mohammedzaheeruddin.sampletest.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;

import mohammedzaheeruddin.sampletest.R;
import mohammedzaheeruddin.sampletest.Utils;
import mohammedzaheeruddin.sampletest.adapters.DisplayAdapter;
import mohammedzaheeruddin.sampletest.db.Dao;
import mohammedzaheeruddin.sampletest.entity.ApiData;
import mohammedzaheeruddin.sampletest.entity.DisplayItem;
import mohammedzaheeruddin.sampletest.entity.Item;
import mohammedzaheeruddin.sampletest.entity.PostData;
import mohammedzaheeruddin.sampletest.retrofit.APIService;
import mohammedzaheeruddin.sampletest.retrofit.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mohammedzaheeruddin on 07-Jun-18.
 */

public class MainActivity extends AppCompatActivity{

    private Context context;
    private Activity activity;
    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    LayoutManager mLayoutManager;
    private MenuItem mSearchAction;
    private EditText edtSeach;

    private ArrayList<DisplayItem> displayItems = new ArrayList<>();
    private ArrayList<DisplayItem> searchList;
    private ArrayList<DisplayItem> recordList;
    private ArrayList<DisplayItem> tempItem = new ArrayList<>();
    DisplayAdapter mAdapter;
    Dao dao;

    private int visibleThreshold = 5;
    int totalItemCount,lastVisibleItem;
    int index = 1;
    int size = 10;
    boolean isLoading;
    private boolean isSearchOpened = false;

    // Retrofit
    private APIService apiService;

    String action = "newCmsPlacesInterview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiService = ApiUtils.getAPIService();
        context = this;
        activity = this;

        initWidgets();
    }

    public void initWidgets(){

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        dao = new Dao(context);
        searchList = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LayoutManager(context,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (Utils.isOnline(activity)){
            getInitialDataFromApi(0);
        }else {
            searchList.clear();
            new RecentRecordList().execute();
        }

        /*mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = mLayoutManager.getItemCount();
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold ) && displayItems.size() > 10) {
                    loadMoreData();
                    isLoading = true;
                }

            }
        });*/

    }

    public void getInitialDataFromApi(final int status){

        PostData postData = new PostData();
        postData.setLan_Id(6);

        apiService.Request(postData).enqueue(new Callback<ApiData>() {
            @Override
            public void onResponse(Call<ApiData> call, Response<ApiData> response) {
                if (response.isSuccessful()){
                    ApiData data = response.body();

                    ArrayList<DisplayItem> itemList = data.getCities();
                    Log.d("debug"," List size == "+itemList.size());
                    tempItem = new ArrayList<DisplayItem>();

                    for (int i =0; i < itemList.size();i++){
                        displayItems.add(new DisplayItem(itemList.get(i).getCity_Id(),
                                itemList.get(i).getCity_Name()));
                    }

                    for (DisplayItem item: displayItems){
                        dao.insertCityDetailsData(item.getCity_Id(),
                                item.getCity_Name());
                    }


                    mAdapter = new DisplayAdapter(context,displayItems);
                    mRecyclerView.setAdapter(mAdapter);

                    for(int i= 0; i<displayItems.size(); i++){
                        if(displayItems.get(i) != null ){
                            tempItem.add(displayItems.get(i));
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ApiData> call, Throwable t) {
                Log.d("debug"," error = = "+t);
            }
        });

        /*apiService.getData(index,size, ServiceConstants.ORDER,ServiceConstants.SORT,ServiceConstants.SITE).enqueue(new Callback<ApiData>() {
            @Override
            public void onResponse(Call<ApiData> call, Response<ApiData> response) {
                if(response.body() != null){
                    isLoading = false;
                }else{
                    isLoading = true;
                }

                if (response.isSuccessful()){
                    ApiData data = response.body();

                    ArrayList<Item> itemList = data.getItems();
                    tempItem = new ArrayList<DisplayItem>();
                    for (int i =0; i < itemList.size();i++){
                        displayItems.add(new DisplayItem(String.valueOf(itemList.get(i).getUser_id()),
                                itemList.get(i).getProfile_image(),
                                itemList.get(i).getDisplay_name(),
                                itemList.get(i).getReputation()));
                    }

                    for (DisplayItem item: displayItems){
                        dao.insertUserDetailsData(item.getUserId(),
                                item.getUserName(),
                                String.valueOf(item.getReputation()),
                                item.getProfileImageUrl());
                    }

                    if(status == (0)){
                        displayItems.add(null);
                        mAdapter = new DisplayAdapter(context,displayItems);
                        mRecyclerView.setAdapter(mAdapter);
                        index = displayItems.size();
                    }else{
                        displayItems.add(null);
                        mAdapter.refreshAdapter(context,isLoading,displayItems);
                        index = displayItems.size();
                    }

                    for(int i= 0; i<displayItems.size(); i++){
                        if(displayItems.get(i) != null ){
                            tempItem.add(displayItems.get(i));
                        }
                    }
                    tempItem.add(null);
                }else if (response.code() == 400){
                    Toast.makeText(context,getString(R.string.activity_no_data),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,getString(R.string.activity_internal_error),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiData> call, Throwable t) {
                Toast.makeText(context,getString(R.string.activity_unable_to_connect),Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    public class RecentRecordList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            recordList = new ArrayList<DisplayItem>();
            recordList.clear();
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            searchList = (ArrayList<DisplayItem>) dao.getCityDetailsInDescendingOrder();

            for (int i = 0; i< searchList.size(); i++) {
                DisplayItem item = searchList.get(i);
                recordList.add(item);
            }

            for(int i= 0; i<recordList.size(); i++){
                if(recordList.get(i) != null ){
                    tempItem.add(recordList.get(i));
                }
            }
            tempItem.add(null);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mAdapter = new DisplayAdapter(context,recordList);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar();

        if(isSearchOpened){

            action.setDisplayShowCustomEnabled(false);
            action.setDisplayShowTitleEnabled(true);

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            mSearchAction.setIcon(getResources().getDrawable(R.drawable.search));
            isSearchOpened = false;
        } else {

            action.setDisplayShowCustomEnabled(true);
            action.setCustomView(R.layout.search_bar);
            action.setDisplayShowTitleEnabled(false);

            edtSeach = (EditText)action.getCustomView().findViewById(R.id.edtSearch); //the text editor
            edtSeach.requestFocus();
            edtSeach.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String searchString = edtSeach.getEditableText().toString().trim();
                    mAdapter.filter(searchString,tempItem);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.close));
            isSearchOpened = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if(isSearchOpened) {
            handleMenuSearch();
            return;
        }
        super.onBackPressed();
    }


    class LayoutManager extends LinearLayoutManager {
        public LayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }

}
