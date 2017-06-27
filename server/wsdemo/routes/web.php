<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function () {
    return view('test');
});
Route::get('temproute/addtemp','RouteDataController@addTemp');
Route::get('routedata/updateVantoc','RouteDataController@updateVanTocTB');
Route::get('routedata/update','RouteDataController@updateVantoc');

Route::post('routedata/getroute','RouteDataController@getRoute');
Route::resource('sanpham','ControllerSanPham');
Route::resource('routedata','RouteDataController');
Route::resource('temproute','TempRouteDataController');