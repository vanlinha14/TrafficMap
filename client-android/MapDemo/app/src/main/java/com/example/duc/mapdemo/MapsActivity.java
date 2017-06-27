package com.example.duc.mapdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.duc.mapdemo.Model.Direction.Direction;
import com.example.duc.mapdemo.Model.Direction.Distance.Distance;
import com.example.duc.mapdemo.Model.Direction.GeoCoder.AddressComponent;
import com.example.duc.mapdemo.Model.Direction.GeoCoder.GeoCoding;
import com.example.duc.mapdemo.Model.Direction.GeoCoder.ResultGeoCoder;
import com.example.duc.mapdemo.Model.Direction.Route;
import com.example.duc.mapdemo.Model.Direction.RouteData.ResponseRouteData;
import com.example.duc.mapdemo.Model.Direction.RouteData.RouteData;
import com.example.duc.mapdemo.Model.Direction.Step;
import com.example.duc.mapdemo.Retrofit.Api.DirectionApi;
import com.example.duc.mapdemo.Retrofit.Api.RouteDataApi;
import com.example.duc.mapdemo.Retrofit.HandleRequest;
import com.example.duc.mapdemo.Retrofit.MyService;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final int REQUEST_ACCESS = 100;
    private static final int INTERVAL = 60000; //1request/1min
    private static final String API_KEY = "AIzaSyAtkY17Wds2Gu43umulgXyS-X-8aExDGAw";
    private static final String MYTAG = "HaHuyDuc";
    private GoogleMap mMap;
    private ArrayList<com.google.android.gms.maps.model.Polyline> polylineList; //luu draw direction
    private static Location MYLOCATION = null;
    private static Location Pre_MyLocation = null;
    private static LatLng DIEMDEN = null;
    DirectionApi dirApi;
    RouteDataApi routeApi;
    Button startBtn,stopBtn,searchBtn;
    EditText searchEd;
    int count_request = 0;
    Double vantoc;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Timer timer;
    private int timeCount;
    private GetRouteDataTask getRouteDataTask;
    private ReveserLocationTask reveserLocationTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        dirApi = HandleRequest.createService(DirectionApi.class); //tao api chi duong
        startBtn = (Button) findViewById(R.id.startBtn);
        stopBtn = (Button) findViewById(R.id.stopBtn);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchEd = (EditText) findViewById(R.id.searchEd);
        polylineList = new ArrayList<>();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Bat dau",Toast.LENGTH_SHORT).show();
                countTime();
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(searchEd.getText()) != null)
                {
                    String origin = String.valueOf(searchEd.getText());
                    //khong dong bo
                    dirApi.requestLocation(origin).enqueue(new Callback<GeoCoding>() {
                        @Override
                        public void onResponse(Call<GeoCoding> call, Response<GeoCoding> response) {
                            Vitri(response.body());
                            RequestDirection();
                        }

                        @Override
                        public void onFailure(Call<GeoCoding> call, Throwable t) {
                            Toast.makeText(getBaseContext(),t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        routeApi = MyService.createService(RouteDataApi.class);
    }

    public void Vitri(GeoCoding vitri)
    {
        mProgress = ProgressDialog.show(this, null, "Vui lòng chờ...", true);
        ResultGeoCoder resultGeoCoder = vitri.getResult(0);
        if(resultGeoCoder != null) {
            com.example.duc.mapdemo.Model.Direction.Location location = resultGeoCoder.getGeometry().getLocation();
            DIEMDEN = new LatLng(location.getLat(), location.getLng());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(DIEMDEN, 18));
            mMap.addMarker(new MarkerOptions().title("Vi tri tim duoc").position(DIEMDEN));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 999)
        {
            bestProvider = locationManager.getBestProvider(criteria, true);
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ACCESS);
            Log.d(MYTAG, "Khong cap quyen: onMapReady");
            return;
        }
        if(isDeviceOnline())
        {
            MYLOCATION = getMyLocation();
            if(MYLOCATION != null) {
                LatLng myLocation = new LatLng(MYLOCATION.getLatitude(), MYLOCATION.getLongitude());
                mMap.addMarker(new MarkerOptions().position(myLocation).title("Vị trí của tôi"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18));

            }
        }
        else
        {
            Toast.makeText(getBaseContext(),"Kiểm tra kết nối Internet", Toast.LENGTH_SHORT).show();
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
            }
        } else {
            // Permission was denied. Display an error message.
        }
    }
    String bestProvider;
    LocationManager locationManager;
    Criteria criteria;
    //Tim nha cung cap vi tri
    private String getEnableLocationProvider() {
        Log.d(MYTAG, "Tim nha cung cap vi tri");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Log.d(MYTAG, "LocationManager:" + locationManager);
        //tieu chi chon nha cung cap vi tri
        criteria = new Criteria();
        //tim nha cung cap tot nhat hien tai: gps, network..
        bestProvider = locationManager.getBestProvider(criteria, true);
        //boolean enabled = locationManager.isProviderEnabled(bestProvider);
        Log.d(MYTAG, "bestProvider:" + bestProvider);
        if (!bestProvider.equals(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getBaseContext(),"Hãy bật gps",Toast.LENGTH_SHORT).show();
        }
        return bestProvider;
    }

    private Location getMyLocation() {
        Log.d(MYTAG, "Get My Location:");
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String locationProvider = getEnableLocationProvider();
        Log.d(MYTAG, "Location Manager:" + locationProvider);
        if (locationManager == null) {
            return null;
        }
        // Millisecond
        final long MIN_TIME_BW_UPDATES = 1000;
        // Met
        final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;


        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(MYTAG, "Khong duoc cap quyen xem vi tri");
                return null;
            }
            locationManager.requestLocationUpdates(locationProvider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            Log.d(MYTAG, "Location Manager truoc khi lay vi tri:" + LocationManager.PASSIVE_PROVIDER);
            MYLOCATION = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            Log.d(MYTAG, "Vi tri hien tai:" + MYLOCATION);
        } catch (SecurityException e) {
            Toast.makeText(this, "Show My Location Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(MYTAG, "Show My Location Error:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return MYLOCATION;
    }

    private void RequestDirection() {
        if(MYLOCATION == null)
        {
            if(isDeviceOnline())
            {
                getMyLocation();
            }
            else
            {
                Toast.makeText(getBaseContext(),"Không có kết nối mạng", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(DIEMDEN == null)
        {
            Toast.makeText(getBaseContext(),"Địa điểm không tìm thấy", Toast.LENGTH_SHORT).show();
            return;
        }
        String origin = String.valueOf(MYLOCATION.getLatitude()) +
                "," + String.valueOf(MYLOCATION.getLongitude());
        String destination = String.valueOf(DIEMDEN.latitude) +
                "," + String.valueOf(DIEMDEN.longitude);
        //khong dong bo
        dirApi.requestDirection(origin,destination).enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {
                drawDirection(response.body());
                reveserLocation();
            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {
                Toast.makeText(getBaseContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    public void clearDirection()
    {
        if(polylineList.size() != 0) {
            for (Polyline p : polylineList) {
                if (p != null)
                    p.remove();
            }
            polylineList.clear();
        }
    }

    ArrayList<LatLng> points = null; // cac diem cua tung doan duong
    ArrayList<RouteData> arrRoute = null;
    Route routeDirection = null;
    public void drawDirection(Direction dir)
    {
        clearDirection();
        points = new ArrayList<>();
        arrRoute = new ArrayList<>();
        for (Route route:dir.getRoutes()) {
            //ve het duong di 1 lan
//            List<LatLng> polylines = PolyUtil.decode(route.getOverviewPolyline().getPoints());
//            PolylineOptions options = new PolylineOptions().color(Color.BLUE).width(12).addAll(polylines);
//            polyline = mMap.addPolyline(options);
            routeDirection = route;
            //Step
            List<Step> steps = routeDirection.getLegs().get(0).getSteps();
            for (Step step : steps)
            {
                LatLng point = new LatLng(step.getStartLocation().getLat(),step.getStartLocation().getLng());
                RouteData rd = new RouteData(point);
                arrRoute.add(rd);
            }
        }
    }
    ProgressDialog mProgress = null;
    ArrayList<RouteData> routeDataList = null;

    int pickColor(Double vantoc)
    {
        if(vantoc == null)
            return Color.BLUE;
        if(vantoc <= 20)
            return Color.RED;
        if(vantoc > 20 && vantoc <= 40)
            return Color.YELLOW;
        if(vantoc > 40)
            return Color.GREEN;

        return Color.BLUE;
    }
    public void drawDirectionVer2()
    {
        List<Step> steps = routeDirection.getLegs().get(0).getSteps();
        for(int i = 0 ;i < steps.size(); i++)
        {
            Step step = steps.get(i);
            List<LatLng> polylines = PolyUtil.decode(step.getPolyline().getPoints());
            int color = pickColor(arrRoute.get(i).getVantoc());
            PolylineOptions options = new PolylineOptions().color(color).width(15).addAll(polylines);
            polylineList.add(mMap.addPolyline(options));
        }
    }

    public void reveserLocation()
    {
        routeDataList = new ArrayList<>();
        reveserLocationTask = new ReveserLocationTask();
        reveserLocationTask.execute();
    }

    ArrayList<RouteData> newRouteDataList;

    public void requestRouteData()
    {
        Log.d(MYTAG,"REQUEST ROUTE DATA");
        newRouteDataList = new ArrayList<>();
        Log.d(MYTAG, "REQUEST ROUTE DATA SIZE:" + routeDataList.size());
        getRouteDataTask = new GetRouteDataTask();
        getRouteDataTask.execute();
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latlng = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));
        Toast.makeText(getBaseContext(),"Thay đổi vị trí", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //thuc hien gui request kiem tra vi tri 1phut 1lan
    public void doSomething()
    {
        count_request++;
        Toast.makeText(getBaseContext(), "So lan:" + count_request, Toast.LENGTH_SHORT).show();
        if (count_request == 1) //bo qua lan dau
            return;
        Pre_MyLocation = MYLOCATION; //luu lai vi tri cu
        getMyLocation(); //cap nhat vi tri hien tai
        String origin = Pre_MyLocation.getLatitude() + "," + Pre_MyLocation.getLongitude();
        String destination = MYLOCATION.getLatitude() + "," + MYLOCATION.getLongitude();
        Log.d(MYTAG, "Toa do origin:" + origin);
        Log.d(MYTAG, "Toa do destination:" + destination);
        dirApi.calculateDistance(origin, destination).enqueue(new Callback<Distance>() {
            @Override
            public void onResponse(Call<Distance> call, Response<Distance> response) {
                calDistance(response.body());
            }

            @Override
            public void onFailure(Call<Distance> call, Throwable t) {
                Log.d(MYTAG, "Fail:" + t.getLocalizedMessage());
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //calDistance(dirApi.calculateDistance(origin,destination).execute().body());

    }

    public void calDistance(Distance dis)
    {
        Log.d(MYTAG,"Khoang cach met:"+dis.getRows().get(0));
        Log.d(MYTAG,"Khoang cach met:"+dis.getRows().get(0).getElements().get(0));
        Log.d(MYTAG,"Khoang cach met:"+dis.getRows().get(0).getElements().get(0).getDistance());
        Integer khoangcach_met =  dis.getRows().get(0).getElements().get(0).getDistance().getValue();
        Log.d(MYTAG,"Khoang cach met:"+String.valueOf(khoangcach_met));
        vantoc = (khoangcach_met/1000.0)*60; // thoi gian 1p => 1/60 gio. kh.cach doi qua don vi km.
        Log.d(MYTAG,"Van toc:"+String.valueOf(vantoc));
        String origin = MYLOCATION.getLatitude() + "," + MYLOCATION.getLongitude(); //
        Log.d(MYTAG,"Toa do vi tri hien tai:"+origin);
        dirApi.reverseLocation(origin).enqueue(new Callback<GeoCoding>() {
            @Override
            public void onResponse(Call<GeoCoding> call, Response<GeoCoding> response) {
                //tim ten duong/phuong/quan
                Log.d(MYTAG,"Result:"+response.body().getResult(0));
                ArrayList<String> arr = geoCoding(response.body().getResult(0));
                postRouteData(arr.get(0),arr.get(1),arr.get(2));
            }

            @Override
            public void onFailure(Call<GeoCoding> call, Throwable t) {
                Toast.makeText(getBaseContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<String> geoCoding(ResultGeoCoder result)
    {
        AddressComponent addressComponent = result.getAddressComponentByType("route");
        //Log.d(MYTAG,"AddressComponent:" + addressComponent);
        String duong = addressComponent.getLongName();
        //Log.d(MYTAG,"Duong:"+duong);
        addressComponent = result.getAddressComponentByType("sublocality_level_1");
        String phuong = addressComponent.getLongName();
        //Log.d(MYTAG,"Phuong:"+phuong);
        addressComponent = result.getAddressComponentByType("administrative_area_level_2");
        String quan = addressComponent.getLongName();
        //Log.d(MYTAG,"Quan:"+quan);
        //
        ArrayList<String> arr = new ArrayList<>();
        arr.add(duong);
        arr.add(phuong);
        arr.add(quan);

        return arr;
    }

    public void postRouteData(String duong, String phuong, String quan)
    {
        routeApi.postRouteData(duong,phuong,quan,vantoc).enqueue(new Callback<RouteData>() {
            @Override
            public void onResponse(Call<RouteData> call, Response<RouteData> response) {
                Log.d(MYTAG,"Them Thanh Cong");
            }

            @Override
            public void onFailure(Call<RouteData> call, Throwable t) {
                Log.d(MYTAG,"Loi:"+t.getLocalizedMessage());
                Toast.makeText(getBaseContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
    private void countTime() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //do something
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeCount += 1;
                        doSomething();
                    }
                });
            }
        }, 0, INTERVAL);
    }
    //check ket noi internet
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    public class GetRouteDataTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Log.d(MYTAG,"(AsyncTask Route Tast) Size Route data:"+routeDataList.size());
            Log.d(MYTAG,"(AsyncTask Route Tast) Size arrRoute:"+arrRoute.size());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(MYTAG,"(AsyncTask Route) Fininsh");
            for(RouteData i : arrRoute)
            {
                Log.d(MYTAG,"(AsyncTask) Result:"+i.toString());
            }
            drawDirectionVer2();
            mProgress.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(MYTAG,"AsyncTask RouteTask Start");
            for(RouteData rd : arrRoute)
            {
                String d = rd.getTenDuong();
                String p = rd.getTenPhuong();
                String q = rd.getTenQuan();
                Log.d(MYTAG, "D P Q:" + d + "," + p + "," + q);
                //dong bo
                try {
                    ResponseRouteData resp = routeApi.getRouteData(d,p,q).execute().body();
                    Log.d(MYTAG,"(AsyncTask) Message:"+resp.message);
                    if(resp.message.equals("success"))
                    {
                        rd.setVantoc(resp.obj.getVantoc());
                        Log.d(MYTAG,"(New route) Them thanh cong");
                    }
                } catch (IOException e) {
                    Log.d(MYTAG,"(AsyncTask) Fail:"+e.getLocalizedMessage());
                }

                //khong dong bo
//                if(d != null && p != null && q != null)
//                {
//                    routeApi.getRouteData(d,p,q).enqueue(new Callback<ResponseRouteData>() {
//                        @Override
//                        public void onResponse(Call<ResponseRouteData> call, Response<ResponseRouteData> response) {
//                            ResponseRouteData resp = response.body();
//                            Log.d(MYTAG,"(AsyncTask) Message:"+resp.message);
//                            if(resp.message.equals("success"))
//                            {
//                                if(!newRouteDataList.contains(resp.obj))
//                                    newRouteDataList.add(resp.obj);
//                                Log.d(MYTAG,"(New route) Them thanh cong");
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseRouteData> call, Throwable t) {
//                            Log.d(MYTAG,"(AsyncTask) Fail:"+t.getLocalizedMessage());
//                        }
//                    });
//                }

            }

//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return null;
        }
    }

    public class ReveserLocationTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Log.d(MYTAG,"(AsyncTask Reveser Task) Size Route data:"+routeDataList.size());
            Log.d(MYTAG,"(AsyncTask Reveser Task) Size arrRouteData:" + arrRoute.size());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(MYTAG,"(AsyncTask) Reveser Fininsh");
            requestRouteData();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(MYTAG,"AsyncTask ReveserTask Start");
//            for(LatLng latLng : points)
//            {
//                String origin = latLng.latitude + "," + latLng.longitude;
//                Log.d(MYTAG, "Origin:" + origin);
//                try {
//                    RouteData routeData = new RouteData();
//                    ArrayList<String> arr = geoCoding(dirApi.reverseLocation(origin).execute().body().getResult(0));
//                    routeData.setTenDuong(arr.get(0));
//                    routeData.setTenPhuong(arr.get(1));
//                    routeData.setTenQuan(arr.get(2));
//                    routeDataList.add(routeData);
//                    Log.d(MYTAG,"Size route data list:" + routeDataList.size());
//                } catch (Exception e) {
//                    Log.d(MYTAG,"(AsyncTask) Fail:"+ e.getLocalizedMessage());
//                }
//            }
            //khong dong bo
//            for(LatLng latLng : points)
//            {
//                String origin = latLng.latitude + "," + latLng.longitude;
//                Log.d(MYTAG,"Origin:"+origin);
//                dirApi.reverseLocation(origin).enqueue(new Callback<GeoCoding>() {
//                    @Override
//                    public void onResponse(Call<GeoCoding> call, Response<GeoCoding> response) {
//                        try {
//                            RouteData routeData = new RouteData();
//                            ArrayList<String> arr = geoCoding(response.body().getResult(0));
//                            routeData.setTenDuong(arr.get(0));
//                            routeData.setTenPhuong(arr.get(1));
//                            routeData.setTenQuan(arr.get(2));
//                            if(!routeDataList.contains(routeData))
//                                routeDataList.add(routeData);
//
//                            Log.d(MYTAG,"Size route data list:" + routeDataList.size());
//                        }catch (Exception e)
//                        {
//                            Log.d(MYTAG,"Request ReveserLocation Fail:" + e.getLocalizedMessage());
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<GeoCoding> call, Throwable t) {
//                        Log.d(MYTAG,"(AsyncTask) Fail:"+ t.getLocalizedMessage());
//                    }
//                });
//            }
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            //test
            for(RouteData data : arrRoute)
            {
                String origin = data.getStartLocation().latitude + "," + data.getStartLocation().longitude;
                ArrayList<String> arr = new ArrayList<>();
                Log.d(MYTAG,"Origin:"+origin);
                try {
                    arr = geoCoding(dirApi.reverseLocation(origin).execute().body().getResult(0));
                } catch (IOException e) {
                    Log.d(MYTAG,"Request ReveserLocation Fail:" + e.getLocalizedMessage());
                }
                data.setTenDuong(arr.get(0));
                data.setTenPhuong(arr.get(1));
                data.setTenQuan(arr.get(2));
            }

            return null;
        }
    }

}

