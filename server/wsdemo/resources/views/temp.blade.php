<form action="{{ action('RouteDataController@addTemp') }}" method="post" accept-charset="utf-8">
	{{ csrf_field() }}
	<input type="text" name="route_data_id" value="" placeholder="ID ROUTE">
	<input type="text" name="vantoc" value="" placeholder="Van toc">
	<input type="submit" name="" value="Submit">
</form>