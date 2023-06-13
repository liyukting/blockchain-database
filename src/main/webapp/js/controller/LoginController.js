function LoginController($scope, $rootScope, UserService) {
	try {
	UserService.securityCheck($.param({
		secu : $rootScope.accessKey
	}), function(response) {
		console.log(response);
		if (response != null && response.result == "1") {
			window.location.assign(getDomain() + "#/profile");
		}
	});

	$scope.submitForm = function() {
		UserService.userLogin($.param({
			email : myForm.elements.email.value,
			password : myForm.elements.password.value
		}), function(response) {
			if (response != null && response.result == "1") {
				$rootScope.accessKey=JSON.parse(JSON.parse(response.json).secu).accessCode;
				window.location.assign(getDomain() + "#/profile");
			}
		});
	}
	} catch (e) {
		console.log(e);
	}
}