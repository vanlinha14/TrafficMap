<form action="{{ route('routedata.store') }}" method="post" accept-charset="utf-8">
	{{ csrf_field() }}
	<input type="text" name="Duong" value="" placeholder="Ten Duong">
	<input type="text" name="Phuong" value="" placeholder="Ten Phuong">
	<input type="text" name="Quan" value="" placeholder="Ten Quan">
	<input type="text" name="vantoc" value="" placeholder="Van Toc">
	<input type="submit" name="" value="Submit">
</form>