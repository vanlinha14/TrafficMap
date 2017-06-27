<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateTemproutedataTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        //
        Schema::create('Temp_RouteData', function (Blueprint $table) {
            $table->increments('id_temp');
            $table->integer('id_route')->unsigned();
            $table->foreign('id_route')->references('id')->on('routedata');
            $table->double('vantoc');
            $table->timestamps();
            //
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        //
    }
}
