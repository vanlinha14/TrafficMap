<?php

namespace App\Console\Commands;

use Illuminate\Console\Command;
use App\TempRouteData;
use Log;
use App\RouteData;
use Illuminate\Support\Facades\DB;

class TempRouteProcessed extends Command
{
    /**
     * The name and signature of the console command.
     *
     * @var string
     */
    protected $signature = 'tempdata:handle';

    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = 'Xu ly du lieu temp route data';

    /**
     * Create a new command instance.
     *
     * @return void
     */
    public function __construct()
    {
        parent::__construct();
    }

    /**
     * Execute the console command.
     *
     * @return mixed
     */
    public function handle()
    {
        //
        Log::info('Ha Huy Duc: Update VantocTB');
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
        DB::table('temp_routedata')->truncate();
        
    }
}
