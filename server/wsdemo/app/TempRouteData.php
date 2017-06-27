<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class TempRouteData extends Model
{
    //
    protected $table = 'temp_routedata';
    
    public function routedata()
    {
        return $this->belongsTo('App\RouteData');
    }
}
