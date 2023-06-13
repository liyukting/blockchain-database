angular.module('myapp', [ 'userModule', 'ngRoute', 'ui.bootstrap' ]).config(
		[ '$routeProvider', '$locationProvider', '$httpProvider',
				function($routeProvider, $locationProvider, $httpProvider) {
					$locationProvider.hashPrefix('');
					$routeProvider.when('/', {
						templateUrl : 'views/search-page.html',
						controller : SearchController
					});
					$routeProvider.when('/chat', {
						templateUrl : 'views/chat-page.html',
						controller : ChatController
					});
					$routeProvider.when('/login', {
						templateUrl : 'views/login-page.html',
						controller : LoginController
					});
					$routeProvider.when('/register', {
						templateUrl : 'views/register-page.html',
						controller : RegisterController
					});
					$routeProvider.when('/settings', {
						templateUrl : 'views/settings-page.html',
						controller : SettingsController
					});
					$routeProvider.when('/:id', {
						templateUrl : 'views/user-profile-page.html',
						controller : ProfileController
					});

				} ]).run(function($rootScope) {
});

var userModule = angular.module('userModule', [ 'ngResource' ]);

userModule.factory('GreetingService', function($resource) {
	return $resource('rest/Greeting/:action', {}, {
		post : {
			method : 'POST',
			params : {
				'action' : 'post'
			},
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		},
		resetChains : {
			method : 'POST',
			params : {
				'action' : 'reset-chains'
			},
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		},
		chain : {
			method : 'POST',
			params : {
				'action' : 'chain'
			},
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		},
		transaction : {
			method : 'POST',
			params : {
				'action' : 'transaction'
			},
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		}
	});
});

userModule.factory('UserService', function($resource) {
	return $resource('rest/user/:action', {}, {
		userRegister : {
			method : 'POST',
			params : {
				'action' : 'user-register'
			},
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		},
		userLogin : {
			method : 'POST',
			params : {
				'action' : 'user-login'
			},
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		},
		userLogout : {
			method : 'POST',
			params : {
				'action' : 'user-logout'
			},
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		},
		securityCheck : {
			method : 'POST',
			params : {
				'action' : 'security-check'
			},
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		},
		userProfile : {
			method : 'POST',
			params : {
				'action' : 'user-profile'
			},
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			}
		},
		helperList : {
			method : 'POST',
			params : {
				'action' : 'helper-list'
			}
		}
	});
});

