function ChatController($scope, $rootScope, UserService) {
	try {
	UserService.securityCheck($.param({
		secu : $rootScope.accessKey
	}), function(response) {
		console.log(response);
		if (!(response != null && response.result == "1")) {
			window.location.assign(getDomain() + "#/login");
		}
	});
	} catch (e) {
		console.log(e);
	}
}