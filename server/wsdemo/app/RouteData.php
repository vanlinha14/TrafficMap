<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class RouteData extends Model
{
    //
    protected $table = 'routedata';
    public function vantoc()
    {
        return $this->hasMany('App\TempRouteData');
    }
}
