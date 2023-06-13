function SearchController($scope, $rootScope, UserService) {
	try {
	UserService.helperList(function(response) {
		$scope.helperList=JSON.parse(response.json)
		console.log($scope.helperList);
	});
	} catch (e) {
		console.log(e);
	}

}