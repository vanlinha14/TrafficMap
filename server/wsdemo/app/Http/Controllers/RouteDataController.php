<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use App\RouteData;
use App\TempRouteData;

class RouteDataController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $a = RouteData::all();
        foreach ($a as $key => $value) {
            $array = array();
            $data = TempRouteData::where('route_data_id',$value->id)->get();
            if (isset($data)) {
                for ($i=0;  $i < count($data); $i++) 
                { 
                $array[] = $data[$i]->vantoc;
                }
                $a[$key]['vantoc'] = $array;
            }
        }
        return $a;
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
        return view('test');
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        //
        $routedata = RouteData::where('Duong', $request->Duong)
                            ->where('Phuong', $request->Phuong)
                            ->where('Quan', $request->Quan)
                            ->first(); // model or null
        $temp = new TempRouteData;
        if (!$routedata) {
           // Do stuff if it doesn't exist.
            $routedata = new RouteData;
            $routedata->Duong = $request->input('Duong');
            $routedata->Phuong = $request->input('Phuong');
            $routedata->Quan = $request->input('Quan');
            $routedata->save();
        }
        $temp->vantoc = $request->vantoc;
        $routedata->vantoc()->save($temp);

        return $routedata;
    }

    public function addTemp()
    {
        
        $temp = new TempRouteData;
        $temp->vantoc = 50;
        $temp->route_data_id = 2;
        $temp->save();
        return $temp;
    }
    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        //
        $route = RouteData::find($id);
        return array(
            'route'=> 
            array(
                'Duong'=>$route->Duong,
                'Phuong'=>$route->Phuong,
                'Quan' => $route->Quan,
                'VantocTB' => $route->VantocTB,
                'tempData' => $route->vantoc
                )
            );
    }

    public function getRoute(Request $request)
    {
        $routedata = RouteData::where('Duong', $request->Duong)
                            ->where('Phuong', $request->Phuong)
                            ->where('Quan', $request->Quan)
                            ->first(); // model or null
        if(!$routedata)
        {
            $message = "null";
            return array('message'=> $message);
        }
        else $message = "success";
        return array('message'=>$message, 'obj'=>$routedata);

    }

    //cron job
    // public function updateVanTocTB()
    // {
    //     $temp = TempRouteData::orderBy('route_data_id')
    //         ->groupBy('route_data_id')
    //         ->select('route_data_id', DB::raw('AVG(vantoc) as VantocTB'))
    //         ->havingRaw('AVG(vantoc)')
    //         ->get();

    //     return $temp;
    // }

    public function updateVantocTB()
    {
        $temp = TempRouteData::orderBy('route_data_id')
            ->groupBy('route_data_id')
            ->select('route_data_id', DB::raw('AVG(vantoc) as VantocTB'))
            ->havingRaw('AVG(vantoc)')
            ->get();

        //$temp->routedata->VantocTB = $temp->vantoc;
        //$temp->save();
        foreach ($temp as $t) {
            $route = RouteData::find($t->route_data_id);
            if(!$route)
                continue;
            $route->VantocTB = $t->VantocTB;
            $route->save();
        }
        return 'success';
        
    }

    public function updateVantoc()
    {
        $route = RouteData::all();
        foreach ($route as $r) {
            $r->VantocTB = rand(10,60);
            $r->save();
        }
        
        return $route;
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        //
    }
}
